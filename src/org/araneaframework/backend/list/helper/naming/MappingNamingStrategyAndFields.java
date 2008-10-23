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

package org.araneaframework.backend.list.helper.naming;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.regexp.RE;
import org.araneaframework.backend.list.helper.ListSqlHelper;
import org.araneaframework.backend.list.helper.fields.Fields;

/**
 * Naming conventions and list of fields provider which is based on mappings.
 * <p>
 * There are 3 types of fields:
 * <ul>
 * <li>Fields presented in <code>SELECT</code> and are read from the
 * {@link ResultSet} - use methods {@link #addMapping(String, String, String)}
 * and {@link #addMapping(String, String)}</li>
 * <li>Fields presented in <code>SELECT</code> but are not read from the
 * {@link ResultSet} - use method
 * {@link #addDatabaseFieldMapping(String, String, String)} and
 * {@link #addDatabaseFieldMapping(String, String)}</li>
 * <li>Fields not presented in <code>SELECT</code> but which are read from
 * the {@link ResultSet} - use method
 * {@link #addResultSetMapping(String, String)}</li>
 * </ul>
 * The term <code>SELECT</code> refers to the corresponding SQL segment
 * constructed by Aranea Lists. That may be wrapped inside another SQL clause
 * for example.
 * </p>
 * <p>
 * Each list field must be added together with it's corresponding column name
 * (and alias).
 * </p>
 * <p>
 * This implementation is based on the {@link ListSqlHelper} methods up to
 * Aranea MVC 1.1-M5.
 * </p>
 * 
 * @see NamingStrategy
 * @see ListSqlHelper#getMappingNamingStrategyAndFields()
 * @author Rein Raudjärv
 * @since 1.1
 */
public class MappingNamingStrategyAndFields implements NamingStrategy, Fields {

  private static final Log log = LogFactory.getLog(MappingNamingStrategyAndFields.class);

  /**
   * Field name --&gt; Database column name (all these fields are in
   * <code>SELECT</code>)
   */
  private Map fieldToColumnName = new HashMap();

  /**
   * Field name --&gt; Database column alias (all these fields are in
   * <code>SELECT</code>)
   */
  private Map fieldToColumnAlias = new LinkedHashMap();

  /**
   * Field name --&gt; Result set column name (all these fields are in the result
   * set)
   */
  private Map fieldToResultSetColumn = new LinkedHashMap();

  /**
   * Adds a <b>field name</b> to database <b>column name</b> and <b>column
   * alias</b> mapping.
   * <p>
   * A given field is listed in the <code>SELECT</code> but is not read from
   * the {@link ResultSet}.
   * 
   * @param fieldName field name.
   * @param columnName database column name.
   * @param columnAlias database column alias.
   * @see #addMapping(String, String, String)
   * @see #addMapping(String, String)
   * @see #addDatabaseFieldMapping(String, String)
   * @see #addResultSetMapping(String, String)
   */
  public void addDatabaseFieldMapping(String fieldName, String columnName,
      String columnAlias) {
    this.fieldToColumnName.put(fieldName, columnName);
    this.fieldToColumnAlias.put(fieldName, columnAlias);
  }

  /**
   * Adds a <b>field name</b> to database <b>column name</b> mapping.
   * <p>
   * A given field is listed in the <code>SELECT</code> but is not read from
   * the {@link ResultSet}.
   * </p>
   * <p>
   * The corresponding <b>column alias</b> is generated automatically.
   * 
   * @param fieldName field name.
   * @param columnName database column name.
   * @see #addMapping(String, String, String)
   * @see #addMapping(String, String)
   * @see #addDatabaseFieldMapping(String, String, String)
   * @see #addResultSetMapping(String, String)
   */
  public void addDatabaseFieldMapping(String fieldName, String columnName) {
    addDatabaseFieldMapping(fieldName, columnName, createAlias(columnName));
  }

  /**
   * Adds a <b>field name</b> to database <b>column alias</b> mapping.
   * <p>
   * A given field is not listed in the <code>SELECT</code> but is read from
   * the {@link ResultSet}.
   * </p>
   * 
   * @param fieldName field name.
   * @param columnAlias database column name in the result set.
   * @see #addMapping(String, String, String)
   * @see #addMapping(String, String)
   * @see #addDatabaseFieldMapping(String, String, String)
   * @see #addDatabaseFieldMapping(String, String)
   */
  public void addResultSetMapping(String fieldName, String columnAlias) {
    this.fieldToResultSetColumn.put(fieldName, columnAlias);
  }

  /**
   * Adds a <b>field name</b> to database <b>column name</b> and <b>column
   * alias</b> mapping.
   * <p>
   * A given field is listed in the <code>SELECT</code> and is read from the
   * {@link ResultSet}.
   * 
   * @param fieldName field name.
   * @param columnName database column name.
   * @param columnAlias database column alias.
   * @see #addMapping(String, String)
   * @see #addDatabaseFieldMapping(String, String, String)
   * @see #addDatabaseFieldMapping(String, String)
   * @see #addResultSetMapping(String, String)
   */
  public void addMapping(String fieldName, String columnName, String columnAlias) {
    addDatabaseFieldMapping(fieldName, columnName, columnAlias);
    addResultSetMapping(fieldName, columnAlias);
  }

  /**
   * Adds a <b>field name</b> to database <b>column name</b>.
   * <p>
   * A given field is listed in the <code>SELECT</code> and is read from the
   * {@link ResultSet}.
   * </p>
   * <p>
   * The corresponding <b>column alias</b> is generated automatically.
   * 
   * @param fieldName field name.
   * @param columnName database column name.
   * @see #addMapping(String, String, String)
   * @see #addDatabaseFieldMapping(String, String, String)
   * @see #addDatabaseFieldMapping(String, String)
   * @see #addResultSetMapping(String, String)
   */
  public void addMapping(String fieldName, String columnName) {
    addMapping(fieldName, columnName, createAlias(columnName));
  }

  // Fields

  public Collection getNames() {
    return fieldToColumnAlias.keySet();
  }

  public Collection getResultSetNames() {
    return fieldToResultSetColumn.keySet();
  }

  // NamingStrategy

  public String fieldToColumnName(String variableName) {
    return (String) fieldToColumnName.get(variableName);
  }

  public String fieldToColumnAlias(String variableName) {
    String result = (String) fieldToColumnAlias.get(variableName);
    if (result == null) {
      result = (String) fieldToResultSetColumn.get(variableName);
    }
    return result;
  }

  // Generating Aliases

  /**
   * @return a new <b>column alias</b>.
   */
  protected String createAlias(String columnName) {
    // Remove prefix
    String tmp = columnName.substring(columnName.lastIndexOf('.') + 1);

    if (!new RE("[a-zA-Z0-9_]*").match(tmp)) {
      tmp = "alias";
    }

    // Make unique
    String alias = tmp;
    int index = 0;
    while (this.fieldToColumnAlias.containsValue(alias)) {
      alias = tmp + index++;
    }

    if (log.isDebugEnabled()) {
      log.debug("Generated '" + alias + "' as alias for field '" + columnName
          + "'");
    }

    return alias;
  }
}
