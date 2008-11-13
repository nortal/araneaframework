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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public abstract class BackendListDataProvider extends BaseListDataProvider {

  private static final long serialVersionUID = 1L;

  private static final Log log = LogFactory.getLog(BackendListDataProvider.class);

  private Set dataUpdateListeners = new HashSet(1);

  public static final boolean USE_CACHE_BY_DEFAULT = false;

  protected ComparatorExpression orderExpr;

  protected Expression filterExpr;

  protected Long lastStart;

  protected Long lastCount;

  protected ListItemsData lastItemRange;

  private boolean forceReload = false;

  protected boolean useCache = USE_CACHE_BY_DEFAULT;

  /**
   * Instantiates the backend list data provider and sets whether to use
   * caching.
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
    forceReload();
    notifyDataChangeListeners();
  }

  /**
   * Sets the order of the list.
   */
  public void setOrderExpression(ComparatorExpression orderExpr) {
    this.orderExpr = orderExpr;
    forceReload();
    notifyDataChangeListeners();
  }

  /**
   * Empty.
   */
  public void refreshData() throws Exception {
    forceReload();
    notifyDataChangeListeners();
  }

  /** @since 1.1 */
  protected void forceReload() {
    this.forceReload = true;
  }

  /**
   * Uses {@link ListDataProvider#getItemRange(Long, Long)}to retrieve the item.
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

  public ListItemsData getItemRange(Long startIdx, Long count) throws Exception {
    if (!this.useCache || this.forceReload || !startIdx.equals(this.lastStart)
        || (count == null || this.lastCount == null) && count != this.lastCount
        || count != null && this.lastCount != null
        && !count.equals(this.lastCount)) {
      ListQuery query = new ListQuery();
      query.setListStructure(listStructure);
      query.setItemRangeStart(startIdx);
      query.setItemRangeCount(count);
      query.setFilterInfo(this.filterInfo);
      query.setOrderInfo(this.orderInfo);
      query.setFilterExpression(this.filterExpr);
      query.setOrderExpression(this.orderExpr);
      this.lastItemRange = getItemRange(query);
      if (log.isTraceEnabled()) {
        log.trace("Refreshing itemrange: startIdx=" + String.valueOf(startIdx)
            + ", count=" + String.valueOf(count));
      }
    }
    this.forceReload = false;
    this.lastStart = startIdx;
    this.lastCount = count;
    return this.lastItemRange;
  }

  /** @since 1.1 */
  protected void notifyDataChangeListeners() {
    for (Iterator i = dataUpdateListeners.iterator(); i.hasNext();) {
      DataUpdateListener listener = (DataUpdateListener) i.next();
      listener.onDataUpdate();
    }
  }

  public void addDataUpdateListener(DataUpdateListener listener) {
    dataUpdateListeners.add(listener);
  }

  public void removeDataUpdateListener(DataUpdateListener listener) {
    dataUpdateListeners.remove(listener);
  }

  /**
   * This method should be overidden to return a range of items from the list
   * data.
   */
  protected abstract ListItemsData getItemRange(ListQuery query)
      throws Exception;
}
