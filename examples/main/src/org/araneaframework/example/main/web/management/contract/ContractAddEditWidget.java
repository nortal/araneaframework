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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Widget;
import org.araneaframework.example.common.framework.container.StandardWizardWidget;
import org.araneaframework.example.common.framework.context.WizardContext;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.ContractMO;

/**
 * This widget is for adding new and editing existing contracts. It returns the Id of stored contract or cancels current
 * call.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class ContractAddEditWidget extends TemplateBaseWidget {

  private static final Log LOG = LogFactory.getLog(ContractAddEditWidget.class);

  private Long id = null;

  private ContractCompanyEditWidget company;

  private ContractPersonEditWidget person;

  private ContractNotesEditWidget notes;

  /**
   * Constructor for adding new contract.
   */
  public ContractAddEditWidget() {}

  /**
   * Constructor for editing existing contract with specified Id.
   * 
   * @param id Contract's Id.
   */
  public ContractAddEditWidget(Long id) {
    this.id = id;
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("contract/contractAddEdit");
    this.company = new ContractCompanyEditWidget();
    this.person = new ContractPersonEditWidget();
    this.notes = new ContractNotesEditWidget();

    StandardWizardWidget wizard = new StandardWizardWidget();
    addWidget("wizard", wizard);
    wizard.addPage(this.company);
    wizard.addPage(this.person);
    wizard.addPage(this.notes);

    if (this.id != null) {
      ContractMO contract = getContractDAO().getById(ContractMO.class, this.id);
      this.company.setCompany(contract.getCompany());
      this.person.setPerson(contract.getPerson());
      this.notes.setNotes(contract.getNotes());
      this.notes.setTotal(contract.getTotal());
    }

    wizard.addEventListener(new WizardContext.EventListener() {

      public void onGoto(Widget page) throws Exception {}

      public void onSubmit() throws Exception {
        if (validate()) {
          ContractMO contract = id != null ? getContractDAO().getById(ContractMO.class, id) : new ContractMO();
          contract.setCompany(company.getCompany());
          contract.setPerson(person.getPerson());
          contract.setNotes(notes.getNotes());
          contract.setTotal(notes.getTotal());
          if (id != null) {
            getContractDAO().edit(contract);
          } else {
            id = getContractDAO().add(contract);
          }
          LOG.debug("Contract saved, id = " + id);
          getFlowCtx().finish(id);
        }
      }

      public void onCancel() throws Exception {
        getFlowCtx().cancel();
      }
    });
  }

  private boolean validate() throws Exception {
    return this.company.getCompany() != null && this.person.getPerson() != null
        && this.notes.getForm().convertAndValidate();
 }
}
