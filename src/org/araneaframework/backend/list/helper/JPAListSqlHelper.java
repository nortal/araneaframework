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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * A list helper that makes use of JPA methods to fetch list items.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public class JPAListSqlHelper extends ListSqlHelper {

  protected EntityManager entityManager;

  protected boolean beginFromSelect = false;

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

  /**
   * Specifies whether the composed query must begin with "SELECT" or with "FROM". By default begins with "FROM". Set
   * <code>beginFromSelect</code> to <code>true</code> to make the query begin with "SELECT";
   * 
   * @param beginFromSelect A Boolean indicating whether the composed query must begin with "SELECT".
   */
  public void setBeginFromSelect(boolean beginFromSelect) {
    this.beginFromSelect = beginFromSelect;
  }

  /**
   * Override to generate custom JPA query.
   */
  @Override
  protected String createItemRangeQuery(String fromSql, String customWhereSql, String customOrderbySql) {
    StringBuffer sb = new StringBuffer();

    if (this.beginFromSelect) {
      sb.append("select ").append(getDatabaseFields());
    }

    sb.append(" from ");
    sb.append(fromSql);

    if (customWhereSql == null) {
      sb.append(getDatabaseFilterWith(" where ", ""));
    } else {
      sb.append(" where (");
      sb.append(customWhereSql);
      sb.append(")");
      sb.append(getDatabaseFilterWith(" and ", ""));
    }

    if (customOrderbySql == null) {
      sb.append(getDatabaseOrderWith(" order by ", ""));
    } else {
      sb.append(" order by ");
      sb.append(customOrderbySql);
      sb.append(getDatabaseOrderWith(", ", ""));
    }

    return sb.toString();
  }

  /**
   * An override to customize executing queries more suitable for JPA queries.
   */
  @Override
  public <T> T execute(ConnectionCallback<T> action) {
    prepareQueries();
    try {
      return action.doInConnection(null);
    } catch (SQLException e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

  /**
   * Override to use custom JPA <b>list data</b> query callback.
   */
  @Override
  public <T> ConnectionCallback<ListItemsData<T>> getListItemsDataCallback(ResultReader<T> reader) {
    return new ListItemsDataCallback<T>(getCountSqlCallback(), getItemRangeSqlCallback(reader));
  }

  /**
   * Override to use custom JPA <b>item range</b> query callback.
   */
  @Override
  public <T> ConnectionCallback<List<T>> getItemRangeSqlCallback(ResultReader<T> reader) {
    return new JPASelectCallback<T>(this.entityManager);
  }

  /**
   * Override to use custom JPA <b>total count</b> query callback.
   */
  @Override
  public ConnectionCallback<Long> getCountSqlCallback() {
    return new JPACountCallback(this.entityManager);
  }

  /**
   * Override to not waist time result reader creation, because it won't be used with JPA.
   */
  @Override
  public <T> ResultReader<T> createBeanResultReader(Class<T> itemClass) {
    return null;
  }

  /**
   * Helper method that creates a JPA query (using given statement) and sets the query parameters (using the given
   * statement).
   * 
   * @param entityManager The entity manager to use for creating the JPA query.
   * @param statement The query data.
   * @return The created but not executed JPA query.
   */
  protected static Query createQuery(EntityManager entityManager, SqlStatement statement) {
    return createQuery(entityManager, statement.getQuery(), statement.getParams());
  }

  /**
   * Helper method that creates a JPA query (using given query string) and sets the query parameters (using the given
   * query parameters).
   * 
   * @param entityManager The entity manager to use for creating the JPA query.
   * @param queryStr The JPA query string to use in the query.
   * @param params The JPA query parameters to use in the query.
   * @return The created but not executed JPA query.
   */
  protected static Query createQuery(EntityManager entityManager, String queryStr, List<Object> params) {
    Query query = entityManager.createQuery(queryStr);

    if (params != null) {
      for (int i = 1; i <= params.size(); i++) {
        Object parameter = params.get(i - 1);
        if (parameter instanceof NullValue) {
          query.setParameter(i, null);
        } else if (parameter != null && parameter.getClass() == java.util.Date.class) {
          query.setParameter(i, (java.util.Date) parameter, TemporalType.TIMESTAMP);
        } else if (parameter != null && parameter.getClass() == java.util.Calendar.class) {
          query.setParameter(i, (java.util.Calendar) parameter, TemporalType.TIMESTAMP);
        } else {
          query.setParameter(i, parameter);
        }
      }
    }
    return query;
  }

  /**
   * A custom override to fetch the total items count using JPA entity manager.
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   * @since 2.0
   */
  protected class JPACountCallback implements ConnectionCallback<Long> {

    private EntityManager entityManager;

    public JPACountCallback(EntityManager entityManager) {
      Assert.notNullParam(entityManager, "entityManager");
      this.entityManager = entityManager;
    }

    public Long doInConnection(Connection con) throws SQLException {
      return ((Number) createQuery(this.entityManager, getCountSqlStatement()).getSingleResult()).longValue();
    }
  }

  /**
   * A custom override to fetch the item range using JPA entity manager.
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   * @since 2.0
   */
  protected class JPASelectCallback<I> implements ConnectionCallback<List<I>> {

    private EntityManager entityManager;

    public JPASelectCallback(EntityManager entityManager) {
      Assert.notNullParam(entityManager, "entityManager");
      this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public List<I> doInConnection(Connection con) throws SQLException {
      Query query = createQuery(entityManager, getRangeSqlStatement());

      if (itemRangeStart != null) {
        query.setFirstResult(itemRangeStart.intValue());
      }
      if (itemRangeCount != null) {
        query.setMaxResults(itemRangeCount.intValue());
      }

      return query.getResultList();
    }
  }
}
