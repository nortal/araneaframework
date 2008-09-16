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

package org.araneaframework.example.main.web.person;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DisplayControl;
import org.araneaframework.uilib.form.control.FloatControl;


/**
 * This widget is for displaying a person.
 * It cancels current call only.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class PersonViewWidget extends TemplateBaseWidget {
	  private static final long serialVersionUID = 1L;
  private Long personId = null;
	
	/**
	 * @param personId Person's Id.
	 */
	public PersonViewWidget(Long id) {
		super();
		this.personId = id;
	}
	
	protected void init() throws Exception {
		setViewSelector("person/personView");

		BeanFormWidget<PersonMO> personForm = new BeanFormWidget<PersonMO>((PersonMO) getGeneralDAO().getById(PersonMO.class, personId));
		personForm.addBeanElement("name", "#First name", new DisplayControl(), true);
		personForm.addBeanElement("surname", "#Last name", new DisplayControl(), false);
		personForm.addBeanElement("phone", "#Phone no", new DisplayControl(), true);
		personForm.addBeanElement("birthdate", "#Birthdate", new DateControl(), false);
		personForm.addBeanElement("salary", "#Salary", new FloatControl(), false);

		addWidget("personForm", personForm);
	}
	
	public void handleEventReturn(String eventParameter) throws Exception {
		getFlowCtx().cancel();
	}	
}
