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

import org.araneaframework.example.main.web.management.person.PersonListWidget;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.framework.FlowContext;

/**
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class ContractPersonEditWidget extends TemplateBaseWidget {

  private static final Log LOG = LogFactory.getLog(ContractPersonEditWidget.class);

  private PersonMO person = null;

  public PersonMO getPerson() {
    return this.person;
  }

  public void setPerson(PersonMO person) {
    this.person = person;
    LOG.debug("Person with id of " + person.getId() + " set to this contract");
  }

  @Override
  protected void init() throws Exception {
    LOG.debug("TemplateContractPersonWidget init called");
    setViewSelector("management/contract/contractPersonEdit");
  }

  public void handleEventChoosePerson() throws Exception {
    PersonListWidget newFlow = new PersonListWidget(false);
    newFlow.setSelectOnly(true);
    getFlowCtx().start(newFlow, new FlowContext.Handler<Long>() {

      public void onFinish(Long id) throws Exception {
        setPerson(getGeneralDAO().getById(PersonMO.class, id));
      }

      public void onCancel() throws Exception {}
    });
  }
}
