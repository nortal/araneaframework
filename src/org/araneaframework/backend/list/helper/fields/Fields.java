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

import java.util.Collection;

import org.araneaframework.backend.list.helper.ListSqlHelper;
import org.araneaframework.backend.list.helper.naming.DefaultNamingStrategy;

/**
 * List of field names provider for SQL Helper.
 * 
 * @see ListSqlHelper#getFields()
 * @see StandardFields
 * @see DefaultNamingStrategy
 * @see ConcatFields
 * 
 * @author Rein Raudjärv
 */
public interface Fields {

	/**
	 * Return the list of field names.
	 * <p>
	 * All these fields are used in <code>SELECT</code> clause
	 * and read from the result set.
	 * </p>
	 * <p>
	 * E.g. ["name", "age", "address.town"]
	 * 
	 * @return List&lt;String&gt; the list of field names.
	 */
	Collection getNames();
	
}
