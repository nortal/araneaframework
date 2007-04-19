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

package org.araneaframework.uilib.list.dataprovider;

import org.apache.log4j.Logger;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.backend.list.model.ListQuery;

/**
 * This class provides a basic list data provider implementation that may be
 * used with SQL- or PL/SQL-based lists.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class BackendListDataProvider implements ListDataProvider {
	private static final Logger log = Logger.getLogger(BackendListDataProvider.class);
	
	public static final boolean USE_CACHE_BY_DEFAULT = false; 

	protected ComparatorExpression orderExpr;
	protected Expression filterExpr;

	protected Long lastStart;
	protected Long lastCount;

	protected ListItemsData lastItemRange;

	protected boolean forceReload = false;
	protected boolean useCache = USE_CACHE_BY_DEFAULT;

	/**
	 * Instantiates the backend list data provider and sets whether to use caching.
	 * 
	 * @param useCache whether to use caching.
	 */
	public BackendListDataProvider(boolean useCache) {
		this.useCache = useCache;
	}
	/**
	 * Instantiates the backend list data provider with cache disabled.
	 */
	public BackendListDataProvider() {
		// empty		
	}

	public void init() throws Exception {
		// for subclasses to implement if needed
	}

	public void destroy() throws Exception {
		// for subclasses to implement if needed
	}

	/**
	 * Sets the filter of the list.
	 */
	public void setFilterExpression(Expression filterExpr) {
		this.filterExpr = filterExpr;

		this.forceReload = true;
	}

	/**
	 * Sets the order of the list.
	 */
	public void setOrderExpression(ComparatorExpression orderExpr) {
		this.orderExpr = orderExpr;

		this.forceReload = true;
	}

	/**
	 * Empty.
	 */
	public void refreshData() throws Exception {
		this.forceReload = true;
	}

	/**
	 * Uses {@link ListDataProvider#getItemRange(Long, Long)}to retrieve the
	 * item.
	 */
	public Object getItem(Long index) throws Exception {
		return getItemRange(index, new Long(1));
	}

	/**
	 * Returns the total item count.
	 */
	public Long getItemCount() throws Exception {
		return getItemRange(new Long(0), new Long(1)).getTotalCount();
	}

	/**
	 * Uses {@link ListDataProvider#getItemRange(Long, Long)}to retrieve all
	 * items.
	 */
	public ListItemsData getAllItems() throws Exception {
		return getItemRange(new Long(0), null);
	}

	public ListItemsData getItemRange(Long startIdx, Long count)
			throws Exception {
		if (!this.useCache || this.forceReload
				|| !startIdx.equals(this.lastStart)
				|| (count == null || this.lastCount == null)
				&& count != this.lastCount || count != null
				&& this.lastCount != null && !count.equals(this.lastCount)) {
			ListQuery query = new ListQuery();
			query.setItemRangeStart(startIdx);
			query.setItemRangeCount(count);
			query.setFilterExpression(this.filterExpr);
			query.setOrderExpression(this.orderExpr);			
			this.lastItemRange = getItemRange(query);
			
			if (log.isDebugEnabled()) {
				log.debug("refreshing itemrange: startIdx=" + startIdx.toString() + ", count="+count.toString());
			}
		}

		this.forceReload = false;
		this.lastStart = startIdx;
		this.lastCount = count;

		return this.lastItemRange;
	}

	/**
	 * This method should be overidden to return a range of items from the list data.
	 * 
	 */
	protected abstract ListItemsData getItemRange(ListQuery query) throws Exception;
}
