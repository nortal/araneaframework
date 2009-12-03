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
  protected String createItemRangeQuery(String fromSql, String customWhereSql, String customOrderbySql) {
    StringBuffer query = new StringBuffer(super.createItemRangeQuery(fromSql, customWhereSql, customOrderbySql));

    if (this.itemRangeCount != null) {
      query.append(" LIMIT ?");
    }

    if (this.itemRangeStart != null) {
      query.append(" OFFSET ?");
    }

    return query.toString();
  }

  @Override
  protected List<Object> getItemRangeQueryParams(Object[] customWhereArgs, Object[] customOrderbyArgs) {
    List<Object> params = super.getItemRangeQueryParams(customWhereArgs, customOrderbyArgs);

    if (this.itemRangeCount != null) {
      params.add(this.itemRangeCount);
    }

    if (this.itemRangeStart != null) {
      params.add(this.itemRangeStart);
    }

    return params;
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
}
