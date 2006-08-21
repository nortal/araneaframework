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

package org.araneaframework.uilib.list.structure.filter;

import org.araneaframework.uilib.list.structure.ListFilter;

/**
 * {@link ListFilter} with one value (filter field).
 */
public interface SingleValueFilter extends ListFilter {
	/**
	 * Returns the value Id.
	 * 
	 * @return the value Id.
	 */
	String getValueId();

	/**
	 * Stores the value Id.
	 * 
	 * @param id value Id.
	 */
	void setValueId(String id);

	/**
	 * Returns the value type.
	 * 
	 * @return the value type.
	 */
	Class getValueType();

	/**
	 * Stores the value type.
	 * 
	 * @param id value type.
	 */
	void setValueType(String id);
}
