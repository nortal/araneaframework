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

package org.araneaframework.backend.list.helper.naming;

import java.util.HashMap;
import java.util.Map;

/**
 * Improved naming conventions between list fields and database columns
 * which enables to defined custom prefixes for database column names.
 * <p>
 * If one is using multiple tables (with aliases) in one SELECT,
 * {@link #addPrefix(String, String)} should be called to transform list field names
 * correspondingly into database column names.
 * </p>
 * <p>
 * Field names are transformed into database column names as following:<br/>
 * <ul>
 * <li>The full name is split into prefix and suffix (following to the last dot).
 * <li>If {@link #addPrefix(String, String)} has been called with the current prefix
 *     it is replaced with the corresponding String (table alias).
 * <li>Otherwise all dots in the prefix and suffix are converted into underscores.
 * <li>Before each upper case letter followed by lower case an underscore is inserted.
 * </ul>
 * Field names are transformed into database column aliases as following:<br/><br/>
 * <ul>
 * <li>All dots are converted into underscores.
 * <li>Before each upper case letter followed by lower case an underscore is inserted.
 * </ul>
 * </p>
 * 
 * @see StandardNamingStrategy
 * 
 * @author Rein Raudjärv
 * 
 * @since 1.1
 */
public class PrefixMapNamingStrategy extends StandardNamingStrategy {

	/** Field name prefix --> Database column name prefix */
	private Map customPrefixes = new HashMap();

	/**
	 * Add a mapping between a prefix in <b>field name</b>
	 * and a prefix in <b>database column name</b> (usually a table alias). 
	 * <p>
	 * E.g.
	 * addPrefix("location", "L")
	 * addPrefix("mother.mother", "MM")
	 * 
	 * @param fieldNamePrefix prefix of the field name (can also be <code>null</code>).
	 * @param columnNamePrefix prefix of the database column name (can also be <code>null</code>).
	 */
	public PrefixMapNamingStrategy addPrefix(String fieldNamePrefix, String columnNamePrefix) {
		customPrefixes.put(fieldNamePrefix, columnNamePrefix);
		return this;
	}

	@Override
  protected String resolvePrefix(String fieldNamePrefix) {
		if (customPrefixes.containsKey(fieldNamePrefix)) {
			return (String) customPrefixes.get(fieldNamePrefix);
		}
		return addUnderscores(fieldNamePrefix);
	}

}
