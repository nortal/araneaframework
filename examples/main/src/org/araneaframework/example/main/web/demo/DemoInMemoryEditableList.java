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

package org.araneaframework.example.main.web.demo;

import java.util.ArrayList;
import java.util.List;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.util.DataDTO;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.form.formlist.FormListUtil;
import org.araneaframework.uilib.form.formlist.FormRow;
import org.araneaframework.uilib.form.formlist.InMemoryFormListHelper;
import org.araneaframework.uilib.form.formlist.adapters.ValidOnlyIndividualFormRowHandler;


/**
 * Editable list component.
 *
 * Seperate forms are used for individual rows, so that client-side validation
 * would work on the same separate rows.
 *
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class DemoInMemoryEditableList extends TemplateBaseWidget {

	private BeanFormListWidget formList;
	private List data = new ArrayList();
	
	private InMemoryFormListHelper inMemoryHelper;

	{
		//Just making the initial data
		//In reality it should have been read from the database
		data.add(new DataDTO(new Long(1), Boolean.TRUE, new Long(10), "12313"));
		data.add(new DataDTO(new Long(2), Boolean.FALSE, new Long(123), "werwer"));
		data.add(new DataDTO(new Long(3), Boolean.TRUE, new Long(10), "adfhadfh"));
	}

	/**
	 * Builds the form with one checkbox, one textbox and a button.
	 */
	public void init() throws Exception {
		super.init();
		
		setViewSelector("demo/demoInMemoryEditableList");		
		
		formList = new BeanFormListWidget(new DemoEditableRowHandler(), DataDTO.class);
		inMemoryHelper = new InMemoryFormListHelper(formList, data);
		
		addWidget("editableList", formList);
	}
	
	public void handleEventTest(String parameter) throws Exception {
	}
	
	public void handleEventReturn(String param) throws Exception {
		getFlowCtx().cancel();
	}
	
	public void handleEventClose(String parameter) throws Exception {
		if (FormListUtil.convertAndValidateRowForms(formList.getFormRows())
				&& !FormListUtil.isRowFormsStateChanged(formList.getFormRows())
				|| "true".equals(parameter))		
			getFlowCtx().cancel();
		else
			putViewDataOnce("askCloseConfirmation", "true");
	}
	
  protected void handleProcess() throws Exception {
	  getMessageCtx().showMessage(MessageContext.INFO_TYPE, "Added: " + inMemoryHelper.getAdded());
	  getMessageCtx().showMessage(MessageContext.INFO_TYPE, "Updated: " + inMemoryHelper.getUpdated());
	  getMessageCtx().showMessage(MessageContext.INFO_TYPE, "Deleted: " + inMemoryHelper.getDeleted());
  }
  
	public class DemoEditableRowHandler extends ValidOnlyIndividualFormRowHandler {
		public Object getRowKey(Object row) {
			return ((DataDTO) row).getId();
		}

		public void saveValidRow(FormRow editableRow) throws Exception {
			//Reading data
			DataDTO rowData = (DataDTO) ((BeanFormWidget)editableRow.getForm()).readBean(editableRow.getRow()); 

			//Saving data
			inMemoryHelper.update(editableRow.getKey(), rowData);
			editableRow.getForm().markBaseState();
		}

		public void deleteRow(Object key) throws Exception {
			//Deleting data
			inMemoryHelper.delete(key);
		}

		public void addValidRow(FormWidget addForm) throws Exception {
			DataDTO rowData = (DataDTO) ((BeanFormWidget)addForm).readBean(new DataDTO()); 

			inMemoryHelper.add(rowData);
		}

		public void initFormRow(FormRow editableRow, Object row)
		                     throws Exception {
			BeanFormWidget rowForm = (BeanFormWidget)editableRow.getForm();

			addCommonFormFields(rowForm);

			FormListUtil.addSaveButtonToRowForm("#", formList, rowForm, editableRow.getKey());
			FormListUtil.addDeleteButtonToRowForm("#", formList, rowForm, editableRow.getKey());

			rowForm.writeBean(row);
			editableRow.getForm().markBaseState();
		}

		public void initAddForm(FormWidget addForm) throws Exception {
			addCommonFormFields(addForm);

			FormListUtil.addAddButtonToAddForm("#", formList, addForm);
		}

		private void addCommonFormFields(FormWidget form) throws Exception {
			form.addElement("stringField", "#String field", new TextControl(), new StringData(), true);
			form.addElement("longField", "#Long field", new NumberControl(), new LongData(), true);
			form.addElement("booleanField", "#Boolean field", new CheckboxControl(), new BooleanData(), true);
		}
	}
}
