/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.translator.tree.definitions;

import org.adamalang.translator.env.Environment;
import org.adamalang.translator.env.FreeEnvironment;
import org.adamalang.translator.parser.token.Token;
import org.adamalang.translator.tree.statements.Block;
import org.adamalang.translator.tree.statements.ControlFlow;
import org.adamalang.translator.tree.types.TyType;
import org.adamalang.translator.tree.types.natives.functions.FunctionPaint;
import org.adamalang.translator.tree.types.topo.TypeCheckerRoot;
import org.adamalang.translator.tree.types.natives.functions.FunctionOverloadInstance;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * a dispatcher is a function attached to enum such that the enum controls the destiny of which
 * function gets called
 */
public class DefineDispatcher extends Definition {
  public final ArrayList<FunctionArg> args;
  public final Token closeParen;
  public final Block code;
  public final Token dispatchToken;
  public final Token doubleColonToken;
  public final Token enumNameToken;
  public final Token functionName;
  public final Token introReturnType;
  public final Token openParen;
  public final Token starToken;
  public final Token valueToken;
  public int positionIndex;
  public TyType returnType;
  public int signatureId;

  public DefineDispatcher(final Token dispatchToken, final Token enumNameToken, final Token doubleColonToken, final Token valueToken, final Token starToken, final Token functionName, final Token openParen, final ArrayList<FunctionArg> args, final Token closeParen, final Token introReturnType, final TyType returnType, final Block code) {
    this.dispatchToken = dispatchToken;
    this.enumNameToken = enumNameToken;
    this.doubleColonToken = doubleColonToken;
    this.valueToken = valueToken;
    this.starToken = starToken;
    this.functionName = functionName;
    this.openParen = openParen;
    this.args = args;
    this.closeParen = closeParen;
    this.introReturnType = introReturnType;
    this.returnType = returnType;
    this.code = code;
    ingest(dispatchToken);
    ingest(code);
  }

  public FunctionOverloadInstance computeFunctionOverloadInstance() {
    final var types = new ArrayList<TyType>();
    for (final FunctionArg arg : args) {
      types.add(arg.type);
    }
    return new FunctionOverloadInstance(" __DISPATCH_" + signatureId + "_" + functionName.text, returnType, types, FunctionPaint.NORMAL);
  }

  @Override
  public void emit(final Consumer<Token> yielder) {
    yielder.accept(dispatchToken);
    yielder.accept(enumNameToken);
    yielder.accept(doubleColonToken);
    if (valueToken != null) {
      yielder.accept(valueToken);
    }
    if (starToken != null) {
      yielder.accept(starToken);
    }
    yielder.accept(functionName);
    yielder.accept(openParen);
    for (final FunctionArg arg : args) {
      if (arg.commaToken != null) {
        yielder.accept(arg.commaToken);
      }
      arg.type.emit(yielder);
      yielder.accept(arg.argNameToken);
    }
    yielder.accept(closeParen);
    if (introReturnType != null) {
      yielder.accept(introReturnType);
      returnType.emit(yielder);
    }
    code.emit(yielder);
  }

  public void typing(TypeCheckerRoot checker) {
    FreeEnvironment fe = FreeEnvironment.root();
    for(FunctionArg arg : args) {
      fe.define(arg.argName);
    }
    code.free(fe);
    checker.register(fe.free, (environment) -> {
      returnType = environment.rules.Resolve(returnType, false);
      environment.rules.FindEnumType(enumNameToken.text, this, false);
      for (final FunctionArg arg : args) {
        arg.typing(environment);
      }
      final var flow = code.typing(prepareEnvironment(environment));
      if (returnType != null && flow == ControlFlow.Open) {
        environment.document.createError(this, String.format("Dispatch '%s' does not return in all cases", functionName.text));
      }
    });
  }

  public Environment prepareEnvironment(final Environment environment) {
    final var toUse = environment.scopeDefine();
    final var enumType = environment.document.types.get(enumNameToken.text);
    toUse.define("self", enumType, true, this);
    for (final FunctionArg arg : args) {
      toUse.define(arg.argName, arg.type, true, arg.type);
    }
    toUse.setReturnType(returnType);
    return toUse;
  }

  public String signature() {
    final var sb = new StringBuilder();
    for (final FunctionArg arg : args) {
      sb.append("+").append(arg.type.getAdamaType());
    }
    return sb.toString();
  }
}
