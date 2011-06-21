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

package org.araneaframework.backend.list.memorybased.expression.logical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.ObjectUtils;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.expression.MultiExpression;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.core.Assert;

/**
 * Provides the IN expression for filtering data in memory-based lists. It verifies that the value of the field is one
 * of provided ones (case-sensitive equal).
 * 
 * @author Allar Tammik
 * @since 1.1.4
 */
public class InExpression extends MultiExpression {

  /**
   * The field value expression.
   */
  protected Expression expr1;

  public InExpression(Expression expr1, List<Expression> exprs) {
    Assert.notNull(expr1, "Operands must be provided.");
    Assert.notNull(exprs, "Operands must be provided.");

    for (Expression expression : exprs) {
      Assert.notNullParam(this, expression, "expr");
    }

    this.expr1 = expr1;
    this.children = exprs;
  }

  // [expr1, exprs[0], exprs[1], ..., exprs[n]]
  @Override
  public Expression[] getChildren() {
    if (!this.children.isEmpty()) {
      List<Expression> result = new ArrayList<Expression>(this.children);
      result.add(0, this.expr1);
      return result.toArray(new Expression[result.size()]);
    }
    return new Expression[] { this.expr1 };
  }

  /**
   * Evaluates by verifying that the field value is one of the values in the array.
   */
  public Boolean evaluate(VariableResolver resolver) throws ExpressionEvaluationException {
    boolean evaluationResult = true;

    if (!this.children.isEmpty()) { // When no value is selected, the filter will do no filtering.
      Object fieldValue = this.expr1.evaluate(resolver);

      if (fieldValue instanceof Collection) {
        List<Object> userSelectedValues = new ArrayList<Object>(this.children.size());
        for (Expression expression : this.children) {
          userSelectedValues.add(expression.evaluate(resolver));
        }
        evaluationResult = ((Collection<?>) fieldValue).containsAll(userSelectedValues);
      } else {
        evaluationResult = false;

        for (Expression expression : this.children) {
          Object userSelectedValue = expression.evaluate(resolver);

          if (ObjectUtils.equals(fieldValue, userSelectedValue)) {
            evaluationResult = true;
            break;
          }
        }
      }
    }

    return evaluationResult;
  }
}
