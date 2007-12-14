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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.araneaframework.Environment;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.list.TypeHelper;
import org.araneaframework.uilib.list.structure.filter.FieldFilter;
import org.araneaframework.uilib.list.structure.filter.composite.AndFilter;
import org.araneaframework.uilib.list.structure.order.FieldOrder;
import org.araneaframework.uilib.list.structure.order.MultiFieldOrder;
import org.araneaframework.uilib.list.structure.order.SimpleFieldOrder;
import org.araneaframework.uilib.util.Event;


public class ListStructure extends BaseListStructure {

	private static final long serialVersionUID = 1L;
	
	private final TypeHelper typeHelper;
	
	private boolean orderableByDefault = false;
	
	private boolean initialized = false;
	private List initEvents = null;
	
	public void init(Environment env) throws Exception {
		if (initEvents != null) {
			for (Iterator it = initEvents.iterator(); it.hasNext();) {
				Runnable event = (Runnable) it.next();
				event.run();
			}
		}
		initialized = true;
		initEvents = null;
		
		this.filter.init(env);
		this.order.init(env);
	}
	
	private boolean isInitialized() {
		return this.initialized;
	}
	
	private void addInitEvent(Event event) {
		if (isInitialized()) {
			event.run();
		} else {
			if (initEvents == null)
				initEvents = new ArrayList();
			initEvents.add(event);
		}		
	}

	public void destroy() throws Exception {
		this.filter.destroy();
		this.order.destroy();
	}
	
	public ListStructure(TypeHelper typeHelper) {
		Assert.notNullParam(this, typeHelper, "typeHelper");
		this.typeHelper = typeHelper;
		this.filter = new AndFilter();
		this.order = new MultiFieldOrder();
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
	
	protected void addFieldOrder(final String fieldId) {
		final SimpleFieldOrder fieldOrder = new SimpleFieldOrder(fieldId); 
		addInitEvent(new Event() {
			public void run() {
				Comparator comp = typeHelper.getFieldComparator(fieldId);
				Validate.notNull(comp, "Could not get comparator for field '" + fieldId + "'");
				fieldOrder.setComparator(comp);
			}
		});
		addOrder(fieldOrder);		
	}
	
	protected void addFieldOrder(String fieldId, Comparator comparator) {
		addOrder(new SimpleFieldOrder(fieldId, comparator));
	}
	
	protected MultiFieldOrder getMultiFieldOrder() {
		return (MultiFieldOrder) this.order; 
	}
	
	public void addOrder(FieldOrder fieldOrder) {
		getMultiFieldOrder().addFieldOrder(fieldOrder);
	}
	
	public FieldOrder getFieldOrder(String field) {
		return getMultiFieldOrder().getFieldOrder(field);
	}
	
	public void clearOrders() {
		getMultiFieldOrder().clearFieldOrders();
	}
	
	/*
	 * Filters
	 */
	
	protected AndFilter getAndFilter() {
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
		getAndFilter().clearFilters();
	}
}
