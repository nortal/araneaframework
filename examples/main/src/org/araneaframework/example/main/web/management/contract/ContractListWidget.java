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

package org.araneaframework.example.main.web.management.contract;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.ContractMO;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;

/**
 * This widget is for listing contracts. It returns selected contract's Id or cancels current call. It also allows a
 * user to add or remove contracts if it's set to edit mode.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class ContractListWidget extends TemplateBaseWidget {

  protected static final Log LOG = LogFactory.getLog(ContractListWidget.class);

  private ListWidget<ContractMO> list;

  public ContractListWidget() {}

  @Override
  protected void init() throws Exception {
    setViewSelector("management/contract/contractList");
    LOG.debug("TemplateContractListWidget init called");
    initList();
  }

  protected void initList() throws Exception {
    this.list = new ListWidget<ContractMO>();
    this.list.setDataProvider(new TemplateContractListDataProvider());
    this.list.addField("id", "#Id");
    this.list.addField("company", "#Company");
    this.list.addField("person", "#Person");
    this.list.addField("notes", "#Notes");
    this.list.addField("dummy", null, false);
    addWidget("contractList", this.list);
  }

  private void refreshList() throws Exception {
    this.list.getDataProvider().refreshData();
  }

  public void handleEventAdd() throws Exception {
    getFlowCtx().start(new ContractAddEditWidget(), new FlowContext.Handler<Long>() {

      public void onFinish(Long id) throws Exception {
        LOG.debug("Contract added with Id of " + id + " sucessfully");
        refreshList();
      }

      public void onCancel() throws Exception {}
    });
  }

  public void handleEventRemove(String eventParameter) throws Exception {
    Long id = this.list.getRowFromRequestId(eventParameter).getId();
    getGeneralDAO().remove(ContractMO.class, id);
    refreshList();
    LOG.debug("Contract with Id of " + id + " removed sucessfully");
  }

  public void handleEventEdit(String eventParameter) throws Exception {
    Long id = this.list.getRowFromRequestId(eventParameter).getId();
    getFlowCtx().start(new ContractAddEditWidget(id));
  }

  public void handleEventCancel() throws Exception {
    getFlowCtx().cancel();
  }

  private class TemplateContractListDataProvider extends MemoryBasedListDataProvider<ContractMO> {

    protected TemplateContractListDataProvider() {
      super(ContractMO.class);
    }

    @Override
    public List<ContractMO> loadData() throws Exception {
      return getGeneralDAO().getAll(ContractMO.class);
    }
  }
}
