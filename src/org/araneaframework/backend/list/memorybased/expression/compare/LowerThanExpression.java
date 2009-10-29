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

import java.util.Comparator;
import org.araneaframework.backend.list.memorybased.Expression;

@SuppressWarnings("unchecked")
public class LowerThanExpression extends ComparableExpression {

  private boolean allowEquals = false;

  /**
   * Creates a new <code>LowerThanExpression</code>.
   * 
   * @param expr1 The expression that should be lower than the other.
   * @param expr2 The expression that should be greater than the other.
   * @param comparator The comparator that is used to compare.
   */
  public LowerThanExpression(Expression expr1, Expression expr2, Comparator comparator) {
    super(expr1, expr2, comparator);
  }

  /**
   * Creates a new <code>LowerThanExpression</code> using a default comparator.
   * 
   * @param expr1 The expression that should be lower than the other.
   * @param expr2 The expression that should be greater than the other.
   */
  public LowerThanExpression(Expression expr1, Expression expr2) {
    super(expr1, expr2);
  }

  /**
   * Creates a new <code>LowerThanExpression</code> that can also be lower-than-or-equal expression. The latter is
   * determined by the boolean parameter <code>allowEquals</code>.
   * 
   * @param expr1 The expression that should be lower than the other.
   * @param expr2 The expression that should be greater than the other.
   * @param comparator The comparator that is used to compare.
   * @param allowEquals Whether expressions can also be equal. By default: equality not allowed.
   * @since 1.2
   */
  public LowerThanExpression(Expression expr1, Expression expr2, Comparator comparator, boolean allowEquals) {
    super(expr1, expr2, comparator);
    this.allowEquals = allowEquals;
  }

  protected boolean doEvaluate(Object value1, Object value2) {
    int comp = this.comparator.compare(value1, value2);
    return this.allowEquals ? comp <= 0 : comp < 0;
  }

  /**
   * Declares whether this expression allows equals.
   * 
   * @return <code>true</code>, if this expression allows equals
   * @since 1.2
   */
  public boolean getAllowsEqual() {
    return this.allowEquals;
  }
}
