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

package org.araneaframework.backend.list.helper.reader;

import java.sql.ResultSet;

/**
 * ResultSet column reader.
 * <p>
 * Method {@link #readFromResultSet(String, ResultSet, Class)} reads the
 * column <code>columnName</code> and converts it into the given <code>javaType</code>.
 * 
 * @see ResultSet
 * @see DefaultResultSetColumnReader
 * @see ConverterBasedColumnReader
 * @see BooleanColumnReader
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface ResultSetColumnReader {

  /**
   * This method should read some custom Java Object from the given <code>ResultSet</code> column.
   * @param columnName column in <code>ResultSet</code>.
   * @param resultSet JDBC result set.
   * @param javaType java type to read to.
   * @return Custom Java Object from the given <code>ResultSet</code> column.
   */
  Object readFromResultSet(String columnName, ResultSet resultSet,
      Class javaType);
}
