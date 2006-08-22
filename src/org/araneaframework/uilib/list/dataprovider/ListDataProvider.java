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

import java.io.Serializable;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.model.ListItemsData;



/**
 * This interface defines the behaviour of a list data provider, that is used by the
 * {@link org.araneaframework.contrib.uilib.lists.ListWidget}to retrieve the list data.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public interface ListDataProvider extends Serializable {

	/**
	 * This method should initialize the data provider, getting all needed handles and resources.
	 */
	public void init() throws Exception;

	/**
	 * This method should deinitialize data provider, releasing all taken handles and resources.
	 */
	public void destroy() throws Exception;

	/**
	 * This method should be used to receive the filter of the list.
	 * 
	 * @param listFilter the filter of the list.
	 */
	public void setFilterExpression(Expression filterExpression);

	/**
	 * This method should be used to receive the current ordering info.
	 * 
	 * @param orderInfo the current ordering info.
	 */
	public void setOrderExpression(ComparatorExpression orderExpression);


	/**
	 * This method should synchronize the list data provider data with thge storage, if any
	 * synchronization is nessesary.
	 */
	public void refreshData() throws Exception;

	/**
	 * This method should return a range of items from the list data.
	 * 
	 * @param startIdx the inclusive 0-based starting index of the item range.
	 * @param count the amount of items to return.
	 * @return a range of items from the list data provider.
	 */
	public ListItemsData getItemRange(Long start, Long count) throws Exception;

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
	public Object getItem(Long index) throws Exception;

	/**
	 * This method should return all items from the list data.
	 * 
	 * @return all items from the list data.
	 */
	public ListItemsData getAllItems() throws Exception;
}
