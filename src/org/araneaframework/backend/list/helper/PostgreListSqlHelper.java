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
import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.helper.builder.expression.PostgreExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.helper.builder.expression.StandardExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.backend.list.sqlexpr.SqlCollectionExpression;
import org.araneaframework.backend.list.sqlexpr.constant.SqlStringExpression;

/**
 * Extends the <code>ListSqLHelper</code> to support PostgreSQL database queries.
 * 
 * @author Roman Tekhov
 * @since 1.1.3
 */
public class PostgreListSqlHelper extends ListSqlHelper {

  protected SqlStatement statement = new SqlStatement();

  protected String countSqlQuery;

  public PostgreListSqlHelper(DataSource dataSource, ListQuery query) {
    super(dataSource, query);
  }

  public PostgreListSqlHelper(DataSource dataSource) {
    super(dataSource);
  }

  public PostgreListSqlHelper(ListQuery query) {
    super(query);
  }

  public PostgreListSqlHelper() {}

  @Override
  protected SqlStatement getCountSqlStatement() {
    if (countSqlQuery != null) {
      return new SqlStatement(countSqlQuery, statement.getParams());
    }

    String temp = new StringBuffer("SELECT COUNT(*) FROM (").append(statement.getQuery()).append(") t").toString();

    return new SqlStatement(temp, this.statement.getParams());
  }

  @Override
  protected SqlStatement getRangeSqlStatement() {
    StringBuffer sb = new StringBuffer(this.statement.getQuery());

    if (this.itemRangeCount != null) {
      sb.append(" LIMIT ?");
    }

    sb.append(" OFFSET ?");

    // Create a SQL statement to hold the query and its parameters:
    SqlStatement rangeStmt = new SqlStatement(sb.toString());
    rangeStmt.addAllParams(this.statement.getParams());

    if (this.itemRangeCount != null) {
      rangeStmt.addParam(this.itemRangeCount);
    }

    rangeStmt.addParam(this.itemRangeStart);
    return rangeStmt;
  }

  @Override
  protected SqlExpression getFieldsSqlExpression() {
    SqlCollectionExpression result = new SqlCollectionExpression();
    for (String fieldName : this.fields.getNames()) {
      String dbField = this.namingStrategy.fieldToColumnName(fieldName);
      String dbAlias = this.namingStrategy.fieldToColumnAlias(fieldName);

      if (dbAlias.equals(dbField)) {
        result.add(new SqlStringExpression(dbField));
      } else {
        result.add(new SqlStringExpression(new StringBuffer(dbField).append(" AS ").append(dbAlias).toString()));
      }
    }
    return result;
  }

  @Override
  protected StandardExpressionToSqlExprBuilder createFilterSqlExpressionBuilder() {
    return new PostgreExpressionToSqlExprBuilder();
  }

  @Override
  public void setCountSqlQuery(String countSqlQuery) {
    this.countSqlQuery = countSqlQuery;
  }

  @Override
  public void setSqlQuery(String sqlQuery) {
    this.statement.setQuery(sqlQuery);
  }

  @Override
  public void addNullParam(int valueType) {
    this.statement.addNullParam(valueType);
  }

  @Override
  public void addStatementParam(Object param) {
    this.statement.addParam(param);
  }

  @Override
  public void addStatementParams(List<Object> params) {
    this.statement.addAllParams(params);
  }
}
