/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See https://www.adama-platform.com/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber ( http://jeffrey.io )
 */
package org.adamalang.translator.tree.statements.testing;

import org.adamalang.translator.env.ComputeContext;
import org.adamalang.translator.env.Environment;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.tree.common.StringBuilderWithTabs;
import org.adamalang.translator.tree.expressions.Expression;
import org.adamalang.translator.tree.statements.ControlFlow;
import org.adamalang.translator.tree.statements.Statement;
import org.adamalang.translator.tree.types.TypeBehavior;
import org.adamalang.translator.tree.types.natives.TyNativeDouble;

import java.util.function.Consumer;

/** march forward time (in seconds) */
public class Forward extends Statement {
  public final Token token;
  public final Expression expression;
  public final Token semicolonToken;

  public Forward(final Token token, final Expression expression, final Token semicolonToken) {
    this.token = token;
    this.expression = expression;
    this.semicolonToken = semicolonToken;
    ingest(token);
    ingest(semicolonToken);
  }

  @Override
  public void emit(final Consumer<Token> yielder) {
    yielder.accept(token);
    expression.emit(yielder);
    yielder.accept(semicolonToken);
  }

  @Override
  public ControlFlow typing(final Environment prior) {
    Environment environment = prior.scopeWithComputeContext(ComputeContext.Computation);
    if (!environment.state.isTesting()) {
      environment.document.createError(this, String.format("Forward is exclusively for testing"), "Testing");
    }
    environment.rules.IsNumeric(expression.typing(environment, new TyNativeDouble(TypeBehavior.ReadOnlyNativeValue, null, null)), false);
    return ControlFlow.Open;
  }

  @Override
  public void writeJava(final StringBuilderWithTabs sb, final Environment environment) {
    sb.append("__forward(");
    expression.writeJava(sb, environment.scopeWithComputeContext(ComputeContext.Computation));
    sb.append(");");
  }
}
