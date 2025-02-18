/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.web.contracts;

import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.web.io.JsonResponder;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ServiceBaseJustHtmlTests {
  @Test
  public void coverage() throws Exception {
    ServiceBase base = ServiceBase.JUST_HTTP(new HttpHandler() {

      @Override
      public void handleOptions(String uri, TreeMap<String, String> headers, String parametersJson, Callback<HttpResult> callback) {
        callback.success(new HttpResult("","".getBytes(StandardCharsets.UTF_8), uri.equalsIgnoreCase("/opt=yes")));
      }

      @Override
      public void handleGet(String uri, TreeMap<String, String> headers, String parametersJson, Callback<HttpResult> callback) {
        callback.success(new HttpResult("yay", "yay".getBytes(StandardCharsets.UTF_8), true));
      }

      @Override
      public void handleDelete(String uri, TreeMap<String, String> headers, String parametersJson, Callback<HttpResult> callback) {
        callback.success(new HttpResult("yay", "yay".getBytes(StandardCharsets.UTF_8), true));
      }

      @Override
      public void handlePost(String uri, TreeMap<String, String> headers, String parametersJson, String body, Callback<HttpResult> callback) {
        callback.success(new HttpResult("post", "post".getBytes(StandardCharsets.UTF_8), true));
      }

      @Override
      public void handleDeepHealth(Callback<String> callback) {
        callback.success("COVERAGE");
      }
    });
    base.establish(null).execute(null, new JsonResponder() {
      @Override
      public void stream(String json) {

      }

      @Override
      public void finish(String json) {

      }

      @Override
      public void error(ErrorCodeException ex) {

      }
    });
    base.establish(null).keepalive();
    base.establish(null).kill();
    base.assets();
    CountDownLatch latch = new CountDownLatch(4);
    base.http().handleOptions("/opt=yes", new TreeMap<>(), "{}", new Callback<HttpHandler.HttpResult>() {
      @Override
      public void success(HttpHandler.HttpResult value) {
        Assert.assertTrue(value.cors);
        latch.countDown();
      }

      @Override
      public void failure(ErrorCodeException ex) {

      }
    });
    base.http().handleGet("x", new TreeMap<>(), "{}", new Callback<HttpHandler.HttpResult>() {
      @Override
      public void success(HttpHandler.HttpResult value) {
        Assert.assertEquals("yay", new String(value.body, StandardCharsets.UTF_8));
        latch.countDown();
      }

      @Override
      public void failure(ErrorCodeException ex) {

      }
    });
    base.http().handleDelete("x", new TreeMap<>(), "{}", new Callback<HttpHandler.HttpResult>() {
      @Override
      public void success(HttpHandler.HttpResult value) {
        Assert.assertEquals("yay", new String(value.body, StandardCharsets.UTF_8));
        latch.countDown();
      }

      @Override
      public void failure(ErrorCodeException ex) {

      }
    });
    base.http().handlePost("x", new TreeMap<>(), "{}", null, new Callback<HttpHandler.HttpResult>() {
      @Override
      public void success(HttpHandler.HttpResult value) {
        Assert.assertEquals("post", new String(value.body, StandardCharsets.UTF_8));
        latch.countDown();
      }

      @Override
      public void failure(ErrorCodeException ex) {
      }
    });
    Assert.assertTrue(latch.await(5000, TimeUnit.MILLISECONDS));

  }
}
