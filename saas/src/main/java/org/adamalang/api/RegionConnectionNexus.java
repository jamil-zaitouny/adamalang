/*
* Adama Platform and Language
* Copyright (C) 2021 - 2023 by Adama Platform Initiative, LLC
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Affero General Public License as published
* by the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Affero General Public License for more details.
* 
* You should have received a copy of the GNU Affero General Public License
* along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package org.adamalang.api;

import org.adamalang.common.SimpleExecutor;
import org.adamalang.contracts.DomainWithPolicyResolver;
import org.adamalang.contracts.SpacePolicyLocator;
import org.adamalang.transforms.PerSessionAuthenticator;
import org.adamalang.web.io.JsonLogger;;

public class RegionConnectionNexus {
  public final String region;
  public final String machine;
  public final JsonLogger logger;
  public final RegionApiMetrics metrics;
  public final SimpleExecutor executor;
  public final DomainWithPolicyResolver domainService;
  public final PerSessionAuthenticator identityService;
  public final SpacePolicyLocator spaceService;

  public RegionConnectionNexus(String region, String machine, JsonLogger logger, RegionApiMetrics metrics, SimpleExecutor executor, DomainWithPolicyResolver domainService, PerSessionAuthenticator identityService, SpacePolicyLocator spaceService) {
    this.region = region;
    this.machine = machine;
    this.logger = logger;
    this.metrics = metrics;
    this.executor = executor;
    this.domainService = domainService;
    this.identityService = identityService;
    this.spaceService = spaceService;
  }
}
