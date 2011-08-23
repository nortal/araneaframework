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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.helper.builder.ValueConverter;
import org.araneaframework.backend.list.helper.builder.compexpr.StandardCompExprToSqlExprBuilder;
import org.araneaframework.backend.list.helper.builder.expression.StandardExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.helper.fields.ConcatFields;
import org.araneaframework.backend.list.helper.fields.Fields;
import org.araneaframework.backend.list.helper.naming.ColumnNameVariableResolver;
import org.araneaframework.backend.list.helper.naming.NamingStrategy;
import org.araneaframework.backend.list.helper.naming.OrNamingStrategy;
import org.araneaframework.backend.list.helper.reader.DefaultResultSetColumnReader;
import org.araneaframework.backend.list.helper.reader.ResultSetColumnReader;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.backend.list.sqlexpr.SqlCollectionExpression;
import org.araneaframework.backend.list.sqlexpr.SqlExpressionUtil;
import org.araneaframework.backend.list.sqlexpr.constant.SqlStringExpression;
import org.araneaframework.backend.util.BeanMapper;
import org.araneaframework.core.exception.AraneaRuntimeException;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * This class provides an SQL based implementation of the list. It takes care of the filtering, ordering and returning
 * data to the web components. Implementations should override abstract methods noted in those methods.
 * <p>
 * Note, that all operations on items are made on the list of "processed", that is ordered and filtered items.
 * <p>
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Rein Raudjärv (rein@araneaframework.org)
 * @since 1.1
 */
public abstract class BaseListSqlHelper {

  private static final Log LOG = LogFactory.getLog(BaseListSqlHelper.class);

  protected static final Long DEFAULT_RANGE_START = 0L;

  protected SqlStatement rangeStatement;

  protected SqlStatement countStatement;

  protected Fields fields;

  protected NamingStrategy namingStrategy;

  protected ValueConverter valueConverter;

  protected ResultSetColumnReader resultSetColumnReader;

  // FILTER AND ORDER fields
  protected Expression filterExpr;

  protected ComparatorExpression orderExpr;

  protected VariableResolver variableResolver;

  protected SqlExpression filterSqlExpr;

  protected SqlExpression orderSqlExpr;

  private boolean filterSqlExprInited = false;

  private boolean orderSqlExprInited = false;

  // ITEM RANGE
  protected Long itemRangeStart = DEFAULT_RANGE_START;

  protected Long itemRangeCount;

  // CONNECTION
  protected transient DataSource ds;

  /**
   * Creates <code>ListSqlHelper</code> without initializing any fields.
   */
  public BaseListSqlHelper() {
    this(null, null);
  }

  /**
   * Creates <code>ListSqlHelper</code> and provides it with the <code>DataSource</code>.
   */
  public BaseListSqlHelper(DataSource dataSource) {
    this(dataSource, null);
  }

  /**
   * Creates <code>ListSqlHelper</code> initializing the appropriate fields.
   */
  public BaseListSqlHelper(ListQuery query) {
    this(null, query);
  }

  /**
   * Creates <code>ListSqlHelper</code> initializing the appropriate fields and providing it with the
   * <code>DataSource</code>.
   */
  public BaseListSqlHelper(DataSource dataSource, ListQuery query) {
    setDataSource(dataSource);
    setListQuery(query);
    init();
  }

  protected void init() {
    this.fields = new ConcatFields();
    this.namingStrategy = new OrNamingStrategy();
    this.valueConverter = null;
    this.resultSetColumnReader = DefaultResultSetColumnReader.getInstance();
  }

  // *********************************************************************
  // * PUBLIC METHODS
  // *********************************************************************

  /**
   * Sets the starting index and count of items in the range and filtering and ordering expressions.
   */
  public void setListQuery(ListQuery query) {
    if (query != null) {
      setFilterExpression(query.getFilterExpression());
      setOrderExpression(query.getOrderExpression());
      setItemRangeStart(query.getItemRangeStart());
      setItemRangeCount(query.getItemRangeCount());
    }
  }

  /**
   * Sets the order expression saving it for later automatic SQL query creation.
   * 
   * @see #getDatabaseOrder()
   * @see #getDatabaseOrderWith(String, String)
   * @see #getDatabaseOrderParams()
   */
  public void setOrderExpression(ComparatorExpression orderExpr) {
    this.orderExpr = orderExpr;
  }

  /**
   * Sets the filter expression saving it for later automatic SQL query creation.
   * 
   * @see #getDatabaseFilter()
   * @see #getDatabaseFilterWith(String, String)
   * @see #getDatabaseFilterParams()
   */
  public void setFilterExpression(Expression filterExpr) {
    this.filterExpr = filterExpr;
  }

  /**
   * Sets the (0-based) starting index of the item range. When <code>itemRangeStart</code> is <code>null</code> then
   * it's same as setting <code>0</code>.
   */
  public void setItemRangeStart(Long itemRangeStart) {
    this.itemRangeStart = (Long) ObjectUtils.defaultIfNull(itemRangeStart, DEFAULT_RANGE_START);
  }

  /**
   * Sets the count of items in the range. Count may be <code>null</code>, in which case the number of returned items
   * is not limited.
   */
  public void setItemRangeCount(Long itemRangeCount) {
    this.itemRangeCount = itemRangeCount == null || itemRangeCount == Long.MAX_VALUE ? null : itemRangeCount;
  }

  // *********************************************************************
  // * DATABASE MAPPING AND CONVERTERS
  // *********************************************************************

  public Fields getFields() {
    return this.fields;
  }

  public void setFields(Fields fields) {
    this.fields = fields;
  }

  public NamingStrategy getNamingStrategy() {
    return this.namingStrategy;
  }

  public void setNamingStrategy(NamingStrategy namingStrategy) {
    this.namingStrategy = namingStrategy;
  }

  public void setValueConverter(ValueConverter valueConverter) {
    this.valueConverter = valueConverter;
  }

  public ValueConverter getValueConverter() {
    return this.valueConverter;
  }

  public void setResultSetColumnReader(ResultSetColumnReader resultSetColumnReader) {
    this.resultSetColumnReader = resultSetColumnReader;
  }

  public ResultSetColumnReader getResultSetColumnReader() {
    return this.resultSetColumnReader;
  }

  // *********************************************************************
  // * BUILDING SQL EXPRESSIONS ACCORDING TO ORDERING AND FILTERING
  // *********************************************************************

  /**
   * Returns the fields <code>SqlExpression</code>, which can be used in "SELECT" clause.
   * 
   * @return the fields <code>SqlExpression</code>, which can be used in "SELECT" clause.
   */
  protected SqlExpression getFieldsSqlExpression() {
    SqlCollectionExpression result = new SqlCollectionExpression();
    Assert.notEmpty(this.fields.getNames(), "No fields defined for SELECT.");

    for (Object element : this.fields.getNames()) {
      String variable = (String) element;
      String dbField = this.namingStrategy.fieldToColumnName(variable);
      String dbAlias = this.namingStrategy.fieldToColumnAlias(variable);

      String sql = dbAlias.equals(dbField) ? dbField : new StringBuffer(dbField).append(' ').append(dbAlias).toString();

      result.add(new SqlStringExpression(sql));
    }
    return result;
  }

  /**
   * Returns the order <code>SqlExpression</code>, which can be used in "ORDER BY" clause.
   * 
   * @return the order <code>SqlExpression</code>, which can be used in "ORDER BY" clause.
   */
  protected SqlExpression getOrderSqlExpression() {
    if (!this.orderSqlExprInited && this.orderExpr != null) {
      StandardCompExprToSqlExprBuilder builder = createOrderSqlExpressionBuilder();
      builder.setMapper(createExpressionBuilderResolver());
      this.orderSqlExpr = SqlExpressionUtil.toSql(this.orderExpr, builder);
    }
    this.orderSqlExprInited = true;
    return this.orderSqlExpr;
  }

  /**
   * Returns the filter <code>SqlExpression</code>, which can be used in "WHERE" clause.
   * 
   * @return the filter <code>SqlExpression</code>, which can be used in "WHERE" clause.
   */
  protected SqlExpression getFilterSqlExpression() {
    if (!this.filterSqlExprInited && this.filterExpr != null) {
      StandardExpressionToSqlExprBuilder builder = createFilterSqlExpressionBuilder();
      builder.setMapper(createExpressionBuilderResolver());
      builder.setConverter(this.valueConverter);
      this.filterSqlExpr = SqlExpressionUtil.toSql(this.filterExpr, builder);
    }
    this.filterSqlExprInited = true;
    return this.filterSqlExpr;
  }

  /**
   * Creates new ordering SQL Expression builder.
   */
  protected StandardCompExprToSqlExprBuilder createOrderSqlExpressionBuilder() {
    return new StandardCompExprToSqlExprBuilder();
  }

  /**
   * Creates new filtering SQL Expression builder.
   */
  protected StandardExpressionToSqlExprBuilder createFilterSqlExpressionBuilder() {
    return new StandardExpressionToSqlExprBuilder();
  }

  // *******************************************************************************************************************
  // The following methods use the expression methods above and convert expressions into String to be used in the query.
  // *******************************************************************************************************************

  /**
   * Returns the database fields list separated by commas, which can be used in "SELECT" clause.
   * 
   * @return the database fields list separated by commas, which can be used in "SELECT" clause.
   */
  public String getDatabaseFields() {
    return getSqlString(getFieldsSqlExpression());
  }

  /**
   * Returns the filter database condition, which can be used in "WHERE" clause.
   * 
   * @return the filter database condition, which can be used in "WHERE" clause.
   * @see #getDatabaseFilterWith(String, String)
   * @see #getDatabaseFilterParams()
   */
  public String getDatabaseFilter() {
    return getSqlString(getFilterSqlExpression());
  }

  /**
   * Returns the database filter query with <code>prefix</code> added before and <code>suffix</code> after it if the
   * query is not empty.
   * 
   * @param prefix Prefix added before the expression.
   * @param suffix Suffix added after the expression.
   * @return the database filter query with <code>prefix</code> added before and <code>suffix</code> after it if the
   *         query is not empty.
   * @see #getDatabaseFilter()
   * @see #getDatabaseFilterParams()
   */
  public String getDatabaseFilterWith(String prefix, String suffix) {
    return this.filterExpr != null ? getSqlStringWith(getFilterSqlExpression(), prefix, suffix) : "";
  }

  /**
   * Returns the <code>List</code> of parameters that should be set in the <code>PreparedStatement</code> that belong to
   * the filter database conditions.
   * 
   * @return the <code>List</code> of parameters that should be set in the <code>PreparedStatement</code> that belong to
   *         the filter database conditions.
   * @see #getDatabaseFilter()
   * @see #getDatabaseFilterWith(String, String)
   */
  public List<Object> getDatabaseFilterParams() {
    return getSqlParams(getFilterSqlExpression());
  }

  /**
   * Returns the order database representation, which can be used in "ORDER BY" clause.
   * 
   * @return the order database representation, which can be used in "ORDER BY" clause.
   * @see #getDatabaseOrderWith(String, String)
   * @see #getDatabaseOrderParams()
   */
  public String getDatabaseOrder() {
    return getSqlString(getOrderSqlExpression());
  }

  /**
   * Returns the database order query with <code>prefix</code> added before and <code>suffix</code> after it if the
   * query is not empty.
   * 
   * @param prefix Prefix added before the expression.
   * @param suffix Suffix added after the expression.
   * @return the database order query with <code>prefix</code> added before and <code>suffix</code> after it if the
   *         query is not empty.
   * @see #getDatabaseOrder()
   * @see #getDatabaseOrderParams()
   */
  public String getDatabaseOrderWith(String prefix, String suffix) {
    return this.orderExpr != null ? getSqlStringWith(getOrderSqlExpression(), prefix, suffix) : "";
  }

  /**
   * Returns the <code>List</code> of parameters that should be set in the <code>PreparedStatement</code> that belong to
   * the order database representation.
   * 
   * @return the <code>List</code> of parameters that should be set in the <code>PreparedStatement</code> that belong to
   *         the order database representation.
   * @see #getDatabaseOrder()
   * @see #getDatabaseOrderWith(String, String)
   */
  public List<Object> getDatabaseOrderParams() {
    return getSqlParams(getOrderSqlExpression());
  }

  // *********************************************************************
  // * PREPARING DATABASE QUERIES
  // *********************************************************************

  /**
   * Sets the SQL query (with arguments) that will be used to retrieve the item range from the list and count the items.
   * <p>
   * <code>ListQuery</code> filter and order conditions are used automatically.
   * </p>
   * <p>
   * To use additional custom filter (and order) conditions, use {@link #setSimpleSqlQuery(String, String, Object[])} or
   * {@link #setSimpleSqlQuery(String, String, Object[], String, Object[])} method. To use more complex query, use
   * {@link #setSqlQuery(String, Object...)} method.
   * </p>
   * <p>
   * The constructed SQL query format is following (LQ = <code>ListQuery</code>):<br/>
   * SELECT (fromSql) [WHERE (LQ filter conditions)] [ORDER BY (LQ order conditions)]
   * </p>
   * Query arguments are automatically added in the appropriate order.
   * 
   * @param fromSql FROM clause String.
   */
  public void setSimpleSqlQuery(String fromSql) {
    setSimpleSqlQuery(fromSql, null, null, null, null);
  }

  /**
   * Sets the SQL query (with arguments) that will be used to retrieve the item range from the list and count the items.
   * <p>
   * <code>ListQuery</code> filter and order conditions are used automatically and they must not be added to this
   * metohd's arguments. This method's Where arguments are only for additional conditions that are not contained in
   * <code>ListQuery</code> already.
   * </p>
   * <p>
   * In simpler cases, use {@link #setSimpleSqlQuery(String)} method. To use also custom order by conditions, use
   * {@link #setSimpleSqlQuery(String, String, Object[], String, Object[])} method. To use more complex query, use
   * {@link #setSqlQuery(String, Object...)} method.
   * </p>
   * <p>
   * The constructed SQL query format is following (LQ = <code>ListQuery</code>):<br/>
   * SELECT (fromSql) [WHERE (customWhereSql) AND (LQ filter conditions)] [ORDER BY (customOrderbySql), (LQ order
   * conditions)]
   * </p>
   * Query arguments are automatically added in the appropriate order.
   * 
   * @param fromSql FROM clause String.
   * @param customWhereSql custom WHERE clause String.
   * @param customWhereArgs custom WHERE clause arguments.
   */
  public void setSimpleSqlQuery(String fromSql, String customWhereSql, Object... customWhereArgs) {
    setSimpleSqlQuery(fromSql, customWhereSql, customWhereArgs, null, null);
  }

  /**
   * Sets the SQL query (with arguments) that will be used to retrieve the item range from the list and count the items.
   * <p>
   * <code>ListQuery</code> filter and order conditions are used automatically and they must not be added to this
   * metohd's arguments. This method's Where and Order by arguments are only for additional conditions that are not
   * contained in <code>ListQuery</code> already.
   * </p>
   * <p>
   * In simpler cases, use {@link #setSimpleSqlQuery(String)} or {@link #setSimpleSqlQuery(String, String, Object[])}
   * method. To use more complex query, use {@link #setSqlQuery(String, Object...)} method.
   * </p>
   * <p>
   * The constructed SQL query format is following (LQ = <code>ListQuery</code>):<br/>
   * SELECT (fromSql) [WHERE (customWhereSql) AND (LQ filter conditions)] [ORDER BY (customOrderbySql), (LQ order
   * conditions)]
   * </p>
   * Query arguments are automatically added in the appropriate order.
   * 
   * @param fromSql FROM clause String.
   * @param customWhereSql custom WHERE clause String.
   * @param customWhereArgs custom WHERE clause arguments.
   * @param customOrderbySql custom ORDER BY clause String.
   * @param customOrderbyArgs custom ORDER BY clause arguments.
   */
  public void setSimpleSqlQuery(String fromSql, String customWhereSql, Object[] customWhereArgs,
      String customOrderbySql, Object[] customOrderbyArgs) {

    Assert.notNull(fromSql, "The FROM SQL String must be specified");

    Assert.isTrue(customWhereSql == null ^ customWhereArgs != null,
        "WHERE SQL String and args must be both specified or null");

    Assert.isTrue(customOrderbySql == null ^ customOrderbyArgs != null,
        "ORDER BY SQL String and args must be both specified or null");

    // Set SQL query string and parameters for both items select query and items count query:
    setSqlQuery(createItemRangeQuery(fromSql, customWhereSql, customOrderbySql), getItemRangeQueryParams(
        customWhereArgs, customOrderbyArgs));

    setCountSqlQuery(createCountQuery(fromSql, customWhereSql), getCountQueryParams(customWhereArgs));
  }

  /**
   * Creates and returns the query string to use for querying item range from the database that match the given where
   * clause. The parameters are validated, so no need to do that anymore in the implemented method.
   * <p>
   * This method was created in Aranea 2.0 to split up the logic of
   * {@link #setSimpleSqlQuery(String, String, Object[], String, Object[])}
   * 
   * @param fromSql The part of query that follows the " FROM " in the query. Never empty nor null.
   * @param customWhereSql The part of query that follows the " WHERE " in the query. Null when customWhereArgs is null.
   * @return The complete query string to use for querying the number of all matching rows.
   * @since 2.0
   */
  protected String createItemRangeQuery(String fromSql, String customWhereSql, String customOrderbySql) {
    StringBuffer sb = new StringBuffer("SELECT ");
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

  /**
   * The implementation of this method is supposed to set all parameters that the range query statement needs to execute
   * properly. The method is given additional parameters that were provided to <code>ListSqlHelper</code> to also add
   * these to the query.
   * <p>
   * This method was created in Aranea 2.0 to split up the logic of
   * {@link #setSimpleSqlQuery(String, String, Object[], String, Object[])}
   * 
   * @param customWhereArgs Custom parameters to the where clause. May be null.
   * @param customOrderbyArgs Custom parameters to the order-by clause. May be null.
   * @return A <code>null</code> or a list of parameters to the item range query.
   * @see #addStatementParams(List)
   * @see #getDatabaseFilterParams()
   * @see #getDatabaseOrderParams()
   * @since 2.0
   */
  protected List<Object> getItemRangeQueryParams(Object[] customWhereArgs, Object[] customOrderbyArgs) {
    List<Object> params = new ArrayList<Object>();

    if (customWhereArgs != null) {
      params.addAll(Arrays.asList(customWhereArgs));
    }

    params.addAll(getDatabaseFilterParams());

    if (customOrderbyArgs != null) {
      params.addAll(Arrays.asList(customOrderbyArgs));
    }

    params.addAll(getDatabaseOrderParams());
    return params;
  }

  /**
   * Creates and returns the query string to use for querying the count of all items in the database that match the
   * given where clause. The parameters are validated, so no need to do that anymore in the implemented method.
   * <p>
   * This method was created in Aranea 2.0 to split up the logic of
   * {@link #setSimpleSqlQuery(String, String, Object[], String, Object[])}
   * 
   * @param fromSql The part of query that follows the " FROM " in the query. Never empty nor null.
   * @param customWhereSql The part of query that follows the " WHERE " in the query. Null when customWhereArgs is null.
   * @return The complete query string to use for querying the number of all matching rows.
   * @since 2.0
   */
  protected String createCountQuery(String fromSql, String customWhereSql) {
    StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM ");
    sb.append(fromSql);

    if (customWhereSql == null) {
      sb.append(getDatabaseFilterWith(" WHERE ", ""));
    } else {
      sb.append(" WHERE (");
      sb.append(customWhereSql);
      sb.append(")");
      sb.append(getDatabaseFilterWith(" AND ", ""));
    }

    return sb.toString();
  }

  /**
   * The implementation of this method is supposed to set all parameters that the range query statement needs to execute
   * properly. The method is given additional parameters that were provided to <code>ListSqlHelper</code> to also add
   * these to the query.
   * <p>
   * This method was created in Aranea 2.0 to split up the logic of
   * {@link #setSimpleSqlQuery(String, String, Object[], String, Object[])}
   * 
   * @param customWhereArgs Custom parameters to the where clause. May be null.
   * @param customOrderbyArgs Custom parameters to the order-by clause. May be null.
   * @return A <code>null</code> or a list of parameters to the total count query.
   * @see #addStatementParams(List)
   * @see #getDatabaseFilterParams()
   * @see #getDatabaseOrderParams()
   * @since 2.0
   */
  protected List<Object> getCountQueryParams(Object[] customWhereArgs) {
    List<Object> params = new ArrayList<Object>();
    if (customWhereArgs != null) {
      params.addAll(Arrays.asList(customWhereArgs));
    }
    params.addAll(getDatabaseFilterParams());
    return params;
  }

  private List<Object> asList(List<Object> list) {
    return list == null ? new LinkedList<Object>() : new LinkedList<Object>(list);
  }

  private List<Object> asList(Object... array) {
    return array == null || array.length == 0 ? new LinkedList<Object>() : new LinkedList<Object>(Arrays.asList(array));
  }

  /**
   * Sets the SQL query that will be used to retrieve the item range from the list and count the items. SQL query must
   * start with SELECT. All query arguments must be added additionally.
   * <p>
   * <code>ListQuery</code> filter and order conditions are not added automatically. To add them, use
   * <code>getDatabaseFilter*</code> and <code>getDatabaseOrder*</code> methods.
   * </p>
   * <p>
   * For simpler cases, use one of the <code>setSimpleSqlQuery</code> methods instead.
   * </p>
   * 
   * @param sqlQuery the SQL query that will be used to retrieve the item range from the list and count the items.
   * @param params Optional parameters to the items range query.
   */
  public final void setSqlQuery(String sqlQuery, Object... params) {
    this.rangeStatement = new SqlStatement(sqlQuery, asList(params));
  }

  /**
   * Sets the SQL query that will be used to retrieve the item range from the list and count the items. SQL query must
   * start with SELECT. All query arguments must be added additionally.
   * <p>
   * <code>ListQuery</code> filter and order conditions are not added automatically. To add them, use
   * <code>getDatabaseFilter*</code> and <code>getDatabaseOrder*</code> methods.
   * </p>
   * <p>
   * For simpler cases, use one of the <code>setSimpleSqlQuery</code> methods instead.
   * </p>
   * 
   * @param sqlQuery the SQL query that will be used to retrieve the item range from the list and count the items.
   * @param params Optional parameters to the items range query.
   */
  public final void setSqlQuery(String sqlQuery, List<Object> params) {
    this.rangeStatement = new SqlStatement(sqlQuery, asList(params));
  }

  /**
   * Sets the SQL query used to count the items in the database. SQL query must start with SELECT.
   * <p>
   * By default, total items count and items range queries are constructed automatically based on the original query.
   * This method should only be used, if it can considerably boost the performance of count query.
   * </p>
   * 
   * @param countSqlQuery the SQL query used to count the items in the database.
   * @param params Optional parameters to the count query.
   */
  public final void setCountSqlQuery(String countSqlQuery, Object... params) {
    this.countStatement = new SqlStatement(countSqlQuery, asList(params));
  }

  /**
   * Sets the SQL query used to count the items in the database. SQL query must start with SELECT.
   * <p>
   * By default, total items count and items range queries are constructed automatically based on the original query.
   * This method should only be used, if it can considerably boost the performance of count query.
   * </p>
   * 
   * @param countSqlQuery the SQL query used to count the items in the database.
   * @param params Optional parameters to the count query.
   */
  public final void setCountSqlQuery(String countSqlQuery, List<Object> params) {
    this.countStatement = new SqlStatement(countSqlQuery, asList(params));
  }

  /**
   * Adds a <code>NULL</code> <code>PreparedStatement</code> parameter to the items range query for later setting.
   * <p>
   * This method should not be used with one of the <code>setSimpleSqlQuery</code> methods.
   * 
   * @param valueType the type of the NULL value.
   */
  public void addStatementNullParam(int valueType) {
    this.rangeStatement.addNullParam(valueType);
  }

  /**
   * Adds a <code>PreparedStatement</code> parameter to the items range query for later setting.
   * <p>
   * This method should not be used with one of the <code>setSimpleSqlQuery</code> methods.
   * 
   * @param param a <code>PreparedStatement</code> parameter.
   */
  public void addStatementParam(Object param) {
    this.rangeStatement.addParam(param);
  }

  /**
   * Adds <code>PreparedStatement</code> parameters to the items range query for later setting.
   * <p>
   * This method should not be used with one of the <code>setSimpleSqlQuery</code> methods.
   * 
   * @param params <code>PreparedStatement</code> parameters.
   */
  public void addStatementParams(List<Object> params) {
    this.rangeStatement.addAllParams(params);
  }

  /**
   * Adds a <code>NULL</code> <code>PreparedStatement</code> parameter to the count query for later setting.
   * <p>
   * This method should not be used with one of the <code>setSimpleSqlQuery</code> methods.
   * 
   * @param valueType the type of the NULL value.
   * @since 2.0
   */
  public void addCountStatementNullParam(int valueType) {
    this.countStatement.addNullParam(valueType);
  }

  /**
   * Adds a <code>PreparedStatement</code> parameter to the count query for later setting.
   * <p>
   * This method should not be used with one of the <code>setSimpleSqlQuery</code> methods.
   * 
   * @param param a <code>PreparedStatement</code> parameter.
   * @since 2.0
   */
  public void addCountStatementParam(Object param) {
    this.countStatement.addParam(param);
  }

  /**
   * Adds <code>PreparedStatement</code> parameters to the count query for later setting.
   * <p>
   * This method should not be used with one of the <code>setSimpleSqlQuery</code> methods.
   * 
   * @param params <code>PreparedStatement</code> parameters.
   * @since 2.0
   */
  public void addCountStatementParams(List<Object> params) {
    this.countStatement.addAllParams(params);
  }

  /**
   * Returns the total count SQL query String and parameters.
   * 
   * @since 2.0
   */
  protected final SqlStatement getCountSqlStatement() {
    return this.countStatement;
  }

  /**
   * Returns the item range SQL query String and parameters.
   * 
   * @since 2.0
   */
  protected final SqlStatement getRangeSqlStatement() {
    return this.rangeStatement;
  }

  // *********************************************************************
  // * EXECUTING SQL AND RETURNING RESULTS
  // *********************************************************************

  /**
   * Stores the <code>DataSource</code>.
   */
  public void setDataSource(DataSource ds) {
    this.ds = ds;
  }

  protected void prepareQueries() {
    Assert.notNull(this.rangeStatement, "The item range query is missing! Please specify it through setSqlQuery() first!");
    Assert.notNull(this.countStatement, "The total count query is missing! Please specify it through setCountSqlQuery() first!");
  }

  /**
   * Execute a JDBC data access operation, implemented as callback action working on a JDBC Connection. The stored
   * <code>DataSource</code> is used to provide JDBC connection for the action. The connection is always closed after
   * the action. This method is used by all other <code>execute</code> methods in <code>ListSqlHelper</code>. To
   * override getting the connection, you have to use one of the <code>ConnectionCallback</code> returning methods and
   * use your own implementation to execute it.
   * 
   * @param action callback object that specifies the action.
   * @return a result object returned by the action, or null.
   */
  public <T> T execute(ConnectionCallback<T> action) {
    if (this.ds == null) {
      throw new RuntimeException("Please pass a DataSource to the ListSqlHelper!");
    }
    prepareQueries();
    Connection con = null;
    try {
      con = this.ds.getConnection();
      return action.doInConnection(con);
    } catch (SQLException e) {
      throw ExceptionUtil.uncheckException(e);
    } finally {
      DbUtil.closeDbObjects(con, null, null);
    }
  }

  /**
   * Executes SQL queries that should retrieve 1) the total count of items in the list and 2) a range of items from the
   * list Provided <code>ResultReader</code> is used to convert the <code>ResultSet</code> into a <code>List</code>. The
   * stored <code>DataSource</code> is used to provide JDBC connection for the action. The connection will be closed
   * automatically.
   * 
   * @param reader <code>ResultSet</code> reader.
   * @return <code>ListItemsData</code> containing the item range and total count.
   */
  public <T> ListItemsData<T> execute(ResultReader<T> reader) {
    return execute(getListItemsDataCallback(reader));
  }

  /**
   * Executes SQL queries that should retrieve 1) the total count of items in the list and 2) a range of items from the
   * list <code>ListSqlHelper</code>'s <code>BeanResultReader</code> is used to convert the <code>ResultSet</code> into
   * a <code>List</code>. The stored <code>DataSource</code> is used to provide JDBC connection for the action. The
   * connection will be closed automatically.
   * 
   * @param itemClass Bean class.
   * @return <code>ListItemsData</code> containing the item range and total count.
   */
  public <T> ListItemsData<T> execute(Class<T> itemClass) {
    return execute(getListItemsDataCallback(createBeanResultReader(itemClass)));
  }

  /**
   * Executes a SQL query that should retrieve the total count of items in the list. The stored <code>DataSource</code>
   * is used to provide JDBC connection for the action. The connection will be closed automatically.
   * 
   * @return the total count of items in the list.
   */
  public Long executeCountSql() {
    return execute(getCountSqlCallback());
  }

  /**
   * Executes a SQL query that should retrieve a range of items from the list. Provided <code>ResultReader</code> is
   * used to convert the <code>ResultSet</code> into a <code>List</code>. The stored <code>DataSource</code> is used to
   * provide JDBC connection for the action. The connection will be closed automatically.
   * 
   * @param reader <code>ResultSet</code> reader.
   * @return <code>List</code> containing the item range.
   */
  public <T> List<T> executeItemRangeSql(ResultReader<T> reader) {
    return execute(getItemRangeSqlCallback(reader));
  }

  /**
   * Executes a SQL query that should retrieve a range of items from the list. <code>ListSqlHelper</code>'s
   * <code>BeanResultReader</code> is used to convert the <code>ResultSet</code> into a <code>List</code>. The stored
   * <code>DataSource</code> is used to provide JDBC connection for the action. The connection will be closed
   * automatically.
   * 
   * @param itemClass Bean class.
   * @return <code>List</code> containing the item range.
   */
  public <T> List<T> executeItemRangeSql(Class<T> itemClass) {
    return execute(getItemRangeSqlCallback(createBeanResultReader(itemClass)));
  }

  /**
   * Provides whether the query should return all rows that where matched, or only those that,by result-set row number,
   * are in a certain rage.
   * 
   * @return <code>true</code> when the query should return all rows.
   * @since 2.0
   */
  protected final boolean isShowAll() {
    return this.itemRangeStart == 0L && this.itemRangeCount == null;
  }

  // *********************************************************************
  // * CALLBACKS
  // *********************************************************************

  /**
   * Returns the total count and item range queries callback. In most cases, you should not use this method directly,
   * instead using one of the <code>execute</code> methods is recommended.
   */
  public <T> ConnectionCallback<ListItemsData<T>> getListItemsDataCallback(ResultReader<T> reader) {
    return new ListItemsDataCallback<T>(getCountSqlCallback(), getItemRangeSqlCallback(reader));
  }

  /**
   * Returns the total count query callback. In most cases, you should not use this method directly, instead using one
   * of the <code>execute</code> methods is recommended.
   */
  public ConnectionCallback<Long> getCountSqlCallback() {
    return new CountSqlCallback();
  }

  /**
   * Returns the item range query callback. In most cases, you should not use this method directly, instead using one of
   * the <code>execute</code> methods is recommended.
   */
  public <T> ConnectionCallback<List<T>> getItemRangeSqlCallback(ResultReader<T> reader) {
    return new ItemRangeSqlCallback<T>(reader);
  }

  /**
   * The item range and total count queries callback that returns <code>ListItemsData</code> object.
   * 
   * @author Rein Raudjärv (rein@araneaframework.org)
   */
  public static class ListItemsDataCallback<T> implements ConnectionCallback<ListItemsData<T>> {

    protected ConnectionCallback<Long> countSqlCallback;

    protected ConnectionCallback<List<T>> itemRangeSqlCallback;

    /**
     * @param countSqlCallback total count query callback.
     * @param itemRangeSqlCallback item range query callback.
     */
    public ListItemsDataCallback(ConnectionCallback<Long> countSqlCallback,
        ConnectionCallback<List<T>> itemRangeSqlCallback) {
      this.countSqlCallback = countSqlCallback;
      this.itemRangeSqlCallback = itemRangeSqlCallback;
    }

    /**
     * Executes both queries, creates and returns <code>ListItemsData</code> object containing results of both queries.
     * 
     * @return the whole results as <code>ListItemsData</code> object.
     */
    public ListItemsData<T> doInConnection(Connection con) throws SQLException {
      ListItemsData<T> result = new ListItemsData<T>();
      result.setTotalCount(this.countSqlCallback.doInConnection(con));
      result.setItemRange(this.itemRangeSqlCallback.doInConnection(con));
      return result;
    }
  }

  /**
   * The total count query callback.
   * 
   * @author Rein Raudjärv (rein@araneaframework.org)
   */
  public class CountSqlCallback implements ConnectionCallback<Long> {

    /**
     * Executes total count query and returns the result.
     * 
     * @return the total count as <code>Long</code> object.
     */
    public Long doInConnection(Connection con) throws SQLException {
      PreparedStatement stmt = null;
      ResultSet rs = null;
      try {
        SqlStatement countSqlStatement = getCountSqlStatement();
        if (LOG.isDebugEnabled()) {
          LOG.debug("Counting database query: " + countSqlStatement.getQuery());
          LOG.debug("Counting statement parameters: " + countSqlStatement.getParams());
        }
        stmt = con.prepareStatement(countSqlStatement.getQuery());
        countSqlStatement.propagateStatementWithParams(stmt);
        try {
          rs = stmt.executeQuery();
        } catch (SQLException e) {
          throw createQueryFailedException(countSqlStatement.getQuery(), countSqlStatement.getParams(), e);
        }
        if (rs.next()) {
          return Long.valueOf(rs.getLong(1));
        }
        return null;
      } finally {
        DbUtil.closeDbObjects(null, stmt, rs);
      }
    }
  }

  /**
   * The item range query callback that returns <code>List</code> of items.
   * 
   * @author Rein Raudjärv (rein@araneaframework.org)
   */
  public class ItemRangeSqlCallback<E> implements ConnectionCallback<List<E>> {

    protected ResultReader<E> reader;

    /**
     * @param reader <code>ResultSet</code> reader that processes the data and returns items as <code>List</code>.
     */
    public ItemRangeSqlCallback(ResultReader<E> reader) {
      this.reader = reader;
    }

    /**
     * Executes the item range query and returns the results.
     * 
     * @return list items as <code>List</code> object.
     */
    public List<E> doInConnection(Connection con) throws SQLException {
      PreparedStatement stmt = null;
      ResultSet rs = null;
      try {
        SqlStatement rangeSqlStatement = getRangeSqlStatement();
        if (LOG.isDebugEnabled()) {
          LOG.debug("Item range database query: " + rangeSqlStatement.getQuery());
          LOG.debug("Item range statement parameters: " + rangeSqlStatement.getParams());
        }
        stmt = con.prepareStatement(rangeSqlStatement.getQuery());
        rangeSqlStatement.propagateStatementWithParams(stmt);
        try {
          rs = stmt.executeQuery();
        } catch (SQLException e) {
          throw createQueryFailedException(rangeSqlStatement.getQuery(), rangeSqlStatement.getParams(), e);
        }
        while (rs.next()) {
          this.reader.processRow(rs);
        }
        return this.reader.getResults();
      } finally {
        DbUtil.closeDbObjects(null, stmt, rs);
      }
    }
  }

  // *********************************************************************
  // * BEAN RESULT READER
  // *********************************************************************

  /**
   * Returns Bean <code>ResultSet</code> reader. In most cases, you should not use this method directly, instead using
   * one of the <code>execute</code> methods is recommended.
   */
  public <T> ResultReader<T> createBeanResultReader(Class<T> itemClass) {
    return new BeanResultReader<T>(itemClass);
  }

  /**
   * Result-set reader that uses <code>beanToResultSetMapping</code> in <code>ListSqlHelper</code> to construct a given
   * type of Bean list.
   * 
   * @author Rein Raudjärv
   */
  public class BeanResultReader<T> implements ResultReader<T> {

    protected Class<T> itemClass;

    protected List<T> results;

    protected BeanMapper<T> beanMapper;

    // For caching
    protected String[] fieldNames;

    protected Class<?>[] fieldTypes;

    protected String[] columnNames;

    /**
     * @param itemClass Bean type.
     */
    public BeanResultReader(Class<T> itemClass) {
      this.itemClass = itemClass;
      this.results = new ArrayList<T>();
      this.beanMapper = new BeanMapper<T>(itemClass, true);
      init();
    }

    /**
     * Cache all the field names, field types and column names to be used for each row.
     */
    public void init() {
      Collection<String> names = BaseListSqlHelper.this.fields.getResultSetNames();
      int count = names.size();
      this.fieldNames = new String[count];
      this.fieldTypes = new Class[count];
      this.columnNames = new String[count];
      int i = 0;
      for (String fieldName : names) {
        // Check get-method
        if (!this.beanMapper.isWritable(fieldName)) {
          throw new RuntimeException("Bean of type '" + this.itemClass.getName()
              + "' does not have accessible setter corresponding to field '" + fieldName + "'");
        }
        this.fieldNames[i] = fieldName;
        this.fieldTypes[i] = this.beanMapper.getPropertyType(fieldName);
        this.columnNames[i] = BaseListSqlHelper.this.namingStrategy.fieldToColumnAlias(fieldName);
        i++;
      }
    }

    /**
     * Processes <code>ResultSet</code> row passing it with the new Bean instance to
     * {@link #readBeanFields(ResultSet, Object)} method.
     */
    public void processRow(ResultSet rs) {
      T record = createBean();
      readBeanFields(rs, record);
      this.results.add(record);
    }

    /**
     * @return new Bean instance.
     */
    protected T createBean() {
      try {
        return this.itemClass.newInstance();
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }

    /**
     * Reads the bean from <code>ResultSet</code>. Implementations may override it to read beans in a custom way.
     * 
     * @param rs <code>ResultSet</code> containing the results of database query.
     * @param bean bean to read.
     */
    protected void readBeanFields(ResultSet rs, Object bean) {
      for (int i = 0; i < this.fieldNames.length; i++) {
        readBeanField(rs, this.columnNames[i], bean, this.fieldNames[i], this.fieldTypes[i]);
      }
    }

    /**
     * Reads the bean field from <code>ResultSet</code>. Implementations may override it to read bean fields in a custom
     * way. A usual situation would be when a bean field is read from more than one <code>ResultSet</code> field.
     * 
     * @param rs <code>ResultSet</code> containing the results of database query.
     * @param rsColumn name of the result set column to read from.
     * @param bean bean to read.
     * @param beanField name of the bean field to read to.
     * @param fieldType type of the bean field.
     */
    protected void readBeanField(ResultSet rs, String rsColumn, Object bean, String beanField, Class<?> fieldType) {
      Object value = BaseListSqlHelper.this.resultSetColumnReader.readFromResultSet(rsColumn, rs, fieldType);
      this.beanMapper.setProperty(bean, beanField, value);
    }

    /**
     * Returns the results.
     */
    public List<T> getResults() {
      return this.results;
    }
  }

  // *********************************************************************
  // * HELPER METHODS
  // *********************************************************************

  /**
   * Creates the VariableResolver for SqlExpressionBuilder that converts Variable names to their Database Field names
   * according to the naming strategy.
   * 
   * @return the VariableResolver for SqlExpressionBuilder that converts Variable names to their Database Field names
   *         according to the naming strategy.
   */
  protected VariableResolver createExpressionBuilderResolver() {
    return this.variableResolver == null ? new ColumnNameVariableResolver(this.namingStrategy) : this.variableResolver;
  }

  // *********************************************************************
  // * UTIL METHODS
  // *********************************************************************

  private static String getSqlString(SqlExpression expr) {
    return expr != null ? expr.toSqlString() : "";
  }

  private static String getSqlStringWith(SqlExpression expr, String prefix, String suffix) {
    StringBuffer sb = new StringBuffer();
    if (expr != null) {
      sb.append(StringUtils.defaultString(prefix));
      sb.append(expr.toSqlString());
      sb.append(StringUtils.defaultString(suffix));
    }
    return sb.toString();
  }

  private static List<Object> getSqlParams(SqlExpression expr) {
    return (expr != null && expr.getValues() != null) ? Arrays.asList(expr.getValues()) : new ArrayList<Object>();
  }

  /**
   * Returns query failed Exception that contains query String and parameters.
   */
  protected static RuntimeException createQueryFailedException(String queryString, List<Object> queryParams,
      SQLException nestedException) {
    String str = new StringBuffer("Executing list query [").append(queryString).append("] with params: ").append(
        queryParams).append(" failed").toString();
    return new AraneaRuntimeException(str, nestedException);
  }
}
