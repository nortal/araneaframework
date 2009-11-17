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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.data.IContractDAO;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;

/**
 * This widget is for listing persons. It returns selected person's Id or cancels current call. It also allows a user to
 * add or remove persons if it's set to edit mode.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 */
public class PersonListWidget extends TemplateBaseWidget {

  protected static final Log LOG = LogFactory.getLog(PersonListWidget.class);

  private boolean editMode = false;

  private boolean selectOnly = false;

  private IContractDAO contractDAO;

  private ListWidget<PersonMO> list;

  public PersonListWidget() {}

  /**
   * @param editMode whether to allow add or remove persons.
   */
  public PersonListWidget(boolean editMode) {
    this.editMode = editMode;
  }

  @Override
  protected void init() {
    setViewSelector("person/personList");
    initList();
  }

  protected void initList() {
    this.list = new BeanListWidget<PersonMO>(PersonMO.class);
    this.list.setSelectFromMultiplePages(true);
    this.list.setDataProvider(new TemplatePersonListDataProvider());
    this.list.addEmptyField("check", "#Choose");
    this.list.setOrderableByDefault(true);
    this.list.addField("name", "#First name").like();
    this.list.addField("surname", "#Last name").setIgnoreCase(false).like();
    this.list.addField("phone", "#Phone no").like();
    this.list.addField("birthdate", "#Birthdate").range();
    this.list.addField("salary", "#Salary").range();
    this.list.setInitialOrder("name", true);

    // The dummy column without label (in list rows, some listRowLinkButton's will be written there).
    // Needed to write out componentListHeader with correct number of columns.
    this.list.addEmptyField("dummy", null);

    addWidget("personList", this.list);
  }

  protected void refreshList() throws Exception {
    this.list.getDataProvider().refreshData();
  }

  public void handleEventAdd() {
    getFlowCtx().start(new PersonAddEditWidget(), null, new FlowContext.Handler<Long>() {

      public void onFinish(Long returnValue) throws Exception {
        LOG.debug("Person added with Id of " + returnValue + " sucessfully");
        refreshList();
      }

      // ignore call cancel
      public void onCancel() throws Exception {}
    });
  }

  public void handleEventRemove(String eventParameter) throws Exception {
    if (!this.editMode) {
      throw new RuntimeException("Event 'remove' shoud be called only in edit mode");
    }
    Long id = this.list.getRowFromRequestId(eventParameter).getId();
    this.contractDAO.removeByPersonId(id);
    getPersonDAO().remove(PersonMO.class, id);
    refreshList();
    LOG.debug("Person with Id of " + id + " removed sucessfully");
  }

  public void handleEventSelect(String eventParameter) {
    Long id = this.list.getRowFromRequestId(eventParameter).getId();
    if (!this.selectOnly) {
      PersonViewWidget newFlow = new PersonViewWidget(id);
      getFlowCtx().start(newFlow, null, null);
    } else {
      getFlowCtx().finish(id);
    }
  }

  public void handleEventEdit(String eventParameter) {
    Long id = this.list.getRowFromRequestId(eventParameter).getId();
    PersonAddEditWidget newFlow = new PersonAddEditWidget(id);
    getFlowCtx().start(newFlow, new FlowContext.Handler<Object>() {

      public void onFinish(Object returnValue) throws Exception {
        refreshList();
      }

      public void onCancel() throws Exception {}
    });
  }

  public void handleEventCancel() {
    getFlowCtx().cancel();
  }

  public void handleEventCollect() {
    List<PersonMO> persons = this.list.getSelectedRows();
    this.list.resetSelectedRows();
    getMessageCtx().showInfoMessage("Following persons were selected: " + StringUtils.join(persons, ", "));
  }

  public void setSelectOnly(boolean selectOnly) {
    this.selectOnly = selectOnly;
  }

  private class TemplatePersonListDataProvider extends MemoryBasedListDataProvider<PersonMO> {

    protected TemplatePersonListDataProvider() {
      super(PersonMO.class);
    }

    @Override
    public List<PersonMO> loadData() throws Exception {
      return getPersonDAO().getAll(PersonMO.class);
    }
  }

  public void injectContractDAO(IContractDAO contractDAO) {
    this.contractDAO = contractDAO;
  }
}
