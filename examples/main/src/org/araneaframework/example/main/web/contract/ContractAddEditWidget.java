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

package org.araneaframework.example.main.web.contract;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Widget;
import org.araneaframework.example.common.framework.container.StandardWizardWidget;
import org.araneaframework.example.common.framework.context.WizardContext;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.ContractMO;

/**
 * This widget is for adding new and editing existing contracts.
 * It returns the Id of stored contract or cancels current call. 
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class ContractAddEditWidget extends TemplateBaseWidget {
	
	  private static final long serialVersionUID = 1L;

  private static final Log log = LogFactory.getLog(ContractAddEditWidget.class);
	
	private Long id = null;
	private ContractCompanyEditWidget company;
	private ContractPersonEditWidget person;
	private ContractNotesEditWidget notes;
	
  /**
   * Constructor for adding new contract. 
   */
	public ContractAddEditWidget() {
		super();
	}
	
  /**
   * Constructor for editing existing contract with specified Id.
   * @param id Contract's Id.
   */
	public ContractAddEditWidget(Long id) {
		this();
		this.id = id;
	}
	
  protected void init() throws Exception {
    setViewSelector("contract/contractAddEdit");
    company = new ContractCompanyEditWidget();
    person = new ContractPersonEditWidget();
    notes = new ContractNotesEditWidget();
    
    StandardWizardWidget wizard = new StandardWizardWidget();
    addWidget("wizard", wizard);    
    wizard.addPage(company);
    wizard.addPage(person);
    wizard.addPage(notes);
    
    if (id != null) {
    	ContractMO contract = (ContractMO) getGeneralDAO().getById(ContractMO.class, id);
    	company.setCompany(contract.getCompany());
    	person.setPerson(contract.getPerson());
    	notes.setNotes(contract.getNotes());
    	notes.setTotal(contract.getTotal());
    }
    
    wizard.addEventListener(new WizardContext.EventListener() {
    	      private static final long serialVersionUID = 1L;
      public void onGoto(Widget page) throws Exception {}    	
    	public void onSubmit() throws Exception {
    		if (validate()) {
    			ContractMO contract = id != null ? (ContractMO) getGeneralDAO().getById(ContractMO.class, id) : new ContractMO();
    			contract.setCompany(company.getCompany());
    			contract.setPerson(person.getPerson());
    			contract.setNotes(notes.getNotes());
    			contract.setTotal(notes.getTotal());
    			if (id != null) {
    				getGeneralDAO().edit(contract);
    			} else {
    				id = getGeneralDAO().add(contract);
    			}
    			log.debug("Contract saved, id = " + id);
    			getFlowCtx().finish(id);
    		}
    	}
    	public void onCancel() throws Exception {
    		getFlowCtx().cancel();
    	}
    });
  }
  
  protected boolean validate() throws Exception {
  	return company.getCompany() != null && person.getPerson() != null && notes.getForm().convertAndValidate();
  }
}
