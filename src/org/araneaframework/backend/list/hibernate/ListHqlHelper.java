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

package org.araneaframework.backend.list.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.helper.builder.compexpr.StandardCompExprToSqlExprBuilder;
import org.araneaframework.backend.list.helper.builder.expression.StandardExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.backend.list.sqlexpr.SqlExpressionUtil;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.uilib.list.SequenceHelper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


/**
 * This class provides an Hibernate based implementation of the list. It takes care of
 * the filtering, ordering and returning data to the web components.
 * 
 * @author Rein Raudjärv
 */
public class ListHqlHelper {

	protected static final Log log = LogFactory.getLog(ListHqlHelper.class);

	// *******************************************************************
	// FIELDS
	// *******************************************************************

	// SESSION FACTORY
	
	private SessionFactory sf; 

	// LIST QUERY
	
	private ListQuery listQuery;

	// SQL EXPRESSION
	
	private SqlExpression filterSqlExpr;
	private SqlExpression orderSqlExpr;
	
	private boolean filterSqlExprInited = false;
	private boolean orderSqlExprInited = false;

	// STATEMENT
	
	private HqlStatement statement;
	private HqlStatement countStatement;

	// *********************************************************************
	// * CONSTRUCTORS
	// *********************************************************************

	/**
	 * Creates <code>ListSqlHelper</code> initializing the appropriate fields
	 * and providing it with the <code>DataSource</code>.
	 */
	public ListHqlHelper(SessionFactory sf, ListQuery query) {
		setSessionFactory(sf);
		setListQuery(query);
	}

	/**
	 * Creates <code>ListSqlHelper</code> and provides it with the
	 * <code>DataSource</code>.
	 */
	public ListHqlHelper(SessionFactory sf) {
		setSessionFactory(sf);
	}
	
	/**
	 * Creates <code>ListSqlHelper</code> initializing the appropriate fields.
	 */
	public ListHqlHelper(ListQuery query) {
		setListQuery(query);
	}

	/**
	 * Creates <code>ListSqlHelper</code> without initializing any fields.
	 */
	public ListHqlHelper() {
		// for bean creation
	}

	// *********************************************************************
	// * PUBLIC METHODS
	// *********************************************************************

	public SessionFactory getSessionFactory() {
		return sf;
	}

	public void setSessionFactory(SessionFactory sf) {
		this.sf = sf;
	}

	public ListQuery getListQuery() {
		return listQuery;
	}

	/**
	 * Sets the starting index and count of items in the range and
	 * filtering and ordering expressions. 
	 */
	public void setListQuery(ListQuery listQuery) {
		this.listQuery = listQuery;
	}
	
	public HqlStatement getStatement() {
		return statement;
	}

	public void setStatement(HqlStatement statement) {
		this.statement = statement;
	}

	public HqlStatement getCountStatement() {
		return countStatement;
	}

	public void setCountStatement(HqlStatement countStatement) {
		this.countStatement = countStatement;
	}	
	
	// *********************************************************************
	// * BUILDING SQL EXPRESSIONS ACCORDING TO ORDERING AND FILTERING
	// *********************************************************************	

	/**
	 * Returns the order <code>SqlExpression</code>, which can be used in
	 * "ORDER BY" clause.
	 * 
	 * @return the order <code>SqlExpression</code>, which can be used in
	 *         "ORDER BY" clause.
	 */
	protected SqlExpression getOrderSqlExpression() {
		if (orderSqlExprInited) {
			return this.orderSqlExpr;
		}

		ComparatorExpression orderExpr = listQuery.getOrderExpression();
		if (orderExpr != null) {
			StandardCompExprToSqlExprBuilder builder = createOrderSqlExpressionBuilder();
			this.orderSqlExpr = SqlExpressionUtil.toSql(orderExpr, builder);
		}
		
		orderSqlExprInited = true;
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
		if (filterSqlExprInited) {
			return this.filterSqlExpr;
		}
		
		Expression filterExpr = listQuery.getFilterExpression();
		if (filterExpr != null) {
			StandardExpressionToSqlExprBuilder builder = createFilterSqlExpressionBuilder();
			this.filterSqlExpr = SqlExpressionUtil.toSql(filterExpr, builder);
		}
		
		filterSqlExprInited = true;
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
	
	/**
	 * Returns the filter database condition, which can be used in "WHERE"
	 * clause.
	 * 
	 * @return the filter database condition, which can be used in "WHERE"
	 * clause.
	 * 
	 * @see #getDatabaseFilterWith(String, String)
	 * @see #getDatabaseFilterParams()
	 */
	public String getDatabaseFilter() {
		return getSqlString(getFilterSqlExpression());
	}

	/**
	 * Returns the database filter query with <code>prefix</code> added before and 
	 * <code>suffix</code> after it if the query is not empty.
	 * 
	 * @param prefix Prefix added before the expression.
	 * @param suffix Suffix added after the expression.
	 * 
	 * @return the database filter query with <code>prefix</code> added before and 
	 *  <code>suffix</code> after it if the query is not empty.
	 * 
	 * @see #getDatabaseFilter()
	 * @see #getDatabaseFilterParams()
	 */
	public String getDatabaseFilterWith(String prefix, String suffix) {
		SqlExpression sqlExpr = getFilterSqlExpression();
		return sqlExpr != null ? getSqlStringWith(sqlExpr, prefix, suffix) : "";		
	}

	/**
	 * Returns the <code>List</code> of parameters that should be set in the
	 * <code>PreparedStatement</code> that
	 * belong to the filter database conditions.
	 * 
	 * @return the <code>List</code> of parameters that should be set in the
	 * <code>PreparedStatement</code> that
	 *         belong to the filter database conditions.
	 *         
	 * @see #getDatabaseFilter()
	 * @see #getDatabaseFilterWith(String, String)
	 */
	public List getDatabaseFilterParams() {
		return getSqlParams(getFilterSqlExpression());
	}

	/**
	 * Returns the order database representation, which can be used in "ORDER BY" clause.
	 * 
	 * @return the order database representation, which can be used in "ORDER BY" clause.
	 * 
	 * @see #getDatabaseOrderWith(String, String)
	 * @see #getDatabaseOrderParams()
	 */
	public String getDatabaseOrder() {
		return getSqlString(getOrderSqlExpression());
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
	 * @see #getDatabaseOrderParams()
	 */
	public String getDatabaseOrderWith(String prefix, String suffix) {
		SqlExpression sqlExpr = getOrderSqlExpression();
		return sqlExpr != null ? getSqlStringWith(sqlExpr, prefix, suffix) : "";		
	}  

	/**
	 * Returns the <code>List</code> of parameters that should be set in the
	 * <code>PreparedStatement</code> that belong to the order database representation.
	 * 
	 * @return the <code>List</code> of parameters that should be set in the
	 * <code>PreparedStatement</code> that belong to the order database representation.
	 * 
	 * @see #getDatabaseOrder()
	 * @see #getDatabaseOrderWith(String, String)
	 */
	public List getDatabaseOrderParams() {
		return getSqlParams(getOrderSqlExpression());
	}

	// *********************************************************************
	// * PREPARING DATABASE QUERIES
	// *********************************************************************		

	public void setSimpleSqlQuery(Class modelClass) {
		setSimpleSqlQuery(modelClass.getName());
	}
	
	/**
	 * Sets the SQL query (with arguments) that will be used to retrieve the
	 * item range from the list and count the items.
	 * <p>
	 * <code>ListQuery</code> filter and order conditions are used
	 * automatically.
	 * </p>
	 * <p>
	 * To use additional custom filter (and order) conditions,
	 * use {@link #setSimpleSqlQuery(String, String, Object[])} or
	 * {@link #setSimpleSqlQuery(String, String, Object[], String, Object[])}
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
		setSimpleSqlQuery(fromSql, null, (Object[]) null, null, (Object[]) null);
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
	 * {@link #setSimpleSqlQuery(String, String, Object[], String, Object[])} method.
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
	
	public void setSimpleSqlQuery(String fromSql, String customWhereSql, List customWhereArgs) {
		setSimpleSqlQuery(fromSql, customWhereSql, customWhereArgs, null, null);
	}
	
	public void setSimpleSqlQuery(String fromSql, String customWhereSql, List customWhereArgs,
			String customOrderbySql, List customOrderbyArgs) {
		setSimpleSqlQuery(fromSql,
				customWhereSql, customWhereArgs.toArray(),
				customOrderbySql, customOrderbyArgs.toArray());
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
	 * {@link #setSimpleSqlQuery(String, String, Object[])} method.
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
		
		statement = new HqlStatement();
		countStatement = new HqlStatement(); 
		
		// SQL String
		StringBuffer sb = new StringBuffer();
		sb.append("FROM ");
		sb.append(fromSql);
		if (customWhereSql == null) {
			sb.append(getDatabaseFilterWith(" WHERE ", ""));	
		} else {
			sb.append(" WHERE (");
			sb.append(customWhereSql);
			sb.append(")");
			sb.append(getDatabaseFilterWith(" AND ", ""));
		}
		countStatement.setQuery("SELECT COUNT(*) " + sb.toString());
		if (customWhereSql == null) {
			sb.append(getDatabaseOrderWith(" ORDER BY ", ""));	
		} else {
			sb.append(" ORDER BY ");
			sb.append(customOrderbySql);
			sb.append(getDatabaseOrderWith(", ", ""));
		}
		statement.setQuery(sb.toString());
		
		// --- SQL arguments ---
		
		// Custom Where Args
		if (customWhereArgs != null) {
			statement.addAllParams(customWhereArgs);
			countStatement.addAllParams(customWhereArgs);
		}
		
		// Standard Where Args
		{
			List params = getDatabaseFilterParams();
			statement.addAllParams(params);
			countStatement.addAllParams(params);
		}
		
		// Custom Order By Args
		if (customOrderbyArgs != null) {
			statement.addAllParams(customOrderbyArgs);
		}
		
		// Standard Order By Args
		statement.addAllParams(getDatabaseOrderParams());
	}
	
	// *********************************************************************
	// * EXECUTING SQL AND RETURING RESULTS
	// *********************************************************************	

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
	public Object execute(HibernateCallback action) {		
		if (this.sf == null) {
			throw new RuntimeException("Please pass a SessionFactory to the ListHqlHelper!");
		}
		
		Session session = null;
		try {
			session = sf.openSession();
			return action.doInHibernate(session);
		} catch (HibernateException e) {
			throw ExceptionUtil.uncheckException(e);
		} catch (SQLException e) {
			throw ExceptionUtil.uncheckException(e);
		} finally {
			if (session != null) {
				session.close();
			}
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
	public ListItemsData execute() {
		return (ListItemsData) execute(getListItemsDataCallback());
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
	public List executeItemRangeSql() {
		return (List) execute(getItemRangeSqlCallback());
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
	public HibernateCallback getListItemsDataCallback() {
		return new ListItemsDataCallback(getCountSqlCallback(), getItemRangeSqlCallback());
	}

	/**
	 * Returns the total count query callback.
	 * 
	 * In most cases, you should not use this method directly, instead
	 * using one of the <code>execute</code> methods is recommended.
	 */
	public HibernateCallback getCountSqlCallback() {
		return new CountSqlCallback();
	}

	/**
	 * Returns the item range query callback.
	 * 
	 * In most cases, you should not use this method directly, instead
	 * using one of the <code>execute</code> methods is recommended.
	 */
	public HibernateCallback getItemRangeSqlCallback() {
		return new ItemRangeSqlCallback();
	}
	
	/**
	 * The item range and total count querites callback that returns
	 * <code>ListItemsData</code> object.
	 * 
	 * @author Rein Raudjärv
	 */
	public static class ListItemsDataCallback implements HibernateCallback {
		
		protected HibernateCallback countSqlCallback;
		protected HibernateCallback itemRangeSqlCallback;
		
		/**
		 * @param countSqlCallback total count query callback.
		 * @param itemRangeSqlCallback item range query callback.
		 */
		public ListItemsDataCallback(HibernateCallback countSqlCallback, HibernateCallback itemRangeSqlCallback) {
			this.countSqlCallback = countSqlCallback;
			this.itemRangeSqlCallback = itemRangeSqlCallback;
		}
		
		/**
		 * Executes both queries, creates and returns <code>ListItemsData</code>
		 * object containing results of both queries.
		 * 
		 * @return the whole results as <code>ListItemsData</code> object.
		 */
		public Object doInHibernate(Session session) throws HibernateException,
				SQLException {
			ListItemsData result = new ListItemsData();
			result.setTotalCount((Long) countSqlCallback.doInHibernate(session));
			result.setItemRange((List) itemRangeSqlCallback.doInHibernate(session));
			return result;
		}
	}
	
	/**
	 * The total count query callback.
	 * 
	 * @author Rein Raudjärv
	 */
	public class CountSqlCallback implements HibernateCallback {
		public Object doInHibernate(Session session) throws HibernateException,
				SQLException {
			
			HqlStatement statement = getCountStatement();
			if (statement == null) {
				throw new NullPointerException("Count query statement has not been set - " +
						"use setSimpleSqlQuery() or set query stament and count query stament both explicitly");
			}
			
			String sql = statement.getQuery();
			List params = statement.getParams();
			
			if (log.isDebugEnabled()) {
				log.debug("Counting database query: " + sql);
				log.debug("Counting statement parameters: " + params);
			}
			
			Query queryObject = session.createQuery(sql);
			statement.prepareQuery(queryObject);
			
			Long count;
			{
				Object result = queryObject.uniqueResult();
				if (result instanceof Long) {
					count = (Long) result;
				} else if (result instanceof Integer) {
					count = new Long(((Integer) result).intValue());				
				} else {
					throw new AssertionError("Count query result must be either Integer or Long");
				}
			}
			
			if (log.isDebugEnabled()) {
				log.debug("Count: " + count);
			}
			
			return count;
		}
	}
	
	/**
	 * The itme range query callback that returns <code>List</code> of items.
	 * 
	 * @author Rein Raudjärv
	 */
	public class ItemRangeSqlCallback implements HibernateCallback {	
		public Object doInHibernate(Session session) throws HibernateException,
				SQLException {
			
			HqlStatement statement = getStatement();
			if (statement == null) {
				throw new NullPointerException("Statement has not been set");
			}
			
			String sql = statement.getQuery();
			List params = statement.getParams();
			
			if (log.isDebugEnabled()) {
				log.debug("Item range database query: " + sql);
				log.debug("Item range statement parameters: " + params);
			}
			
			Query queryObject = session.createQuery(sql);
			statement.prepareQuery(queryObject);
			
			// First result
			Long itemRangeStart = listQuery.getItemRangeStart();
			if (itemRangeStart != null && itemRangeStart.longValue() != 0) {
				queryObject.setFirstResult(itemRangeStart.intValue());
			}
			
			// Max results
			Long itemRangeCount = listQuery.getItemRangeCount();
			if (itemRangeCount != null && itemRangeCount.longValue() != SequenceHelper.FULL_ITEMS_ON_PAGE) {
				queryObject.setMaxResults(itemRangeCount.intValue());
			}
			
			return queryObject.list();
		}
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
	
	private static List getSqlParams(SqlExpression expr) {
		return (expr != null && expr.getValues() != null) ? Arrays.asList(expr.getValues()) : new ArrayList();
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
