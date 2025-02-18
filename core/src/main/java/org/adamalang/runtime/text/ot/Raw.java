/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.runtime.text.ot;

/** a base string operand */
public class Raw implements Operand {
  public final String str;

  public Raw(String str) {
    this.str = str;
  }

  @Override
  public void transposeRangeIntoJoin(int at, int length, Join join) {
    join.children.add(new Range(this, at, length));
  }

  @Override
  public String get() {
    return str;
  }

  @Override
  public int length() {
    return str.length();
  }
}
