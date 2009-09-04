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

import java.util.Iterator;
import org.araneaframework.backend.list.SqlExpression;

public class SqlCollectionExpression extends SqlMultiExpression {

  public String toSqlString() {
    StringBuffer sb = new StringBuffer();
    for (Iterator i = this.children.iterator(); i.hasNext();) {
      SqlExpression expr = (SqlExpression) i.next();
      sb.append(expr.toSqlString());
      if (i.hasNext()) {
        sb.append(", ");
      }
    }
    return sb.toString();
  }
}
