/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.frontend.global;

import org.adamalang.api.GlobalApiMetrics;
import org.adamalang.api.RegionApiMetrics;
import org.adamalang.common.SimpleExecutor;
import org.adamalang.common.keys.PrivateKeyWithId;
import org.adamalang.common.metrics.MetricsFactory;
import org.adamalang.extern.Email;
import org.adamalang.extern.SignalControl;
import org.adamalang.frontend.FrontendConfig;
import org.adamalang.frontend.FrontendMetrics;
import org.adamalang.multiregion.MultiRegionClient;
import org.adamalang.mysql.DataBase;
import org.adamalang.mysql.impl.GlobalFinder;
import org.adamalang.mysql.impl.GlobalMetricsReporter;
import org.adamalang.mysql.impl.MySQLFinderCore;
import org.adamalang.mysql.impl.GlobalCapacityOverseer;
import org.adamalang.web.assets.AssetSystem;
import org.adamalang.web.client.WebClientBase;
import org.adamalang.web.io.JsonLogger;

import java.io.File;
import java.security.PrivateKey;

public class GlobalExternNexus {
  public final FrontendConfig config;
  public final Email email;
  public final DataBase database;
  public final GlobalApiMetrics globalApiMetrics;
  public final RegionApiMetrics regionApiMetrics;
  public final File attachmentRoot;
  public final JsonLogger accessLogger;
  public final String masterKey;
  public final MultiRegionClient adama;
  public final WebClientBase webBase;
  public final String region;
  public final PrivateKey webHostKey;
  public final int publicKeyId;
  public final AssetSystem assets;
  public final FrontendMetrics frontendMetrics;
  public final String[] superPublicKeys;
  public final String[] regionalPublicKeys;
  public final SignalControl signalControl;
  public final GlobalFinder finder;
  public final MySQLFinderCore finderCore;
  public final GlobalCapacityOverseer overseer;
  public final PrivateKeyWithId signingKey;
  public final GlobalMetricsReporter metricsReporter;
  public final SimpleExecutor metrics;

  public GlobalExternNexus(FrontendConfig config, Email email, DataBase database, MultiRegionClient adama, AssetSystem assets, MetricsFactory metricsFactory, File attachmentRoot, JsonLogger accessLogger, String masterKey, WebClientBase webBase, String region, PrivateKey webHostKey, int publicKeyId, String[] superPublicKeys,  String[] regionalPublicKeys, SignalControl signalControl, GlobalFinder finder, PrivateKeyWithId signingKey) {
    this.config = config;
    this.email = email;
    this.database = database;
    this.globalApiMetrics = new GlobalApiMetrics(metricsFactory);
    this.regionApiMetrics = new RegionApiMetrics(metricsFactory);
    this.frontendMetrics = new FrontendMetrics(metricsFactory);
    this.attachmentRoot = attachmentRoot;
    this.accessLogger = accessLogger;
    this.masterKey = masterKey;
    this.adama = adama;
    this.assets = assets;
    this.webBase = webBase;
    this.region = region;
    this.webHostKey = webHostKey;
    this.publicKeyId = publicKeyId;
    this.superPublicKeys = superPublicKeys;
    this.regionalPublicKeys = regionalPublicKeys;
    this.signalControl = signalControl;
    this.finder = finder;
    this.finderCore = finder.core;
    this.overseer = new GlobalCapacityOverseer(database);
    this.signingKey = signingKey;
    this.metrics = SimpleExecutor.create("metrics-report");
    this.metricsReporter = new GlobalMetricsReporter(database, metrics);
    attachmentRoot.mkdir();
  }
  public void close() throws Exception {
    database.close();
    adama.shutdown();
    webBase.shutdown();
    metrics.shutdown();
  }
}
