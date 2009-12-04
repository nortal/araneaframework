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

package org.araneaframework.backend.list.sqlexpr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.araneaframework.backend.list.SqlExpression;

public abstract class SqlMultiExpression implements SqlExpression {

  protected List<SqlExpression> children = new ArrayList<SqlExpression>();

  public SqlMultiExpression add(SqlExpression expression) {
    this.children.add(expression);
    return this;
  }

  public SqlMultiExpression setChildren(SqlExpression[] children) {
    this.children = Arrays.asList(children);
    return this;
  }

  public Object[] getValues() {
    List<Object> values = new ArrayList<Object>();
    for (SqlExpression expr : this.children) {
      Object[] childValues = expr.getValues();
      if (childValues != null) {
        values.addAll(Arrays.asList(childValues));
      }
    }
    return values.toArray();
  }
}
