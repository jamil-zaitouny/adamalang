/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.common;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

public class OnceTests {
  @Test
  public void flow() {
    AtomicLong l = new AtomicLong(123);
    Once<Long> o = new Once<>();
    Assert.assertEquals(123L, (long) o.access(() -> l.get()));
    l.getAndIncrement();
    Assert.assertEquals(123L, (long) o.access(() -> l.get()));
  }
}
