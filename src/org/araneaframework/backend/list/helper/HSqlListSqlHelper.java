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

package org.araneaframework.backend.list.helper;

import java.util.List;
import javax.sql.DataSource;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.core.Assert;

/**
 * @author Rein Raudjärv (rein@araneaframework.org)
 */
public class HSqlListSqlHelper extends ListSqlHelper {

  private static final String SELECT_PREFIX = "SELECT ";

  protected SqlStatement statement = new SqlStatement();

  protected String countSqlQuery = null;

  public HSqlListSqlHelper(DataSource dataSource, ListQuery query) {
    super(dataSource, query);
  }

  public HSqlListSqlHelper(DataSource dataSource) {
    super(dataSource);
  }

  public HSqlListSqlHelper(ListQuery query) {
    super(query);
  }

  public HSqlListSqlHelper() {
    super();
  }

  protected SqlStatement getCountSqlStatement() {
    if (this.countSqlQuery != null) {
      return new SqlStatement(this.countSqlQuery, this.statement.getParams());
    }

    String temp = new StringBuffer("SELECT COUNT(*) FROM (").append(
        this.statement.getQuery()).append(")").toString();

    return new SqlStatement(temp, this.statement.getParams());
  }

  protected SqlStatement getRangeSqlStatement() {
    Assert.isTrue(this.statement.getQuery().toUpperCase().startsWith(
        SELECT_PREFIX), "SQL query must start with SELECT");

    SqlStatement result;
    if (isShowAll()) {
      result = (SqlStatement) this.statement.clone();
    } else {
      StringBuffer query = new StringBuffer();
      query.append("SELECT LIMIT ? ? ");
      query.append(this.statement.getQuery().substring(SELECT_PREFIX.length()));
      result = new SqlStatement(query.toString());
      result.addParam(itemRangeStart);
      result.addParam(itemRangeCount);
      result.addAllParams(this.statement.getParams());
    }

    return result;
  }

  protected boolean isShowAll() {
    return (itemRangeStart == null || itemRangeStart.longValue() == 0)
        && (itemRangeCount == null || itemRangeCount.longValue() == Long.MAX_VALUE);
  }

  /**
   * Sets the SQL query that will be used to retrieve the item range from the
   * list and count the items. The SQL query must start with SELECT expression
   * withoud the word "SELECT".
   * 
   * @param sqlQuery the SQL query that will be used to retrieve the item range
   *            from the list and count the items.
   */
  public void setSqlQuery(String sqlQuery) {
    this.statement.setQuery(sqlQuery);
  }

  /**
   * Sets the SQL query used to count the items in the database.
   * 
   * @param countSqlQuery the SQL query used to count the items in the database.
   */
  public void setCountSqlQuery(String countSqlQuery) {
    this.countSqlQuery = countSqlQuery;
  }

  /**
   * Adds a <code>NULL</code> <code>PreparedStatement</code> parameter for
   * later setting.
   * 
   * @param valueType the type of the NULL value.
   */
  public void addNullParam(int valueType) {
    this.statement.addNullParam(valueType);
  }

  /**
   * Adds a <code>PreparedStatement</code> parameter for later setting.
   * 
   * @param param a <code>PreparedStatement</code> parameter.
   */
  public void addStatementParam(Object param) {
    this.statement.addParam(param);
  }

  /**
   * Adds <code>PreparedStatement</code> parameters for later setting.
   * 
   * @param params <code>PreparedStatement</code> parameters.
   */
  public void addStatementParams(List<Object> params) {
    this.statement.addAllParams(params);
  }
}
