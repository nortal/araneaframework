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

package org.araneaframework.uilib.list.structure.order;

import org.araneaframework.uilib.list.structure.ListOrder;

/**
 * Static list ordering information about one {@link org.araneaframework.uilib.list.structure.ListColumn}.
 */
public interface ColumnOrder extends ListOrder {
	/**
	 * Returns the {@link org.araneaframework.uilib.list.structure.ListColumn} id.
	 * @return {@link org.araneaframework.uilib.list.structure.ListColumn} id
	 */
	String getColumnId();

	/**
	 * Sets the {@link org.araneaframework.uilib.list.structure.ListColumn} id 
	 * @param id {@link org.araneaframework.uilib.list.structure.ListColumn} id
	 */
	void setColumnId(String id);
}
