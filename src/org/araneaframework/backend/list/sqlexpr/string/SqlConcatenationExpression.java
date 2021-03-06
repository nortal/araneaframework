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

package org.araneaframework.backend.list.sqlexpr.string;

import java.util.Iterator;
import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.sqlexpr.SqlMultiExpression;

public class SqlConcatenationExpression extends SqlMultiExpression {

  public String toSqlString() {
    StringBuffer sb = new StringBuffer();
    for (Iterator<SqlExpression> i = this.children.iterator(); i.hasNext();) {
      sb.append(i.next().toSqlString());
      if (i.hasNext()) {
        sb.append(" || ");
      }
    }
    return sb.toString();
  }
}
