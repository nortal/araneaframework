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
import org.araneaframework.uilib.form.formlist.FormListUtil;
import org.araneaframework.uilib.form.formlist.FormRow;
import org.araneaframework.uilib.form.formlist.InMemoryFormListHelper;
import org.araneaframework.uilib.form.formlist.adapter.ValidOnlyIndividualFormRowHandler;

/**
 * Editable list component. Seperate forms are used for individual rows, so that
 * client-side validation would work on the same separate rows.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class DemoInMemoryEditableList extends TemplateBaseWidget {

  private static final long serialVersionUID = 1L;

  private BeanFormListWidget formList;

  private List data = new ArrayList();

  private InMemoryFormListHelper inMemoryHelper;

  {
    // Just making the initial data.
    // In reality it should have been read from the database.
    data.add(new DataDTO(new Long(1), Boolean.TRUE, new Long(10), "12313"));
    data.add(new DataDTO(new Long(2), Boolean.FALSE, new Long(123), "werwer"));
    data.add(new DataDTO(new Long(3), Boolean.TRUE, new Long(10), "adfhadfh"));
  }

  /**
   * Builds the form with one checkbox, one textbox and a button.
   */
  public void init() throws Exception {
    setViewSelector("demo/demoInMemoryEditableList");
    formList = new BeanFormListWidget(new DemoEditableRowHandler(), DataDTO.class);
    inMemoryHelper = new InMemoryFormListHelper(formList, data);
    addWidget("editableList", formList);
  }

  public void handleEventTest() throws Exception {}

  public void handleEventReturn() throws Exception {
    getFlowCtx().cancel();
  }

  public void handleEventClose(String parameter) throws Exception {
    if (FormListUtil.convertAndValidateRowForms(formList.getFormRows())
        && !FormListUtil.isRowFormsStateChanged(formList.getFormRows())
        || "true".equals(parameter)) {
      getFlowCtx().cancel();
    } else {
      putViewDataOnce("askCloseConfirmation", "true");
    }
  }

  private class FeedBackProvidingListener implements OnClickEventListener {

    private static final long serialVersionUID = 1L;

    public void onClick() throws Exception {
      getMessageCtx().showInfoMessage("Added: " + inMemoryHelper.getAdded().values());
      getMessageCtx().showInfoMessage("Updated: " + inMemoryHelper.getUpdated().values());
      getMessageCtx().showInfoMessage("Deleted: " + inMemoryHelper.getDeleted());
    }
  }

  private class DemoEditableRowHandler extends ValidOnlyIndividualFormRowHandler {

    private static final long serialVersionUID = 1L;

    public Object getRowKey(Object row) {
      return ((DataDTO) row).getId();
    }

    public void saveValidRow(FormRow editableRow) throws Exception {
      BeanFormWidget form = (BeanFormWidget) editableRow.getForm();

      // Reading data
      DataDTO rowData = (DataDTO) form.writeToBean(editableRow.getRow());

      // Saving data
      inMemoryHelper.update(editableRow.getKey(), rowData);
      form.markBaseState();
    }

    public void deleteRow(Object key) throws Exception {
      // Deleting data
      inMemoryHelper.delete(key);
    }

    public void addValidRow(FormWidget addForm) throws Exception {
      BeanFormWidget form = (BeanFormWidget) addForm;
      DataDTO rowData = (DataDTO) form.writeToBean(new DataDTO());
      inMemoryHelper.add(rowData);
    }

    public void initFormRow(FormRow editableRow, Object row) throws Exception {
      BeanFormWidget rowForm = (BeanFormWidget) editableRow.getForm();
      addCommonFormFields(rowForm);

      // We override the default event listeners with FeedBackProvidingListener.
      // The IDs of the buttons remain the same: "save" and "delete".
      FeedBackProvidingListener listener = new FeedBackProvidingListener();

      ButtonControl saveButton = FormListUtil.addSaveButtonToRowForm("#", formList, rowForm, editableRow.getKey());
      saveButton.addOnClickEventListener(listener);

      ButtonControl deleteButton = FormListUtil.addDeleteButtonToRowForm("#", formList, rowForm, editableRow.getKey());
      deleteButton.addOnClickEventListener(listener);

      rowForm.readFromBean(row);
      editableRow.getForm().markBaseState();
    }

    public void initAddForm(FormWidget addForm) throws Exception {
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
