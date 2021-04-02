/* The Adama Programming Language For Board Games!
 *    See http://www.adama-lang.org/ for more information.
 * (c) copyright 2020 Jeffrey M. Barber (http://jeffrey.io) */
package org.adamalang.runtime.delta;

import org.adamalang.runtime.json.JsonStreamWriter;
import org.adamalang.runtime.json.PrivateLazyDeltaWriter;
import org.adamalang.runtime.natives.NtClient;
import org.adamalang.runtime.natives.NtDynamic;
import org.junit.Assert;
import org.junit.Test;

public class DDynamicTests {
  @Test
  public void flow() {
    final var db = new DDynamic();
    final var stream = new JsonStreamWriter();
    final var writer = PrivateLazyDeltaWriter.bind(NtClient.NO_ONE, stream, null);
    final var A = new NtDynamic("false");
    final var B = new NtDynamic("true");
    db.show(A, writer);
    db.show(A, writer);
    db.hide(writer);
    db.hide(writer);
    db.show(A, writer);
    db.show(A, writer);
    db.show(A, writer);
    db.show(A, writer);
    db.show(B, writer);
    db.show(B, writer);
    db.show(A, writer);
    db.show(A, writer);
    db.show(A, writer);
    Assert.assertEquals("falsenullfalsetruefalse", stream.toString());
  }
}
