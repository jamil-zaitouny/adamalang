/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.web.service.mocks;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.junit.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MockHttpCallback implements Callback<FullHttpResponse> {
  public FullHttpResponse response;
  public ErrorCodeException ex;
  private CountDownLatch done;

  public MockHttpCallback() {
    this.done = new CountDownLatch(1);
  }

  public void awaitDone() throws Exception {
    Assert.assertTrue(done.await(1000, TimeUnit.MILLISECONDS));
  }

  public void assertErrorCode(int code) {
    Assert.assertNull(response);
    Assert.assertNotNull(ex);
    Assert.assertEquals(code, ex.code);
  }

  public void assertStatusCode(int code) {
    Assert.assertNull(ex);
    Assert.assertNotNull(response);
    Assert.assertEquals(HttpResponseStatus.valueOf(code), response.status());
  }

  public void assertBody(String body) {
    Assert.assertNull(ex);
    Assert.assertNotNull(response);
    Assert.assertEquals(
        body.trim(),
        new String(response.content().array()).trim().replaceAll(Pattern.quote("\r"), ""));
  }

  @Override
  public void success(FullHttpResponse value) {
    this.response = value;
    done.countDown();
  }

  @Override
  public void failure(ErrorCodeException ex) {
    this.ex = ex;
    done.countDown();
  }
}
