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
import org.araneaframework.uilib.list.TypeHelper;
import org.araneaframework.uilib.list.structure.filter.FieldFilter;
import org.araneaframework.uilib.list.structure.filter.composite.AndFilter;
import org.araneaframework.uilib.list.structure.order.FieldOrder;
import org.araneaframework.uilib.list.structure.order.MultiFieldOrder;
import org.araneaframework.uilib.list.structure.order.SimpleColumnOrder;


public class ListStructure extends BaseListStructure {

	private static final long serialVersionUID = 1L;
	
	private final TypeHelper typeHelper;
	
	private boolean orderableByDefault = false;

	public ListStructure(TypeHelper typeHelper) {
		Validate.notNull(typeHelper);
		this.typeHelper = typeHelper;
	}
	
	protected TypeHelper getTypeHelper() {
		return this.typeHelper;
	}
	
	/*
	 * Fields
	 */

	public void addField(String id, String label) {
		addField(id, label, getTypeHelper().getFieldType(id), isOrderableByDefault());
	}

	public void addField(String id, String label, boolean orderable) {
		addField(id, label, getTypeHelper().getFieldType(id), orderable);
	}

	public void addField(String id, String label, Class type) {
		addField(id, label, type, isOrderableByDefault());
	}

	public void addField(String id, String label, Class type, boolean orderable) {
		addField(new ListField(id, label));
		if (type != null) {
			getTypeHelper().addFieldType(id, type);
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
		addOrder(new SimpleColumnOrder(fieldId, comparator));		
	}
	
	protected MultiFieldOrder getMultiFieldOrder() {
		if (this.order == null) {
			clearOrders();
		}
		Validate.isTrue(this.order instanceof MultiFieldOrder, "ListOrder must be a MultiColumnOrder instance");
		return (MultiFieldOrder) this.order; 
	}
	
	public void addOrder(FieldOrder fieldOrder) {
		getMultiFieldOrder().addFieldOrder(fieldOrder);
	}
	
	public FieldOrder getFieldOrder(String field) {
		return getMultiFieldOrder().getFieldOrder(field);
	}
	
	public void clearOrders() {
		this.order = new MultiFieldOrder();
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
	
	public void addFilter(ListFilter filter) {
		getAndFilter().addFilter(filter);
	}
	
	public FieldFilter getFieldFilter(String field) {
		Iterator i = getAndFilter().getFilters().iterator();
		while (i.hasNext()) {
			ListFilter listFilter = (ListFilter) i.next();
			if (listFilter instanceof FieldFilter) {
				FieldFilter columnFilter = (FieldFilter) listFilter;
				if (columnFilter.getFieldId().equals(field)) {
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
