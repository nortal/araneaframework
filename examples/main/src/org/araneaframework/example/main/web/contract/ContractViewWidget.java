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

import org.apache.log4j.Logger;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.ContractMO;
import org.araneaframework.example.main.web.company.CompanyViewWidget;
import org.araneaframework.example.main.web.person.PersonViewWidget;


/**
 * This widget is for displaying a contract. It cancels current call only.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class ContractViewWidget extends TemplateBaseWidget {

	private static final Logger log = Logger
			.getLogger(ContractViewWidget.class);

	private Long id = null;

	private ContractMO contract;

	/**
	 * @param id
	 *          Company's Id.
	 */
	public ContractViewWidget(Long id) {
		super();
		this.id = id;
	}

	protected void init() throws Exception {
		super.init();
		log.debug("TemplateContractViewWidget init called");
		setViewSelector("contract/contractView");
    
		contract = (ContractMO) getGeneralDAO().getById(ContractMO.class, id);
		putViewData("contract", contract);
	}

	public void handleEventViewCompany(String eventParameter) throws Exception {
		log.debug("Event 'viewCompany' received!");
		getFlowCtx().start(
				new CompanyViewWidget(contract.getCompany().getId()), null, null);
	}

	public void handleEventViewPerson(String eventParameter) throws Exception {
		log.debug("Event 'viewPerson' received!");
		getFlowCtx().start(
				new PersonViewWidget(contract.getPerson().getId()), null, null);
	}

	public void handleEventReturn(String eventParameter) throws Exception {
		log.debug("Event 'return' received!");
		getFlowCtx().cancel();
	}
}
