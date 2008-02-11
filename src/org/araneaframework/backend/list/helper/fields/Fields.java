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

package org.araneaframework.backend.list.helper.fields;

import java.sql.ResultSet;
import java.util.Collection;

import org.araneaframework.backend.list.helper.ListSqlHelper;
import org.araneaframework.backend.list.helper.naming.MappingNamingStrategyAndFields;

/**
 * List of field names provider for SQL Helper.
 * <p>
 * These names correspond to bean fields (e.g. "name", "age", "address.town"].
 * </p>
 * <p>
 * There are two lists of fields:
 * <ul>
 * <li>fields presented in <code>SELECT</code> - returned by {@link #getNames()}</li>
 * <li>Fields read from the {@link ResultSet} - returned by {@link #getResultSetNames()}</li>
 * </ul>
 * 
 * @see ListSqlHelper#getFields()
 * @see StandardFields
 * @see MappingNamingStrategyAndFields
 * @see ConcatFields
 * 
 * @author Rein Raudjärv
 * 
 * @since 1.1
 */
public interface Fields {

	/**
	 * Return the list of fields used in <code>SELECT</code>.
	 * 
	 * @return List&lt;String&gt; the names of the fields used in SELECT.
	 */
	Collection getNames();
	
	/**
	 * Return the list of fields read from the {@link ResultSet}.
	 * 
	 * @return List&lt;String&gt; the names of the fields read from the {@link ResultSet}.
	 */
	Collection getResultSetNames();	
	
}
