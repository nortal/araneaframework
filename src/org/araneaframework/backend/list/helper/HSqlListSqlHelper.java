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

import java.util.List;

import org.araneaframework.backend.list.model.ListQuery;


public class HSqlListSqlHelper extends ListSqlHelper {
	
	private static final Long ZERO = new Long(0);
	private static final Long MAX = new Long(Long.MAX_VALUE);

	protected SqlStatement statement = new SqlStatement();

	protected String countSqlQuery = null;
	
	public HSqlListSqlHelper(ListQuery query) {
		super(query);
	}

	public HSqlListSqlHelper() {
		super();
	}

	protected SqlStatement getCountSqlStatement() {
		if (this.countSqlQuery != null) {
			return new SqlStatement(this.countSqlQuery, this.statement
					.getParams());
		}
		String temp = new StringBuffer("SELECT COUNT(*) FROM (SELECT ").append(
				this.statement.getQuery()).append(")").toString();
		return new SqlStatement(temp, this.statement.getParams());
	}

	protected SqlStatement getRangeSqlStatement() {
		SqlStatement result;
		
		if (isShowAll()) {
			result = new SqlStatement("SELECT " + this.statement.getQuery());
			result.addAllParams(this.statement.getParams());
		} else {
			StringBuffer query = new StringBuffer();
			query.append("SELECT LIMIT ? ? ");
			query.append(this.statement.getQuery());

			result = new SqlStatement(query.toString());
			result.addParam(this.itemRangeStart);
			result.addParam(this.itemRangeCount);
			result.addAllParams(this.statement.getParams());
		}		
		
		return result;
	}
	
	protected boolean isShowAll() {
		return this.itemRangeStart == null || ZERO.equals(this.itemRangeStart)
			&& (this.itemRangeCount == null || MAX.equals(this.itemRangeCount));
	}

	/**
	 * Sets the SQL query that will be used to retrieve the item range from the
	 * list and count the items. The SQL query must start with SELECT expression
	 * withoud the word "SELECT".
	 * 
	 * @param sqlQuery
	 *            the SQL query that will be used to retrieve the item range
	 *            from the list and count the items.
	 */
	public void setSqlQuery(String sqlQuery) {
		this.statement.setQuery(sqlQuery);
	}

	/**
	 * Sets the SQL query used to count the items in the database.
	 * 
	 * @param countSqlQuery
	 *            the SQL query used to count the items in the database.
	 */
	public void setCountSqlQuery(String countSqlQuery) {
		this.countSqlQuery = countSqlQuery;
	}

	/**
	 * Adds a <code>NULL</code> <code>PreparedStatement</code> parameter for
	 * later setting.
	 * 
	 * @param valueType
	 *            the type of the NULL value.
	 */
	public void addNullParam(int valueType) {
		this.statement.addNullParam(valueType);
	}

	/**
	 * Adds a <code>PreparedStatement</code> parameter for later setting.
	 * 
	 * @param param
	 *            a <code>PreparedStatement</code> parameter.
	 */
	public void addStatementParam(Object param) {
		this.statement.addParam(param);
	}

	/**
	 * Adds <code>PreparedStatement</code> parameters for later setting.
	 * 
	 * @param params
	 *            <code>PreparedStatement</code> parameters.
	 */
	public void addStatementParams(List params) {
		this.statement.addAllParams(params);
	}
}
