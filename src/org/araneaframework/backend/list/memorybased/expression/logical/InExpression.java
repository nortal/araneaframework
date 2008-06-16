/**
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
 **/

package org.araneaframework.backend.list.memorybased.expression.logical;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.expression.MultiExpression;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.core.Assert;

/**
 * Provides the IN expression for filtering data in memory-based lists. It
 * verifies that the value of the field is one of provided ones (case-sensitive
 * equal).
 * 
 * @author Allar Tammik
 * @since 1.1.4
 */
public class InExpression extends MultiExpression {

  private static final long serialVersionUID = 1L;

  /**
   * The field value expression.
   */
  protected Expression expr1;

  public InExpression(Expression expr1, List exprs) {

    Assert.isTrue(expr1 != null && exprs != null, "Operands must be provided");

    for (Iterator it = exprs.iterator(); it.hasNext();) {
      Assert.notNullParam(this, it.next(), "expr");
    }

    this.expr1 = expr1;
    this.children = exprs;
  }

  // [expr1, exprs[0], exprs[1], ..., exprs[n]]
  public Expression[] getChildren() {
    if (!children.isEmpty()) {
      List result = new ArrayList(children);
      result.add(0, expr1);
      return (Expression[]) result.toArray(new Expression[result.size()]);
    }

    return new Expression[] { expr1 };
  }

  /**
   * Evaluates by verifying that the field value is one of the values in the
   * array.
   */
  public Object evaluate(VariableResolver resolver)
      throws ExpressionEvaluationException {

    if (this.children.size() == 0) {
      return Boolean.TRUE;
    }

    Object fieldValue = this.expr1.evaluate(resolver);

    for (Iterator i = this.children.iterator(); i.hasNext();) {
      Expression expr = (Expression) i.next();
      Object userSelectedValue = expr.evaluate(resolver);

      if (new EqualsBuilder().append(fieldValue,
          userSelectedValue).isEquals()) {
        return Boolean.TRUE;
      }
    }

    return Boolean.FALSE;
  }

}
