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
import java.util.HashMap;
import java.util.Map;
import org.araneaframework.uilib.list.util.Converter;

/**
 * ResultSetColumnReader that can have custom converters for each column.
 * <p>
 * This implementation wraps an existing {@link ResultSetColumnReader} by adding
 * additional behavior if a certain column is being read.
 * </p>
 * To add a custom converter for a column use method
 * {@link #addResultSetDeconverterForColumn(String, Converter)}. For that
 * column {@link Converter#reverseConvert(Object)} is invoked and
 * {@link Converter#getDestinationType()} is used as a Java type retrieved from
 * the {@link ResultSet}.
 * 
 * @author Rein Raudjärv
 * @since 1.1
 */
@SuppressWarnings("unchecked")
public class ConverterBasedColumnReader extends FilterResultSetColumnReader {

  private final Map<String, Converter> converters = new HashMap<String, Converter>();

  public ConverterBasedColumnReader(ResultSetColumnReader child) {
    super(child);
  }

  /**
   * Adds a de-converter for <code>ResultSet</code>.
   * <p>
   * The converter is used to {@link Converter#reverseConvert(Object)} values
   * from <code>ResultSet</code>.
   * 
   * @param columnName ResultSet column name.
   * @param converter converter that is used by <code>reverseConvert()</code>
   *            method.
   */
  public void addResultSetDeconverterForColumn(String columnName,
      Converter<?, ?> converter) {
    this.converters.put(columnName, converter);
  }

  @Override
  public <T> T readFromResultSet(String columnName, ResultSet resultSet, Class<T> javaType) {

    // Find converter
    Converter converter = this.converters.get(columnName);

    if (converter == null) { // No converter registered
      return super.readFromResultSet(columnName, resultSet, javaType);
    }

    // Converter found
    // Change type that is used to read data
    javaType = converter.getDestinationType();

    // Read data
    Object value = super.readFromResultSet(columnName, resultSet, javaType);

    // Convert
    return (T) converter.reverseConvert(value);
  }
}
