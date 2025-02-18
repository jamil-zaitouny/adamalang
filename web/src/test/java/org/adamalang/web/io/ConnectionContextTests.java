/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.web.io;

import org.junit.Assert;
import org.junit.Test;

public class ConnectionContextTests {
  @Test
  public void stripColonIp() {
    ConnectionContext a = new ConnectionContext("you", "123:42", "house", "");
    Assert.assertEquals("123", a.remoteIp);
  }

  @Test
  public void nulls() {
    ConnectionContext a = new ConnectionContext(null, null, null, "");
    Assert.assertEquals("", a.remoteIp);
    Assert.assertEquals("", a.userAgent);
    Assert.assertEquals("", a.origin);
  }
}
