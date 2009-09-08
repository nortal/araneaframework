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

package org.araneaframework.backend.list.sqlexpr.compare;

import org.araneaframework.backend.list.SqlExpression;

public class SqlLowerThanExpression extends SqlComparableExpression {

  protected boolean allowEquals = false;

  public SqlLowerThanExpression(SqlExpression expr1, SqlExpression expr2) {
    super(expr1, expr2);
  }

  /**
   * Greates a new <code>SqlLowerThanExpression</code> that can also be
   * lower-than-or-equal expression. The latter is determined by the boolean
   * parameter <code>allowEquals</code>.
   * 
   * @param expr1 The expression that should be lower than the other.
   * @param expr2 The expression that should be greater than the other.
   * @param allowEquals Whether expressions can also be equal. By default: equality not allowed.
   * @since 1.2
   */
  public SqlLowerThanExpression(SqlExpression expr1, SqlExpression expr2, boolean allowEquals) {
    super(expr1, expr2);
    this.allowEquals = allowEquals;
  }

  protected String getOperator() {
    return allowEquals ? "<=" : "<";
  }
}
