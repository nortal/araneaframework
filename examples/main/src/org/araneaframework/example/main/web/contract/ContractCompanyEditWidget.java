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

package org.araneaframework.example.main.web.contract;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.CompanyMO;
import org.araneaframework.example.main.web.company.CompanyListWidget;
import org.araneaframework.framework.FlowContext;

/**
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class ContractCompanyEditWidget extends TemplateBaseWidget {

  private static final Log LOG = LogFactory.getLog(ContractCompanyEditWidget.class);

  private CompanyMO company = null;

  public CompanyMO getCompany() {
    return company;
  }

  public void setCompany(CompanyMO company) {
    this.company = company;
  }

  @Override
  protected void init() throws Exception {
    LOG.debug("TemplateContractCompanyWidget init called");
    setViewSelector("contract/contractCompanyEdit");
    addEventListener("chooseCompany", new ProxyEventListener(this));
  }

  public void handleEventChooseCompany() throws Exception {
    getFlowCtx().start(new CompanyListWidget(false), new FlowContext.Handler<Long>() {

      public void onFinish(Long id) throws Exception {
        setCompany(getCompanyDAO().getById(CompanyMO.class, id));
        LOG.debug("Company with id of " + id + " set to this contract");
      }

      public void onCancel() throws Exception {}
    });
  }
}
