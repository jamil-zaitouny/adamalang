/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.runtime.deploy;

import org.adamalang.common.Callback;
import org.adamalang.common.ErrorCodeException;
import org.adamalang.runtime.contracts.LivingDocumentFactoryFactory;
import org.adamalang.runtime.contracts.PlanFetcher;
import org.adamalang.runtime.data.Key;
import org.adamalang.translator.jvm.LivingDocumentFactory;

import java.util.Collection;

/** ensures an instance is always alive by fetching plans... on demand  */
public class OndemandDeploymentFactoryBase implements LivingDocumentFactoryFactory, Undeploy, Deploy {
  private final DeploymentMetrics metrics;
  private final DeploymentFactoryBase base;
  private final PlanFetcher fetcher;
  private final DeploySync sync;

  public OndemandDeploymentFactoryBase(DeploymentMetrics metrics, DeploymentFactoryBase base, PlanFetcher fetcher, DeploySync sync) {
    this.metrics = metrics;
    this.base = base;
    this.fetcher = fetcher;
    this.sync = sync;
  }

  @Override
  public void fetch(Key key, Callback<LivingDocumentFactory> callback) {
    if (base.contains(key.space)) {
      metrics.deploy_cache_hit.run();
      base.fetch(key, callback);
    } else {
      metrics.deploy_cache_miss.run();
      fetcher.find(key.space, metrics.deploy_plan_fetch.wrap(new Callback<>() {
        @Override
        public void success(DeploymentBundle bundle) {
          try {
            base.deploy(key.space, bundle.plan, bundle.keys);
            sync.watch(key.space);
            base.fetch(key, callback);
          } catch (ErrorCodeException ex) {
            callback.failure(ex);
          }
        }

        @Override
        public void failure(ErrorCodeException ex) {
          callback.failure(ex);
        }
      }));
    }
  }

  @Override
  public void deploy(String space, Callback<Void> callback) {
    fetcher.find(space, metrics.deploy_plan_push.wrap(new Callback<DeploymentBundle>() {
      @Override
      public void success(DeploymentBundle bundle) {
        try {
          base.deploy(space, bundle.plan, bundle.keys);
          sync.watch(space);
          callback.success(null);
        } catch (ErrorCodeException ex) {
          failure(ex);
        }
      }

      @Override
      public void failure(ErrorCodeException ex) {
        callback.failure(ex);
      }
    }));
  }

  @Override
  public void undeploy(String space) {
    base.undeploy(space);
    metrics.deploy_undo.run();
  }

  @Override
  public Collection<String> spacesAvailable() {
    return base.spacesAvailable();
  }
}
