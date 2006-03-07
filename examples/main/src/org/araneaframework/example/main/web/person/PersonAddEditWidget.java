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

import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.TextControl;

/**
 * This widget is for adding new or editing existing persons.
 * Upon successful completion it returns the ID of stored person. 
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class PersonAddEditWidget extends TemplateBaseWidget {
	private Long personId = null;
	private boolean editMode;
	
	private BeanFormWidget form; 
	
	/**
	 * Constructor for adding new person. 
	 */
	public PersonAddEditWidget() {
	}
	
	/**
	 * Constructor for editing existing person with specified Id.
	 * @param personId Person's Id.
	 */
	public PersonAddEditWidget(Long id) {
		this.personId = id;
		editMode = true;
	}
	
	protected void init() throws Exception {
		super.init();

		setViewSelector("person/personAddEdit");
		// Set the label conditionally. Set view data is used in JSP for component header. 
		putViewData("label", editMode ? "person.edit.form.label" : "person.add.form.label");
		addGlobalEventListener(new ProxyEventListener(this));
		
		form = buildPersonEditForm();
		addWidget("personForm", form);
	}

	private BeanFormWidget buildPersonEditForm() throws Exception {
		BeanFormWidget form = new BeanFormWidget(PersonMO.class);
		form.addBeanElement("name", "#First name", new TextControl(), true);
		form.addBeanElement("surname", "#Last name", new TextControl(), false);
		form.addBeanElement("phone", "#Phone no", new TextControl(), true);
		form.addBeanElement("birthdate", "#Birthdate", new DateControl(), false);
		
		if (editMode) {
			PersonMO person = (PersonMO) getGeneralDAO().getById(PersonMO.class, personId);
			form.writeBean(person);
		}

		return form;
	}
	
	public void handleEventSave(String eventParameter) throws Exception {
		if (form.convertAndValidate()) {
			PersonMO person = personId != null ? (PersonMO) getGeneralDAO().getById(PersonMO.class, personId) : new PersonMO();
			person = (PersonMO) form.readBean(person);
			
			if (editMode) {
				getGeneralDAO().edit(person);
			} else {
				personId = getGeneralDAO().add(person);      	      	
			}
			getFlowCtx().finish(personId);
		}
	}
	
	public void handleEventCancel(String eventParameter) throws Exception {
		getFlowCtx().cancel();
	}
}
