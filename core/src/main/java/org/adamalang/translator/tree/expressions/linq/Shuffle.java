/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.translator.tree.expressions.linq;

import org.adamalang.translator.env.Environment;
import org.adamalang.translator.env.FreeEnvironment;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.tree.expressions.Expression;
import org.adamalang.translator.tree.types.TyType;

import java.util.function.Consumer;

public class Shuffle extends LinqExpression {
  public final Token shuffleToken;

  public Shuffle(final Token shuffleToken, final Expression sql) {
    super(sql);
    this.shuffleToken = shuffleToken;
    ingest(shuffleToken);
    ingest(sql);
  }

  @Override
  public void emit(final Consumer<Token> yielder) {
    sql.emit(yielder);
    yielder.accept(shuffleToken);
  }

  @Override
  protected TyType typingInternal(final Environment environment, final TyType suggestion) {
    final var type = sql.typing(environment, null);
    if (environment.rules.IsNativeListOfStructure(type, false)) {
      return type;
    }
    return null;
  }

  @Override
  public void writeJava(final StringBuilder sb, final Environment environment) {
    sql.writeJava(sb, environment);
    sb.append(".shuffle(").append(intermediateExpression ? "false, " : "true, ").append("__random)");
  }

  @Override
  public void free(FreeEnvironment environment) {
    sql.free(environment);
  }
}
