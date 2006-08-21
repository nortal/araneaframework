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
 * {@link ListFilter} with one field (column).
 * 
 * @see DualFieldFilter
 */
public interface SingleFieldFilter extends ListFilter {
	/**
	 * Returns the field Id.
	 * 
	 * @return the field Id.
	 */
	String getField();

	/**
	 * Stores the field Id.
	 * 
	 * @param id field Id.
	 */
	void setField(String id);
}
