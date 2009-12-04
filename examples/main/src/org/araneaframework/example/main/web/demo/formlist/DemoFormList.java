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
import org.araneaframework.uilib.form.formlist.BeanFormRow;
import org.araneaframework.uilib.form.formlist.FormListUtil;
import org.araneaframework.uilib.form.formlist.adapter.ValidOnlyIndividualBeanFormRowHandler;
import org.araneaframework.uilib.form.formlist.model.MapFormListModel;

/**
 * Editable list component.
 * 
 * Seperate forms are used for individual rows, so that client-side validation would work on the same separate rows.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class DemoFormList extends TemplateBaseWidget {

  private BeanFormListWidget<Long, DataDTO> formList;

  private Map<Long, DataDTO> data = new LinkedHashMap<Long, DataDTO>();

  // Plays the role of a sequence
  private Long lastId;

  
  public DemoFormList() {
    // Just making the initial data. In reality it should have been read from the database
    this.data.put(1L, new DataDTO(1L, Boolean.TRUE, 10L, "12313"));
    this.data.put(2L, new DataDTO(2L, Boolean.FALSE, 123L, "werwer"));
    this.data.put(3L, new DataDTO(3L, Boolean.TRUE, 10L, "adfhadfh"));
    this.lastId = 3L;
  }

  /**
   * Builds the form with one checkbox, one textbox and a button.
   */
  @Override
  public void init() throws Exception {
    setViewSelector("demo/formlist/editableList");
    this.formList = new BeanFormListWidget<Long, DataDTO>(new DemoFormRowHandler(),
        new MapFormListModel<DataDTO>(this.data), DataDTO.class);
    addWidget("editableList", this.formList);
  }

  public void handleEventReturn() throws Exception {
    getFlowCtx().cancel();
  }

  public class DemoFormRowHandler extends ValidOnlyIndividualBeanFormRowHandler<Long, DataDTO> {

    public Long getRowKey(DataDTO row) {
      return row.getId();
    }

    @Override
    public void saveValidRow(BeanFormRow<Long, DataDTO> editableRow) throws Exception {
      // Reading data
      DataDTO rowData = editableRow.getForm().writeToBean();
      rowData.setId(editableRow.getKey());

      // Saving data
      data.put(editableRow.getKey(), rowData);
    }

    @Override
    public void deleteRow(Long key) throws Exception {
      // Deleting data
      data.remove(key);
    }

    @Override
    public void addValidRow(BeanFormWidget<DataDTO> addForm) throws Exception {
      lastId++;
      DataDTO rowData = addForm.writeToBean();
      rowData.setId(lastId);
      data.put(lastId, rowData);
    }

    @Override
    public void initFormRow(BeanFormRow<Long, DataDTO> editableRow, DataDTO row) throws Exception {
      BeanFormWidget<DataDTO> rowForm = editableRow.getForm();
      addCommonFormFields(rowForm);

      FormListUtil.addSaveButtonToRowForm("#", formList, rowForm, getRowKey(row));
      FormListUtil.addDeleteButtonToRowForm("#", formList, rowForm, getRowKey(row));

      rowForm.readFromBean(row);
    }

    @Override
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
