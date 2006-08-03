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

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.araneaframework.backend.list.SqlExpression;


public class SqlStatement implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	protected String query;
	protected List parameters;

	public SqlStatement(String query, List parameters) {
		this.query = query;
		this.parameters = parameters;
	}

	public SqlStatement(String query) {
		this(query, new ArrayList());
	}

	public SqlStatement() {
		this(null);
	}

	public List getParams() {
		return this.parameters;
	}

	public void setParams(List params) {
		if (params == null) {
			throw new RuntimeException("Parameters list can not be NULL, use an empty list instead");
		}
		this.parameters = params;
	}

	public String getQuery() {
		return this.query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * Adds a parameter at the specified position.
	 * 
	 * @param index
	 *            index at witch the specified parameter will be inserted.
	 * @param param
	 *            a parameter.
	 */
	public void addParam(int index, Object param) {
		this.parameters.add(index, param);
	}

	/**
	 * Adds a <code>NULL</code> parameter at the specified position.
	 * 
	 * @param index
	 *            index at witch the specified parameter will be inserted.
	 * @param valueType
	 *            the type of the NULL value.
	 */
	public void addNullParam(int index, int valueType) {
		this.addParam(index, new NullValue(valueType));
	}

	/**
	 * Adds parameters at the specified position.
	 * 
	 * @param index
	 *            index at witch the specified parameters will be inserted.
	 * @param params
	 *            parameters.
	 */
	public void addAllParams(int index, List params) {
		this.parameters.addAll(index, params);
	}

	/**
	 * Adds parameters from the given <code>SqlExpression</code> at the specified position.
	 * 
	 * @param index
	 *            index at witch the specified parameters will be inserted.
	 * @param expr
	 *            <code>SqlExpression</code> instance.
	 *            
	 * @deprecated
	 */
	public void addAllExpressionParams(int index, SqlExpression expr) {
		if (expr != null) {
			this.parameters.addAll(index, Arrays.asList(expr.getValues()));			
		}
	}

	/**
	 * Counts all parameters.
	 * 
	 * @return paramaetrs count.
	 */
	public int countParams() {
		return this.parameters.size();
	}

	/**
	 * Adds a parameter.
	 * 
	 * @param param
	 *            a parameter.
	 */
	public void addParam(Object param) {
		addParam(countParams(), param);
	}

	/**
	 * Adds a <code>NULL</code> parameter.
	 * 
	 * @param valueType
	 *            the type of the NULL value.
	 */
	public void addNullParam(int valueType) {
		addNullParam(countParams(), valueType);
	}

	/**
	 * Adds parameters.
	 * 
	 * @param params
	 *            parameters.
	 */
	public void addAllParams(List params) {
		addAllParams(countParams(), params);
	}

	/**
	 * Adds parameters from the given <code>SqlExpression</code>.
	 * 
	 * @param expr
	 *            <code>SqlExpression</code> instance.
	 *            
	 * @deprecated
	 */
	public void addAllExpressionParams(SqlExpression expr) {
		addAllExpressionParams(countParams(), expr);
	}
	
	/**
	 * Clears all parameters.
	 */
	public void clearParams() {
		this.parameters.clear();
	}

	/**
	 * Constructs a new <code>SqlStatement</code> with the same
	 * <code>Query</code> and <code>Parameters</code>.
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		return new SqlStatement(this.query, new ArrayList(this.parameters));
	}

	// *********************************************************************
	// * PREPARED STATEMENT PROPAGATION
	// *********************************************************************

	/**
	 * Helper method that sets the parameters to the
	 * <code>PreparedStatement</code>.
	 * 
	 * @param statement
	 *            <code>PreparedStatement</code> which parameters will be set.
	 * @param parameters
	 *            parameters for the <code>PreparedStatement</code>.
	 * @throws SQLException
	 */
	protected void propagateStatementWithParams(PreparedStatement pstmt)
			throws SQLException {
		for (int i = 1; i <= this.parameters.size(); i++) {
			Object parameter = this.parameters.get(i - 1);
			if (parameter instanceof NullValue) {
				pstmt.setNull(i, ((NullValue) parameter).getType());
			} else {
				// converting java.util.Date into java.sql.Date
				// TODO is this correct?
			    if (parameter != null && java.util.Date.class.isAssignableFrom(parameter.getClass())) {
			    	parameter = new java.sql.Date(((java.util.Date) parameter).getTime());
			    }
				pstmt.setObject(i, parameter);
			}
		}
	}
}
