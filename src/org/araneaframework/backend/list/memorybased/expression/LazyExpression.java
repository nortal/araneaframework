/*
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.araneaframework.backend.list.memorybased.expression;

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.ExpressionFactory;
import org.araneaframework.core.util.Assert;

/**
 * Expression that is initialized lazily.
 * 
 * @author Rein Raudjärv
 * @since 1.1
 */
public class LazyExpression<T> implements Expression {

  private final ExpressionFactory factory;

  private Expression expression;

  public LazyExpression(ExpressionFactory factory) {
    Assert.notNull(factory, "ExpressionFactory must be provided");
    this.factory = factory;
  }

  public ExpressionFactory getFactory() {
    return this.factory;
  }

  public Expression getExpression() {
    if (this.expression == null) {
      this.expression = this.factory.createExpression();
      Assert.notNull(this.expression, "ExpressionFactory must not create a null object");
    }
    return this.expression;
  }

  public Object evaluate(VariableResolver resolver) throws ExpressionEvaluationException {
    return getExpression().evaluate(resolver);
  }
}
