/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.net.client.routing;

import org.adamalang.common.*;
import org.adamalang.net.client.ClientMetrics;
import org.adamalang.net.client.contracts.RoutingSubscriber;
import org.adamalang.net.client.contracts.SpaceTrackingEvents;
import org.adamalang.net.client.routing.finder.FinderServiceRouter;
import org.adamalang.net.client.routing.finder.MachinePicker;
import org.adamalang.net.client.routing.reactive.ReativeRoutingEngine;
import org.adamalang.runtime.data.FinderService;
import org.adamalang.runtime.data.Key;

import java.util.Set;

public class ClientRouter {
  private final SimpleExecutor executor;
  public final ReativeRoutingEngine engine;
  public final Router routerForDocuments;

  public ClientRouter(SimpleExecutor executor, ReativeRoutingEngine engine, Router routerForDocuments) {
    this.executor = executor;
    this.engine = engine;
    this.routerForDocuments = routerForDocuments;
  }

  public void shutdown() {
    AwaitHelper.block(executor.shutdown(), 1000);
  }

  public static ClientRouter REACTIVE(ClientMetrics metrics) {
    SimpleExecutor executor = SimpleExecutor.create("routing-executor");
    ReativeRoutingEngine engine = new ReativeRoutingEngine(metrics, executor, new SpaceTrackingEvents() {
      @Override
      public void gainInterestInSpace(String space) {
      }

      @Override
      public void shareTargetsFor(String space, Set<String> targets) {
      }

      @Override
      public void lostInterestInSpace(String space) {
      }
    }, 250, 250);
    return new ClientRouter(executor, engine, engine);
  }

  public static ClientRouter FINDER(ClientMetrics metrics, FinderService finder, String region) {
    SimpleExecutor executor = SimpleExecutor.create("routing-executor");
    ReativeRoutingEngine engine = new ReativeRoutingEngine(metrics, executor, new SpaceTrackingEvents() {
      @Override
      public void gainInterestInSpace(String space) {
      }

      @Override
      public void shareTargetsFor(String space, Set<String> targets) {
      }

      @Override
      public void lostInterestInSpace(String space) {
      }
    }, 250, 250);
    MachinePicker picker = new MachinePicker() {
      @Override
      public void pickHost(Key key, Callback<String> callback) {
        MachinePicker self = this;
        engine.get(key, new RoutingSubscriber() {
          @Override
          public void onRegion(String region) {
            // impossible for now
          }

          @Override
          public void failure(ErrorCodeException ex) {
            callback.failure(ex);
          }

          @Override
          public void onMachine(String machine) {
            if (machine == null) {
              failure(new ErrorCodeException(-1));
            } else {
              callback.success(machine);
            }
          }
        });
      }
    };
    FinderServiceRouter routerForDocuments = new FinderServiceRouter(executor, finder, picker, region);
    return new ClientRouter(executor, engine, routerForDocuments);
  }
}
