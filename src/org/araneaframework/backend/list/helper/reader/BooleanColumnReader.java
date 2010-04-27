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
import org.apache.commons.lang.ObjectUtils;

/**
 * ResultSetColumnReader for Boolean type.
 * <p>
 * This implementation wraps an existing {@link ResultSetColumnReader} by adding additional behavior for boolean Java
 * type.
 * 
 * @author Rein Raudjärv
 * @since 1.1
 */
public class BooleanColumnReader extends FilterResultSetColumnReader {

  private final Object trueValue;

  private final Object falseValue;

  private final Object nullValue;

  public BooleanColumnReader(ResultSetColumnReader child, Object trueValue, Object falseValue, Object nullValue) {
    super(child);
    this.trueValue = trueValue;
    this.falseValue = falseValue;
    this.nullValue = nullValue;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T readFromResultSet(String columnName, ResultSet resultSet, Class<T> javaType) {
    if (Boolean.class.equals(javaType)) {
      Object value = super.readFromResultSet(columnName, resultSet, Object.class);

      if (ObjectUtils.equals(this.trueValue, value)) {
        return (T) Boolean.TRUE;
      } else if (ObjectUtils.equals(this.falseValue, value)) {
        return (T) Boolean.FALSE;
      } else if (ObjectUtils.equals(this.nullValue, value)) {
        return null;
      }
      throw new IllegalStateException("Unexpected value '" + value + "' (expected either '" + this.trueValue + "', '"
          + this.falseValue + "', or '" + this.nullValue + "')");
    }
    // Other type
    return super.readFromResultSet(columnName, resultSet, javaType);
  }
}
