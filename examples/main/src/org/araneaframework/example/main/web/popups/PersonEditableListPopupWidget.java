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

package org.araneaframework.example.main.web.popups;

import java.io.Writer;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardActionListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.data.IContractDAO;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.example.main.message.PopupMessageFactory;
import org.araneaframework.example.main.web.sample.NameWidget;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.support.PopupWindowProperties;
import org.araneaframework.uilib.core.PopupFlowWidget;
import org.araneaframework.uilib.event.OnClickEventListener;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.form.formlist.FormListUtil;
import org.araneaframework.uilib.form.formlist.FormRow;
import org.araneaframework.uilib.form.formlist.adapter.ValidOnlyIndividualFormRowHandler;
import org.araneaframework.uilib.list.EditableBeanListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;

public class PersonEditableListPopupWidget extends TemplateBaseWidget {
  private static final long serialVersionUID = 1L;
  protected static final Log log = LogFactory.getLog(PersonEditableListPopupWidget.class);
  private  IContractDAO contractDAO; 

  private MemoryBasedListDataProvider dataProvider = new DataProvider();

  private boolean usePopupFlow = true;
  private boolean useAction = false;

  private EditableBeanListWidget list;
  private BeanFormListWidget formList;

  protected void init() throws Exception {
    setViewSelector("person/popupeditableList");

    list = new EditableBeanListWidget(new PersonEditableRowHandler(), PersonMO.class);
    this.formList = list.getFormList();
    addWidget("list", list);
    list.setOrderableByDefault(true);
    list.addField("id", "#Id", false);
    list.addField("name", "#First name").like();
    list.addField("surname", "#Last name").like();
    list.addField("phone", "#Phone no").like();		
    list.addField("birthdate", "#Birthdate").range();
    list.addField("salary", "#Salary").range();
    list.addField("dummy", null, false);

    list.setDataProvider(dataProvider);
  }

  private class DataProvider extends MemoryBasedListDataProvider {
    private static final long serialVersionUID = 1L;
    private List data;

    protected DataProvider() {
      super(PersonMO.class);
    }
    public List loadData() throws Exception {
      if (data == null)
        data = getGeneralDAO().getAll(PersonMO.class);
      return data;
    }
  }

  public class PersonEditableRowHandler extends ValidOnlyIndividualFormRowHandler {
    private static final long serialVersionUID = 1L;

    public Object getRowKey(Object rowData) {
      return ((PersonMO) rowData).getId();
    }

    public void saveValidRow(FormRow editableRow) throws Exception {

    }

    public void deleteRow(Object key) throws Exception {
      Long id = (Long) key;
      contractDAO.removeByPersonId(id);
      getGeneralDAO().remove(PersonMO.class, id);
      list.getDataProvider().refreshData();
    }

    public void addValidRow(FormWidget addForm) throws Exception {
      PersonMO rowData = (PersonMO) (((BeanFormWidget)addForm).writeToBean(new PersonMO()));
      getGeneralDAO().add(rowData);
      list.getDataProvider().refreshData();
      formList.resetAddForm();
    }

    public void initFormRow(FormRow editableRow, Object rowData) throws Exception {
      BeanFormWidget rowForm = (BeanFormWidget)editableRow.getForm();
      addCommonFormFields(rowForm);

      FormListUtil.addButtonToRowForm("#", rowForm, new PopupListenerFactory().createListener(rowData, rowForm) , "popupButton");
      rowForm.addActionListener("testAction", new TestActionListener());
      rowForm.readFromBean(rowData);
    }

    public void initAddForm(FormWidget addForm) throws Exception {
      addCommonFormFields((BeanFormWidget)addForm);
      FormListUtil.addAddButtonToAddForm("#", formList, addForm);
    }


    private void addCommonFormFields(BeanFormWidget form) throws Exception {
      form.addBeanElement("name", "#First name", new TextControl(), true);
      form.addBeanElement("surname", "#Last name", new TextControl(),  true);
      form.addBeanElement("phone", "#Phone no", new TextControl(), false);
      form.addBeanElement("birthdate", "#Birthdate", new DateControl(), false);
      form.addBeanElement("salary", "#Salary", new FloatControl(), false);
    }
  }

  private class PopupListenerFactory {
    public OnClickEventListener createListener(Object data, Widget w) {
      if (usePopupFlow)
        return new PopupFlowListener((PersonMO)data);

      if (useAction)
        return new PopupClientListenerActionInvoker(PersonEditableListPopupWidget.this, w);

      return new PopupClientListener(PersonEditableListPopupWidget.this, w);
    }
  }

  private class PopupFlowListener implements OnClickEventListener {
    private PersonMO person;
    public PopupFlowListener(PersonMO eventParam) {
      this.person = eventParam;
    }

    public void onClick() throws Exception {
      FormRow formRow = (FormRow) list.getFormList().getFormRows().get(list.getFormList().getFormRowHandler().getRowKey(person)); 
      final BeanFormWidget rowForm = (BeanFormWidget) formRow.getForm(); 
      PopupFlowWidget pfw = new PopupFlowWidget(new NameWidget(), new PopupWindowProperties(), new PopupMessageFactory());
      getFlowCtx().start(pfw, null, new MyHandler(rowForm, person)); 
    }
  }

  private class PopupClientListener implements OnClickEventListener {
    private Widget starter;
    private Widget receiver;

    public PopupClientListener(Widget opener, Widget receiver) {
      starter = opener;
      this.receiver = receiver;
    }

    public void onClick() throws Exception {
      String widgetId = receiver.getScope().toString();
      widgetId = widgetId + ".name";

      StandalonePopupFlowWrapperWidget toStart = new StandalonePopupFlowWrapperWidget(new NameWidget());
      toStart.setFinishService(new ApplyReturnValueService(widgetId));

      PopupWindowProperties p = new PopupWindowProperties();
      p.setHeight("600");
      p.setWidth("1000");
      p.setScrollbars("yes");

      getPopupCtx().open(new PopupMessageFactory().buildMessage(toStart), p, starter);
    }
  }

  private class PopupClientListenerActionInvoker implements OnClickEventListener {
    private Widget starter;
    private Widget receiver;

    public PopupClientListenerActionInvoker(Widget opener, Widget receiver) {
      starter = opener;
      this.receiver = receiver;
    }

    public void onClick() throws Exception {
      String widgetId = receiver.getScope().toString();
      widgetId = widgetId + ".name";

      StandalonePopupFlowWrapperWidget toStart = new StandalonePopupFlowWrapperWidget(new NameWidget());
      toStart.setFinishService(new ParentActionInvokingService(widgetId));

      PopupWindowProperties p = new PopupWindowProperties();
      p.setHeight("600");
      p.setWidth("1000");
      p.setScrollbars("yes");

      getPopupCtx().open(new PopupMessageFactory().buildMessage(toStart), p, starter);
    }
  }

  private class TestActionListener extends StandardActionListener {
    public void processAction(Object actionId, String actionParam, InputData input, OutputData output) throws Exception {
      StringBuffer s = new StringBuffer("alert('this is a message from action that came back to haunt you, return value being: ");
      s.append(actionParam);
      s.append("')");

      Writer out = ((HttpOutputData) output).getWriter();
      out.write(s.toString());
    }
  }

  private class MyHandler implements FlowContext.Handler { 
    private BeanFormWidget form; 
    private PersonMO rowObject; 

    public MyHandler(BeanFormWidget form, PersonMO rowObject) { 
      this.form = form; 
      this.rowObject = rowObject; 
    }

    public void onCancel() throws Exception {
    }

    public void onFinish(Object returnValue) { 
      rowObject.setName(returnValue.toString());
      form.readFromBean(rowObject);
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
    if (usePopupFlow)
      return "Server-side return";
    if (useAction)
      return "Client-side return calling serverside action";
    return "Client-side return";
  }
}
