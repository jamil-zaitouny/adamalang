/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.runtime.delta;

import org.adamalang.runtime.contracts.DeltaNode;
import org.adamalang.runtime.json.PrivateLazyDeltaWriter;
import org.adamalang.runtime.natives.NtTimeSpan;

/** a timespan that respects privacy */
public class DTimeSpan implements DeltaNode {
  private NtTimeSpan prior;

  public DTimeSpan() {
    prior = null;
  }

  /** the double is no longer visible (was made private) */
  public void hide(final PrivateLazyDeltaWriter writer) {
    if (prior != null) {
      writer.writeNull();
      prior = null;
    }
  }

  @Override
  public void clear() {
    prior = null;
  }

  /** memory usage */
  @Override
  public long __memory() {
    return (prior != null ? prior.memory() : 0) + 32;
  }

  /** the double is visible, so show changes */
  public void show(final NtTimeSpan value, final PrivateLazyDeltaWriter writer) {
    if (!value.equals(prior)) {
      writer.writeNtTimeSpan(value);
    }
    prior = value;
  }
}
