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

public class DPrincipalTests {
  @Test
  public void flow() {
    final var db = new DPrincipal();
    final var stream = new JsonStreamWriter();
    final var writer = PrivateLazyDeltaWriter.bind(NtPrincipal.NO_ONE, stream, null, TestKey.ENCODER);
    final var A = new NtPrincipal("a", "local");
    db.show(NtPrincipal.NO_ONE, writer);
    db.show(NtPrincipal.NO_ONE, writer);
    db.show(A, writer);
    db.show(A, writer);
    db.hide(writer);
    db.hide(writer);
    db.show(A, writer);
    db.show(A, writer);
    db.show(NtPrincipal.NO_ONE, writer);
    db.show(NtPrincipal.NO_ONE, writer);
    Assert.assertEquals("{\"@t\":1,\"agent\":\"?\",\"authority\":\"?\"}{\"@t\":1,\"agent\":\"a\",\"authority\":\"local\"}null{\"@t\":1,\"agent\":\"a\",\"authority\":\"local\"}{\"@t\":1,\"agent\":\"?\",\"authority\":\"?\"}", stream.toString());
    Assert.assertEquals(36, db.__memory());
    db.clear();
  }
}
