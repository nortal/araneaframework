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

package org.araneaframework.example.main.web.release;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.release.model.ExampleData;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.constraint.BaseFieldConstraint;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.CharData;
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.form.formlist.BeanFormRow;
import org.araneaframework.uilib.form.formlist.FormListUtil;
import org.araneaframework.uilib.form.formlist.adapter.ValidOnlyIndividualBeanFormRowHandler;
import org.araneaframework.uilib.list.EditableBeanListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.araneaframework.uilib.list.util.FormUtil;

public class DemoSimpleEditableList extends TemplateBaseWidget {

  protected List<ExampleData.Client> friends = new ArrayList<ExampleData.Client>();

  private MemoryBasedListDataProvider<ExampleData.Client> dataProvider = new DataProvider();

  /* Editable list. */
  private EditableBeanListWidget<Long, ExampleData.Client> list;

  /*
   * Actual holder of editable list rows (resides inside EditableBeanListWidget). Look inside init() method to see where
   * it comes from.
   */
  private BeanFormListWidget<Long, ExampleData.Client> formList;

  // Plays the role of a sequence:
  private Long lastId = 0L;

  
  public DemoSimpleEditableList() {
    Random rn = new Random();
    List<String> allSuggestions = new ArrayList<String>();
    for (String country : Locale.getISOCountries()) {
      allSuggestions.add(new Locale("en", country).getDisplayCountry(Locale.ENGLISH));
    }

    for (int i = 0; i < ExampleData.males.length; i++) {
      ExampleData.Client friend = new ExampleData.Client();
      friend.setForename(ExampleData.males[i]);
      friend.setId(this.lastId);
      friend.setSex(ExampleData.Client.SEX_M);
      friend.setSurname(ExampleData.fungi[rn.nextInt(ExampleData.fungi.length)]);
      friend.setCountry(allSuggestions.get(rn.nextInt(allSuggestions.size())));
      this.friends.add(friend);
      this.lastId = this.lastId + 1;
    }

    for (int i = 0; i < ExampleData.females.length; i++) {
      ExampleData.Client friend = new ExampleData.Client();
      friend.setForename(ExampleData.females[i]);
      friend.setId(this.lastId);
      friend.setSex(ExampleData.Client.SEX_F);
      friend.setSurname(ExampleData.fungi[rn.nextInt(ExampleData.fungi.length)]);
      friend.setCountry(allSuggestions.get(rn.nextInt(allSuggestions.size())));
      this.friends.add(friend);
      this.lastId = this.lastId + 1;
    }
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("release/simpleInmemoryEditableList");

    // PersonMO class is already familiar from form examples. FormRowHandler class that will handle different row
    // operations.

    this.list = new EditableBeanListWidget<Long, ExampleData.Client>(new FriendEditableRowHandler(),
        ExampleData.Client.class);

    this.formList = this.list.getFormList();
    addWidget("list", this.list);
    this.list.setOrderableByDefault(true);

    // list.addField("id", "#Id", false);
    // Filtering by fields other than ID is enabled:
    this.list.addField("sex", "common.sex").like();
    this.list.addField("forename", "common.firstname").like();
    this.list.addField("surname", "common.lastname").like();
    this.list.addField("country", "common.Country").like();
    this.list.addField("dummy", null, false);
    this.list.setDataProvider(this.dataProvider);
  }

  private class DataProvider extends MemoryBasedListDataProvider<ExampleData.Client> {

    protected DataProvider() {
      super(ExampleData.Client.class);
    }

    @Override
    public List<ExampleData.Client> loadData() throws Exception {
      return DemoSimpleEditableList.this.friends;
    }
  }

  private class FriendEditableRowHandler extends ValidOnlyIndividualBeanFormRowHandler<Long, ExampleData.Client> {

    public Long getRowKey(ExampleData.Client rowData) {
      return rowData.getId();
    }

    @Override
    public void saveValidRow(BeanFormRow<Long, ExampleData.Client> editableRow) throws Exception {
      ExampleData.Client rowData = editableRow.getForm().writeToBean();
      rowData.setId(editableRow.getKey());
      ExampleData.Client toModify = (ExampleData.Client) CollectionUtils.find(friends,
          new BeanPropertyValueEqualsPredicate("id", editableRow.getKey()));
      toModify.edit(rowData);
      editableRow.close();
    }

    @Override
    public void deleteRow(Long key) throws Exception {
      ExampleData.Client toRemove = (ExampleData.Client) CollectionUtils.find(friends,
          new BeanPropertyValueEqualsPredicate("id", key));
      friends.remove(toRemove);
      list.getDataProvider().refreshData();
    }

    @Override
    public void addValidRow(BeanFormWidget<ExampleData.Client> addForm) throws Exception {
      ExampleData.Client newFriend = addForm.writeToBean();
      friends.add(newFriend);
      list.getDataProvider().refreshData();
      formList.resetAddForm();
    }

    // Called to initialize each row in editable list.
    @Override
    public void initFormRow(BeanFormRow<Long, ExampleData.Client> editableRow, ExampleData.Client rowData) throws Exception {
      editableRow.close();
      BeanFormWidget<ExampleData.Client> rowForm = editableRow.getForm();
      addCommonFormFields(rowForm);
      FormListUtil.addEditSaveButtonToRowForm("#", editableRow);
      FormListUtil.addDeleteButtonToRowForm("#", editableRow);
      rowForm.readFromBean(rowData);
    }

    @Override
    public void initAddForm(BeanFormWidget<ExampleData.Client> addForm) throws Exception {
      addCommonFormFields(addForm);
      FormListUtil.addAddButtonToAddForm("#", formList, addForm);
    }

    private void addCommonFormFields(BeanFormWidget<ExampleData.Client> form) throws Exception {
      form.addElement("sex", buildSexElement());
      form.addBeanElement("forename", "common.forename", new TextControl(), true);
      form.addBeanElement("surname", "common.surname", new TextControl(), true);
      form.addBeanElement("country", "common.Country", new TextControl(), true);
    }
  }

  private FormElement<String, Character> buildSexElement() {
    FormElement<String, Character> result = FormUtil.createElement("common.sex", new TextControl(), new CharData(), true);
    result.setConstraint(new SexConstraint());
    return result;
  }

  private static class SexConstraint extends BaseFieldConstraint<String, String> {

    @Override
    protected void validateConstraint() throws Exception {
      String value = getValue();
      if (!StringUtils.equalsIgnoreCase(value, "m") || StringUtils.equalsIgnoreCase(value, "f")) {
        addError("simpleEditableList.errmsg", t(getLabel()));
      }
    }
  }
}
