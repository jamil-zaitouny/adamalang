/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.translator.tree.statements;

import org.adamalang.translator.env.ComputeContext;
import org.adamalang.translator.env.Environment;
import org.adamalang.translator.env.FreeEnvironment;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.tree.common.StringBuilderWithTabs;
import org.adamalang.translator.tree.expressions.Expression;
import org.adamalang.translator.tree.types.TyType;
import org.adamalang.translator.tree.types.TypeBehavior;
import org.adamalang.translator.tree.types.checking.properties.CanAssignResult;
import org.adamalang.translator.tree.types.checking.properties.StorageTweak;
import org.adamalang.translator.tree.types.checking.ruleset.RuleSetCommon;
import org.adamalang.translator.tree.types.natives.TyNativeMessage;
import org.adamalang.translator.tree.types.natives.TyNativeReactiveRecordPtr;
import org.adamalang.translator.tree.types.reactive.TyReactiveRecord;
import org.adamalang.translator.tree.types.traits.details.DetailInventDefaultValueExpression;
import org.adamalang.translator.tree.types.traits.details.DetailNativeDeclarationIsNotStandard;

import java.util.function.Consumer;

/** type var = expr; | let var = expr; */
public class DefineVariable extends Statement {
  public final String name;
  public final Token nameToken;
  public final Token preludeToken;
  private final Token endToken;
  private final Token equalToken;
  private TyType type;
  private Expression value;

  public DefineVariable(final Token preludeToken, final Token nameToken, final TyType type, final Token equalToken, final Expression value, final Token endToken) {
    this.preludeToken = preludeToken;
    if (preludeToken != null) {
      ingest(preludeToken);
    }
    this.nameToken = nameToken;
    ingest(nameToken);
    name = nameToken.text;
    this.type = type;
    if (type != null) {
      ingest(type);
    }
    this.equalToken = equalToken;
    this.value = value;
    if (equalToken != null) {
      ingest(value);
    }
    this.endToken = endToken;
    if (endToken != null) {
      ingest(endToken);
    }
  }

  @Override
  public void emit(final Consumer<Token> yielder) {
    if (preludeToken != null) {
      yielder.accept(preludeToken);
    }
    if (type != null) {
      type.emit(yielder);
    }
    yielder.accept(nameToken);
    if (equalToken != null) {
      yielder.accept(equalToken);
      value.emit(yielder);
    }
    if (endToken != null) {
      yielder.accept(endToken);
    }
  }

  @Override
  public ControlFlow typing(final Environment environment) {
    // type the value
    TyType valueType = null;
    if (value != null) {
      valueType = environment.rules.Resolve(value.typing(environment.scopeWithComputeContext(ComputeContext.Computation), type), false);
      // infer the value type if auto
      if (type == null) {
        type = environment.rules.Resolve(valueType, false);
      }
    }
    // resolve it
    type = RuleSetCommon.Resolve(environment, type, false);
    // invent a value if we can
    if (value == null && type != null && type instanceof DetailInventDefaultValueExpression && !(type instanceof DetailNativeDeclarationIsNotStandard)) {
      value = ((DetailInventDefaultValueExpression) type).inventDefaultValueExpression(this);
      valueType = value.typing(environment.scopeWithComputeContext(ComputeContext.Computation), type);
    }
    if (type != null && type instanceof TyReactiveRecord) {
      type = new TyNativeReactiveRecordPtr(TypeBehavior.ReadWriteWithSetGet, (TyReactiveRecord) type);
      if (value == null) {
        environment.document.createError(type, String.format("Reactive pointers must be initialized"));
      }
    }
    // is capable of getting an assignment
    if (type != null && valueType != null) {
      final var result = environment.rules.CanAssignWithSet(type, valueType, false);
      final var canStore = environment.rules.CanTypeAStoreTypeB(type, valueType, StorageTweak.None, false);
      if (!canStore || result == CanAssignResult.No) {
        type = null;
      }
      if (type instanceof TyNativeMessage && type != null && valueType != null && valueType.behavior == TypeBehavior.ReadOnlyNativeValue) {
        type = type.makeCopyWithNewPosition(type, valueType.behavior);
      }
    }
    if (type != null) {
      type.typing(environment);
      if (environment.defined(name)) {
        environment.document.createError(this, String.format("Variable '%s' was already defined", name));
      } else {
        environment.define(name, type.makeCopyWithNewPosition(this, type.behavior), type.behavior == TypeBehavior.ReadOnlyNativeValue, type);
      }
    }
    return ControlFlow.Open;
  }

  @Override
  public void writeJava(final StringBuilderWithTabs sb, final Environment environment) {
    if (type != null) {
      boolean isReadOnly = type.behavior == TypeBehavior.ReadOnlyNativeValue;
      if (isReadOnly) {
        sb.append("final ");
      }
      sb.append(type.getJavaConcreteType(environment)).append(" " + name);
      if (value != null) {
        sb.append(" = ");
        if (type instanceof DetailNativeDeclarationIsNotStandard) {
          final var child = new StringBuilder();
          value.writeJava(child, environment.scopeWithComputeContext(ComputeContext.Computation));
          sb.append(String.format(((DetailNativeDeclarationIsNotStandard) type).getPatternWhenValueProvided(environment), child));
        } else {
          value.writeJava(sb, environment.scopeWithComputeContext(ComputeContext.Computation));
        }
        sb.append(";");
      } else {
        if (type instanceof DetailNativeDeclarationIsNotStandard) {
          sb.append(" = ").append(((DetailNativeDeclarationIsNotStandard) type).getStringWhenValueNotProvided(environment));
        }
        sb.append(";");
      }
      environment.define(name, type, isReadOnly, type);
    }
  }

  @Override
  public void free(FreeEnvironment environment) {
    environment.define(name);
    if (value != null) {
      value.free(environment);
    }
  }
}
