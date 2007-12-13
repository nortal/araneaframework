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

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.araneaframework.uilib.list.util.Converter;

/**
 * ResultSetColumnReader that can have custom converters for each column.
 * 
 * @author Rein Raudjärv
 */
public class StandardResultSetColumnReader extends FilterResultSetColumnReader {

	private final Map converters = new HashMap();
	
	public StandardResultSetColumnReader(ResultSetColumnReader child) {
		super(child);
	}
	
	/**
	 * Adds a deconverter for <code>ResultSet</code>.
	 * <p>
	 * The converter is used to {@link Converter#reverseConvert(Object)}
	 * values from <code>ResultSet</code>.
	 * 
	 * @param columnName
	 *            ResultSet column name.
	 * @param converter
	 *            converter that is used by <code>reverseConvert()</code>
	 *            method.
	 */
	public void addResultSetDeconverterForColumn(String columnName, Converter converter) {
		this.converters.put(columnName, converter);
	}
	
	public Object readFromResultSet(String columnName, ResultSet resultSet, Class javaType) {
		// Find converter
		Converter converter = (Converter) converters.get(columnName);
		if (converter == null) {
			// No converter registered
			return super.readFromResultSet(columnName, resultSet, javaType);
		}
		// Converter found
		
		// Change type that is used to read data
		javaType = converter.getDestinationType();
		
		// Read data
		Object value = super.readFromResultSet(columnName, resultSet, javaType);
		
		// Convert
		value = converter.reverseConvert(value);
		
		return value;
	}

}
