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
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.form.formlist.FormListUtil;
import org.araneaframework.uilib.form.formlist.FormRow;
import org.araneaframework.uilib.form.formlist.adapter.ValidOnlyIndividualFormRowHandler;
import org.araneaframework.uilib.form.formlist.model.MapFormListModel;


/**
 * Editable list component.
 *
 * Seperate forms are used for individual rows, so that client-side validation
 * would work on the same separate rows.
 *
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class DemoEmbeddedDisplayableEditableList extends TemplateBaseWidget {
	  private static final long serialVersionUID = 1L;

  public DemoEmbeddedDisplayableEditableList() {		
	}

	private BeanFormListWidget editableRows;
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
		setViewSelector("demo/demoEmbeddedDisplayableEditableList");
		
		editableRows = new BeanFormListWidget(
        new DemoEditableRowHandler(),
        new MapFormListModel(data),
        DataDTO.class);

		addWidget("editableList", editableRows);
	}

	public class DemoEditableRowHandler extends ValidOnlyIndividualFormRowHandler {
		    private static final long serialVersionUID = 1L;

    public Object getRowKey(Object row) {
			return ((DataDTO) row).getId();
		}

		public void saveValidRow(FormRow editableRow) throws Exception {
			DataDTO rowData = (DataDTO) ((BeanFormWidget)editableRow.getForm()).readBean(new DataDTO());
			rowData.setId((Long) editableRow.getKey());
			data.put(editableRow.getKey(), rowData);

			editableRow.close();
		}

		public void deleteRow(Object key) throws Exception {
			data.remove(key);
		}

		public void addValidRow(FormWidget addForm) throws Exception {
			lastId = new Long(lastId.longValue() + 1);

			DataDTO rowData = (DataDTO) ((BeanFormWidget)addForm).readBean(new DataDTO());
			rowData.setId(lastId);

			data.put(lastId, rowData);
		}

		public void initFormRow(FormRow editableRow, Object row) throws Exception {
			editableRow.close();
			
			BeanFormWidget rowForm = (BeanFormWidget)editableRow.getForm();
			
			addCommonFormFields(rowForm);
			
			FormListUtil.addEditSaveButtonToRowForm("#", editableRows, rowForm, getRowKey(row));
			FormListUtil.addDeleteButtonToRowForm("#", editableRows, rowForm, getRowKey(row));
			
			rowForm.writeBean(row);      
      
      rowForm.addWidget("embeddedFormList", new DemoDisplayableEditableList());
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
