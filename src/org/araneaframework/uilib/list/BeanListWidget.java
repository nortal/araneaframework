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

package org.araneaframework.uilib.list;

import org.araneaframework.backend.util.BeanMapper;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.list.structure.ComparableType;
import org.araneaframework.uilib.list.structure.filter.ColumnFilter;
import org.araneaframework.uilib.list.structure.order.ColumnOrder;
import org.araneaframework.uilib.list.structure.order.SimpleColumnOrder;


public class BeanListWidget extends ListWidget {
	
	private static final long serialVersionUID = 1L;
	
	private Class beanClass;
	
	public BeanListWidget(Class beanClass) {
		super();
		this.beanClass = beanClass;
		this.filterForm = new BeanFormWidget(beanClass);
	}	
	
	private void validateFilterForm() {
		if (this.filterForm == null) {
			throw new AraneaRuntimeException("FilterForm must be set first");
		}
		if (!BeanFormWidget.class.isAssignableFrom(this.filterForm.getClass())) {
			throw new AraneaRuntimeException("FilterForm must be BeanFilterForm");
		}		
	}
	
	private BeanFormWidget getBeanForm() {
		validateFilterForm();
		return (BeanFormWidget) this.filterForm;
	}
	
	private void propagateValueType(Object obj, String column) {
		if (obj == null) {
			return;
		}
		if (ComparableType.class.isAssignableFrom(obj.getClass())) {
			((ComparableType) obj).setValueType(getColumnType(column));
		}
	}
	
	private Class getColumnType(String columnId) {
		return getBeanFieldType(this.beanClass, columnId);
	}
	
	public void addBeanColumn(String id, String label, ColumnOrder order, ColumnFilter filter, Control control) {
		if (filter != null) {
			validateFilterForm();
			propagateValueType(filter, id);
		}
		if (control != null) {
			addBeanFilterFormElementInternal(id, label, control);						
		}
		super.addListColumn(id, label, order, filter);
	}
	
	public void addBeanColumn(String id, String label, boolean isOrdered, ColumnFilter filter, Control control) {
		ColumnOrder order = null;
		if (isOrdered) {
			order = new SimpleColumnOrder();
			propagateValueType(order, id);
		}
		addBeanColumn(id, label, order, filter, control);
	}
	
	public void addBeanColumn(String id, String label, ColumnOrder order) {
		addBeanColumn(id, label, order, null, null);
	}
	
	public void addBeanColumn(String id, String label, boolean isOrdered) {
		addBeanColumn(id, label, isOrdered, null, null);
	}
	
	public void addBeanFilterFormElement(String id, String label, Control control) {
		validateFilterForm();
		addBeanFilterFormElementInternal(id, label, control);
	}
	
	private void addBeanFilterFormElementInternal(String id, String label, Control control) {		
		try {
			addBeanElement(getBeanForm(), id, label, control, false);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	
	
	public void addBeanFilterFormElement(String id, Control control) {
		addBeanFilterFormElement(id, getColumnLabel(id), control);
	}
	
	/*
	 * Helper methods
	 */
	
	private static void addBeanElement(BeanFormWidget form, String fullId, String label, Control control, boolean mandatory) throws Exception {
		if (fullId.indexOf(".") != -1) {
			String subFormId = fullId.substring(0, fullId.indexOf("."));
			String nextFullId =  fullId.substring(subFormId.length() + 1);
			
			BeanFormWidget subForm = null;
			
			if (form.getElement(subFormId) != null) {
				subForm = (BeanFormWidget) form.getElement(subFormId);        	
			} else {
				subForm = form.addBeanSubForm(subFormId);        	
			}
			
			addBeanElement(subForm, nextFullId, label, control, mandatory);
			return;
		}
		
		form.addBeanElement(fullId, label, control, mandatory);
	}
	
	private static Class getBeanFieldType(Class beanClass, String fullId) {
		BeanMapper beanMapper = new BeanMapper(beanClass);
		
		String fieldId, nextFullId;
		
		if (fullId.indexOf(".") != -1) {
			fieldId = fullId.substring(0, fullId.indexOf("."));
			nextFullId = fullId.substring(fieldId.length() + 1);
		} else {
			fieldId = fullId;
			nextFullId = null;
		}
		
		if (!beanMapper.fieldExists(fieldId)) {
			throw new AraneaRuntimeException("Could not infer type for bean field '" + fullId + "'!");			
		}
		
		if (nextFullId != null) {
			return getBeanFieldType(beanMapper.getBeanFieldType(fieldId), nextFullId);	
		}
		return beanMapper.getBeanFieldType(fullId);
	}
}
