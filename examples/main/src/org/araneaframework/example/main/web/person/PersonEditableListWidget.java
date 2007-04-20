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
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.data.IContractDAO;
import org.araneaframework.example.main.business.data.PersonListDAO;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.form.formlist.FormListUtil;
import org.araneaframework.uilib.form.formlist.FormRow;
import org.araneaframework.uilib.form.formlist.FormRowHandler;
import org.araneaframework.uilib.form.formlist.adapter.ValidOnlyIndividualFormRowHandler;
import org.araneaframework.uilib.list.EditableBeanListWidget;
import org.araneaframework.uilib.list.dataprovider.BackendListDataProvider;
import org.araneaframework.uilib.list.dataprovider.ListDataProvider;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;


public abstract class PersonEditableListWidget extends TemplateBaseWidget {
	protected static final Logger log = Logger.getLogger(PersonEditableListWidget.class);
	private  IContractDAO contractDAO; 
	/* Editable list. */ 
	private EditableBeanListWidget list;
	/* Actual holder of editable list rows (resides inside EditableBeanListWidget).
       Look inside init() method to see where it comes from. */ 
	private BeanFormListWidget formList;
	
	protected void init() throws Exception {
		setViewSelector("person/editableList");
		
		/* PersonMO class is already familiar from form examples. 
       FormRowHandler class that will handle the different row operations. */
		list = new EditableBeanListWidget(buildFormRowHandler(), PersonMO.class);
		this.formList = list.getFormList();
		addWidget("list", list);
		list.setOrderableByDefault(true);
		list.addField("id", "#Id", false);
		/* Filtering by fields other than ID is enabled. */
		list.addField("name", "#First name").like();
		list.addField("surname", "#Last name").like();
		list.addField("phone", "#Phone no").like();		
		list.addField("birthdate", "#Birthdate").range();
		list.addField("salary", "#Salary").range();
		list.addField("dummy", null, false);
		
		/* Set the provider through which list acquires its data. Exactly the same as for ordinary lists. */
		list.setDataProvider(buildListDataProvider());
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
	        /* Implementation of FormRowHandler that also calls dataprovider's
	         * data refresh methods when list editing events occur. */
			return new PersonEditableRowHandler();
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
	
	/* Row handling functions. As this handler extends ValidOnlyIndividualFormRowHandler class,
	 * its saveRow method does nothing: instead saveValidRow method should be implemented that
	 * saves only these forms (rows) which data passes validation.  
	 */ 
	public class PersonEditableRowHandler extends ValidOnlyIndividualFormRowHandler {
		    private static final long serialVersionUID = 1L;

    /* Implementation of the method that must return unique key for each row
		 * in editable list. As we hold database objects (PersonMO-s) in this list, 
		 * it is natural to use synthetic ID field for a key.*/ 
		public Object getRowKey(Object rowData) {
			return ((PersonMO) rowData).getId();
		}
		
		// Implementation of method that should save EDITED rows which data passes validation.
		public void saveValidRow(FormRow editableRow) throws Exception {
			/* Reads data from form. FormRow.getForm() method returns the widget that is 
			 * currently holding row object data -- it is either FormWidget or BeanFormWidget, as
			 * in our case we are using EditableBeanListWidget that holds row data in BeanFormWidgets,
			 * we can cast the return type accordingly. */
			PersonMO rowData = (PersonMO) ((BeanFormWidget)editableRow.getForm()).readBean(new PersonMO());
			rowData.setId((Long) editableRow.getKey());
			
			// Save modified object.
			getGeneralDAO().edit(rowData);
			
			// Set the row closed (for further editing, it must be opened again). 
			editableRow.close();
		}
		
		public void deleteRow(Object key) throws Exception {
			Long id = (Long) key;
			contractDAO.removeByPersonId(id);
			getGeneralDAO().remove(PersonMO.class, id);
			list.getDataProvider().refreshData();
		}
		
		// Implementation of method that should save ADDED rows which data passes validation.
		public void addValidRow(FormWidget addForm) throws Exception {
			PersonMO rowData = (PersonMO) (((BeanFormWidget)addForm).readBean(new PersonMO()));
			getGeneralDAO().add(rowData);
			list.getDataProvider().refreshData();
			// this callback must be made here!
			formList.resetAddForm();
		}
		
		// Called to initialize each row in editable list.
		public void initFormRow(FormRow editableRow, Object rowData) throws Exception {
			// Set initial status of list rows to closed - they cannot be edited before opened.
			editableRow.close();
			
			// Get the rowForm (this is the formwidget holding row object data). 
			BeanFormWidget rowForm = (BeanFormWidget)editableRow.getForm();
			// See below.
			addCommonFormFields(rowForm);
			/* A button that opens row for editing upon receiving onClick event.
			 * Activating button in already opened row saves the row data. */
			FormListUtil.addEditSaveButtonToRowForm("#", formList, rowForm, getRowKey(rowData));
			/* A button that deletes this row and its data (calls deleteRow()). */
			FormListUtil.addDeleteButtonToRowForm("#", formList, rowForm, getRowKey(rowData));

			rowForm.writeBean(rowData);
		}
		
		// Called to initialize a blank row meant for adding new records.
		public void initAddForm(FormWidget addForm) throws Exception {
			addCommonFormFields((BeanFormWidget)addForm);
			// Button that saves the content of the new record (calls addValidRow()). 
			FormListUtil.addAddButtonToAddForm("#", formList, addForm);
		}
		
		// Adds PersonMO bean fields to given BeanFormWidget.
		private void addCommonFormFields(BeanFormWidget form) throws Exception {
			form.addBeanElement("name", "#First name", new TextControl(), true);
			form.addBeanElement("surname", "#Last name", new TextControl(),  true);
			form.addBeanElement("phone", "#Phone no", new TextControl(), false);
			form.addBeanElement("birthdate", "#Birthdate", new DateControl(), false);
			form.addBeanElement("salary", "#Salary", new FloatControl(), false);
		}
	}
	
	  public void injectContractDAO(IContractDAO contractDAO) {
		    this.contractDAO = contractDAO;
		  }
}
