/*
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
 */

package org.araneaframework.example.main.web.demo.formlist;

import java.util.LinkedHashMap;
import java.util.Map;
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
 * Separate forms are used for individual rows, so that client-side validation would work on the same separate rows.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class DemoDisplayableEditableList extends TemplateBaseWidget {

  private BeanFormListWidget<Long, DataDTO> formListWidget;

  private Map<Long, DataDTO> editableRows = new LinkedHashMap<Long, DataDTO>();

  // Plays the role of a sequence
  private Long lastId;

  public DemoDisplayableEditableList() {
    // Just making the initial data. In reality it should have been read from the database
    this.editableRows.put(1L, new DataDTO(1L, Boolean.TRUE, 10L, "12313"));
    this.editableRows.put(2L, new DataDTO(2L, Boolean.FALSE, 123L, "werwer"));
    this.editableRows.put(3L, new DataDTO(3L, Boolean.TRUE, 10L, "adfhadfh"));
    this.lastId = new Long(3);
  }

  /**
   * Builds the form with one checkbox, one textbox and a button.
   */
  @Override
  public void init() throws Exception {
    setViewSelector("demo/formlist/displayableEditableList");
    this.formListWidget = new BeanFormListWidget<Long, DataDTO>(new DemoEditableRowHandler(),
        new MapFormListModel<DataDTO>(this.editableRows), DataDTO.class);
    addWidget("editableList", this.formListWidget);
  }

  /**
   * A custom implementation of valid only individual form row handler that deals with DataDTOs.
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   */
  public class DemoEditableRowHandler extends ValidOnlyIndividualFormRowHandler<Long, DataDTO> {

    public Long getRowKey(DataDTO row) {
      return row.getId();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void initFormRow(FormRow<Long, DataDTO> editableRow, DataDTO row) throws Exception {
      BeanFormWidget<DataDTO> rowForm = (BeanFormWidget<DataDTO>) editableRow.getForm();

      addCommonFormFields(rowForm);

      FormListUtil.addEditSaveButtonToRowForm("#", formListWidget, rowForm, getRowKey(row));
      FormListUtil.addDeleteButtonToRowForm("#", formListWidget, rowForm, getRowKey(row));

      rowForm.readFromBean(row);
    }

    @Override
    public void initAddForm(FormWidget addForm) throws Exception {
      addCommonFormFields(addForm);
      FormListUtil.addAddButtonToAddForm("#", formListWidget, addForm);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void saveValidRow(FormRow<Long, DataDTO> editableRow) throws Exception {
      DataDTO rowData = ((BeanFormWidget<DataDTO>) editableRow.getForm()).writeToBean();
      rowData.setId(editableRow.getKey());
      editableRow.setRow(rowData);
      editableRows.put(editableRow.getKey(), rowData);
      editableRow.close();
    }

    @Override
    public void deleteRow(Long key) throws Exception {
      editableRows.remove(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addValidRow(FormWidget addForm) throws Exception {
      DataDTO rowData = ((BeanFormWidget<DataDTO>) addForm).writeToBean();
      rowData.setId(++lastId);
      editableRows.put(lastId, rowData);
    }

    private void addCommonFormFields(FormWidget form) throws Exception {
      form.addElement("stringField", "#String field", new TextControl(), new StringData(), true);
      form.addElement("longField", "#Long field", new NumberControl(), new LongData(), true);
      form.addElement("booleanField", "#Boolean field", new CheckboxControl(), new BooleanData(), true);
    }
  }
}
