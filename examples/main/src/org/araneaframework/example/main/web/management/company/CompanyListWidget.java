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

package org.araneaframework.example.main.web.management.company;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.data.IContractDAO;
import org.araneaframework.example.main.business.model.CompanyMO;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.context.DefaultHandler;
import org.araneaframework.uilib.list.BeanListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This widget is for listing companies. It returns selected company's Id or cancels current call. It also allows a user
 * to add or remove companies if it's set to edit mode.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class CompanyListWidget extends TemplateBaseWidget {

  protected static final Log LOG = LogFactory.getLog(CompanyListWidget.class);

  private BeanListWidget<CompanyMO> list;

  private boolean editMode = true;

  @Autowired
  private IContractDAO contractDAO;

  public CompanyListWidget() {}

  public CompanyListWidget(boolean editMode) {
    this.editMode = editMode;
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("management/company/companyList");
    initList();
  }

  protected void initList() throws Exception {
    // Create the new list widget whose records are JavaBeans, instances of CompanyMO.
    // CompanyMO has fields named id, name and address.
    this.list = new BeanListWidget<CompanyMO>(CompanyMO.class);
    addWidget("companyList", this.list);

    // Set the data provider for the list:
    this.list.setDataProvider(new TemplateCompanyListDataProvider());

    // add the displayed columns to list.
    // addField(String id, String label, boolean orderable)
    // note that # before the label means that label is treated as unlocalized and outputted as-is
    this.list.addEmptyField("radio", null);

    // addField(...) returns FieldFilterHelper, like() sets LIKE filter on the column
    this.list.addField("name", "companies.name", true).like();
    this.list.addField("address", "companies.address", true).like();
    this.list.addEmptyField("dummy");
  }

  private void refreshList() {
    this.list.getDataProvider().refreshData();
  }

  public void handleEventAdd() {
    getFlowCtx().start(new CompanyEditWidget(), new FlowContext.Handler<Long>() {

      public void onFinish(Long returnValue) {
        LOG.debug("Company added with ID=" + returnValue + " sucessfully");
        refreshList();  // trick to refresh the list data when we suspect it has changed
      }

      public void onCancel() {}
    });
  }

  public void handleEventRemove(String eventParameter) throws Exception {
    Long id = this.list.getRowFromRequestId(eventParameter).getId();
    this.contractDAO.removeByCompanyId(id);
    getGeneralDAO().remove(CompanyMO.class, id);
    refreshList();
    LOG.debug("Company with ID=" + id + " removed sucessfully");
  }

  public void handleEventSelect(String eventParameter) {
    Long id = this.list.getRowFromRequestId(eventParameter).getId();
    LOG.debug("Company selected with ID=" + id);
    if (this.editMode) {
      getFlowCtx().start(new CompanyEditWidget(id), new FlowContext.Handler<Long>() {

        public void onFinish(Long returnValue) {
          LOG.debug("Company added with ID=" + returnValue + " sucessfully");
          refreshList();
        }

        public void onCancel() {}
      });
    } else {
      getFlowCtx().finish(id);
    }
  }

  public void handleEventEdit(String eventParameter) throws Exception {
    Long id = this.list.getRowFromRequestId(eventParameter).getId();
    LOG.debug("Company selected with ID=" + id);
    getFlowCtx().start(new CompanyEditWidget(id), new DefaultHandler<Long>() {

      @Override
      public void onFinish(Long returnValue) {
        LOG.debug("Company added with ID=" + returnValue + " sucessfully");
        refreshList();
      }
    });
  }

  public void handleEventCancel() {
    getFlowCtx().cancel();
  }

  public void handleEventCollect() {
    CompanyMO company = this.list.getSelectedRow();
    if (company != null) {
      getMessageCtx().showInfoMessage("companies.selected", company.getName());
    }
  }

  private class TemplateCompanyListDataProvider extends MemoryBasedListDataProvider<CompanyMO> {

    // Overloading constructor with correct bean type.
    protected TemplateCompanyListDataProvider() {
      super(CompanyMO.class);
    }

    // Overloading the real data loading method. Should
    // return java.util.List containing CompanuMO objects.
    @Override
    public List<CompanyMO> loadData() throws Exception {
      // Here, database query is performed and all rows from COMPANY table retrieved.
      // But you could also get the data from parsing some XML file, /dev/random etc.
      // All that matters is that returned List really contains CompanyMO objects.
      return getGeneralDAO().getAll(CompanyMO.class);
    }
  }
}
