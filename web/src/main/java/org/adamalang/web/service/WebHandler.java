/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.web.service;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.CookieHeaderNames;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.web.contracts.AssetDownloader;
import org.adamalang.web.contracts.HttpHandler;
import org.adamalang.web.firewall.WebRequestShield;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class WebHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
  private static final byte[] EMPTY_RESPONSE = new byte[0];
  private static final byte[] OK_RESPONSE = "OK".getBytes(StandardCharsets.UTF_8);
  private static final byte[] ASSET_FAILED_ATTACHMENT = ("<html><head><title>Asset Failure</title></head><body>Failure to initiate asset attachment.</body></html>").getBytes(StandardCharsets.UTF_8);
  private static final byte[] ASSET_COOKIE_LACKING = "<html><head><title>Bad Request</title></head><body>Asset cookie was not set.</body></html>".getBytes(StandardCharsets.UTF_8);
  private static final byte[] NOT_FOUND_RESPONSE = "<html><head><title>Bad Request; Not Found</title></head><body>Sorry, the request was not found within our handler space.</body></html>".getBytes(StandardCharsets.UTF_8);

  private static final Logger LOG = LoggerFactory.getLogger(WebHandler.class);
  private final WebConfig webConfig;
  private final WebMetrics metrics;
  private final HttpHandler httpHandler;
  private final AssetDownloader downloader;

  public WebHandler(WebConfig webConfig, WebMetrics metrics, HttpHandler httpHandler, AssetDownloader downloader) {
    this.webConfig = webConfig;
    this.metrics = metrics;
    this.httpHandler = httpHandler;
    this.downloader = downloader;
  }

  /** internal: copy the origin to access control when allowed */
  private void transferCors(final FullHttpResponse res, final FullHttpRequest req, boolean allow) {
    String origin = req.headers().get(HttpHeaderNames.ORIGIN);
    if (origin != null && allow) { // CORS support directly
      res.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
      res.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, true);
    }
  }

  /** handle the pre-flight options request */
  private void handlePreflight(final ChannelHandlerContext ctx, final FullHttpRequest req, boolean allow) {
    final FullHttpResponse res = new DefaultFullHttpResponse(req.protocolVersion(), HttpResponseStatus.OK, Unpooled.wrappedBuffer(EMPTY_RESPONSE));
    transferCors(res, req, allow);
    sendWithKeepAlive(webConfig, ctx, req, res);
  }

  /** send an immediate data result */
  private void sendImmediate(Runnable metric, FullHttpRequest req, final ChannelHandlerContext ctx, HttpResponseStatus status, byte[] content, String contentType, boolean cors) {
    metric.run();
    final FullHttpResponse res = new DefaultFullHttpResponse(req.protocolVersion(), status, Unpooled.wrappedBuffer(content));
    HttpUtil.setContentLength(res, content.length);
    if (contentType != null) {
      res.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
    }
    transferCors(res, req, cors);
    sendWithKeepAlive(webConfig, ctx, req, res);
  }

  /** handle an asset request */
  private void handleAsset(FullHttpRequest req, final ChannelHandlerContext ctx, AssetRequest assetRequest, boolean cors) {
    downloader.request(assetRequest, new AssetDownloader.AssetStream() {
      private boolean started = false;
      private String contentType = null;

      @Override
      public void headers(long length, String contentType) {
        this.contentType = contentType;
      }

      @Override
      public void body(byte[] chunk, int offset, int length, boolean last) {
        if (!started && last) {
          byte[] content = Arrays.copyOfRange(chunk, offset, length);
          final FullHttpResponse res = new DefaultFullHttpResponse(req.protocolVersion(), HttpResponseStatus.OK, Unpooled.wrappedBuffer(content));
          HttpUtil.setContentLength(res, content.length);
          res.headers().set(HttpHeaderNames.CONTENT_TYPE, this.contentType);
          transferCors(res, req, cors);
          sendWithKeepAlive(webConfig, ctx, req, res);
        } else {
          if (!started) {
            DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, this.contentType);
            ctx.write(response);
            started = true;
          }
          ctx.write(new DefaultHttpContent(Unpooled.wrappedBuffer(Arrays.copyOfRange(chunk, offset, length))));
          if (last) {
            ctx.writeAndFlush(new DefaultLastHttpContent());
          }
        }
      }

      @Override
      public void failure(int code) {
        if (started) {
          ctx.close();
        } else {
          sendImmediate(metrics.webhandler_asset_failed, req, ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, ("Download asset failure:" + code).getBytes(StandardCharsets.UTF_8), "text/plain", false);
        }
      }
    });
  }

  /** handle secret and encrypted assets */
  private void handleEncryptedAsset(FullHttpRequest req, final ChannelHandlerContext ctx) {
    String assetKey = AssetRequest.extractAssetKey(req.headers().get(HttpHeaderNames.COOKIE));
    if (assetKey != null) {
      try {
        String encryptedId = req.uri().substring("/~assets/".length());
        metrics.webhandler_assets_start.run();
        handleAsset(req, ctx, AssetRequest.parse(encryptedId, assetKey), true);
      } catch (Exception err) {
        sendImmediate(metrics.webhandler_assets_failed_start, req, ctx, HttpResponseStatus.OK, ASSET_FAILED_ATTACHMENT, "text/html; charset=UTF-8", false);
      }
    } else {
      sendImmediate(metrics.webhandler_assets_no_cookie, req, ctx, HttpResponseStatus.OK, ASSET_COOKIE_LACKING, "text/html; charset=UTF-8", false);
    }
  }

  private boolean handleInternal(final ChannelHandlerContext ctx, final FullHttpRequest req) {
    if (webConfig.healthCheckPath.equals(req.uri())) { // health checks
      sendImmediate(metrics.webhandler_healthcheck, req, ctx, HttpResponseStatus.OK, ("HEALTHY:" + System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8), "text/text; charset=UTF-8", true);
      return true;
    } else if (req.uri().startsWith("/libadama.js")) { // in-memory JavaScript library for the client
      sendImmediate(metrics.webhandler_client_download, req, ctx, HttpResponseStatus.OK, JavaScriptClient.ADAMA_JS_CLIENT_BYTES, "text/javascript; charset=UTF-8", true);
      return true;
    } else if (req.uri().startsWith("/rxhtml.js")) { // in-memory JavaScript library for RxHTML (to be integrated into client)
      sendImmediate(metrics.webhandler_client_download, req, ctx, HttpResponseStatus.OK, JavaScriptRxHtml.RXHTML_JS_BYTES, "text/javascript; charset=UTF-8", true);
      return true;
    } else if (req.uri().startsWith("/~assets/")) { // assets that are encrypted and private to the connection
      handleEncryptedAsset(req, ctx);
      return true;
    } else if (req.uri().startsWith("/~upload")) {
      // TODO: post upload to an asset
    } else if (req.uri().startsWith("/~p")) { // set an asset key
      final FullHttpResponse res = new DefaultFullHttpResponse(req.protocolVersion(), HttpResponseStatus.OK, Unpooled.wrappedBuffer(OK_RESPONSE));
      String value = req.uri().substring(3);
      String origin = req.headers().get(HttpHeaderNames.ORIGIN);
      if (origin != null) { // CORS support directly
        res.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        res.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_CREDENTIALS, true);
      }
      DefaultCookie cookie = new DefaultCookie("SAK", value);
      cookie.setSameSite(CookieHeaderNames.SameSite.None);
      cookie.setMaxAge(60 * 60 * 24 * 7);
      cookie.setHttpOnly(true);
      cookie.setSecure(true);
      res.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
      res.headers().set(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
      sendWithKeepAlive(webConfig, ctx, req, res);
      return true;
    }
    return false;
  }

  private void handleHttpResult(HttpHandler.HttpResult httpResultIncoming, final ChannelHandlerContext ctx, final FullHttpRequest req) {
    HttpHandler.HttpResult httpResult = httpResultIncoming;
    if (httpResult == null) { // no response found
      sendImmediate(metrics.webhandler_notfound, req, ctx, HttpResponseStatus.NOT_FOUND, NOT_FOUND_RESPONSE, "text/html; charset=UTF-8", true);
      return;
    }

    AssetRequest isAsset = AssetRequest.from(httpResult);
    if (isAsset != null) { // the result is an asset
      handleAsset(req, ctx, isAsset, httpResult.cors); // TODO: have caching instruction, and transform instruction
      return;
    }

    // otherwise, send the body
    metrics.webhandler_found.run();
    final FullHttpResponse res = new DefaultFullHttpResponse(req.protocolVersion(), HttpResponseStatus.OK, Unpooled.wrappedBuffer(httpResult.body));
    HttpUtil.setContentLength(res, httpResult.body.length);
    res.headers().set(HttpHeaderNames.CONTENT_TYPE, httpResult.contentType);
    transferCors(res, req, httpResult.cors);
    sendWithKeepAlive(webConfig, ctx, req, res);
  }

  @Override
  protected void channelRead0(final ChannelHandlerContext ctx, final FullHttpRequest req) throws Exception {
    // Step 1: Quick reject anything the shield doesn't like
    if (WebRequestShield.block(req.uri())) {
      sendImmediate(metrics.webhandler_firewall, req, ctx, HttpResponseStatus.GONE, EMPTY_RESPONSE, null, false);
      return;
    }

    // Step 2: Handle internal routing for Adama only stuff
    if (handleInternal(ctx, req)) {
      return;
    }

    // Step 4: Handle the result from the web request
    Callback<HttpHandler.HttpResult> callback = new Callback<>() {
      @Override
      public void success(HttpHandler.HttpResult value) {
        ctx.executor().execute(() -> {
          handleHttpResult(value, ctx, req);
        });
      }

      @Override
      public void failure(ErrorCodeException ex) {
        LOG.error("failed-handler:", ex);
        handleHttpResult(null, ctx, req);
      }
    };

    // Step 3: Parse the request and then route to the appropriate handler
    try {
      AdamaWebRequest wta = new AdamaWebRequest(req);
      if (req.method() == HttpMethod.OPTIONS) {
        metrics.webhandler_options.run();
        httpHandler.handleOptions(wta.uri, new Callback<Boolean>() {
          @Override
          public void success(Boolean allow) { handlePreflight(ctx, req, allow); }

          @Override
          public void failure(ErrorCodeException ex) {
            sendImmediate(metrics.webhandler_wta_crash, req, ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, EMPTY_RESPONSE, null, false);
          }
        });
      } else if (req.method() == HttpMethod.POST || req.method() == HttpMethod.PUT) {
        metrics.webhandler_post.run();
        httpHandler.handlePost(wta.uri, wta.headers, wta.parameters, wta.body, callback);
      } else {
        metrics.webhandler_get.run();
        httpHandler.handleGet(wta.uri, wta.headers, wta.parameters, callback);
      }
    } catch (Exception ex) {
      LOG.error("failure-to-build-wta:", ex);
      sendImmediate(metrics.webhandler_wta_crash, req, ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR, EMPTY_RESPONSE, null, true);
    }
  }

  private static void sendWithKeepAlive(final WebConfig webConfig, final ChannelHandlerContext ctx, final FullHttpRequest req, final FullHttpResponse res) {
    final var responseStatus = res.status();
    final var keepAlive = HttpUtil.isKeepAlive(req) && responseStatus.code() == 200;
    HttpUtil.setKeepAlive(res, keepAlive);
    final var future = ctx.writeAndFlush(res);
    if (!keepAlive) {
      future.addListener(ChannelFutureListener.CLOSE);
    }
  }
}
