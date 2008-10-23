/*
 * Copyright 2006-2008 Webmedia Group Ltd.
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

package org.araneaframework.backend.list.sqlexpr.constant;

import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.core.Assert;

public class SqlStringExpression implements SqlExpression {

  private String string;

  private Object[] values;

  public SqlStringExpression(String string, Object[] values) {
    Assert.notNull(string, "String cannot be null");
    Assert.notNull(values, "Values array cannot be null");
    this.string = string;
    this.values = values;
  }

  public SqlStringExpression(String string) {
    this(string, new Object[0]);
  }

  public String toSqlString() {
    return this.string;
  }

  public Object[] getValues() {
    return this.values;
  }
}
