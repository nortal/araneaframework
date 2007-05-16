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
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.example.main.web.person.PersonListWidget;
import org.araneaframework.framework.FlowContext;


/**
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class ContractPersonEditWidget extends TemplateBaseWidget {
	
	  private static final long serialVersionUID = 1L;
  private static final Logger log = Logger.getLogger(ContractPersonEditWidget.class);
	private PersonMO person = null;

  public PersonMO getPerson() {
		return person;
	}

	public void setPerson(PersonMO person) {
		this.person = person;
	}

	protected void init() throws Exception {
    log.debug("TemplateContractPersonWidget init called");
    setViewSelector("contract/contractPersonEdit");
  }
  
  protected void process() {
    putViewData("person", person);
  }
  
	public void handleEventChoosePerson(String eventParameter) throws Exception {
	    PersonListWidget newFlow = new PersonListWidget(false);
	    newFlow.setSelectOnly(true);
	    getFlowCtx().start(newFlow, new FlowContext.Handler() {
				        private static final long serialVersionUID = 1L;
        public void onFinish(Object returnValue) throws Exception {
					Long id = (Long) returnValue;
					person = (PersonMO) getGeneralDAO().getById(PersonMO.class, id);
					log.debug("Person with id of " + id + " set to this contract");
	      }
	      public void onCancel() throws Exception {
	      }
	    });
	}
}
