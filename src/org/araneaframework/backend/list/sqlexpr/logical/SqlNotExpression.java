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

package org.araneaframework.backend.list.sqlexpr.logical;

import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.core.Assert;

public class SqlNotExpression implements SqlExpression {

  private SqlExpression expr;

  public SqlNotExpression(SqlExpression expr) {
    Assert.notNull(expr, "SqlExpression must be provided");
    this.expr = expr;
  }

  public String toSqlString() {
    StringBuffer sb = new StringBuffer();
    sb.append("NOT ").append(this.expr.toSqlString());
    return sb.toString();
  }

  public Object[] getValues() {
    return this.expr.getValues();
  }
}
