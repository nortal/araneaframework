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
import java.util.Iterator;
import java.util.List;
import org.araneaframework.backend.list.SqlExpression;

public abstract class SqlMultiExpression implements SqlExpression {

  protected List children = new ArrayList();

  public SqlMultiExpression add(SqlExpression expression) {
    this.children.add(expression);
    return this;
  }

  public SqlMultiExpression setChildren(SqlExpression[] children) {
    this.children = Arrays.asList(children);
    return this;
  }

  public Object[] getValues() {
    List values = new ArrayList();
    for (Iterator i = this.children.iterator(); i.hasNext();) {
      SqlExpression expr = (SqlExpression) i.next();
      Object[] childValues = expr.getValues();
      if (childValues != null) {
        values.addAll(Arrays.asList(childValues));
      }
    }
    return values.toArray();
  }
}
