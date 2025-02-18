/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.common.metrics;

/** for measuring a resource which may be active in memory */
public interface Inflight {
  /** bump the resource up */
  void up();

  /** bump the resource down */
  void down();

  /** set the value directly */
  void set(int value);
}
