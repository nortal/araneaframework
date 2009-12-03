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

import java.util.ArrayList;

import java.util.List;

import javax.sql.DataSource;
import org.araneaframework.backend.list.model.ListQuery;

/**
 * Overrides <code>ListSqlHelper</code> methods so that the queries would be HSQLDB-compatible.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 */
public class HSqlListSqlHelper extends ListSqlHelper {

  public HSqlListSqlHelper() {}

  public HSqlListSqlHelper(DataSource dataSource, ListQuery query) {
    super(dataSource, query);
  }

  public HSqlListSqlHelper(DataSource dataSource) {
    super(dataSource);
  }

  public HSqlListSqlHelper(ListQuery query) {
    super(query);
  }

  @Override
  protected String createItemRangeQuery(String fromSql, String customWhereSql, String customOrderbySql) {
    if (isShowAll()) {
      return super.createItemRangeQuery(fromSql, customWhereSql, customOrderbySql);
    }

    StringBuffer sb = new StringBuffer("SELECT LIMIT ? ? ");
    sb.append(getDatabaseFields());

    sb.append(" FROM ");
    sb.append(fromSql);

    if (customWhereSql == null) {
      sb.append(getDatabaseFilterWith(" WHERE ", ""));
    } else {
      sb.append(" WHERE (");
      sb.append(customWhereSql);
      sb.append(")");
      sb.append(getDatabaseFilterWith(" AND ", ""));
    }

    if (customOrderbySql == null) {
      sb.append(getDatabaseOrderWith(" ORDER BY ", ""));
    } else {
      sb.append(" ORDER BY ");
      sb.append(customOrderbySql);
      sb.append(getDatabaseOrderWith(", ", ""));
    }

    return sb.toString();
  }

  @Override
  protected List<Object> getItemRangeQueryParams(Object[] customWhereArgs, Object[] customOrderbyArgs) {
    List<Object> params = new ArrayList<Object>();

    if (!isShowAll()) {
      params.add(this.itemRangeStart);
      params.add(this.itemRangeCount);
    }

    params.addAll(super.getItemRangeQueryParams(customWhereArgs, customOrderbyArgs));
    return params;
  }
}
