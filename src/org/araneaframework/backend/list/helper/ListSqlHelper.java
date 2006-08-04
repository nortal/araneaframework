/**
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
 **/

package org.araneaframework.backend.list.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.helper.builder.ValueConverter;
import org.araneaframework.backend.list.helper.builder.compexpr.StandardCompExprToSqlExprBuilder;
import org.araneaframework.backend.list.helper.builder.expression.StandardExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.helper.reader.DefaultResultSetColumnReader;
import org.araneaframework.backend.list.helper.reader.ResultSetColumnReader;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.backend.list.sqlexpr.SqlCollectionExpression;
import org.araneaframework.backend.list.sqlexpr.constant.SqlStringExpression;
import org.araneaframework.backend.util.GeneralBeanMapper;
import org.araneaframework.backend.util.RecursiveBeanMapper;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.uilib.list.util.Converter;
import org.araneaframework.uilib.list.util.converter.DummyConverter;


/**
 * This class provides an SQL based implementation of the list. It takes care of
 * the filtering, ordering and returning data to the web components.
 * Implementations should override abstract methods noted in those methods.
 * <p>
 * Note, that all operations on items are made on the list of "processed", that
 * is ordered and filtered items.
 * <p>
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public abstract class ListSqlHelper {

	protected static Logger log = Logger.getLogger(ListSqlHelper.class);

	protected static final Long DEFAULT_RANGE_START = new Long(0);

	// *******************************************************************
	// FIELDS
	// *******************************************************************

	// MAPPING

	// Value Name ? --> Converter (Converter that is used by convert() method)
	protected Map valueConverters = new HashMap();
	// Bean Field Name ? --> Deconverter (Converter that is used by reverseConvert() method)
	protected Map beanDeconverters = new HashMap();
	// Variable Name ? --> Database Field Name
	protected Map variableToDatabaseMapping = new HashMap();
	// Bean Field Name ? --> ResultSet Column Name
	protected Map beanToResultSetMapping = new HashMap();

	// FILTER AND ORDER

	protected Expression filterExpr;
	protected ComparatorExpression orderExpr;

	protected SqlExpression filterSqlExpr;
	protected SqlExpression orderSqlExpr;

	// ITEM RANGE

	protected Long itemRangeStart;
	protected Long itemRangeCount;	

	// CONNECTION

	protected DataSource ds;

	// RESULTSET READING

	protected ResultSetColumnReader resultSetColumnReader =
		DefaultResultSetColumnReader.getInstance();

	// *********************************************************************
	// * CONSTRUCTORS
	// *********************************************************************

	/**
	 * Creates <code>ListSqlHelper</code> initializing the appropriate fields
	 * and providing it with the <code>DataSource</code>.
	 */
	public ListSqlHelper(DataSource dataSource, ListQuery query) {
		setDataSource(dataSource);
		setListQuery(query);
	}

	/**
	 * Creates <code>ListSqlHelper</code> and provides it with the
	 * <code>DataSource</code>.
	 */
	public ListSqlHelper(DataSource dataSource) {
		setDataSource(dataSource);
	}
	
	/**
	 * Creates <code>ListSqlHelper</code> initializing the appropriate fields.
	 */
	public ListSqlHelper(ListQuery query) {
		setListQuery(query);
	}

	/**
	 * Creates <code>ListSqlHelper</code> without initializing any fields.
	 */
	public ListSqlHelper() {
		// for bran creation
	}

	// *********************************************************************
	// * PUBLIC METHODS
	// *********************************************************************

	/**
	 * Sets the starting index and count of items in the range and
	 * filtering and ordering expressions. 
	 */
	public void setListQuery(ListQuery query) {
		this.filterExpr = query.getFilterExpression();
		this.orderExpr = query.getOrderExpression();
		setItemRangeStart(query.getItemRangeStart());
		setItemRangeCount(query.getItemRangeCount());
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

	/**
	 * Sets the <code>ResultSet</code> column reader.
	 */
	public void setResultSetColumnReader(ResultSetColumnReader resultSetColumnReader) {
		this.resultSetColumnReader = resultSetColumnReader;
	}

	// *********************************************************************
	// * DATABASE MAPPING AND CONVERTERS
	// *********************************************************************	

	/**
	 * Sets the converter between the filtering-ordering values in
	 * <code>Expressions</code> and values in <code>SqlExpressions</code>.
	 * 
	 * @param valueName
	 *            value name in <code>Expression</code> and
	 *            <code>ComparatorExpression</code>.
	 * @param converter
	 *            converter that is used by <code>convert()</code> method.
	 */
	public void setConverter(String valueName, Converter converter) {
		this.valueConverters.put(valueName, converter);
	}

	/**
	 * Sets the converter between the values in <code>ResultSet</code> and
	 * bean fields <code>beanFieldName</code>.
	 * 
	 * @param beanFieldName
	 *            bean field name.
	 * @param converter
	 *            converter that is used by <code>reverseConvert()</code>
	 *            method.
	 */
	public void setDeconverter(String beanFieldName, Converter converter) {
		this.beanDeconverters.put(beanFieldName, converter);
	}

	/**
	 * Sets the mapping between the filtering-ordering variable name
	 * <code>variableName</code> and the database field name
	 * <code>databaseField</code>.
	 * 
	 * @param variableName
	 *            variable name in <code>Expression</code> and
	 *            <code>ComparatorExpression</code>.
	 * @param databaseFieldName
	 *            database field name.
	 */
	public void setDatabaseFieldMapping(String variableName,
			String databaseFieldName) {
		this.variableToDatabaseMapping.put(variableName, databaseFieldName);
	}

	/**
	 * Sets the mapping between the bean field name <code>beanFieldName</code>
	 * and the name of <code>ResultSet</code> column.
	 * 
	 * @param beanFieldName
	 *            bean field name.
	 * @param resultSetColumnName
	 *            <code>ResultSet</code> column name.
	 */
	public void setResultSetMapping(String beanFieldName,
			String resultSetColumnName) {
		this.beanToResultSetMapping.put(beanFieldName, resultSetColumnName);
	}

	/**
	 * Sets the mapping between the filtering-ordering variable / bean field
	 * name <code>columnName</code> and the database field name
	 * <code>databaseFieldName</code>. Use this function if the names of the
	 * <code>ResultSet</code> column and the database field name used in
	 * "WHERE" clause coinside or if you read the <code>ResultSet</code>
	 * manually.
	 * 
	 * @param columnName
	 *            variable name in <code>Expression</code> and
	 *            <code>ComparatorExpression</code> and the bean field name.
	 * @param databaseFieldName
	 *            database field name.
	 */
	public void setColumnMapping(String columnName, String databaseFieldName) {
		setColumnMapping(columnName, databaseFieldName, makeFieldNameUnique(removePrefix(databaseFieldName)));
	}

	private String makeFieldNameUnique(String databaseFieldName) {
		String alias = databaseFieldName;
		int index = 0;
		while (this.beanToResultSetMapping.containsValue(alias)) {
			alias = alias + index++;
		}
		if (log.isDebugEnabled()) {
			log.debug("Using '" + alias + "' as alias for field '" + databaseFieldName + "'");
		}
		return alias;
	}

	private static String removePrefix(String databaseFieldName) {
		return databaseFieldName.substring(databaseFieldName.lastIndexOf('.') + 1);
	}

	/**
	 * Sets the mapping between the filtering-ordering variable / bean field
	 * name <code>columnName</code> and the database field names
	 * <code>databaseFieldMapping</code>. Use this function if the names of
	 * the <code>ResultSet</code> column and the database field name used in
	 * "WHERE" clause differ.
	 * 
	 * @param columnName
	 *            variable name in <code>Expression</code> and
	 *            <code>ComparatorExpression</code> and the bean field name.
	 * @param databaseFieldName
	 *            database field name.
	 * @param resultSetColumnName
	 *            <code>ResultSet</code> column name.
	 */
	public void setColumnMapping(String columnName, String databaseFieldName,
			String resultSetColumnName) {
		setDatabaseFieldMapping(columnName, databaseFieldName);
		setResultSetMapping(columnName, resultSetColumnName);
	}

	/**
	 * Sets the converter between the filtering-ordering values with the name of /
	 * bean field with the name of <code>columnName</code> and its database
	 * field / <code>ResultSet</code> column.
	 * 
	 * @param columnName
	 *            value name in <code>Expression</code> and
	 *            <code>ComparatorExpression</code> and the bean field name.
	 * @param converter
	 *            converter that is used by <code>convert()</code> and
	 *            <code>reverseConvert()</code> method.
	 */
	public void setColumnConverter(String columnName, Converter converter) {
		setConverter(columnName, converter);
		setDeconverter(columnName, converter);
	}

	// *********************************************************************
	// * BUILDING SQL EXPRESSIONS ACCORDING TO ORDERING AND FILTERING
	// *********************************************************************	
		
	/**
	 * Sets the <code>ComparatorExpression</code> saving it for later use.
	 */
	public void setOrderExpression(ComparatorExpression orderExpr) {
		this.orderExpr = orderExpr;
	}

	/**
	 * Sets the <code>Expression</code> saving it for later use.
	 */
	public void setFilterExpression(Expression filterExpr) {
		this.filterExpr = filterExpr;
	}

	/**
	 * Returns the fields <code>SqlExpression</code>, which can be used in
	 * "SELECT" clause.
	 * 
	 * @return the fields <code>SqlExpression</code>, which can be used in
	 *         "SELECT" clause.
	 */
	protected SqlExpression getFieldsSqlExpression() {
		SqlCollectionExpression fields = new SqlCollectionExpression();

		for (Iterator i = this.variableToDatabaseMapping.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry entry = (Entry) i.next();

			String variable = (String) entry.getKey();
			String dbField = (String) entry.getValue();
			String alias = (String) this.beanToResultSetMapping.get(variable);
			
			String sql;
			if (alias == null || alias.equals(dbField)) {
				sql = dbField;
			} else {
				sql = new StringBuffer(dbField).append(" ").append(alias).toString();
			}
			fields.add(new SqlStringExpression(sql));
		}
		return fields;
	}

	/**
	 * Returns the order <code>SqlExpression</code>, which can be used in
	 * "ORDER BY" clause.
	 * 
	 * @return the order <code>SqlExpression</code>, which can be used in
	 *         "ORDER BY" clause.
	 */
	protected SqlExpression getOrderSqlExpression() {
		if (this.orderSqlExpr != null) {
			return this.orderSqlExpr;
		}

		if (this.orderExpr == null) {
			return null;
		}
		StandardCompExprToSqlExprBuilder builder = createOrderSqlExpressionBuilder();
		builder.setMapper(createExpressionBuilderResolver());
		this.orderSqlExpr = builder.buildSqlExpression(this.orderExpr);
		return this.orderSqlExpr;
	}

	/**
	 * Returns the filter <code>SqlExpression</code>, which can be used in
	 * "WHERE" clause.
	 * 
	 * @return the filter <code>SqlExpression</code>, which can be used in
	 *         "WHERE" clause.
	 */
	protected SqlExpression getFilterSqlExpression() {
		if (this.filterSqlExpr != null) {
			return this.filterSqlExpr;
		}

		if (this.filterExpr == null) {
			return null;
		}
		StandardExpressionToSqlExprBuilder builder = createFilterSqlExpressionBuilder();
		builder.setMapper(createExpressionBuilderResolver());
		builder.setConverter(createExpressionBuilderConverter());
		this.filterSqlExpr = builder.buildSqlExpression(this.filterExpr);
		return this.filterSqlExpr;
	}
	
	/**
	 * Creates new ordering SQL Expression builder..
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
	
	/**
	 * Returns the database fields list seperated by commas, which can be used in "SELECT" clause.
	 * 
	 * @return the database fields list seperated by commas, which can be used in "SELECT" clause.
	 */
	public String getDatabaseFields() {
		SqlExpression expr = this.getFieldsSqlExpression();
		return expr != null ? expr.toSqlString() : "";
	}

	/**
	 * Returns the filter database condition, which can be used in "WHERE" clause.
	 * 
	 * @return the filter database condition, which can be used in "WHERE" clause.
	 */
	public String getDatabaseFilter() {
		SqlExpression expr = this.getFilterSqlExpression();
		return expr != null ? expr.toSqlString() : "";
	}

	public String getDatabaseFilterWith(String prefix, String suffix) {
		StringBuffer whereCondition = new StringBuffer();		
		if (this.filterExpr != null) {
			whereCondition.append(prefix);
			whereCondition.append(getDatabaseFilter());
			whereCondition.append(suffix);
		}
		return whereCondition.toString();
	}  

	/**
	 * Returns the <code>List</code> of parameters that should be set in the
	 * <code>PreparedStatement</code> that
	 * belong to the filter database conditions.
	 * 
	 * @return the <code>List</code> of parameters that should be set in the
	 * <code>PreparedStatement</code> that
	 *         belong to the filter database conditions.
	 */
	public List getDatabaseFilterParams() {
		SqlExpression expr = this.getFilterSqlExpression();
		return expr != null ? Arrays.asList(expr.getValues()) : new ArrayList();
	}

	/**
	 * Returns the order database representation, which can be used in "ORDER BY" clause.
	 * 
	 * @return the order database representation, which can be used in "ORDER BY" clause.
	 */
	public String getDatabaseOrder() {
		SqlExpression expr = this.getOrderSqlExpression();
		return expr != null ? expr.toSqlString() : "";
	}

	/**
	 * Returns the database order query with <code>prefix</code> added before and 
	 * <code>suffix</code> after it if the query is not empty.
	 * 
	 * @param prefix Prefix added before the expression.
	 * @param suffix Suffix added after the expression.
	 * 
	 * @return the database order query with <code>prefix</code> added before and 
	 *  <code>suffix</code> after it if the query is not empty.
	 * 
	 * @see #getDatabaseOrder()
	 */
	public String getDatabaseOrderWith(String prefix, String suffix) {
		StringBuffer orderQuery = new StringBuffer();
		if (this.orderExpr != null) {
			orderQuery.append(prefix);
			orderQuery.append(getDatabaseOrder());
			orderQuery.append(suffix);
		}
		return orderQuery.toString();
	}  

	/**
	 * Returns the <code>List</code> of parameters that should be set in the
	 * <code>PreparedStatement</code> that belong to the order database representation.
	 * 
	 * @return the <code>List</code> of parameters that should be set in the
	 * <code>PreparedStatement</code> that belong to the order database representation.
	 */
	public List getDatabaseOrderParams() {
		SqlExpression expr = this.getOrderSqlExpression();
		return expr != null ? Arrays.asList(expr.getValues()) : new ArrayList();
	}

	// *********************************************************************
	// * PREPARING DATABASE QUERIES
	// *********************************************************************		

	/**
	 * Sets the SQL query (with arguments) that will be used to retrieve the
	 * item range from the list and count the items.
	 * <p>
	 * <code>ListQuery</code> filter and order conditions are used
	 * automatically.
	 * </p>
	 * <p>
	 * To use additional custom filter (and order) conditions,
	 * use {@link #setSimpleSqlQuery(String, String, List)} or
	 * {@link #setSimpleSqlQuery(String, String, List, String, List)}
	 * method. To use more complex query, use {@link #setSqlQuery(String)}
	 * method.
	 * </p>
	 * <p>
	 * The constrcuted SQL query format is following
	 * (LQ = <ocde>ListQuery</code>):<br/>
	 * SELECT (fromSql) [WHERE (LQ filter conditions)]
	 * [ORDER BY (LQ order conditions)]
	 * </p>
	 * Query arguments are automatically added in the appropriate order. 
	 * 
	 * @param fromSql FROM clause String.
	 */
	public void setSimpleSqlQuery(String fromSql) {
		setSimpleSqlQuery(fromSql, null, null, null, null);
	}
	
	/**
	 * Sets the SQL query (with arguments) that will be used to retrieve the
	 * item range from the list and count the items.
	 * <p>
	 * <code>ListQuery</code> filter and order conditions are used automatically
	 * and they must not be added to this metohd's arguments. 
	 * This method's Where arguments are only for additional
	 * conditions that are not contained in <code>ListQuery</code> already.
	 * </p>
	 * <p>
	 * In simpler cases, use {@link #setSimpleSqlQuery(String)} method.
	 * To use also custom order by conditions, use 
	 * {@link #setSimpleSqlQuery(String, String, List, String, List)} method.
	 * To use more complex query, use {@link #setSqlQuery(String)} method.
	 * </p>
	 * <p>
	 * The constrcuted SQL query format is following
	 * (LQ = <ocde>ListQuery</code>):<br/>
	 * SELECT (fromSql) [WHERE (customWhereSql) AND (LQ filter conditions)]
	 * [ORDER BY (customOrderbySql), (LQ order conditions)]
	 * </p>
	 * Query arguments are automatically added in the appropriate order. 
	 * 
	 * @param fromSql FROM clause String.
	 * @param customWhereSql custom WHERE clause String.
	 * @param customWhereArgs custom WHERE clause arguments.
	 */
	public void setSimpleSqlQuery(String fromSql, String customWhereSql, Object[] customWhereArgs) {
		setSimpleSqlQuery(fromSql, customWhereSql, customWhereArgs, null, null);
	}	
	
	/**
	 * Sets the SQL query (with arguments) that will be used to retrieve the
	 * item range from the list and count the items.
	 * <p>
	 * <code>ListQuery</code> filter and order conditions are used automatically
	 * and they must not be added to this metohd's arguments. 
	 * This method's Where and Order by arguments are only for additional
	 * conditions that are not contained in <code>ListQuery</code> already.
	 * </p>
	 * <p>
	 * In simpler cases, use {@link #setSimpleSqlQuery(String)} or
	 * {@link #setSimpleSqlQuery(String, String, List)} method.
	 * To use more complex query, use {@link #setSqlQuery(String)} method.
	 * </p>
	 * <p>
	 * The constrcuted SQL query format is following
	 * (LQ = <ocde>ListQuery</code>):<br/>
	 * SELECT (fromSql) [WHERE (customWhereSql) AND (LQ filter conditions)]
	 * [ORDER BY (customOrderbySql), (LQ order conditions)]
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
		
		if (fromSql == null) {
			throw new IllegalArgumentException("FROM SQL String must be specified"); 
		}
		if (customWhereSql == null && customWhereArgs != null) {
			throw new IllegalArgumentException("WHERE SQL String and args must be both specified or null"); 
		}
		if (customOrderbySql == null && customOrderbyArgs != null) {
			throw new IllegalArgumentException("ORDER BY SQL String and args must be both specified or null"); 
		}
		
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
		if (customWhereSql == null) {
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
	 * Sets the SQL query that will be used to retrieve the item range from the
	 * list and count the items. SQL query must start with SELECT.
	 * All query arguments must be added additionally.
	 * <p>
	 * <code>ListQuery</code> filter and order conditions are not added
	 * automatically. To add them, use <code>getDatabaseFilter*</code> and
	 * <code>getDatabaseOrder*</code> methods.
	 * </p>
	 * <p>
	 * For simpler cases, use
	 * one of the <code>setSimpleSqlQuery</code> methods instead.
	 * </p>
	 * 
	 * @param sqlQuery
	 *            the SQL query that will be used to retrieve the item range
	 *            from the list and count the items.
	 */
	public abstract void setSqlQuery(String sqlQuery);

	/**
	 * Sets the SQL query used to count the items in the database. SQL query
	 * must start with SELECT.
	 * <p>
	 * By default, total items count and items range queries are constructed
	 * automatically based on the original query. This method should only be
	 * used, if it can considerably boost the perfomacne of count query. 
	 * </p>
	 * 
	 * @param countSqlQuery
	 *            the SQL query used to count the items in the database.
	 */
	public abstract void setCountSqlQuery(String countSqlQuery);

	/**
	 * Adds a <code>NULL</code> <code>PreparedStatement</code> parameter for
	 * later setting.
	 * <p>
	 * This method should not be used with one of the
	 * <code>setSimpleSqlQuery</code> methods.
	 * </p>
	 * 
	 * @param valueType
	 *            the type of the NULL value.
	 */
	public abstract void addNullParam(int valueType);

	/**
	 * Adds a <code>PreparedStatement</code> parameter for later setting.
	 * <p>
	 * This method should not be used with one of the
	 * <code>setSimpleSqlQuery</code> methods.
	 * </p>
	 * 
	 * @param param
	 *            a <code>PreparedStatement</code> parameter.
	 */
	public abstract void addStatementParam(Object param);

	/**
	 * Adds <code>PreparedStatement</code> parameters for later setting.
	 * <p>
	 * This method should not be used with one of the
	 * <code>setSimpleSqlQuery</code> methods.
	 * </p>
	 * 
	 * @param params
	 *            <code>PreparedStatement</code> parameters.
	 */
	public abstract void addStatementParams(List params);
	
	/**
	 * Returns the total count SQL query String and parameters. 
	 */
	protected abstract SqlStatement getCountSqlStatement();

	/**
	 * Returns the itme range SQL query String and parameters. 
	 */
	protected abstract SqlStatement getRangeSqlStatement();	

	// *********************************************************************
	// * EXECUTING SQL AND RETURING RESULTS
	// *********************************************************************	
	
	/**
	 * Stores the <code>DataSource</code>.
	 */
	public void setDataSource(DataSource ds) {
		this.ds = ds;
	}
	
	/**
	 * Execute a JDBC data access operation, implemented as callback action
	 * working on a JDBC Connection.
	 * 
	 * The stored <code>DataSource</code> is used to provide JDBC connection for
	 * the action. The connection is always closed after the action.
	 * 
	 * This method is used by all other <code>execute</code> methods in
	 * <code>ListSqlHelper</code>. To override getting the connection, you
	 * have to use one of the <code>ConnectionCallback</code> returning methods
	 * and use your own implementation to execute it.
	 * 
	 * @param action callback object that specifies the action.
	 * @return a result object returned by the action, or null.
	 */
	public Object execute(ConnectionCallback action) {		
		if (this.ds == null) {
			throw new RuntimeException("Please pass a DataSource to the ListSqlHelper!");
		}
		
		Connection con = null;
		try {
			con = ds.getConnection();
			return action.doInConnection(con);
		} catch (SQLException e) {
			throw ExceptionUtil.uncheckException(e);
		} finally {
			DbHelper.closeDbObjects(con, null, null);
		}
	}
	
	/**
	 * Executes SQL queries that should retrieve 1) the total count of items in
	 * the list and 2) a range of items from the list
	 * 
	 * Provided <code>ResultReader</code> is used to convert the
	 * <code>ResultSet</code> into a <code>List</code>.
	 * 
	 * The stored <code>DataSource</code> is used to provide JDBC connection for
	 * the action. The connection will be closed automatically. 
	 * 
	 * @param reader
	 *         <code>ResultSet</code> reader.
	 * @return <code>ListItemsData</code> containing the item range and total
	 *         count.
	 */
	public ListItemsData execute(ResultReader reader) {
		return (ListItemsData) execute(getListItemsDataCallback(reader));
	}

	/**
	 * Executes SQL queries that should retrieve 1) the total count of items in
	 * the list and 2) a range of items from the list
	 * 
	 * <code>ListSqlHelper</code>'s <code>BeanResultReader</code> is used
	 * to convert the <code>ResultSet</code> into a <code>List</code>.
	 * 
	 * The stored <code>DataSource</code> is used to provide JDBC connection for
	 * the action. The connection will be closed automatically. 
	 * 
	 * @param itemClass
	 *         Bean class.
	 * @return <code>ListItemsData</code> containing the item range and total
	 *         count.
	 */
	public ListItemsData execute(Class itemClass) {
		return (ListItemsData) execute(getListItemsDataCallback(getBeanResultReader(itemClass)));
	}
	
	/**
	 * Executes a SQL query that should retrieve the total count of items in the
	 * list.
	 * 
	 * The stored <code>DataSource</code> is used to provide JDBC connection for
	 * the action. The connection will be closed automatically. 
	 *
	 * @return the total count of items in the list.
	 */
	public Long executeCountSql() {
		return (Long) execute(getCountSqlCallback());
	}

	/**
	 * Executes a SQL query that should retrieve a range of items from the
	 * list.
	 * 
	 * Provided <code>ResultReader</code> is used to convert the
	 * <code>ResultSet</code> into a <code>List</code>.
	 * 
	 * The stored <code>DataSource</code> is used to provide JDBC connection for
	 * the action. The connection will be closed automatically.
	 *  
	 * @param reader
	 *         <code>ResultSet</code> reader.
	 * @return <code>List</code> containing the item range.
	 */	
	public List executeItemRangeSql(ResultReader reader) {
		return (List) execute(getItemRangeSqlCallback(reader));
	}
	
	/**
	 * Executes a SQL query that should retrieve a range of items from the
	 * list.
	 * 
	 * <code>ListSqlHelper</code>'s <code>BeanResultReader</code> is used
	 * to convert the <code>ResultSet</code> into a <code>List</code>.
	 * 
	 * The stored <code>DataSource</code> is used to provide JDBC connection for
	 * the action. The connection will be closed automatically.
	 *  
	 * @param itemClass
	 *         Bean class.
	 * @return <code>List</code> containing the item range.
	 */	
	public List executeItemRangeSql(Class itemClass) {
		return (List) execute(getItemRangeSqlCallback(getBeanResultReader(itemClass)));
	}
	
	// *********************************************************************
	// * CALLBACKS
	// *********************************************************************	
	
	/**
	 * Returns the total count and item ragne queries callback.
	 * 
	 * In most cases, you should not use this method directly, instead
	 * using one of the <code>execute</code> methods is recommended.
	 */
	public ConnectionCallback getListItemsDataCallback(ResultReader reader) {
		return new ListItemsDataCallback(getCountSqlCallback(), getItemRangeSqlCallback(reader));
	}

	/**
	 * Returns the total count query callback.
	 * 
	 * In most cases, you should not use this method directly, instead
	 * using one of the <code>execute</code> methods is recommended.
	 */
	public ConnectionCallback getCountSqlCallback() {
		return new CountSqlCallback();
	}

	/**
	 * Returns the item range query callback.
	 * 
	 * In most cases, you should not use this method directly, instead
	 * using one of the <code>execute</code> methods is recommended.
	 */
	public ConnectionCallback getItemRangeSqlCallback(ResultReader reader) {
		return new ItemRangeSqlCallback(reader);
	}
	
	/**
	 * The item range and total count querites callback that returns
	 * <code>ListItemsData</code> object.
	 * 
	 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
	 */
	public class ListItemsDataCallback implements ConnectionCallback {
		
		protected ConnectionCallback countSqlCallback;
		protected ConnectionCallback itemRangeSqlCallback;
		
		/**
		 * @param countSqlCallback total count query callback.
		 * @param itemRangeSqlCallback item range query callback.
		 */
		public ListItemsDataCallback(ConnectionCallback countSqlCallback, ConnectionCallback itemRangeSqlCallback) {
			this.countSqlCallback = countSqlCallback;
			this.itemRangeSqlCallback = itemRangeSqlCallback;
		}
		
		/**
		 * Executes both queries, creates and returns <code>ListItemsData</code>
		 * object containing results of both queries.
		 * 
		 * @return the whole results as <code>ListItemsData</code> object.
		 */
		public Object doInConnection(Connection con) throws SQLException {			
			ListItemsData result = new ListItemsData();
			result.setTotalCount((Long) countSqlCallback.doInConnection(con));
			result.setItemRange((List) itemRangeSqlCallback.doInConnection(con));
			return result;
		}
	}
	
	/**
	 * The total count query callback.
	 * 
	 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
	 */
	public class CountSqlCallback implements ConnectionCallback {
		/** 
		 * Executes total count query and returns the result.
		 * 
		 * @return the total count as <code>Long</code> object.  
		 */
		public Object doInConnection(Connection con) throws SQLException {
			PreparedStatement stmt = null;
			ResultSet rs = null;
			try {
				SqlStatement countSqlStatement = getCountSqlStatement();

				log.debug("Counting database query: " + countSqlStatement.getQuery());
				log.debug("Counting statement parameters: " + countSqlStatement.getParams());

				stmt = con.prepareStatement(countSqlStatement.getQuery());
				countSqlStatement.propagateStatementWithParams(stmt);

				try {
					rs = stmt.executeQuery();	
				} catch (SQLException e) {
					throw createQueryFailedException(countSqlStatement.getQuery(), countSqlStatement.getParams(), e);
				}
				
				if (rs.next()) {
					return new Long(rs.getLong(1));
				}
				return null;
			}
			finally {
				DbHelper.closeDbObjects(null, stmt, rs);
			}
		}		
	}
	
	/**
	 * The itme range query callback that returns <code>List</code> of items.
	 * 
	 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
	 */
	public class ItemRangeSqlCallback implements ConnectionCallback {	
		protected ResultReader reader;
		
		/**
		 * @param reader <code>ResultSet</code> reader that processes the data
		 * and returns items as <code>List</code>.
		 */
		public ItemRangeSqlCallback(ResultReader reader) {
			this.reader = reader;
		}
		
		/**
		 * Executes the item range query and returns the reuslts.
		 * 
		 * @return list items as <code>List</code> object. 
		 */
		public Object doInConnection(Connection con) throws SQLException {
			PreparedStatement stmt = null;
			ResultSet rs = null;
			
			try {
				SqlStatement rangeSqlStatement = getRangeSqlStatement();

				log.debug("Item range database query: " + rangeSqlStatement.getQuery());
				log.debug("Item range statement parameters: " + rangeSqlStatement.getParams());

				stmt = con.prepareStatement(rangeSqlStatement.getQuery());
				rangeSqlStatement.propagateStatementWithParams(stmt);

				try {
					rs = stmt.executeQuery();
				} catch (SQLException e) {
					throw createQueryFailedException(rangeSqlStatement.getQuery(), rangeSqlStatement.getParams(), e);
				}
				
				while (rs.next()) {
					reader.processRow(rs);
				}
				return reader.getResults();
			}
			finally {
				DbHelper.closeDbObjects(null, stmt, rs);
			}
		}
	}
	
	// *********************************************************************
	// * BEAN RESULT READER
	// *********************************************************************	
	
	/**
	 * Returns Bean <code>ResultSet</code> reader.
	 * 
	 * In most cases, you should not use this method directly, instead
	 * using one of the <code>execute</code> methods is recommended.
	 */
	public ResultReader getBeanResultReader(Class itemClass) {
		return new BeanResultReader(itemClass);
	}
	
	/**
	 * Resultset reader that uses <code>beanToResultSetMapping</code> in
	 * <code>ListSqlHelper</code> to construct a given type of Bean list.
	 * 
	 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
	 */
	public class BeanResultReader implements ResultReader {
		
		protected Class itemClass;
		protected List results;
		protected GeneralBeanMapper beanMapper;
		
		/**
		 * @param itemClass Bean type.
		 */
		public BeanResultReader(Class itemClass) {
			this.itemClass = itemClass;
			this.results = new ArrayList();
			this.beanMapper = new RecursiveBeanMapper(itemClass, true);
		}
		
		/** 
		 * Processes <code>ResultSet</code> row passing it with the
		 * new Bean instance to {@link #readBeanFields(ResultSet, Object)}
		 * method.
		 */
		public void processRow(ResultSet rs) throws SQLException {
			try {
				while (rs.next()) {
					Object record = itemClass.newInstance();
					readBeanFields(rs, record);
					this.results.add(record);
				}
			}
			catch (InstantiationException e) {
				throw ExceptionUtil.uncheckException(e);
			}
			catch (IllegalAccessException e) {
				throw ExceptionUtil.uncheckException(e);
			}
		}

		/**
		 * Reads the bean from <code>ResultSet</code>. Implementations
		 * may override it to read beans in a custom way.
		 * 
		 * @param resultSet
		 *            <code>ResultSet</code> containing the results of database
		 *            query.
		 * @param bean
		 *            bean to read.
		 */
		protected void readBeanFields(ResultSet resultSet, Object bean) {
			Collection fields = beanToResultSetMapping.keySet();
			for (Iterator i = fields.iterator(); i.hasNext();) {
				String field = (String) i.next();

				if (!this.beanMapper.fieldIsWritable(field))
					throw new RuntimeException(
							"The field specified in the mapping doesn't have a corresponding Value Object field!");

				readBeanField(resultSet, bean, field);
			}
		}

		/**
		 * Reads the bean field from <code>ResultSet</code>.
		 * Implementations may override it to read bean fields in a custom
		 * way. A usual situation would be when a bean field is read from
		 * more than one <code>ResultSet</code> field.
		 * 
		 * @param resultSet
		 *            <code>ResultSet</code> containing the results of database
		 *            query.
		 * @param bean
		 *            bean to read.
		 * @param field
		 *            bean field to read.
		 */
		protected void readBeanField(ResultSet resultSet, Object bean, String field) {
			String resultSetColumnName = (String) beanToResultSetMapping
			.get(field);
			Converter deconverter = (Converter) beanDeconverters.get(field);

			Class valueType;
			if (deconverter != null) {
				valueType = deconverter.getDestinationType();
			} else {
				valueType = this.beanMapper.getBeanFieldType(field);
			}

			Object value = resultSetColumnReader.readFromResultSet(
					resultSetColumnName, resultSet, valueType);
			if (deconverter != null) {
				value = deconverter.reverseConvert(value);
			}
			this.beanMapper.setBeanFieldValue(bean, field, value);
		}
		
		/** 
		 * Returns the results.
		 */
		public List getResults() {
			return this.results;
		}
	}
	
	// *********************************************************************
	// * HELPER METHODS
	// *********************************************************************

	/**
	 * Creates the ValueConverter for SqlExpressionBuilder that converts Values
	 * according to the previously set Converters.
	 * 
	 * @return the ValueConverter for SqlExpressionBuilder that converts Values
	 *         according to the previously set Converters.
	 */
	protected ValueConverter createExpressionBuilderConverter() {
		ConverterManager manager = new ConverterManager();
		manager.addGlobalConverter(new DummyConverter());

		Iterator i = this.valueConverters.keySet().iterator();
		while (i.hasNext()) {
			String valueName = (String) i.next();
			Converter converter = (Converter) this.valueConverters
			.get(valueName);
			if (converter != null) {
				manager.addConverter(valueName, converter);
			}
		}
		return manager;
	}

	/**
	 * Creates the VariableResolver for SqlExpressionBuilder that converts
	 * Variable names to their Database Field names according to the previously
	 * set mapping.
	 * 
	 * @return the VariableResolver for SqlExpressionBuilder that converts
	 *         Variable names to their Database Field names according to the
	 *         previously set mapping.
	 */
	protected VariableResolver createExpressionBuilderResolver() {
		Map map = new HashMap();

		Iterator i = this.variableToDatabaseMapping.keySet().iterator();
		while (i.hasNext()) {
			String varName = (String) i.next();
			map.put(varName, this.variableToDatabaseMapping.get(varName));
		}
		return new VariableMapper(map);
	}
	
	/**
	 * Returns query failed Exception that contains query String and params. 
	 */
	protected static RuntimeException createQueryFailedException(
			String QueryString, List queryParams, SQLException nestedException) {
		String str = new StringBuffer("Executing list query [").append(QueryString).
			append("] with params: ").append(queryParams).append(" failed").toString();
		return new AraneaRuntimeException(str, nestedException);
	}
}
