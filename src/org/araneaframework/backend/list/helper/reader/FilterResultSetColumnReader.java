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

package org.araneaframework.backend.list.helper.reader;

import java.sql.ResultSet;

/**
 * Filter implementation of {@link ResultSetColumnReader}.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 * @since 1.1
 */
public class FilterResultSetColumnReader implements ResultSetColumnReader {

  private final ResultSetColumnReader child;

  public FilterResultSetColumnReader(ResultSetColumnReader child) {
    this.child = child;
  }

  public <T> T readFromResultSet(String columnName, ResultSet resultSet, Class<T> javaType) {
    return child.readFromResultSet(columnName, resultSet, javaType);
  }
}
