/**
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
**/

package org.araneaframework.backend.list.helper.reader;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


/**
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 */
public class DefaultResultSetColumnReader implements ResultSetColumnReader {
  
  protected static final ResultSetColumnReader instance = new DefaultResultSetColumnReader();
  
  /**
   * Tries to read the same type as is given in the <code>javaType</code>.
   */
  public Object readFromResultSet(String columnName, ResultSet resultSet, Class javaType) throws SQLException {        
    if (resultSet.getObject(columnName) == null)
      return null;
    
    if (Long.class.isAssignableFrom(javaType))      
      return new Long(resultSet.getLong(columnName));
    
    if (Integer.class.isAssignableFrom(javaType))
      return new Integer(resultSet.getInt(columnName));
    
    if (Boolean.class.isAssignableFrom(javaType))
      return new Boolean(resultSet.getBoolean(columnName));
    
    if (BigDecimal.class.isAssignableFrom(javaType))
      return resultSet.getBigDecimal(columnName);
    
    if (Timestamp.class.isAssignableFrom(javaType))
      return resultSet.getTimestamp(columnName);
    
    if (java.sql.Date.class.isAssignableFrom(javaType))
      return resultSet.getDate(columnName);        
    
	if (java.util.Date.class.isAssignableFrom(javaType))
		return new java.util.Date(resultSet.getTimestamp(columnName).getTime());
    
    if (String.class.isAssignableFrom(javaType))
      return resultSet.getString(columnName);
    
    throw new RuntimeException("Could not read column '" + columnName + "' with Java type '" + javaType + "' from the ResultSet!");
  }

  /**
   * Returns a Singleton instance of the <code>DefaultResultSetReader</code>.
   */
  public static ResultSetColumnReader getInstance() {
    return instance;
  }

}
