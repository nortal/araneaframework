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
import java.util.List;
import javax.sql.DataSource;
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
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.Assert;
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
  protected Long itemRangeStart;

  protected Long itemRangeCount;

  // CONNECTION
  protected DataSource ds;

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
   * Sets the (0-based) starting index of the item range.
   */
  public void setItemRangeStart(Long itemRangeStart) {
    if (itemRangeStart == null) {
      itemRangeStart = DEFAULT_RANGE_START;
    }
    this.itemRangeStart = itemRangeStart;
  }

  /**
   * Sets the count of items in the range.
   */
  public void setItemRangeCount(Long itemRangeCount) {
    this.itemRangeCount = itemRangeCount;
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
    if (this.orderSqlExprInited) {
      return this.orderSqlExpr;
    }
    if (this.orderExpr != null) {
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
    if (this.filterSqlExprInited) {
      return this.filterSqlExpr;
    }
    if (this.filterExpr != null) {
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
   * {@link #setSqlQuery(String)} method.
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
   * {@link #setSqlQuery(String)} method.
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
   * method. To use more complex query, use {@link #setSqlQuery(String)} method.
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

    Assert.isTrue(customWhereSql == null && customWhereArgs != null,
        "WHERE SQL String and args must be both specified or null");

    Assert.isTrue(customOrderbySql == null && customOrderbyArgs != null,
        "ORDER BY SQL String and args must be both specified or null");

    // SQL String
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

    setSqlQuery(sb.toString());

    // SQL arguments
    if (customWhereArgs != null) {
      addStatementParams(Arrays.asList(customWhereArgs));
    }
    addStatementParams(getDatabaseFilterParams());
    if (customOrderbyArgs != null) {
      addStatementParams(Arrays.asList(customOrderbyArgs));
    }
    addStatementParams(getDatabaseOrderParams());
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
   */
  public abstract void setSqlQuery(String sqlQuery);

  /**
   * Sets the SQL query used to count the items in the database. SQL query must start with SELECT.
   * <p>
   * By default, total items count and items range queries are constructed automatically based on the original query.
   * This method should only be used, if it can considerably boost the perfomacne of count query.
   * </p>
   * 
   * @param countSqlQuery the SQL query used to count the items in the database.
   */
  public abstract void setCountSqlQuery(String countSqlQuery);

  /**
   * Adds a <code>NULL</code> <code>PreparedStatement</code> parameter for later setting.
   * <p>
   * This method should not be used with one of the <code>setSimpleSqlQuery</code> methods.
   * </p>
   * 
   * @param valueType the type of the NULL value.
   */
  public abstract void addNullParam(int valueType);

  /**
   * Adds a <code>PreparedStatement</code> parameter for later setting.
   * <p>
   * This method should not be used with one of the <code>setSimpleSqlQuery</code> methods.
   * </p>
   * 
   * @param param a <code>PreparedStatement</code> parameter.
   */
  public abstract void addStatementParam(Object param);

  /**
   * Adds <code>PreparedStatement</code> parameters for later setting.
   * <p>
   * This method should not be used with one of the <code>setSimpleSqlQuery</code> methods.
   * </p>
   * 
   * @param params <code>PreparedStatement</code> parameters.
   */
  public abstract void addStatementParams(List<Object> params);

  /**
   * Returns the total count SQL query String and parameters.
   */
  protected abstract SqlStatement getCountSqlStatement();

  /**
   * Returns the itme range SQL query String and parameters.
   */
  protected abstract SqlStatement getRangeSqlStatement();

  // *********************************************************************
  // * EXECUTING SQL AND RETURNING RESULTS
  // *********************************************************************

  /**
   * Stores the <code>DataSource</code>.
   */
  public void setDataSource(DataSource ds) {
    this.ds = ds;
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
      sb.append(prefix);
      sb.append(expr.toSqlString());
      sb.append(suffix);
    }
    return sb.toString();
  }

  private static List<Object> getSqlParams(SqlExpression expr) {
    return (expr != null && expr.getValues() != null) ? Arrays.asList(expr.getValues()) : new ArrayList<Object>();
  }

  /**
   * Returns query failed Exception that contains query String and parameters.
   */
  protected static RuntimeException createQueryFailedException(String QueryString, List<Object> queryParams,
      SQLException nestedException) {
    String str = new StringBuffer("Executing list query [").append(QueryString).append("] with params: ").append(
        queryParams).append(" failed").toString();
    return new AraneaRuntimeException(str, nestedException);
  }
}
