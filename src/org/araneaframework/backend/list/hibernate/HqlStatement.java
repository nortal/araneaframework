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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.araneaframework.backend.list.helper.NullValue;
import org.hibernate.Query;


/**
 * @author Rein Raudjärv
 */
public class HqlStatement implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	protected String query;
	protected List parameters;

	public HqlStatement(String query, List parameters) {
		this.query = query;
		this.parameters = parameters;
	}

	public HqlStatement(String query) {
		this(query, new ArrayList());
	}

	public HqlStatement() {
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
	 * Adds parameters at the specified position.
	 * 
	 * @param index
	 *            index at witch the specified parameters will be inserted.
	 * @param params
	 *            parameters.
	 */
	public void addAllParams(int index, Object[] params) {
		for (int i = 0; i < params.length; i++) {
			this.parameters.add(index + i, params[i]);
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
	 * Adds parameters.
	 * 
	 * @param params
	 *            parameters.
	 */
	public void addAllParams(Object[] params) {
		addAllParams(countParams(), params);
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
		return new HqlStatement(this.query, new ArrayList(this.parameters));
	}
	
	// *********************************************************************
	// * QUERY PROPAGATION
	// *********************************************************************

	/**
	 * Helper method that sets the parameters to the
	 * <code>Query</code>.
	 * 
	 * @param queryObject <code>Query</code> which parameters will be set.
	 */
	public void prepareQuery(Query queryObject) {
		int i = 0;
		for (Iterator it = parameters.iterator(); it.hasNext(); i++) {
			Object value = it.next();
			queryObject.setParameter(i, value);
		}
	}	
}
