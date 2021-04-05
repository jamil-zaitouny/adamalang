/* The Adama Programming Language For Board Games!
 *    See http://www.adama-lang.org/ for more information.
 * (c) copyright 2020 Jeffrey M. Barber (http://jeffrey.io) */
package org.adamalang.netty.contracts;

import java.util.HashMap;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.adamalang.runtime.exceptions.ErrorCodeException;

public interface JsonResponder {
  public void failure(ErrorCodeException ex);
  public void respond(ObjectNode node, boolean done, HashMap<String, String> headers);
}
