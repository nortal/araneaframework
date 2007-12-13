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

package org.araneaframework.backend.list.helper.naming;

import org.araneaframework.backend.list.helper.ListSqlHelper;

/**
 * Naming conventions between list fields and database columns.
 * <p>
 * Method {@link #fieldToColumnName(String)} converts field names into column names.
 * Method {@link #fieldToColumnAlias(String)} converts field names into column aliases.
 * 
 * @see ListSqlHelper#getNamingStrategy()
 * @see StandardNamingStrategy
 * @see MappingNamingStrategyAndFields
 * 
 * @author Rein Raudjärv
 */
public interface NamingStrategy {

	/**
	 * Converts <b>field name</b> into <b>database column name</b>.
	 * <p>
	 * E.g.<br/>
	 * <code>firstName</code> -> <code>FIRST_NAME</code><br/>
	 * <code>group.name</code> -> <code>G.NAME</code><br/>
	 * <code>name</code> -> <code>FIRSTNAME || " " || LASTNAME</code>
	 * <code>total</code> -> <code>sum(POINTS)</code>
	 * 
	 * @param fieldName field name of the list.
	 * @return corresponding database column name used in SQL query.
	 */
	String fieldToColumnName(String fieldName);
	
	/**
	 * Converts <b>field name</b> into <b>database column alias</b>.
	 * <p>
	 * When two or more tables are used in <code>SELECT</code> with
	 * the same column name they must be distinguishable. Thus
	 * each column name must be followed by a unique alias which is
	 * referred later in <code>WHERE</code> and <code>ORDER BY</code>
	 * clause and also in the result set.   
	 * </p>
	 * <p>
	 * In <code>ORDER BY</code> expressions cannot be used like
	 * <code>COUNT(points)</code> instead these expressions must be
	 * defined together with aliases in the column list right after
	 * <code>SELECT</code>.
	 * </p>
	 * <p>
	 * An alias must not contain dots neither it can be a SQL expression.
	 * </p>
	 * <p>
	 * E.g.<br/>
	 * <code>description</code> -> <code>DESCRIPTION</code><br/>
	 * <code>mother.firstName</code> -> <code>MOTHER_FIRST_NAME</code><br/>
	 * <code>father.firstName</code> -> <code>FATHER_FIRST_NAME</code><br/>
	 * <code>total</code> -> <code>TOTAL</code><br/>
	 * 
	 * @param fieldName field name of the list.
	 * @return corresponding database column alias used in SQL query.
	 */
	String fieldToColumnAlias(String fieldName);
	
}
