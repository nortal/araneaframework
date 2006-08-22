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
import java.util.Map;
import org.apache.commons.collections.map.LinkedMap;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.util.DataDTO;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.list.formlist.BeanFormListWidget;
import org.araneaframework.uilib.list.formlist.FormListUtil;
import org.araneaframework.uilib.list.formlist.FormRow;
import org.araneaframework.uilib.list.formlist.adapters.MapFormRowHandlerDecorator;
import org.araneaframework.uilib.list.formlist.adapters.ValidOnlyIndividualFormRowHandler;


/**
 * Editable list component.
 *
 * Seperate forms are used for individual rows, so that client-side validation
 * would work on the same separate rows.
 *
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class DemoFormList extends TemplateBaseWidget {
	private BeanFormListWidget formList;
	private Map data = new LinkedMap();

	//Plays the role of a sequence
	private Long lastId;

	{
		//Just making the initial data
		//In reality it should have been read from the database
		data.put(new Long(1), new DataDTO(new Long(1), Boolean.TRUE, new Long(10), "12313"));
		data.put(new Long(2), new DataDTO(new Long(2), Boolean.FALSE, new Long(123), "werwer"));
		data.put(new Long(3), new DataDTO(new Long(3), Boolean.TRUE, new Long(10), "adfhadfh"));

		lastId = new Long(3);
	}

	/**
	 * Builds the form with one checkbox, one textbox and a button.
	 */
	public void init() throws Exception {
		super.init();

		setViewSelector("demo/demoEditableList");
		
		formList = new BeanFormListWidget(new DemoFormRowHandler(), DataDTO.class);

		formList.setFormRowHandler(
				new MapFormRowHandlerDecorator(
					data, 
					formList, 
					formList.getFormRowHandler()));
		formList.setRows(new ArrayList(data.values()));
		
		addWidget("editableList", formList);
	}
	
	public void handleEventReturn(String param) throws Exception {
		getFlowCtx().cancel();
	}

	public class DemoFormRowHandler extends ValidOnlyIndividualFormRowHandler {
		public Object getRowKey(Object row) {
			return ((DataDTO) row).getId();
		}

		public void saveValidRow(FormRow editableRow) throws Exception {
			//Reading data
			DataDTO rowData = (DataDTO) ((BeanFormWidget)editableRow.getRowForm()).readBean(new DataDTO()); 
			rowData.setId((Long) editableRow.getRowKey());

			//Saving data
			data.put(editableRow.getRowKey(), rowData);
		}

		public void deleteRow(Object key) throws Exception {
			//Deleting data
			data.remove(key);
		}

		public void addValidRow(FormWidget addForm) throws Exception {
			lastId = new Long(lastId.longValue() + 1);

			DataDTO rowData = (DataDTO) ((BeanFormWidget)addForm).readBean(new DataDTO());
			rowData.setId(lastId);

			data.put(lastId, rowData);
		}

		public void initFormRow(FormRow editableRow, Object row)
		                     throws Exception {
			BeanFormWidget rowForm = (BeanFormWidget)editableRow.getRowForm();

			addCommonFormFields(rowForm);

			FormListUtil.addSaveButtonToRowForm("#", formList, rowForm, getRowKey(row));
			FormListUtil.addDeleteButtonToRowForm("#", formList, rowForm, getRowKey(row));

			rowForm.writeBean(row);
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
