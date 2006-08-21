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

package org.araneaframework.uilib.list.contrib.structure;

import java.util.Iterator;

import org.araneaframework.uilib.list.contrib.structure.order.ColumnOrder;
import org.araneaframework.uilib.list.contrib.structure.order.MultiColumnOrder;
import org.araneaframework.uilib.list.structure.ListFilter;
import org.araneaframework.uilib.list.structure.filter.ColumnFilter;
import org.araneaframework.uilib.list.structure.filter.composite.AndFilter;


public class ListStructure extends BaseListStructure {

	private static final long serialVersionUID = 1L;
	
	/*
	 * Columns
	 */

	public void addColumn(String id, String label) {
		addColumn(new ListColumn(id, label));
	}
	
	public void addColumn(ListColumn column, ColumnOrder columnOrder, ColumnFilter columnFilter) {
		String id = column.getId();
		addColumn(column);
		if (columnOrder != null) {
			columnOrder.setColumnId(id);
			addColumnOrder(columnOrder);
		}
		if (columnFilter != null) {
			columnFilter.setColumnId(id);
			addFilter(columnFilter);
		}
	}
	
	public void addColumn(String id, String label, ColumnOrder columnOrder, ColumnFilter columnFilter) {
		addColumn(new ListColumn(id, label), columnOrder, columnFilter);
	}
	
	/*
	 * Orders
	 */
	
	protected MultiColumnOrder getMultiColumnOrder() {
		if (this.order == null) {
			clearColumnOrders();
		}
		if (!MultiColumnOrder.class.isAssignableFrom(this.order.getClass())) {
			throw new RuntimeException("ListOrder must be a MultiColumnOrder instance");
		}
		return (MultiColumnOrder) this.order; 
	}
	
	public void addColumnOrder(ColumnOrder columnOrder) {
		getMultiColumnOrder().addColumnOrder(columnOrder);
	}
	
	public ColumnOrder getColumnOrder(String column) {
		return getMultiColumnOrder().getColumnOrder(column);
	}
	
	public void clearColumnOrders() {
		this.order = new MultiColumnOrder();
	}
	
	/*
	 * Filters
	 */
	
	protected AndFilter getAndFilter() {
		if (this.filter == null) {
			clearFilters();
		}
		if (!AndFilter.class.isAssignableFrom(this.filter.getClass())) {
			throw new RuntimeException("ListFilter must be an AndFilter instance");
		}
		return (AndFilter) this.filter; 
	}
	
	public void addFilter(ListFilter subFilter) {
		getAndFilter().addFilter(subFilter);
	}
	
	public ColumnFilter getColumnFilter(String column) {
		Iterator i = getAndFilter().getFilters().iterator();
		while (i.hasNext()) {
			ListFilter listFilter = (ListFilter) i.next();
			if (ColumnFilter.class.isAssignableFrom(listFilter.getClass())) {
				ColumnFilter columnFilter = (ColumnFilter) listFilter;
				if (columnFilter.getColumnId().equals(column)) {
					return columnFilter;
				}
			}
		}
		return null;
	}
	
	public void clearFilters() {
		this.filter = new AndFilter();
	}
}
