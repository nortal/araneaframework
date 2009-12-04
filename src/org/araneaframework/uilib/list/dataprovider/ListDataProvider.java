/*
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
 */

package org.araneaframework.uilib.list.dataprovider;

import java.io.Serializable;
import java.util.Map;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.uilib.list.OrderInfo;
import org.araneaframework.uilib.list.structure.ListStructure;



/**
 * This interface defines the behaviour of a list data provider, that is used by the
 * {@link org.araneaframework.uilib.list.ListWidget}to retrieve the list data.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Rein Raudjärv
 */
public interface ListDataProvider<T> extends Serializable {

	/**
	 * This method should initialize the data provider, getting all needed handles and resources.
	 */
	public void init() throws Exception;

	/**
	 * This method should destroy data provider, releasing all taken handles and resources.
	 */
	public void destroy() throws Exception;

	/**
	 * This method should be used to store the structure of the list.  
	 * 
	 * @param listStructure the structure of the list.
	 */
	public void setListStructure(ListStructure listStructure);
	
	/**
	 * This method should be used to receive the current filter info.
	 * <p>
	 * The corresponding {@link Expression} is constructed <b>lazily</b>.
	 * </p>
	 * <p>
	 * The same {@link Map} instance can be modified at the back-end
	 * using the method {@link ListQuery#getFilterInfo()} before the
	 * actual {@link Expression} is constructed.
	 * 
	 * @param filterInfo the filter of the list.
	 * 
	 * @since 1.1
	 */
	public void setFilterInfo(Map<String, Object> filterInfo);
	
	/**
	 * This method should be used to receive the current ordering info.
	 * <p>
	 * The corresponding {@link ComparatorExpression} is constructed <b>lazily</b>.
	 * </p>
	 * <p>
	 * The same {@link OrderInfo} instance can be modified at the back-end
	 * using the method {@link ListQuery#getOrderInfo()} before the
	 * actual {@link ComparatorExpression} is constructed.
	 * 
	 * @param orderInfo the current ordering info.
	 * 
	 * @since 1.1
	 */
	public void setOrderInfo(OrderInfo orderInfo);
	
	/**
	 * This method receives the filter info expression explicitly.
	 * 
	 * @param filterExpression the filter of the list.
	 * 
	 * @see #setFilterInfo(Map)
	 */
	public void setFilterExpression(Expression filterExpression);

	/**
	 * This method receives the ordering info expression explicitly.
	 * 
	 * @param orderExpression the current ordering info.
	 * 
	 * @see #setOrderInfo(OrderInfo)
	 */
	public void setOrderExpression(ComparatorExpression orderExpression);

	/**
	 * This method should synchronize the list data provider data with the storage, if any
	 * synchronization is necessary.
	 */
	public void refreshData() throws Exception;

	/**
	 * This method should return a range of items from the list data.
	 * 
	 * @param start the inclusive 0-based starting index of the item range.
	 * @param count the amount of items to return.
	 * @return a range of items from the list data provider.
	 */
	public ListItemsData<T> getItemRange(Long start, Long count) throws Exception;

	/**
	 * This method should return the total item count in the list data.
	 * 
	 * @return the total item count in the list data.
	 */
	public Long getItemCount() throws Exception;

	/**
	 * This method should return an individual item from the list data.
	 * 
	 * @param index the index of item to be returned.
	 * @return an individual item from the list data.
	 */
	public T getItem(Long index) throws Exception;

	/**
	 * This method should return all items from the list data.
	 * 
	 * @return all items from the list data.
	 */
	public ListItemsData<T> getAllItems() throws Exception;
	
	/** @since 1.1 */
	public void addDataUpdateListener(DataUpdateListener listener);
	/** @since 1.1 */
	public void removeDataUpdateListener(DataUpdateListener listener);

	/** @since 1.1 */
	interface DataUpdateListener extends Serializable {
		public void onDataUpdate();
	}
}
