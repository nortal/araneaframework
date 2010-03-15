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

/**
 * Overrides <code>ListSqlHelper</code> methods so that the queries would be Oracle-compatible.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 */
public class OracleListSqlHelper extends ListSqlHelper {

  public OracleListSqlHelper(DataSource dataSource, ListQuery query) {
    super(dataSource, query);
  }

  public OracleListSqlHelper(DataSource dataSource) {
    super(dataSource);
  }

  public OracleListSqlHelper(ListQuery query) {
    super(query);
  }

  public OracleListSqlHelper() {
    super();
  }

//  @Override
//  protected String createCountQuery(String fromSql, String customWhereSql) {
//    StringBuffer sb = new StringBuffer();
//    sb.append("SELECT * FROM (SELECT rownum listRowNum, listItemData.* FROM (");
//    sb.append(super.createCountQuery(fromSql, customWhereSql));
//    sb.append(") listItemData) WHERE listRowNum >= ?");
//
//    if (this.itemRangeCount != null) {
//      sb.append(" AND listRowNum <= ?");
//    }
//
//    return sb.toString();
//  }

//  @Override
//  protected List<Object> getCountQueryParams(Object[] customWhereArgs) {
//    List<Object> params = super.getCountQueryParams(customWhereArgs);
//
//    params.add(this.itemRangeStart.longValue() + 1);
//
//    if (this.itemRangeCount != null) {
//      params.add(this.itemRangeStart.longValue() + this.itemRangeCount.longValue());
//    }
//
//    return params;
//  }

  @Override
  protected String createItemRangeQuery(String fromSql, String customWhereSql, String customOrderbySql) {
    StringBuffer sb = new StringBuffer();
    sb.append("SELECT * FROM (SELECT rownum listRowNum, listItemData.* FROM (");
    sb.append(super.createItemRangeQuery(fromSql, customWhereSql, customOrderbySql));
    sb.append(") listItemData) WHERE listRowNum >= ?");

    if (this.itemRangeCount != null) {
      sb.append(" AND listRowNum <= ?");
    }

    return sb.toString();
  }

  @Override
  protected List<Object> getItemRangeQueryParams(Object[] customWhereArgs, Object[] customOrderbyArgs) {
    List<Object> params = super.getItemRangeQueryParams(customWhereArgs, customOrderbyArgs);

    params.add(this.itemRangeStart.longValue() + 1);

    if (this.itemRangeCount != null) {
      params.add(this.itemRangeStart.longValue() + this.itemRangeCount.longValue());
    }

    return params;
  }
}
