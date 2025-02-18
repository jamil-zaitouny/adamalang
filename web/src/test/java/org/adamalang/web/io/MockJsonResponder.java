/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.web.io;

import org.adamalang.common.ErrorCodeException;

import java.util.ArrayList;

public class MockJsonResponder implements JsonResponder {
  public final ArrayList<String> events;

  public MockJsonResponder() {
    this.events = new ArrayList<>();
  }

  @Override
  public void stream(String json) {
    events.add("STREAM:" + json);
  }

  @Override
  public void finish(String json) {
    events.add("FINISH:" + json);
  }

  @Override
  public void error(ErrorCodeException ex) {
    events.add("ERROR:" + ex.code);
  }
}
