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

package org.araneaframework.example.main.web.person;

import java.util.List;
import org.apache.log4j.Logger;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.data.PersonListDAO;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.example.main.business.util.TemplateUiLibUtil;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.form.data.TimestampData;
import org.araneaframework.uilib.list.EditableBeanListWidget;
import org.araneaframework.uilib.list.dataprovider.BackendListDataProvider;
import org.araneaframework.uilib.list.dataprovider.ListDataProvider;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.araneaframework.uilib.list.formlist.FormListUtil;
import org.araneaframework.uilib.list.formlist.FormListWidget;
import org.araneaframework.uilib.list.formlist.FormRow;
import org.araneaframework.uilib.list.formlist.FormRowHandler;
import org.araneaframework.uilib.list.formlist.adapters.MemoryBasedListFormRowHandlerDecorator;
import org.araneaframework.uilib.list.formlist.adapters.ValidOnlyIndividualFormRowHandler;
import org.araneaframework.uilib.list.structure.ListColumn;
import org.araneaframework.uilib.list.structure.filter.column.RangeColumnFilter;
import org.araneaframework.uilib.list.structure.filter.column.SimpleColumnFilter;


public abstract class PersonEditableListWidget extends TemplateBaseWidget {
	
	protected static final Logger log = Logger.getLogger(PersonEditableListWidget.class);
	
	private EditableBeanListWidget list;	
	private FormListWidget formList;
	
	public void init() throws Exception {
		super.init();
		
		addGlobalEventListener(new ProxyEventListener(this));
		setViewSelector("person/editableList");
		
		list = new EditableBeanListWidget(PersonMO.class);
		list.addBeanColumn("id", "#Id", false);
		list.addBeanColumn("name", "#First name", true, new SimpleColumnFilter.Like(), new TextControl());
		list.addBeanColumn("surname", "#Last name", true, new SimpleColumnFilter.Like(), new TextControl());
		list.addBeanColumn("phone", "#Phone no", true, new SimpleColumnFilter.Like(), new TextControl());
		
		RangeColumnFilter rangeFilter = new RangeColumnFilter.DateNonStrict();
		list.addBeanColumn("birthdate", "#Birthdate", true, rangeFilter, null);
		list.addFilterFormElement(rangeFilter.getStartFilterInfoKey(), "#Birthdate Start", new DateControl(), new DateData());
		list.addFilterFormElement(rangeFilter.getEndFilterInfoKey(), "#Birthdate End", new DateControl(), new DateData());
		
		list.addListColumn(new ListColumn("dummy"));
		
		list.setListDataProvider(buildListDataProvider());
		list.setFormRowHandler(buildFormRowHandler());

		this.formList = list.getFormList();		
		addWidget("list", list);
	}
	
	protected abstract ListDataProvider buildListDataProvider() throws Exception;
	
	protected abstract FormRowHandler buildFormRowHandler() throws Exception;
	
	public static class Memory extends PersonEditableListWidget {
		private static final long serialVersionUID = 1L;
		
		private MemoryBasedListDataProvider dataProvider = new DataProvider();

		protected ListDataProvider buildListDataProvider() throws Exception {
			return dataProvider;
		}

		protected FormRowHandler buildFormRowHandler() throws Exception {
			return new MemoryBasedListFormRowHandlerDecorator(dataProvider,
					new PersonEditableRowHandler());
		}
		
		private class DataProvider extends MemoryBasedListDataProvider {
			private static final long serialVersionUID = 1L;
			
			protected DataProvider() {
				super(PersonMO.class);
			}
			public List loadData() throws Exception {		
				return getGeneralDAO().getAll(PersonMO.class);
			}
		}
	}
	
	public static class Backend extends PersonEditableListWidget {
		private static final long serialVersionUID = 1L;
		
		protected ListDataProvider buildListDataProvider() throws Exception {
			return new DataProvider();
		}

		protected FormRowHandler buildFormRowHandler() throws Exception {
			return new PersonEditableRowHandler();
		}
		
		private class DataProvider extends BackendListDataProvider {
			private static final long serialVersionUID = 1L;
			
			protected DataProvider() {
				super(false);
			}
			protected ListItemsData getItemRange(ListQuery query) throws Exception {
				return ((PersonListDAO) getBeanFactory().getBean("personListDAO")).getItems(query);
			}
		}
	}	
	
	public class PersonEditableRowHandler extends ValidOnlyIndividualFormRowHandler {
		private static final long serialVersionUID = 1L;
		
		public Object getRowKey(Object row) {
			return ((PersonMO) row).getId();
		}
		
		public void saveValidRow(FormRow editableRow) throws Exception {
			//Reading data
			PersonMO rowData = (PersonMO) TemplateUiLibUtil.readDtoFromForm(new PersonMO(), editableRow.getRowForm());
			rowData.setId((Long) editableRow.getRowKey());
			
			//Saving data
			getGeneralDAO().edit(rowData);
			
			editableRow.close();
		}
		
		public void deleteRow(Object key) throws Exception {
			//Deleting data
			Long id = (Long) key;
			getGeneralDAO().remove(PersonMO.class, id);
		}
		
		public void addValidRow(FormWidget addForm) throws Exception {
			PersonMO rowData = (PersonMO) TemplateUiLibUtil.readDtoFromForm(new PersonMO(), addForm);
			getGeneralDAO().add(rowData);
			formList.resetAddForm();
		}
		
		public void initFormRow(FormRow editableRow, Object row) throws Exception {
			editableRow.close();
			
			FormWidget rowForm = editableRow.getRowForm();
			
			addCommonFormFields(rowForm);
			
			FormListUtil.addEditSaveButtonToRowForm("#", formList, rowForm, getRowKey(row));
			FormListUtil.addDeleteButtonToRowForm("#", formList, rowForm, getRowKey(row));
			
			TemplateUiLibUtil.writeDtoToForm(row, rowForm);
		}
		
		public void initAddForm(FormWidget addForm) throws Exception {
			addCommonFormFields(addForm);
			
			FormListUtil.addAddButtonToAddForm("#", formList, addForm);
		}
		
		private void addCommonFormFields(FormWidget form) throws Exception {
			form.addElement("name", "#First name", new TextControl(), new StringData(), true);
			form.addElement("surname", "#Last name", new TextControl(), new StringData(), true);
			form.addElement("phone", "#Phone no", new TextControl(), new StringData(), false);
			form.addElement("birthdate", "#Birthdate", new DateControl(), new TimestampData(), false);
		}
	}
}
