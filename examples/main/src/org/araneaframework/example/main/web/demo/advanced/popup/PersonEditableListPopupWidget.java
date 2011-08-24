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

package org.araneaframework.example.main.web.demo.advanced.popup;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.action.StandardActionListener;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.data.IContractDAO;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.example.main.message.PopupMessageFactory;
import org.araneaframework.example.main.web.demo.simple.NameWidget;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.support.PopupWindowProperties;
import org.araneaframework.uilib.core.PopupFlowWidget;
import org.araneaframework.uilib.event.OnClickEventListener;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.form.formlist.BeanFormRow;
import org.araneaframework.uilib.form.formlist.FormListUtil;
import org.araneaframework.uilib.form.formlist.FormRow;
import org.araneaframework.uilib.form.formlist.adapter.ValidOnlyIndividualBeanFormRowHandler;
import org.araneaframework.uilib.list.EditableBeanListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;

public class PersonEditableListPopupWidget extends TemplateBaseWidget {

  protected static final Log LOG = LogFactory.getLog(PersonEditableListPopupWidget.class);

  private IContractDAO contractDAO;

  private final MemoryBasedListDataProvider<PersonMO> dataProvider = new DataProvider();

  private EditableBeanListWidget<Long, PersonMO> list;

  private BeanFormListWidget<Long, PersonMO> formList;

  private boolean usePopupFlow = true;

  private boolean useAction = false;

  @Override
  protected void init() throws Exception {
    setViewSelector("demo/advanced/popups/personEditableListPopup");

    this.list = new EditableBeanListWidget<Long, PersonMO>(new PersonEditableRowHandler(), PersonMO.class);
    this.formList = this.list.getFormList();
    addWidget("list", this.list);
    this.list.setOrderableByDefault(true);
    this.list.addField("id", "common.id", false);
    this.list.addField("name", "common.firstname").like();
    this.list.addField("surname", "common.lastname").like();
    this.list.addField("phone", "common.phone").like();
    this.list.addField("birthdate", "common.birthdate").range();
    this.list.addField("salary", "common.salary").range();
    this.list.addEmptyField("dummy");
    this.list.setDataProvider(this.dataProvider);
  }

  private class DataProvider extends MemoryBasedListDataProvider<PersonMO> {

    private List<PersonMO> data;

    protected DataProvider() {
      super(PersonMO.class);
    }

    @Override
    public List<PersonMO> loadData() throws Exception {
      if (this.data == null) {
        this.data = getGeneralDAO().getAll(PersonMO.class);
      }
      return this.data;
    }
  }

  public class PersonEditableRowHandler extends ValidOnlyIndividualBeanFormRowHandler<Long, PersonMO> {

    public Long getRowKey(PersonMO rowData) {
      return rowData.getId();
    }

    @Override
    public void saveValidRow(BeanFormRow<Long, PersonMO> editableRow) throws Exception {
    }

    @Override
    public void deleteRow(Long key) throws Exception {
      PersonEditableListPopupWidget.this.contractDAO.removeByPersonId(key);
      getGeneralDAO().remove(PersonMO.class, key);
      PersonEditableListPopupWidget.this.list.getDataProvider().refreshData();
    }

    @Override
    public void addValidRow(BeanFormWidget<PersonMO> addForm) throws Exception {
      PersonMO rowData = addForm.writeToBean();
      getGeneralDAO().add(rowData);
      PersonEditableListPopupWidget.this.list.getDataProvider().refreshData();
      PersonEditableListPopupWidget.this.formList.resetAddForm();
    }

    @Override
    public void initFormRow(BeanFormRow<Long, PersonMO> editableRow, PersonMO rowData) throws Exception {
      BeanFormWidget<PersonMO> rowForm = editableRow.getForm();
      addCommonFormFields(rowForm);

      FormListUtil.addButtonToRowForm("#", rowForm, new PopupListenerFactory().createListener(rowData, rowForm),
          "popupButton");
      rowForm.addActionListener("testAction", new TestActionListener());
      rowForm.readFromBean(rowData);
    }

    @Override
    public void initAddForm(BeanFormWidget<PersonMO> addForm) throws Exception {
      addCommonFormFields(addForm);
      FormListUtil.addAddButtonToAddForm("#", PersonEditableListPopupWidget.this.formList, addForm);
    }

    private void addCommonFormFields(BeanFormWidget<PersonMO> form) throws Exception {
      form.addBeanElement("name", "common.firstname", new TextControl(), true);
      form.addBeanElement("surname", "common.lastname", new TextControl(), true);
      form.addBeanElement("phone", "common.phone", new TextControl(), false);
      form.addBeanElement("birthdate", "common.birthdate", new DateControl(), false);
      form.addBeanElement("salary", "common.salary", new FloatControl(), false);
    }
  }

  private class PopupListenerFactory {

    public OnClickEventListener createListener(Object data, Widget w) {
      if (PersonEditableListPopupWidget.this.usePopupFlow) {
        return new PopupFlowListener((PersonMO) data);
      } else if (PersonEditableListPopupWidget.this.useAction) {
        return new PopupClientListenerActionInvoker(PersonEditableListPopupWidget.this, w);
      }
      return new PopupClientListener(PersonEditableListPopupWidget.this, w);
    }
  }

  private class PopupFlowListener implements OnClickEventListener {

    private final PersonMO person;

    public PopupFlowListener(PersonMO eventParam) {
      this.person = eventParam;
    }

    @SuppressWarnings("unchecked")
    public void onClick() {
      Long key = PersonEditableListPopupWidget.this.list.getFormList().getFormRowHandler().getRowKey(this.person);
      FormRow<Long, PersonMO> formRow = PersonEditableListPopupWidget.this.list.getFormList().getFormRows().get(key);
      final BeanFormWidget<PersonMO> rowForm = (BeanFormWidget<PersonMO>) formRow.getForm();
      PopupFlowWidget pfw = new PopupFlowWidget(new NameWidget(), new PopupWindowProperties(),
          new PopupMessageFactory());
      getFlowCtx().start(pfw, null, new MyHandler(rowForm, this.person));
    }
  }

  private class PopupClientListener implements OnClickEventListener {

    private final Widget starter;

    private final Widget receiver;

    public PopupClientListener(Widget opener, Widget receiver) {
      this.starter = opener;
      this.receiver = receiver;
    }

    public void onClick() {
      String widgetId = this.receiver.getScope().toString();
      widgetId = widgetId + ".name";

      StandalonePopupFlowWrapperWidget toStart = new StandalonePopupFlowWrapperWidget(new NameWidget());
      toStart.setFinishService(new ApplyReturnValueService(widgetId));

      PopupWindowProperties p = new PopupWindowProperties();
      p.setHeight("600");
      p.setWidth("1000");
      p.setScrollbars("yes");

      getPopupCtx().open(new PopupMessageFactory().buildMessage(toStart), p, this.starter);
    }
  }

  private class PopupClientListenerActionInvoker implements OnClickEventListener {

    private final Widget starter;

    private final Widget receiver;

    public PopupClientListenerActionInvoker(Widget opener, Widget receiver) {
      this.starter = opener;
      this.receiver = receiver;
    }

    public void onClick() {
      String widgetId = this.receiver.getScope().toString();
      widgetId = widgetId + ".name";

      StandalonePopupFlowWrapperWidget toStart = new StandalonePopupFlowWrapperWidget(new NameWidget());
      toStart.setFinishService(new ParentActionInvokingService(widgetId));

      PopupWindowProperties p = new PopupWindowProperties();
      p.setHeight("600");
      p.setWidth("1000");
      p.setScrollbars("yes");

      getPopupCtx().open(new PopupMessageFactory().buildMessage(toStart), p, this.starter);
    }
  }

  private static class TestActionListener extends StandardActionListener {

    @Override
    public void processAction(String actionId, String actionParam, InputData input, OutputData output) {
      StringBuffer s = new StringBuffer(
          "alert('this is a message from action that came back to haunt you, return value being: ");
      s.append(actionParam);
      s.append("')");

      try {
        Writer out = ((HttpOutputData) output).getWriter();
        out.write(s.toString());
      } catch (IOException e) {
        ExceptionUtil.uncheckException(e);
      }
    }
  }

  private static class MyHandler implements FlowContext.Handler<String> {

    private final BeanFormWidget<PersonMO> form;

    private final PersonMO rowObject;

    public MyHandler(BeanFormWidget<PersonMO> form, PersonMO rowObject) {
      this.form = form;
      this.rowObject = rowObject;
    }

    public void onCancel() throws Exception {
    }

    public void onFinish(String returnValue) {
      this.rowObject.setName(returnValue);
      this.form.readFromBean(this.rowObject);
    }
  }

  public void injectContractDAO(IContractDAO contractDAO) {
    this.contractDAO = contractDAO;
  }

  public void setUsePopupFlow(boolean b) {
    this.usePopupFlow = b;
  }

  public void setUseAction(boolean b) {
    this.useAction = b;
  }

  public String getTitle() {
    if (this.usePopupFlow) {
      return "Server-side return";
    } else if (this.useAction) {
      return "Client-side return calling serverside action";
    } else {
      return "Client-side return";
    }
  }
}
