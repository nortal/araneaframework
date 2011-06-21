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

package org.araneaframework.example.main.web.management.person;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.list.model.ListItemsData;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.data.IContractDAO;
import org.araneaframework.example.main.business.data.PersonListDAO;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.form.formlist.BeanFormRow;
import org.araneaframework.uilib.form.formlist.FormListUtil;
import org.araneaframework.uilib.form.formlist.FormRowHandler;
import org.araneaframework.uilib.form.formlist.adapter.ValidOnlyIndividualBeanFormRowHandler;
import org.araneaframework.uilib.list.EditableBeanListWidget;
import org.araneaframework.uilib.list.dataprovider.BackendListDataProvider;
import org.araneaframework.uilib.list.dataprovider.ListDataProvider;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;

public abstract class PersonEditableListWidget extends TemplateBaseWidget {

  protected static final Log LOG = LogFactory.getLog(PersonEditableListWidget.class);

  private IContractDAO contractDAO;

  // Editable list.
  private EditableBeanListWidget<Long, PersonMO> list;

  // Actual holder of editable list rows (resides inside EditableBeanListWidget). Look inside init() method to see where
  // it comes from.
  private BeanFormListWidget<Long, PersonMO> formList;

  @Override
  protected void init() throws Exception {
    setViewSelector("management/person/editableList");

    // PersonMO class is already familiar from form examples. FormRowHandler class that will handle the different row
    // operations.
    this.list = new EditableBeanListWidget<Long, PersonMO>(buildFormRowHandler(), PersonMO.class);
    this.formList = this.list.getFormList();
    addWidget("list", this.list);

    // Filtering by fields other than ID is enabled.
    this.list.setOrderableByDefault(true);
    this.list.addField("id", "common.id", false);
    this.list.addField("name", "common.firstname").like();
    this.list.addField("surname", "common.lastname").like();
    this.list.addField("phone", "persons.address").like();
    this.list.addField("birthdate", "persons.birthdate").range();
    this.list.addField("salary", "persons.salary").range();
    this.list.addEmptyField("dummy");

    /*
     * Set the provider through which list acquires its data. Exactly the same as for ordinary lists.
     */
    this.list.setDataProvider(buildListDataProvider());
  }

  public abstract boolean isMemoryBased();

  protected abstract ListDataProvider<PersonMO> buildListDataProvider() throws Exception;

  protected abstract FormRowHandler<Long, PersonMO> buildFormRowHandler() throws Exception;

  public static class Memory extends PersonEditableListWidget {

    private MemoryBasedListDataProvider<PersonMO> dataProvider = new DataProvider();

    @Override
    protected ListDataProvider<PersonMO> buildListDataProvider() throws Exception {
      return this.dataProvider;
    }

    @Override
    protected FormRowHandler<Long, PersonMO> buildFormRowHandler() throws Exception {

      // Implementation of FormRowHandler that also calls dataprovider's data refresh methods when list editing events
      // occur.
      return new PersonEditableRowHandler();
    }

    private class DataProvider extends MemoryBasedListDataProvider<PersonMO> {

      protected DataProvider() {
        super(PersonMO.class);
      }

      @Override
      public List<PersonMO> loadData() throws Exception {
        return getGeneralDAO().getAll(PersonMO.class);
      }
    }

    @Override
    public boolean isMemoryBased() {
      return true;
    }
  }

  public static class Backend extends PersonEditableListWidget {

    @Override
    protected ListDataProvider<PersonMO> buildListDataProvider() throws Exception {
      return new DataProvider();
    }

    @Override
    protected FormRowHandler<Long, PersonMO> buildFormRowHandler() throws Exception {
      return new PersonEditableRowHandler();
    }

    private class DataProvider extends BackendListDataProvider<PersonMO> {

      protected DataProvider() {
        super(false);
      }

      @Override
      protected ListItemsData<PersonMO> getItemRange(ListQuery query) throws Exception {
        return ((PersonListDAO) getBeanFactory().getBean("personListDAO")).getItems(query);
      }
    }

    @Override
    public boolean isMemoryBased() {
      return false;
    }
  }

  /**
   * Row handling functions. As this handler extends ValidOnlyIndividualFormRowHandler class, its saveRow method does
   * nothing: instead saveValidRow method should be implemented that saves only these forms (rows) which data passes
   * validation.
   */
  public class PersonEditableRowHandler extends ValidOnlyIndividualBeanFormRowHandler<Long, PersonMO> {

    /*
     * Implementation of the method that must return unique key for each row in editable list. As we hold database
     * objects (PersonMO-s) in this list, it is natural to use synthetic ID field for a key.
     */
    public Long getRowKey(PersonMO rowData) {
      return rowData.getId();
    }

    /**
     * Implementation of method that should save EDITED rows which data passes validation.
     */
    @Override
    public void saveValidRow(BeanFormRow<Long, PersonMO> editableRow) throws Exception {

      /*
       * Reads data from form. FormRow.getForm() method returns the widget that is currently holding row object data --
       * it is either FormWidget or BeanFormWidget, as in our case we are using EditableBeanListWidget that holds row
       * data in BeanFormWidgets, we can cast the return type accordingly.
       */
      PersonMO rowData = editableRow.getForm().writeToBean();
      rowData.setId(editableRow.getKey());

      // Save modified object.
      getGeneralDAO().edit(rowData);

      // Set the row closed (for further editing, it must be opened again).
      editableRow.close();
      list.getDataProvider().refreshData();
    }

    @Override
    public void deleteRow(Long key) throws Exception {
      contractDAO.removeByPersonId(key);
      getGeneralDAO().remove(PersonMO.class, key);
      list.getDataProvider().refreshData();
    }

    // Implementation of method that should save ADDED rows which data passes
    // validation.
    @Override
    public void addValidRow(BeanFormWidget<PersonMO> addForm) throws Exception {
      PersonMO rowData = addForm.writeToBean();
      getGeneralDAO().add(rowData);
      list.getDataProvider().refreshData();

      // this callback must be made here!
      formList.resetAddForm();
    }

    // Called to initialize each row in editable list.
    @Override
    public void initFormRow(BeanFormRow<Long, PersonMO> editableRow, PersonMO rowData) throws Exception {

      // Set initial status of list rows to closed - they cannot be edited before opened.
      editableRow.close();

      // Get the rowForm (this is the form widget holding row object data).
      BeanFormWidget<PersonMO> rowForm = editableRow.getForm();

      // See below.
      addCommonFormFields(rowForm);

      /*
       * A button that opens row for editing upon receiving onClick event. Activating button in already opened row saves
       * the row data.
       */
      FormListUtil.addEditSaveButtonToRowForm("#", editableRow);

      /* A button that deletes this row and its data (calls deleteRow()). */
      FormListUtil.addDeleteButtonToRowForm("#", editableRow);

      rowForm.readFromBean(rowData);
    }

    // Called to initialize a blank row meant for adding new records.
    @Override
    public void initAddForm(BeanFormWidget<PersonMO> addForm) throws Exception {
      addCommonFormFields(addForm);

      // Button that saves the content of the new record (calls addValidRow()).
      FormListUtil.addAddButtonToAddForm("#", formList, addForm);
    }

    // Adds PersonMO bean fields to given BeanFormWidget.
    private void addCommonFormFields(BeanFormWidget<PersonMO> form) throws Exception {
      form.addBeanElement("name", "common.firstname", new TextControl(), true);
      form.addBeanElement("surname", "common.lastname", new TextControl(), true);
      form.addBeanElement("phone", "persons.address", new TextControl(), false);
      form.addBeanElement("birthdate", "persons.birthdate", new DateControl(), false);
      form.addBeanElement("salary", "persons.salary", new FloatControl(), false);
    }
  }

  public void injectContractDAO(IContractDAO contractDAO) {
    this.contractDAO = contractDAO;
  }
}
