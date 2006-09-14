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

package org.araneaframework.uilib.list.structure;

import java.util.Comparator;
import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.structure.filter.FieldFilter;
import org.araneaframework.uilib.list.structure.filter.composite.AndFilter;
import org.araneaframework.uilib.list.structure.order.FieldOrder;
import org.araneaframework.uilib.list.structure.order.MultiColumnOrder;
import org.araneaframework.uilib.list.structure.order.SimpleColumnOrder;


public class ListStructure extends BaseListStructure {

	private static final long serialVersionUID = 1L;
	
	private final ListWidget list;
	private final TypeHelper typeHelper;
	
	private boolean orderableByDefault = false;

	public ListStructure(ListWidget list) {
		this.list = list;
		this.typeHelper = list.getTypeHelper();
	}
	
	/*
	 * Fields
	 */

	public void addField(String id, String label) {
		addField(id, label, list.getFieldType(id), isOrderableByDefault());
	}

	public void addField(String id, String label, boolean orderable) {
		addField(id, label, list.getFieldType(id), orderable);
	}

	public void addField(String id, String label, Class type) {
		addField(id, label, type, isOrderableByDefault());
	}

	public void addField(String id, String label, Class type, boolean orderable) {
		addField(new ListField(id, label));
		if (type != null) {
			typeHelper.addCustomType(id, type);
		}
		if (orderable) {
			addFieldOrder(id);
		}
	}
	
	/*
	 * Orders
	 */	
	
	public boolean isOrderableByDefault() {
		return orderableByDefault;
	}

	public void setOrderableByDefault(boolean orderableByDefault) {
		this.orderableByDefault = orderableByDefault;
	}
	
	protected void addFieldOrder(String fieldId) {
		Comparator comp = typeHelper.getFieldComparator(fieldId);
		Validate.notNull(comp, "Could not get comparator for field '" + fieldId + "'");
		addFieldOrder(fieldId, comp);		
	}
	
	protected void addFieldOrder(String fieldId, Comparator comparator) {
		addFieldOrder(new SimpleColumnOrder(fieldId, comparator));		
	}
	
	protected MultiColumnOrder getMultiColumnOrder() {
		if (this.order == null) {
			clearColumnOrders();
		}
		if (!MultiColumnOrder.class.isAssignableFrom(this.order.getClass())) {
			throw new RuntimeException("ListOrder must be a MultiColumnOrder instance");
		}
		return (MultiColumnOrder) this.order; 
	}
	
	public void addFieldOrder(FieldOrder fieldOrder) {
		getMultiColumnOrder().addColumnOrder(fieldOrder);
	}
	
	public FieldOrder getColumnOrder(String field) {
		return getMultiColumnOrder().getColumnOrder(field);
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
	
	public FieldFilter getColumnFilter(String column) {
		Iterator i = getAndFilter().getFilters().iterator();
		while (i.hasNext()) {
			ListFilter listFilter = (ListFilter) i.next();
			if (FieldFilter.class.isAssignableFrom(listFilter.getClass())) {
				FieldFilter columnFilter = (FieldFilter) listFilter;
				if (columnFilter.getFieldId().equals(column)) {
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
