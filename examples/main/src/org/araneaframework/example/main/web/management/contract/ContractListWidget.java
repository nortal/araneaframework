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
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.ContractMO;
import org.araneaframework.framework.context.DefaultHandler;
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

  public ContractListWidget() {
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("management/contract/contractList");
    LOG.debug("TemplateContractListWidget init called");
    initList();
  }

  protected void initList() throws Exception {
    this.list = new ListWidget<ContractMO>();
    this.list.setDataProvider(new TemplateContractListDataProvider());
    this.list.addField("id", "common.id");
    this.list.addField("company", "contract.company");
    this.list.addField("person", "contract.person");
    this.list.addField("notes", "contract.notes");
    this.list.addField("dummy", null, false);
    addWidget("contractList", this.list);
  }

  private void refreshList() {
    try {
      this.list.getDataProvider().refreshData();
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
    }
  }

  public void handleEventAdd() {
    getFlowCtx().start(new ContractAddEditWidget(), new DefaultHandler<Long>() {

      @Override
      public void onFinish(Long id) {
        LOG.debug("Contract added with Id of " + id + " sucessfully");
        refreshList();
      }
    });
  }

  public void handleEventRemove(String eventParameter) {
    Long id = this.list.getRowFromRequestId(eventParameter).getId();
    getGeneralDAO().remove(ContractMO.class, id);
    refreshList();
    LOG.debug("Contract with Id of " + id + " removed sucessfully");
  }

  public void handleEventEdit(String eventParameter) {
    Long id = this.list.getRowFromRequestId(eventParameter).getId();
    getFlowCtx().start(new ContractAddEditWidget(id));
  }

  public void handleEventCancel() {
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
