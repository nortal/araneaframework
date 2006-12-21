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

package org.araneaframework.example.main.web.popups;

import java.util.List;
import org.apache.log4j.Logger;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.data.IContractDAO;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.example.main.message.PopupMessageFactory;
import org.araneaframework.example.main.web.sample.NameWidget;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.http.support.PopupWindowProperties;
import org.araneaframework.uilib.core.PopupFlowWidget;
import org.araneaframework.uilib.event.OnClickEventListener;
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
import org.araneaframework.uilib.list.dataprovider.ListDataProvider;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;

public abstract class PersonEditableListPopupWidget extends TemplateBaseWidget {
	protected static final Logger log = Logger.getLogger(PersonEditableListPopupWidget.class);
	private  IContractDAO contractDAO; 
	
	private boolean usePopupFlow = true;
	
	/* Editable list. */ 
	private EditableBeanListWidget list;
	/* Actual holder of editable list rows (resides inside EditableBeanListWidget).
       Look inside init() method to see where it comes from. */ 
	private BeanFormListWidget formList;
	
	protected void init() throws Exception {
		setViewSelector("person/popupeditableList");
		
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

		/* Get the convenient reference to BeanFormListWidget hiding inside EditableBeanListWidget. */
		
	}
	
	protected void process() throws Exception {
		super.process();
	}

	protected abstract ListDataProvider buildListDataProvider() throws Exception;
	
	protected abstract FormRowHandler buildFormRowHandler() throws Exception;
	
	public static class Memory extends PersonEditableListPopupWidget {
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
		      private List data;
			      
			protected DataProvider() {
				super(PersonMO.class);
			}
			public List loadData() throws Exception {
				if (data == null)
					data = getGeneralDAO().getAll(PersonMO.class);
				return data;
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
		
		public void saveValidRow(FormRow editableRow) throws Exception {

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
			// Get the rowForm (this is the formwidget holding row object data). 
			BeanFormWidget rowForm = (BeanFormWidget)editableRow.getForm();
			// See below.
			addCommonFormFields(rowForm);
			
			FormListUtil.addButtonToRowForm("#", rowForm, new PopupListenerFactory().createListener(rowData) , "popupButton");
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
	
	private class PopupListenerFactory {
		public OnClickEventListener createListener(Object data) {
			if (usePopupFlow)
				return new PopupFlowListener((PersonMO)data);
			return new PopupClientListener((PersonMO)data);
		}
	}
	
	private class PopupFlowListener implements OnClickEventListener {
		private PersonMO person;
		public PopupFlowListener(PersonMO eventParam) {
			this.person = eventParam;
		}
		
		public void onClick() throws Exception {
		      final PersonMO rowObject = person; 
		      
		      FormRow formRow = (FormRow) list.getFormList().getFormRows().get(list.getFormList().getFormRowHandler().getRowKey(rowObject)); 
		      final BeanFormWidget rowForm = (BeanFormWidget) formRow.getForm(); 
		      PopupFlowWidget pfw = new PopupFlowWidget(new NameWidget(), new PopupWindowProperties(), new PopupMessageFactory());
		      getFlowCtx().start(pfw, null, new MyHandler(rowForm, rowObject)); 
		}
	}
	
	private class PopupClientListener implements OnClickEventListener {
		private PersonMO person;
		
		
		public PopupClientListener(PersonMO eventParam) {
			this.person = eventParam;
		}
		
		public void onClick() throws Exception {
		      // get the id of updatable formelement, tricky
		      String widgetId = getInputData().getScope().toString();
		      widgetId = widgetId.substring(0, widgetId.lastIndexOf('.'));
		      widgetId = widgetId + ".name";
		      
		      ClientSideFlowContainerWidget toStart = new ClientSideFlowContainerWidget(new NameWidget());
		      toStart.setFinishService(new ApplyReturnValueService(widgetId));

		      getPopupCtx().open(new PopupMessageFactory().buildMessage(toStart), new PopupWindowProperties(), null);
		}
	}
	
	  private class MyHandler implements FlowContext.Handler { 
		    private BeanFormWidget form; 
		    private PersonMO rowObject; 
		    
		    public MyHandler(BeanFormWidget form, PersonMO rowObject) { 
		      this.form = form; 
		      this.rowObject = rowObject; 
		    }

		    public void onCancel() throws Exception {
			}

			public void onFinish(Object returnValue) { 
		      rowObject.setName(returnValue.toString());
		      form.writeBean(rowObject);
		      
		    } 
		  }

	
	  public void injectContractDAO(IContractDAO contractDAO) {
		    this.contractDAO = contractDAO;
		  }
	  
	  public void setUsePopupFlow(boolean b) {
		  this.usePopupFlow = b;
	  }
	  
	  public String getTitle() {
		  if (usePopupFlow)
			  return "Server-side return";
		  return "Client-side return";
	  }
}
