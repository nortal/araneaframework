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

import java.util.ArrayList;
import java.util.List;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.util.DataDTO;
import org.araneaframework.uilib.event.OnClickEventListener;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.form.formlist.BeanFormRow;
import org.araneaframework.uilib.form.formlist.FormListUtil;
import org.araneaframework.uilib.form.formlist.InMemoryFormListHelper;
import org.araneaframework.uilib.form.formlist.adapter.ValidOnlyIndividualBeanFormRowHandler;

/**
 * Editable list component. Seperate forms are used for individual rows, so that
 * client-side validation would work on the same separate rows.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class DemoInMemoryEditableList extends TemplateBaseWidget {

  private BeanFormListWidget<Long, DataDTO> formList;

  private List<DataDTO> data = new ArrayList<DataDTO>();

  private InMemoryFormListHelper<Long, DataDTO> inMemoryHelper;

  
  public DemoInMemoryEditableList() {
    // Just making the initial data. In reality it should have been read from the database.
    this.data.add(new DataDTO(1L, true, 10L, "12313"));
    this.data.add(new DataDTO(2L, false, 123L, "werwer"));
    this.data.add(new DataDTO(3L, true, 10L, "adfhadfh"));
  }

  /**
   * Builds the form with one checkbox, one textbox and a button.
   */
  @Override
  public void init() throws Exception {
    setViewSelector("demo/formlist/inMemoryEditableList");
    this.formList = new BeanFormListWidget<Long, DataDTO>(new DemoEditableRowHandler(), DataDTO.class);
    this.inMemoryHelper = new InMemoryFormListHelper<Long, DataDTO>(this.formList, this.data);
    addWidget("editableList", this.formList);
  }

  public void handleEventTest() throws Exception {}

  public void handleEventReturn() throws Exception {
    getFlowCtx().cancel();
  }

  public void handleEventClose(String parameter) throws Exception {
    if (FormListUtil.convertAndValidateRowForms(this.formList.getFormRows())
        && !FormListUtil.isRowFormsStateChanged(this.formList.getFormRows()) || Boolean.parseBoolean(parameter)) {
      getFlowCtx().cancel();
    } else {
      putViewDataOnce("askCloseConfirmation", "true");
    }
  }

  private class FeedBackProvidingListener implements OnClickEventListener {

    public void onClick() throws Exception {
      getMessageCtx().showInfoMessage("Added: " + inMemoryHelper.getAdded().values());
      getMessageCtx().showInfoMessage("Updated: " + inMemoryHelper.getUpdated().values());
      getMessageCtx().showInfoMessage("Deleted: " + inMemoryHelper.getDeleted());
    }
  }

  private class DemoEditableRowHandler extends ValidOnlyIndividualBeanFormRowHandler<Long, DataDTO> {

    public Long getRowKey(DataDTO row) {
      return row.getId();
    }

    @Override
    public void saveValidRow(BeanFormRow<Long, DataDTO> editableRow) throws Exception {
      // Reading data
      DataDTO rowData = editableRow.getForm().writeToBean();

      // Saving data
      inMemoryHelper.update(editableRow.getKey(), rowData);
      editableRow.getForm().markBaseState();
    }

    @Override
    public void deleteRow(Long key) throws Exception {
      // Deleting data
      inMemoryHelper.delete(key);
    }

    @Override
    public void addValidRow(BeanFormWidget<DataDTO> addForm) throws Exception {
      inMemoryHelper.add(addForm.writeToBean());
    }

    @Override
    public void initFormRow(BeanFormRow<Long, DataDTO> editableRow, DataDTO row) throws Exception {
      BeanFormWidget<DataDTO> rowForm = editableRow.getForm();
      addCommonFormFields(rowForm);

      // We override the default event listeners with FeedBackProvidingListener.
      // The IDs of the buttons remain the same: "save" and "delete".
      FeedBackProvidingListener listener = new FeedBackProvidingListener();

      ButtonControl saveButton = FormListUtil.addSaveButtonToRowForm("#", editableRow);
      saveButton.addOnClickEventListener(listener);

      ButtonControl deleteButton = FormListUtil.addDeleteButtonToRowForm("#", editableRow);
      deleteButton.addOnClickEventListener(listener);

      rowForm.readFromBean(row);
      editableRow.getForm().markBaseState();
    }

    @Override
    public void initAddForm(BeanFormWidget<DataDTO> addForm) throws Exception {
      addCommonFormFields(addForm);

      // The ID of the "addButton" is "add" (to reference it in JSP).
      ButtonControl addButton = FormListUtil.addAddButtonToAddForm("#", formList, addForm);

      // Here we also set our custom event listener to override the default one.
      addButton.addOnClickEventListener(new FeedBackProvidingListener());
    }

    private void addCommonFormFields(FormWidget form) throws Exception {
      form.addElement("stringField", "#String field", new TextControl(), new StringData(), true);
      form.addElement("longField", "#Long field", new NumberControl(), new LongData(), true);
      form.addElement("booleanField", "#Boolean field", new CheckboxControl(), new BooleanData(), true);
    }
  }

}
