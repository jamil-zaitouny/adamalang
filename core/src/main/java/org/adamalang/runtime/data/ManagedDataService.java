/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.runtime.data;

import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.common.NamedRunnable;
import org.adamalang.runtime.data.managed.Action;
import org.adamalang.runtime.data.managed.Base;

public class ManagedDataService implements DataService {
  private final Base base;

  public ManagedDataService(Base base) {
    this.base = base;
  }

  @Override
  public void get(Key key, Callback<LocalDocumentChange> callback) {
    base.on(key, (machine) -> {
      machine.open();
      machine.read(new Action(() -> {
        base.data.get(key, callback);
      }, callback));
    });
  }

  @Override
  public void initialize(Key key, RemoteDocumentUpdate patch, Callback<Void> callback) {
    base.finder.bind(key, base.region, base.target, new Callback<Void>() {
      @Override
      public void success(Void value) {
        base.on(key, (machine) -> {
          machine.open();
          machine.write(new Action(() -> {
            base.data.initialize(key, patch, callback);
          }, callback));
        });
      }

      @Override
      public void failure(ErrorCodeException ex) {
        callback.failure(ex);
      }
    });
  }

  @Override
  public void patch(Key key, RemoteDocumentUpdate[] patches, Callback<Void> callback) {
    base.on(key, (machine) -> {
      machine.write(new Action(() -> {
        base.data.patch(key, patches, callback);
      }, callback));
    });
  }

  @Override
  public void compute(Key key, ComputeMethod method, int seq, Callback<LocalDocumentChange> callback) {
    base.on(key, (machine) -> {
      machine.read(new Action(() -> {
        base.data.compute(key, method, seq, callback);
      }, callback));
    });
  }

  private void deleteLocal(Key key, Callback<Void> callback) {
    base.on(key, (machine) -> {
      machine.write(new Action(() -> {
        base.data.delete(key, new Callback<Void>() {
          @Override
          public void success(Void value) {
            base.executor.execute(new NamedRunnable("managed-delete") {
              @Override
              public void execute() throws Exception {
                machine.close();;
                base.documents.remove(key);
              }
            });
          }

          @Override
          public void failure(ErrorCodeException ex) {
            callback.failure(ex);
          }
        });
      }, callback));
    });
  }

  @Override
  public void delete(Key key, Callback<Void> callback) {
    base.finder.delete(key, base.target, new Callback<Void>() {
      @Override
      public void success(Void value) {
        deleteLocal(key, callback);
      }

      @Override
      public void failure(ErrorCodeException ex) {
        callback.failure(ex);
      }
    });
  }

  @Override
  public void snapshot(Key key, int seq, String snapshot, int history, Callback<Integer> callback) {
    base.on(key, (machine) -> {
      machine.write(new Action(() -> {
        base.data.snapshot(key, seq, snapshot, history, callback);
      }, callback));
    });
  }

  @Override
  public void close(Key key, Callback<Void> callback) {
    base.on(key, (machine) -> {
      // I mean, ok, but how does shit leave the base?
      machine.close();
      callback.success(null);
    });
  }
}
