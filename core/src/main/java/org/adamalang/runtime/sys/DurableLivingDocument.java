/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.runtime.sys;

import org.adamalang.ErrorCodes;
import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.common.ExceptionLogger;
import org.adamalang.common.NamedRunnable;
import org.adamalang.runtime.contracts.DataService;
import org.adamalang.runtime.contracts.DocumentMonitor;
import org.adamalang.runtime.contracts.Key;
import org.adamalang.runtime.contracts.Perspective;
import org.adamalang.runtime.exceptions.PerformDocumentDeleteException;
import org.adamalang.runtime.exceptions.PerformDocumentRewindException;
import org.adamalang.runtime.json.JsonStreamReader;
import org.adamalang.runtime.json.JsonStreamWriter;
import org.adamalang.runtime.json.PrivateView;
import org.adamalang.runtime.natives.NtAsset;
import org.adamalang.runtime.natives.NtClient;
import org.adamalang.translator.jvm.LivingDocumentFactory;

import java.util.ArrayDeque;

/** A LivingDocument tied to a document id and DataService */
public class DurableLivingDocument {
  private static final ExceptionLogger LOGGER = ExceptionLogger.FOR(DurableLivingDocument.class);
  public final DocumentThreadBase base;
  public final Key key;
  private final ArrayDeque<IngestRequest> pending;
  private LivingDocumentFactory currentFactory;
  private LivingDocument document;
  private Integer requiresInvalidateMilliseconds;
  private boolean inflightPatch;
  private boolean catastrophicFailureOccurred;
  private long lastExpire;

  private DurableLivingDocument(
      final Key key,
      final LivingDocument document,
      final LivingDocumentFactory currentFactory,
      final DocumentThreadBase base) {
    this.key = key;
    this.document = document;
    this.currentFactory = currentFactory;
    this.base = base;
    this.requiresInvalidateMilliseconds = null;
    this.pending = new ArrayDeque<>(8);
    this.inflightPatch = false;
    this.catastrophicFailureOccurred = false;
    this.lastExpire = 0;
  }

  public static void fresh(
      final Key key,
      final LivingDocumentFactory factory,
      final NtClient who,
      final String arg,
      final String entropy,
      final DocumentMonitor monitor,
      final DocumentThreadBase base,
      final Callback<DurableLivingDocument> callback) {
    try {
      DurableLivingDocument document =
          new DurableLivingDocument(key, factory.create(monitor), factory, base);
      document.construct(
          who,
          arg,
          entropy,
          Callback.transform(
              callback, ErrorCodes.DURABLE_LIVING_DOCUMENT_STAGE_FRESH_PERSIST, (seq) -> document));
    } catch (Throwable ex) {
      callback.failure(
          ErrorCodeException.detectOrWrap(
              ErrorCodes.DURABLE_LIVING_DOCUMENT_STAGE_FRESH_DRIVE, ex, LOGGER));
    }
  }

  public static void load(
      final Key key,
      final LivingDocumentFactory factory,
      final DocumentMonitor monitor,
      final DocumentThreadBase base,
      final Callback<DurableLivingDocument> callback) {
    try {
      LivingDocument doc = factory.create(monitor);
      base.service.get(
          key,
          Callback.transform(
              callback,
              ErrorCodes.DURABLE_LIVING_DOCUMENT_STAGE_LOAD_READ,
              (data) -> {
                JsonStreamReader reader = new JsonStreamReader(data.patch);
                reader.ingestDedupe(doc.__get_intern_strings());
                doc.__insert(reader);
                JsonStreamWriter writer = new JsonStreamWriter();
                doc.__dump(writer);
                return new DurableLivingDocument(key, doc, factory, base);
              }));
    } catch (Throwable ex) {
      callback.failure(
          ErrorCodeException.detectOrWrap(
              ErrorCodes.DURABLE_LIVING_DOCUMENT_STAGE_LOAD_DRIVE, ex, LOGGER));
    }
  }

  public LivingDocument document() {
    return document;
  }

  public Integer getAndCleanRequiresInvalidateMilliseconds() {
    Integer result = requiresInvalidateMilliseconds;
    requiresInvalidateMilliseconds = null;
    return result;
  }

  public LivingDocumentFactory getCurrentFactory() {
    return currentFactory;
  }

  public void deploy(LivingDocumentFactory factory, Callback<Integer> callback)
      throws ErrorCodeException {
    LivingDocument newDocument = factory.create(document.__monitor);
    JsonStreamWriter writer = new JsonStreamWriter();
    document.__dump(writer);
    newDocument.__insert(new JsonStreamReader(writer.toString()));
    document.__usurp(newDocument);
    document = newDocument;
    currentFactory = factory;
    invalidate(callback);
  }

  public JsonStreamWriter forge(final String command, final NtClient who) {
    final var writer = new JsonStreamWriter();
    writer.beginObject();
    writer.writeObjectFieldIntro("command");
    writer.writeFastString(command);
    writer.writeObjectFieldIntro("timestamp");
    writer.writeLong(base.time.nowMilliseconds());
    if (who != null) {
      writer.writeObjectFieldIntro("who");
      writer.writeNtClient(who);
    }
    return writer;
  }

  public void triggerExpire() {
    long timeSinceLastExpire = base.time.nowMilliseconds() - lastExpire;
    if (timeSinceLastExpire > 60000) {
      lastExpire = base.time.nowMilliseconds();
      expire(10 * 60 * 1000, Callback.DONT_CARE_INTEGER);
    }
  }

  private void finishSuccessDataServicePatch(
      Integer whenToInvalidateMilliseconds, Callback<Integer> callback, int seq) {
    this.requiresInvalidateMilliseconds = whenToInvalidateMilliseconds;
    this.inflightPatch = false;

    if (pending.size() == 0) {
      if (requiresInvalidateMilliseconds != null && requiresInvalidateMilliseconds == 0) {
        requiresInvalidateMilliseconds = null;
        invalidate(callback);
      } else {
        callback.success(seq);
        if (requiresInvalidateMilliseconds != null) {
          base.executor.schedule(
              new NamedRunnable("finish-success-patch") {
                @Override
                public void execute() throws Exception {
                  invalidate(Callback.DONT_CARE_INTEGER);
                }
              },
              requiresInvalidateMilliseconds);
        }
      }
    } else {
      callback.success(seq);
      executeNow(pending.removeFirst());
    }
  }

  private void catastrophicFailure() {
    document.__nukeViews();
    base.map.remove(key);
    base.metrics.inflight_documents.down();
    catastrophicFailureOccurred = true;
    while (pending.size() > 0) {
      pending
          .removeFirst()
          .callback
          .failure(new ErrorCodeException(ErrorCodes.CATASTROPHIC_DOCUMENT_FAILURE_EXCEPTION));
    }
  }

  private void executeNow(IngestRequest request) {
    try {
      final var change = document.__transact(request.request, currentFactory);
      inflightPatch = true;
      if (change.update.requiresFutureInvalidation) {
        this.requiresInvalidateMilliseconds = change.update.whenToInvalidateMilliseconds;
      }
      base.service.patch(
          key,
          change.update,
          new Callback<>() {
            @Override
            public void success(Void value) {
              base.executor.execute(
                  new NamedRunnable("execute-now-patch-callback") {
                    @Override
                    public void execute() throws Exception {
                      finishSuccessDataServicePatch(
                          change.update.requiresFutureInvalidation
                              ? change.update.whenToInvalidateMilliseconds
                              : null,
                          request.callback,
                          change.update.seq);
                      change.complete();
                      if (request.cleanupTest) {
                        if (document.__canRemoveFromMemory()) {
                          scheduleCleanup();
                        }
                      }
                    }
                  });
            }

            @Override
            public void failure(ErrorCodeException ex) {
              base.executor.execute(
                  new NamedRunnable("failed-patch") {
                    @Override
                    public void execute() throws Exception {
                      if (ex.code == ErrorCodes.UNIVERSAL_PATCH_FAILURE_HEAD_SEQ_OFF) {
                        if (!request.tryAgain()) {
                          request.callback.failure(ex);
                          catastrophicFailure();
                          return;
                        }
                        document.__insert(new JsonStreamReader(change.update.undo));
                        base.service.compute(
                            key,
                            DataService.ComputeMethod.HeadPatch,
                            document.__seq.get(),
                            base.metrics.catch_up_patch.wrap(new Callback<>() {
                              @Override
                              public void success(DataService.LocalDocumentChange value) {
                                base.executor.execute(
                                    new NamedRunnable("catch-up-for-failed-patch") {
                                      @Override
                                      public void execute() throws Exception {
                                        document.__insert(new JsonStreamReader(value.patch));
                                        executeNow(request);
                                      }
                                    });
                              }

                              @Override
                              public void failure(ErrorCodeException ex) {
                                request.callback.failure(ex);
                                catastrophicFailure();
                              }
                            }));
                      } else {
                        request.callback.failure(ex);
                        catastrophicFailure();
                      }
                    }
                  });
            }
          });
    } catch (PerformDocumentRewindException rewind) {
      base.service.compute(
          key,
          DataService.ComputeMethod.Rewind,
          rewind.seq,
          new Callback<DataService.LocalDocumentChange>() {
            @Override
            public void success(DataService.LocalDocumentChange value) {
              base.executor.execute(
                  new NamedRunnable("document-rewind-success") {
                    @Override
                    public void execute() throws Exception {
                      final var writer = forge("apply", request.who);
                      writer.writeObjectFieldIntro("patch");
                      writer.injectJson(value.patch);
                      writer.endObject();
                      executeNow(new IngestRequest(request.who, writer.toString(), request.callback, false));
                    }
                  });
            }

            @Override
            public void failure(ErrorCodeException ex) {
              base.executor.execute(
                  new NamedRunnable("document-rewind-failure") {
                    @Override
                    public void execute() throws Exception {
                      request.callback.failure(ex);
                      catastrophicFailure();
                    }
                  });
            }
          });
    } catch (PerformDocumentDeleteException destroy) {
      base.service.delete(
          key,
          new Callback<>() {
            @Override
            public void success(Void value) {
              base.executor.execute(
                  new NamedRunnable("document-destroy-success") {
                    @Override
                    public void execute() throws Exception {
                      request.callback.failure(
                          new ErrorCodeException(ErrorCodes.DOCUMENT_SELF_DESTRUCT_SUCCESSFUL));
                      catastrophicFailure();
                    }
                  });
            }

            @Override
            public void failure(ErrorCodeException ex) {
              base.executor.execute(
                  new NamedRunnable("document-destroy-failure") {
                    @Override
                    public void execute() throws Exception {
                      request.callback.failure(ex);
                      catastrophicFailure();
                    }
                  });
            }
          });
    } catch (ErrorCodeException ex) {
      request.callback.failure(ex);
    }
  }

  private void ingest(NtClient who, String requestJson, Callback<Integer> callback, boolean cleanupTest) {
    IngestRequest request = new IngestRequest(who, requestJson, callback, cleanupTest);
    if (catastrophicFailureOccurred) {
      request.callback.failure(
          new ErrorCodeException(ErrorCodes.CATASTROPHIC_DOCUMENT_FAILURE_EXCEPTION));
      return;
    }
    if (inflightPatch) {
      if (pending.size() >= 128) {
        base.metrics.document_queue_full.run();
        callback.failure(new ErrorCodeException(ErrorCodes.DOCUMENT_QUEUE_BUSY_TOO_MANY_PENDING_ITEMS));
      } else {
        pending.add(request);
      }
    } else {
      executeNow(request);
    }
  }

  private void construct(
      final NtClient who, final String arg, final String entropy, Callback<Integer> callback) {
    try {
      final var writer = forge("construct", who);
      writer.writeObjectFieldIntro("arg");
      writer.injectJson(arg);
      if (entropy != null) {
        writer.writeObjectFieldIntro("entropy");
        writer.writeFastString(entropy);
      }
      writer.endObject();
      final var change = document.__transact(writer.toString(), currentFactory);
      base.service.initialize(
          key,
          change.update,
          Callback.handoff(
              callback,
              ErrorCodes.DURABLE_LIVING_DOCUMENT_STAGE_CONSTRUCT_PERSIST,
              () -> {
                change.complete();
                invalidate(callback);
              }));
    } catch (Throwable ex) {
      callback.failure(
          ErrorCodeException.detectOrWrap(
              ErrorCodes.DURABLE_LIVING_DOCUMENT_STAGE_CONSTRUCT_DRIVE, ex, LOGGER));
    }
  }

  public void invalidate(Callback<Integer> callback) {
    final var request = forge("invalidate", null);
    request.endObject();
    ingest(NtClient.NO_ONE, request.toString(), base.metrics.document_invalidate.wrap(callback), false);
  }

  public int getCodeCost() {
    return document.__getCodeCost();
  }

  public void zeroOutCodeCost() {
    document.__zeroOutCodeCost();
  }

  public int getConnectionsCount() {
    return document.__getConnectionsCount();
  }

  public long getMemoryBytes() {
    return document.__memory();
  }

  public void expire(long limit, Callback<Integer> callback) {
    final var request = forge("expire", null);
    request.writeObjectFieldIntro("limit");
    request.writeLong(limit);
    request.endObject();
    ingest(NtClient.NO_ONE, request.toString(), base.metrics.document_expire.wrap(callback), true);
  }

  public void connect(final NtClient who, Callback<Integer> callback) {
    final var request = forge("connect", who);
    request.endObject();
    ingest(who, request.toString(), base.metrics.document_connect.wrap(callback), false);
  }

  public boolean isConnected(final NtClient who) {
    return document.__isConnected(who);
  }

  public void createPrivateView(
      final NtClient who, final Perspective perspective, Callback<PrivateView> callback) {
    PrivateView result = document.__createView(who, perspective);
    invalidate(
        Callback.transform(
            callback,
            ErrorCodes.DURABLE_LIVING_DOCUMENT_STAGE_ATTACH_PRIVATE_VIEW,
            (seq) -> result));
  }

  public int garbageCollectPrivateViewsFor(final NtClient who) {
    return document.__garbageCollectViews(who);
  }

  public void scheduleCleanup() {
    base.executor.schedule(
        new NamedRunnable("document-cleanup") {
          @Override
          public void execute() throws Exception {
            if (document.__canRemoveFromMemory()) {
              base.map.remove(key);
              base.metrics.inflight_documents.down();
            }
          }
        },
        base.getMillisecondsForCleanupCheck());
  }

  public void disconnect(final NtClient who, Callback<Integer> callback) {
    final var request = forge("disconnect", who);
    request.endObject();
    ingest(who, request.toString(), base.metrics.document_disconnect.wrap(callback), true);
  }

  public void send(
      final NtClient who,
      final String marker,
      final String channel,
      final String message,
      Callback<Integer> callback) {
    final var writer = forge("send", who);
    writer.writeObjectFieldIntro("channel");
    writer.writeFastString(channel);
    if (marker != null) {
      writer.writeObjectFieldIntro("marker");
      writer.writeString(marker);
    }
    writer.writeObjectFieldIntro("message");
    writer.injectJson(message);
    writer.endObject();
    ingest(who, writer.toString(), base.metrics.document_send.wrap(callback), false);
  }

  public void apply(NtClient who, String patch, Callback<Integer> callback) {
    final var writer = forge("apply", who);
    writer.writeObjectFieldIntro("patch");
    writer.injectJson(patch);
    writer.endObject();
    ingest(who, writer.toString(), base.metrics.document_apply.wrap(callback), false);
  }

  public boolean canAttach(NtClient who) {
    return document.__onCanAssetAttached(who);
  }

  public void attach(NtClient who, NtAsset asset, Callback<Integer> callback) {
    final var writer = forge("attach", who);
    writer.writeObjectFieldIntro("asset");
    writer.writeNtAsset(asset);
    writer.endObject();
    ingest(who, writer.toString(), base.metrics.document_attach.wrap(callback), false);
  }

  public String json() {
    final var writer = new JsonStreamWriter();
    document.__dump(writer);
    return writer.toString();
  }

  public void reconcileClients() {
    for (NtClient client : document.__reconcileClientsToForceDisconnect()) {
      disconnect(client, Callback.DONT_CARE_INTEGER);
    }
  }

  private static class IngestRequest {
    private final NtClient who;
    private final String request;
    private final Callback<Integer> callback;
    private int attempts;
    public final boolean cleanupTest;

    private IngestRequest(NtClient who, String request, Callback<Integer> callback, boolean cleanup) {
      this.who = who;
      this.request = request;
      this.callback = callback;
      this.attempts = 0;
      this.cleanupTest = cleanup;
    }

    public boolean tryAgain() {
      return attempts++ < 5;
    }
  }
}
