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
import java.util.Map;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.uilib.list.OrderInfo;
import org.araneaframework.uilib.list.structure.ListStructure;


/**
 * Back-end list query data.
 * 
 * @see ListItemsData
 * 
 * @author Rein Raudjärv
 */
public class ListQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	/** @since 1.1 */
	protected ListStructure listStructure;
	
	/** @since 1.1 */
	protected Map filterInfo;
	/** @since 1.1 */
	protected OrderInfo orderInfo;
	
	protected Expression filterExpression;	
	protected ComparatorExpression orderExpression;
	
	protected Long itemRangeStart;	
	protected Long itemRangeCount;
	
	/**
	 * Returns the {@link ListStructure}used to describe the list.
	 * 
	 * @return the {@link ListStructure}used to describe the list.
	 * 
	 * @since 1.1 
	 */	
	public ListStructure getListStructure() {
		return listStructure;
	}
	
	/**
	 * Sets the {@link ListStructure} used to describe the list.
	 * 
	 * @since 1.1 
	 */	
	public void setListStructure(ListStructure listStructure) {
		this.listStructure = listStructure;
	}
	
	/**
	 * Returns the filter info of the list.
	 * 
	 * @see #getFilterExpression()
	 * 
	 * @since 1.1 
	 */	
	public Map getFilterInfo() {
		return filterInfo;
	}
	
	/**
	 * Sets the filter info of the list. 
	 * 
	 * @since 1.1 
	 */	
	public void setFilterInfo(Map filterInfo) {
		this.filterInfo = filterInfo;
	}
	
	/**
	 * Returns the order info the list.
	 * 
	 * @see #getOrderExpression()
	 * 
	 * @since 1.1 
	 */
	public OrderInfo getOrderInfo() {
		return orderInfo;
	}
	
	/**
	 * Sets the order info the list.
	 * 
	 * @since 1.1 
	 */
	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}	
	
	/**
	 * Returns the filter expression (based on the filter info) of the list.
	 * 
	 * @see #getFilterInfo()
	 */
	public Expression getFilterExpression() {
		return this.filterExpression;
	}

	/**
	 * Sets the filter expression of the list. 
	 */	
	public void setFilterExpression(Expression filterExpression) {
		this.filterExpression = filterExpression;
	}
	
	/**
	 * Returns the order expression (based on the order info) of the list.
	 * 
	 * @see #getOrderInfo()
	 */
	public ComparatorExpression getOrderExpression() {
		return this.orderExpression;
	}

	/**
	 * Sets the order expression of the list. 
	 */
	public void setOrderExpression(ComparatorExpression orderExpression) {
		this.orderExpression = orderExpression;
	}
	
	/**
	 * Returns the item range start.
	 * 
	 * @see #getItemRangeCount()
	 */
	public Long getItemRangeStart() {
		return this.itemRangeStart;
	}

	/**
	 * Sets the item range start.
	 */
	public void setItemRangeStart(Long itemRangeStart) {
		this.itemRangeStart = itemRangeStart;
	}
	
	/**
	 * Returns the item range count.
	 * 
	 * @see #getItemRangeStart()
	 */	
	public Long getItemRangeCount() {
		return this.itemRangeCount;
	}

	/**
	 * Sets the item range count.
	 */	
	public void setItemRangeCount(Long itemRangeCount) {
		this.itemRangeCount = itemRangeCount;
	}

}
