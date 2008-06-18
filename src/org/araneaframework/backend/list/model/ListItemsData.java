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

package org.araneaframework.backend.list.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Back-end list query results.
 *
 * @see ListQuery
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class ListItemsData<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/** 
	 * @since 1.1
	 * @deprecated use {@link #emptyListItemsData()} 
	 * 
	 */
	public static final ListItemsData EMPTY =
		new ListItemsData(Collections.EMPTY_LIST, new Long(0L));

	public static <T> ListItemsData<T> emptyListItemsData(){
	  List<T> emptyList = Collections.emptyList();
    return new ListItemsData<T>(emptyList, new Long(0L));
	}
	
	private List<T> itemRange;

	private Long totalCount;
	
	/** @since 1.0.11 */
	public ListItemsData() {}
	
	/** @since 1.0.11 */
	public ListItemsData(List<T> itemRange, Long totalCount) {
		this.itemRange = itemRange;
		this.totalCount = totalCount;
	}

	/**
	 * @return Returns the itemRange.
	 */
	public List<T> getItemRange() {
		return this.itemRange;
	}

	/**
	 * @param itemRange
	 *            The itemRange to set.
	 */
	public void setItemRange(List<T> itemRange) {
		this.itemRange = itemRange;
	}

	/**
	 * @return Returns the totalCount.
	 */
	public Long getTotalCount() {
		return this.totalCount;
	}

	/**
	 * @param totalCount
	 *            The totalCount to set.
	 */
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
}
