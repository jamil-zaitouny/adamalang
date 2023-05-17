/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (hint: it's MIT-based) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2020 - 2023 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.services;

import org.adamalang.common.metrics.MetricsFactory;
import org.adamalang.common.metrics.RequestResponseMonitor;

public class FirstPartyMetrics {
  public final RequestResponseMonitor amazon_ses_send;

  public FirstPartyMetrics(MetricsFactory factory) {
    amazon_ses_send = factory.makeRequestResponseMonitor("fpm_amazon_ses_send");
  }
}
