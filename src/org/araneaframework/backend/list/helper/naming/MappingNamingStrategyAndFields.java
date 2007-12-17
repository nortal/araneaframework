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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.list.helper.ListSqlHelper;
import org.araneaframework.backend.list.helper.fields.Fields;


/**
 * Naming conventions and list of fields provider which is based on mappings. 
 * <p>
 * Each list field must be added together with it's corresponding column name (and alias).
 * </p>
 * <p>
 * This implementation is based on the {@link ListSqlHelper} methods up to Aranea MVC 1.1-M5.
 * </p>
 * <p>
 * The following methods were removed due to their stupidity:
 * <ul>
 * <li><code>addDatabaseFieldMapping(String, String, String)</code></li>
 * <li><code>addResultSetMapping(String, String)</code></li>
 * <li><code>addMapping(String, String, String, String)</code></li>
 * </ul>
 * 
 * @see NamingStrategy
 * @see ListSqlHelper#getMappingNamingStrategyAndFields()
 * 
 * @author Rein Raudjärv
 * 
 * @since 1.1
 */
public class MappingNamingStrategyAndFields implements NamingStrategy, Fields {

	private static final Log log = LogFactory.getLog(MappingNamingStrategyAndFields.class);
	
	/** Field name --> Database column name (not all these fields have to be in <code>SELECT</code>) */
	private Map fieldToColumnName = new HashMap();
	/** Field name --> Database column alias (all these fields are in <code>SELECT</code> and result set) */
	private Map fieldToColumnAlias = new LinkedHashMap();
	
	/**
	 * Adds a <b>field name</b> to database <b>column name</b> and <b>column alias</b> mapping.
	 * <p>
	 * A given field is not listed in the <code>SELECT</code> clause.
	 * 
	 * @param fieldName
	 *            field name.
	 * @param columnName
	 *            database column name.
	 *            
	 * @see #addMapping(String, String)
	 * @see #addMapping(String, String, String)
	 */
	public void addDatabaseFieldMapping(String fieldName, String columnName) {
		this.fieldToColumnName.put(fieldName, columnName);
	}
	
	/**
	 * Adds a <b>field name</b> to database <b>column name</b> and <b>column alias</b> mapping.
	 * <p>
	 * A given field is also listed in the <code>SELECT</code> clause.
	 * 
	 * @param fieldName
	 *            field name.
	 * @param columnName
	 *            database column name.
	 * @param columnAlias
	 *            database column alias.
	 *
	 * @see #addMapping(String, String)
	 * @see #addDatabaseFieldMapping(String, String)
	 */
	public void addMapping(String fieldName, String columnName, String columnAlias) {
		addDatabaseFieldMapping(fieldName, columnName);
		this.fieldToColumnAlias.put(fieldName, columnAlias);
	}
	
	/**
	 * Adds a <b>field name</b> to database <b>column name</b> and <b>column alias</b> mapping.
	 * <p>
	 * A given field is also listed in the <code>SELECT</code> clause.
	 * </p>
	 * <p>
	 * The corresponding <b>column alias</b> is generated automatically.
	 * 
	 * @param fieldName
	 *            field name.
	 * @param columnName
	 *            database column name.
	 *
	 * @see #addMapping(String, String, String)
	 * @see #addDatabaseFieldMapping(String, String)
	 */	
	public void addMapping(String fieldName, String columnName) {
		String dbAlias = createAlias(columnName);
		addMapping(fieldName, columnName, dbAlias);
	}

	/**
	 * @return corresponding <b>column alias</b>.
	 */
	protected String createAlias(String columnName) {
		// Remove prefix
		String tmp = columnName.substring(columnName.lastIndexOf('.') + 1);
		// TODO: replace with 1.3 compatible regexp check
		if (!StringUtils.containsOnly(tmp, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_")) {
			tmp = "alias";
		}
		
		// Make unique
		String alias = tmp;
		int index = 0;
		while (this.fieldToColumnAlias.containsValue(alias)) {
			alias = tmp + index++;
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Generated '" + alias + "' as alias for field '" + columnName + "'");
		}
		return alias;
	}
	
	public String fieldToColumnName(String variableName) {
		return (String) fieldToColumnName.get(variableName);
	}
	
	public String fieldToColumnAlias(String variableName) {
		return (String) fieldToColumnAlias.get(variableName);
	}

	public Collection getNames() {
		return fieldToColumnAlias.keySet();
	}

}
