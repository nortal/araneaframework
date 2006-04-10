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
import org.araneaframework.backend.list.helper.ConverterManager;
import org.araneaframework.backend.list.helper.DbHelper;
import org.araneaframework.backend.list.helper.VariableMapper;
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
import org.araneaframework.backend.util.BeanMapper;
import org.araneaframework.uilib.list.util.Converter;
import org.araneaframework.uilib.list.util.converter.DummyConverter;


/**
 * This class provides an SQL based implementation of the list. It takes care of
 * the filtering, ordering and returning data to the web components.
 * Implementations should override abstract methods noted in thos methods.
 * <p>
 * Note, that all operations on items are made on the list of "processed", that
 * is ordered and filtered items.
 * <p>
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
 */
public abstract class ListSqlHelper {
	
	private static Logger log = Logger.getLogger(ListSqlHelper.class);
	
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
	protected Connection con;
	protected PreparedStatement itemRangeStatement;
	
	// RESULTS
	
	protected Long totalCount;
	protected List itemData;
	protected ResultSet itemRangeResultSet;
	
	// RESULTSET READING
	
	protected ResultSetColumnReader resultSetReader = DefaultResultSetColumnReader
	.getInstance();
	protected BeanMapper beanMapper;
	
	// *********************************************************************
	// * CONSTRUCTORS
	// *********************************************************************
	
	/**
	 * Creates <code>ListSqlHelper</code> initializing the appropriate fields.
	 * 
	 * @param filterExpr
	 *            the filter.
	 * @param orderExpr
	 *            the order.
	 * @param itemRangeStart
	 *            start of item range.
	 * @param itemRangeCount
	 *            count of items in range.
	 */
	public ListSqlHelper(ListQuery query) {		
		this.filterExpr = query.getFilterExpression();
		this.orderExpr = query.getOrderExpression();
		this.itemRangeStart = query.getItemRangeStart();
		this.itemRangeCount = query.getItemRangeCount();
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
	
	public ResultSetColumnReader getResultSetReader() {
		return this.resultSetReader;
	}

	public void setResultSetReader(ResultSetColumnReader resultSetReader) {
		this.resultSetReader = resultSetReader;
	}
	
	/*
	 * Database mapping and converters.
	 */

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
	protected void setResultSetMapping(String beanFieldName,
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
	
	private String makeFieldNameUnique(String field) {
		String alias = field;
		int index = 0;
		while (this.beanToResultSetMapping.containsValue(alias)) {
			alias = alias + index++;
		}
		return alias;
	}
	
	private static String removePrefix(String field) {
		return field.substring(field.lastIndexOf('.') + 1);
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
	
	/*
	 * Building SqlExpressions according to Ordering and Filtering.
	 */
	
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
			
			String sql = alias == null || alias.equals(dbField)
				? dbField : new StringBuffer(dbField).append(" ").append(alias).toString();
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
		StandardCompExprToSqlExprBuilder builder = new StandardCompExprToSqlExprBuilder();
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
		StandardExpressionToSqlExprBuilder builder = new StandardExpressionToSqlExprBuilder();
		builder.setMapper(createExpressionBuilderResolver());
		builder.setConverter(createExpressionBuilderConverter());
		this.filterSqlExpr = builder.buildSqlExpression(this.filterExpr);
		return this.filterSqlExpr;
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
	 * Returns the <code>List</code> of parameters that should be set in the <code>PreparedStatement</code> that
	 * belong to the filter database conditions.
	 * 
	 * @return the <code>List</code> of parameters that should be set in the <code>PreparedStatement</code> that
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
	 * Returns the <code>List</code> of parameters that should be set in the <code>PreparedStatement</code> that
	 * belong to the order database representation.
	 * 
	 * @return the <code>List</code> of parameters that should be set in the <code>PreparedStatement</code> that
	 *         belong to the order database representation.
	 */
	public List getDatabaseOrderParams() {
		SqlExpression expr = this.getOrderSqlExpression();
		return expr != null ? Arrays.asList(expr.getValues()) : new ArrayList();
	}
	
	/*
	 * Preparing database queries.
	 */
	
	/**
	 * Sets the count of items in the range.
	 * 
	 * @param itemRangeCount
	 *            the count of items in the range.
	 */
	public void setItemRangeCount(Long itemRangeCount) {
		this.itemRangeCount = itemRangeCount;
	}
	
	/**
	 * Sets the (0-based) starting index of the item range.
	 * 
	 * @param itemRangeStart
	 *            the (0-based) starting index of the item range.
	 */
	public void setItemRangeStart(Long itemRangeStart) {
		this.itemRangeStart = itemRangeStart;
	}
	
	protected abstract SqlStatement getCountSqlStatement();
	
	protected abstract SqlStatement getRangeSqlStatement();	
	
	/*
	 * Executing database queries and returning their results.
	 */
	
	/**
	 * Implementations should set the <code>DataSource</code> on
	 * <code>setSessionContext</code>.
	 * 
	 * @throws SQLException
	 */
	public void setDataSource(DataSource ds) throws SQLException {
		this.ds = ds;
		this.con = ds.getConnection();
	}
	
	/**
	 * Executes the SQL query that should return the total count of items in the
	 * list, retrieving the total count.
	 * 
	 * @throws SQLException
	 */
	public void executeCountSql() throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {			
			SqlStatement countSqlStatement = getCountSqlStatement();
			
			log.debug("Counting database query: " + countSqlStatement.getQuery());
			log.debug("Counting statement parameters: " + countSqlStatement.getParams());
			
			stmt = this.con.prepareStatement(countSqlStatement.getQuery());
			countSqlStatement.propagateStatementWithParams(stmt);
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				this.totalCount = new Long(rs.getLong(1));
			}
		} finally {
			DbHelper.closeDbObjects(null, stmt, rs);
		}
	}
	
	/**
	 * Executes an SQL query that should retrieve a range of items from the
	 * list, saves <code>ResultSet</code> for further processing.
	 * 
	 * @throws SQLException
	 */
	public void executeItemRangeSql() throws SQLException {
		SqlStatement rangeSqlStatement = getRangeSqlStatement();
		log.debug("Item range database query: " + rangeSqlStatement.getQuery());
		log.debug("Item range statement parameters: " + rangeSqlStatement.getParams());
		
		this.itemRangeStatement = this.con.prepareStatement(rangeSqlStatement.getQuery());
		rangeSqlStatement.propagateStatementWithParams(this.itemRangeStatement);
		
		/*if (this.itemRangeCount != null && (this.itemRangeCount.longValue() < 1000)) {
			this.itemRangeStatement.setFetchSize(this.itemRangeCount.intValue());
		}*/
		
		this.itemRangeResultSet = this.itemRangeStatement.executeQuery();
	}
	
	/**
	 * Executes the item range and total count SQL queries.
	 * 
	 * @throws SQLException
	 */
	public void execute() throws SQLException {
		if (this.ds == null) {
			throw new RuntimeException(
			"Please pass a DataSource to the ListSqlHelper!");
		}
		
		executeCountSql();
		executeItemRangeSql();
	}
	
	/**
	 * Tries to retrieve the item range from the saved <code>ResultSet</code>
	 * and returns the <code>ListItemsData</code> containing the item range
	 * and total count.
	 * 
	 * @param beanClass
	 *            Bean class.
	 * @return <code>ListItemsData</code> containing the item range and total
	 *         count.
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SQLException
	 */
	public ListItemsData getListItemsData(Class beanClass) throws SQLException,
	InstantiationException, IllegalAccessException {
		ListItemsData result = new ListItemsData();
		result.setTotalCount(this.totalCount);
		
		this.beanMapper = new BeanMapper(beanClass);
		
		List itemRange = new ArrayList();
		//XXX add capacity
		
		while (this.itemRangeResultSet.next()) {
			Object record = beanClass.newInstance();
			readBeanFields(this.itemRangeResultSet, record);
			itemRange.add(record);
		}
		
		result.setItemRange(itemRange);
		
		return result;
	}
	
	/**
	 * Returns the item range <code>ResultSet</code>.
	 * 
	 * @return the item range <code>ResultSet</code>.
	 */
	public ResultSet getItemRangeResultSet() {
		return this.itemRangeResultSet;
	}
	
	/**
	 * Returns the total count of items in the list.
	 * 
	 * @return the total count of items in the list.
	 */
	public Long getTotalCount() {
		return this.totalCount;
	}
	
	/**
	 * Closes the <code>ListSqlHelper</code> closing the opened database
	 * objects.
	 */
	public void close() {
		DbHelper.closeDbObjects(this.con, this.itemRangeStatement,
				this.itemRangeResultSet);
	}
	
	// *********************************************************************
	// * HELPER METHODS
	// *********************************************************************
	
	/**
	 * Reads the bean from <code>ResultSet</code>. Implementations
	 * may override it to read beans in a custom way.
	 * 
	 * @param resultSet
	 *            <code>ResultSet</code> containing the results of database
	 *            query.
	 * @param bean
	 *            bean to read.
	 * @throws SQLException
	 *             in case of a database error.
	 */
	protected void readBeanFields(ResultSet resultSet, Object bean)
	throws SQLException {
		log.debug("Starting to read value object fields.");
		
		Collection fields = this.beanToResultSetMapping.keySet();
		for (Iterator i = fields.iterator(); i.hasNext();) {
			String field = (String) i.next();
			
			if (!this.beanMapper.fieldIsWritable(field))
				throw new RuntimeException(
				"The field specified in the mapping doesn't have a corresponding Value Object field!");
			
			readBeanField(resultSet, bean, field);
		}
		
		log.debug("Finished reading value object fields.");
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
	 * @throws SQLException
	 *             in case of a database error.
	 */
	protected void readBeanField(ResultSet resultSet, Object bean, String field)
	throws SQLException {
		
		String resultSetColumnName = (String) this.beanToResultSetMapping
		.get(field);
		Converter deconverter = (Converter) this.beanDeconverters.get(field);
		
		log.debug("Reading VO field '" + field + "' from ResultSet column '"
				+ resultSetColumnName + "'.");
		
		Class valueType;
		if (deconverter != null) {
			valueType = deconverter.getDestinationType();
		} else {
			valueType = this.beanMapper.getBeanFieldType(field);
		}
		
		Object value = this.resultSetReader.readFromResultSet(
				resultSetColumnName, resultSet, valueType);
		if (deconverter != null) {
			value = deconverter.reverseConvert(value);
		}
		this.beanMapper.setBeanFieldValue(bean, field, value);
		
		log.debug("Read VO field '" + field + "' from ResultSet column '"
				+ resultSetColumnName + "' with value '" + value + "'.");
	}
	
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
	
}
