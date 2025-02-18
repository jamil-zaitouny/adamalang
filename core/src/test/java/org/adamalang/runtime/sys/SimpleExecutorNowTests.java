/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.runtime.sys;

import org.adamalang.common.NamedRunnable;
import org.adamalang.common.SimpleExecutor;
import org.junit.Test;

public class SimpleExecutorNowTests {
  @Test
  public void coverage() {
    SimpleExecutor.NOW.execute(new NamedRunnable("y") {
      @Override
      public void execute() throws Exception {

      }
    });
    SimpleExecutor.NOW.schedule(new NamedRunnable("x") {
      @Override
      public void execute() throws Exception {

      }
    }, 1000L);
    SimpleExecutor.NOW.shutdown();
  }
}
