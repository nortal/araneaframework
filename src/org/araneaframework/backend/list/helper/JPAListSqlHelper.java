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
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.core.Assert;

/**
 * A list helper that makes use of JPA methods to fetch list items.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public class JPAListSqlHelper extends HSqlListSqlHelper {

  protected EntityManager entityManager;

  public JPAListSqlHelper(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public JPAListSqlHelper(ListQuery query) {
    super(query);
  }

  public JPAListSqlHelper(EntityManager entityManager, ListQuery query) {
    this(query);
    this.entityManager = entityManager;
  }

  @Override
  protected JPAStatement getCountSqlStatement() {
    if (this.countSqlQuery != null) {
      return new JPAStatement(this.countSqlQuery, this.statement.getParams());
    }

    String temp = new StringBuffer(SELECT_COUNT_PREFIX).append(this.statement.getQuery()).append(')').toString();
    return new JPAStatement(temp, this.statement.getParams());
  }

  @Override
  protected JPAStatement getRangeSqlStatement() {
    Assert.isTrue(StringUtils.startsWithIgnoreCase(this.statement.getQuery(), SELECT_PREFIX),
        "SQL query must start with SELECT");

    JPAStatement result;
    if (isShowAll()) {
      result = (JPAStatement) this.statement.clone();
    } else {
      StringBuffer query = new StringBuffer();
      query.append(SELECT_LIMIT_PREFIX);
      query.append(this.statement.getQuery().substring(SELECT_PREFIX.length()));
      result = new JPAStatement(query.toString());
      result.addParam(this.itemRangeStart);
      result.addParam(this.itemRangeCount);
      result.addAllParams(this.statement.getParams());
    }

    return result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> ListItemsData<T> execute(Class<T> target) {
    ListItemsData<T> result = new ListItemsData<T>();
    result.setTotalCount(getTotalCount(entityManager));
    result.setItemRange(getListItems(entityManager));
    return result;
  }

  protected Long getTotalCount(EntityManager entityManager) {
    JPAStatement countStmt = getCountSqlStatement();
    Query query = entityManager.createQuery(countStmt.getQuery());
    countStmt.propagateStatementWithParams(query);
    return ((Number) query.getSingleResult()).longValue();
    
  }

  @SuppressWarnings("unchecked")
  protected List getListItems(EntityManager entityManager) {
    JPAStatement rangeStmt = getRangeSqlStatement();
    Query query = entityManager.createQuery(rangeStmt.getQuery());
    rangeStmt.propagateStatementWithParams(query);
    return query.getResultList();
    
  }
}
