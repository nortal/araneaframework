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

package org.araneaframework.backend.list.memorybased.expression.compare;

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.expression.CompositeExpression;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;

public class IsNullExpression implements CompositeExpression {

  private static final long serialVersionUID = 1L;

  protected Expression expr;

  public IsNullExpression(Expression expr) {
    if (expr == null) {
      throw new IllegalArgumentException("Expression must be provided");
    }
    this.expr = expr;
  }

  public Object evaluate(VariableResolver resolver)
      throws ExpressionEvaluationException {
    return (this.expr.evaluate(resolver) == null) ? Boolean.TRUE
        : Boolean.FALSE;
  }

  public Expression[] getChildren() {
    return new Expression[] { this.expr };
  }
}
