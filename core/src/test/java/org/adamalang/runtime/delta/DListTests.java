/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.runtime.delta;

import org.adamalang.runtime.delta.secure.TestKey;
import org.adamalang.runtime.json.JsonStreamWriter;
import org.adamalang.runtime.json.PrivateLazyDeltaWriter;
import org.adamalang.runtime.natives.NtPrincipal;
import org.junit.Assert;
import org.junit.Test;

public class DListTests {
  @Test
  public void flow() {
    final var list = new DList<DBoolean>();
    {
      final var stream = new JsonStreamWriter();
      final var writer = PrivateLazyDeltaWriter.bind(NtPrincipal.NO_ONE, stream, null, TestKey.ENCODER);
      final var delta = writer.planObject();
      list.getPrior(0, DBoolean::new).show(true, delta.planField(0));
      list.getPrior(1, DBoolean::new).show(false, delta.planField(1));
      list.getPrior(2, DBoolean::new).show(true, delta.planField(2));
      list.rectify(3, delta);
      delta.end();
      Assert.assertEquals("{\"0\":true,\"1\":false,\"2\":true,\"@s\":3}", stream.toString());
      Assert.assertEquals(248, list.__memory());
    }
    {
      final var stream = new JsonStreamWriter();
      final var writer = PrivateLazyDeltaWriter.bind(NtPrincipal.NO_ONE, stream, null, TestKey.ENCODER);
      final var delta = writer.planObject();
      delta.manifest();
      list.getPrior(0, DBoolean::new).show(true, delta.planField(0));
      list.getPrior(1, DBoolean::new).show(false, delta.planField(1));
      list.getPrior(2, DBoolean::new).show(true, delta.planField(2));
      list.rectify(3, delta);
      delta.end();
      Assert.assertEquals("{}", stream.toString());
      Assert.assertEquals(248, list.__memory());
    }
    {
      final var stream = new JsonStreamWriter();
      final var writer = PrivateLazyDeltaWriter.bind(NtPrincipal.NO_ONE, stream, null, TestKey.ENCODER);
      final var delta = writer.planObject();
      delta.manifest();
      list.getPrior(0, DBoolean::new).show(true, delta.planField(0));
      list.getPrior(1, DBoolean::new).show(true, delta.planField(1));
      list.rectify(2, delta);
      delta.end();
      Assert.assertEquals("{\"1\":true,\"2\":null,\"@s\":2}", stream.toString());
      Assert.assertEquals(208, list.__memory());
    }
    {
      final var stream = new JsonStreamWriter();
      final var writer = PrivateLazyDeltaWriter.bind(NtPrincipal.NO_ONE, stream, null, TestKey.ENCODER);
      list.hide(writer);
      list.hide(writer);
      Assert.assertEquals("null", stream.toString());
      Assert.assertEquals(128, list.__memory());
    }
    list.clear();
  }
}
