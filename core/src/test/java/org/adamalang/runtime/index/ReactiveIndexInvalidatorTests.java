/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.runtime.index;

import org.adamalang.runtime.mocks.MockRecord;
import org.junit.Assert;
import org.junit.Test;

import java.util.TreeSet;

public class ReactiveIndexInvalidatorTests {
  @Test
  public void flow1() {
    final var unknowns = new TreeSet<MockRecord>();
    final var index = new ReactiveIndex<>(unknowns);
    unknowns.add(MockRecord.make(123));
    final ReactiveIndexInvalidator<MockRecord> inv =
        new ReactiveIndexInvalidator<>(index, MockRecord.make(123)) {
          @Override
          public int pullValue() {
            return 42;
          }
        };
    inv.reindex();
    unknowns.clear();
    Assert.assertEquals(0, unknowns.size());
    inv.__raiseInvalid();
    Assert.assertEquals(1, unknowns.size());
    inv.deindex();
    Assert.assertEquals(0, unknowns.size());
  }

  @Test
  public void flow2() {
    final var unknowns = new TreeSet<MockRecord>();
    final var index = new ReactiveIndex<>(unknowns);
    unknowns.add(MockRecord.make(123));
    final ReactiveIndexInvalidator<MockRecord> inv =
        new ReactiveIndexInvalidator<>(index, MockRecord.make(123)) {
          @Override
          public int pullValue() {
            return 42;
          }
        };
    inv.reindex();
    unknowns.clear();
    inv.deindex();
    Assert.assertEquals(0, unknowns.size());
  }

  @Test
  public void flow3() {
    final var unknowns = new TreeSet<MockRecord>();
    final var index = new ReactiveIndex<>(unknowns);
    unknowns.add(MockRecord.make(123));
    final ReactiveIndexInvalidator<MockRecord> inv =
        new ReactiveIndexInvalidator<>(index, MockRecord.make(123)) {
          @Override
          public int pullValue() {
            return 42;
          }
        };
    Assert.assertEquals(1, unknowns.size());
    inv.deindex();
    Assert.assertEquals(0, unknowns.size());
  }
}
