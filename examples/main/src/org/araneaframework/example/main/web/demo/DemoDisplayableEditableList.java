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
import java.util.LinkedHashMap;
import java.util.Map;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.util.DataDTO;
import org.araneaframework.example.main.business.util.TemplateUiLibUtil;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.list.formlist.FormListUtil;
import org.araneaframework.uilib.list.formlist.FormListWidget;
import org.araneaframework.uilib.list.formlist.FormRow;
import org.araneaframework.uilib.list.formlist.adapters.ValidOnlyIndividualFormRowHandler;


/**
 * Editable list component.
 *
 * Seperate forms are used for individual rows, so that client-side validation
 * would work on the same separate rows.
 *
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class DemoDisplayableEditableList extends TemplateBaseWidget {
	public DemoDisplayableEditableList() {		
	}

	private FormListWidget editableRows;
	private Map data = new LinkedHashMap();

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

		addGlobalEventListener(new ProxyEventListener(this));
    setViewSelector("demo/demoDisplayableEditableList");
		
		editableRows = new FormListWidget(new DemoEditableRowHandler());

		FormListUtil.associateFormListWithMap(editableRows, data);
		editableRows.setRows(new ArrayList(data.values()));

		addWidget("editableList", editableRows);
	}

	public class DemoEditableRowHandler extends ValidOnlyIndividualFormRowHandler {
		public Object getRowKey(Object row) {
			return ((DataDTO) row).getId();
		}

		public void saveValidRow(FormRow editableRow) throws Exception {
			//Reading data
			DataDTO rowData = (DataDTO) TemplateUiLibUtil.readDtoFromForm(new DataDTO(), editableRow.getRowForm());
			rowData.setId((Long) editableRow.getRowKey());

			//Saving data
			data.put(editableRow.getRowKey(), rowData);
      
      editableRow.close();
		}

		public void deleteRow(Object key) throws Exception {
			//Deleting data
			data.remove(key);
		}

		public void addValidRow(FormWidget addForm) throws Exception {
			lastId = new Long(lastId.longValue() + 1);

			DataDTO rowData = (DataDTO) TemplateUiLibUtil.readDtoFromForm(new DataDTO(), addForm);
			rowData.setId(lastId);

			data.put(lastId, rowData);
		}

		public void initFormRow(FormRow editableRow, Object row)
		                     throws Exception {
      editableRow.close();

			FormWidget rowForm = editableRow.getRowForm();

			addCommonFormFields(rowForm);

			FormListUtil.addEditSaveButtonToRowForm("#", editableRows, rowForm, getRowKey(row));
			FormListUtil.addDeleteButtonToRowForm("#", editableRows, rowForm, getRowKey(row));

      TemplateUiLibUtil.writeDtoToForm(row, rowForm);
		}

		public void initAddForm(FormWidget addForm) throws Exception {
			addCommonFormFields(addForm);

			FormListUtil.addAddButtonToAddForm("#", editableRows, addForm);
		}

		private void addCommonFormFields(FormWidget form) throws Exception {
			form.addElement("stringField", "#String field", new TextControl(), new StringData(), true);
			form.addElement("longField", "#Long field", new NumberControl(), new LongData(), true);
			form.addElement("booleanField", "#Boolean field", new CheckboxControl(), new BooleanData(), true);
		}
	}
}
