/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.impl.global;

import org.adamalang.ErrorCodes;
import org.adamalang.common.*;
import org.adamalang.frontend.Session;
import org.adamalang.frontend.global.GlobalExternNexus;
import org.adamalang.mysql.DataBase;
import org.adamalang.mysql.model.Users;
import org.adamalang.contracts.UserIdResolver;

public class GlobalUserIdResolver implements UserIdResolver {
  private static final ExceptionLogger LOGGER = ExceptionLogger.FOR(UserIdResolver.class);
  private final SimpleExecutor executor;
  private final DataBase dataBase;

  public GlobalUserIdResolver(SimpleExecutor executor, GlobalExternNexus nexus) {
    this.executor = executor;
    this.dataBase = nexus.database;
  }

  public void execute(Session session, String email, Callback<Integer> callback) {
    executor.execute(new NamedRunnable("resolving-user-id") {
      @Override
      public void execute() throws Exception {
        try {
          callback.success(Users.getOrCreateUserId(dataBase, email));
        } catch (Exception ex) {
          callback.failure(ErrorCodeException.detectOrWrap(ErrorCodes.USERID_RESOLVE_UNKNOWN_EXCEPTION, ex, LOGGER));
        }
      }
    });
  }
}
