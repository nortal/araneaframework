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

import java.util.Collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.sqlexpr.SqlCollectionExpression;
import org.araneaframework.core.Assert;

/**
 * An IN expression for backend-based lists. Verifies that the field value is
 * one of the provided (user-selected) values.
 * 
 * @author Allar Tammik
 * @since 1.1.4
 */
public class SqlInExpression extends SqlCollectionExpression {

  /**
   * The field value expression.
   */
  protected SqlExpression expr1;

  public SqlInExpression(SqlExpression expr1, SqlExpression[] exprs) {
    Assert.notNull(expr1, "All arguments must be provided");
    Assert.notEmpty(exprs, "All arguments must be provided");

    List<SqlExpression> exprList = Arrays.asList(exprs);
    Assert.noNullElements(exprList, "All arguments in array must be provided");

    this.expr1 = expr1;
    this.children = exprList;
  }

  public Object[] getValues() {
    List<Object> values = new ArrayList<Object>();
    Object[] childValues = this.expr1.getValues();

    if (childValues != null) {
      Collections.addAll(values, childValues);
    }

    for (Iterator<SqlExpression> i = this.children.iterator(); i.hasNext();) {
      childValues = i.next().getValues();
      if (childValues != null) {
        values.addAll(Arrays.asList(childValues));
      }
    }

    return values.toArray();
  }

  /**
   * Returns a <code>String</code> like this "x IN (y,w,z)".
   */
  public String toSqlString() {
    StringBuffer sb = new StringBuffer();
    if (!this.children.isEmpty()) {
      sb.append(this.expr1.toSqlString());
      sb.append(" IN (");
      sb.append(super.toSqlString());
      sb.append(")");
    }
    return sb.toString();
  }
}
