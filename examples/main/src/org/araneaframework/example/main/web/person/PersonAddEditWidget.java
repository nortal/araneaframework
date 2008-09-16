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
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.TextControl;

/**
 * This widget is for adding new or editing existing persons.
 * Upon successful completion it returns the ID of stored person. 
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class PersonAddEditWidget extends TemplateBaseWidget {
  private static final long serialVersionUID = 1L;
  /* The ID field of the person data model, only has value if object has already
    been saved to/loaded from database. */
	private Long personId = null;
	// Whether the person is being edited or added
	private boolean editMode;
	
	/* The form. Person data (represented by class PersonMO) will be binded to it, thus 
	   usage of BeanFormWidget instead of FormWidget. */
	private BeanFormWidget<PersonMO> form;

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
		// Sets the view selector that will be used for rendering this widget. */ 
		setViewSelector("person/personAddEdit");
		// This viewdata is used in JSP to set component header (different for editing and adding). 
		putViewData("label", editMode ? "person.edit.form.label" : "person.add.form.label");

		form = buildPersonEditForm();
		addWidget("personForm", form);
	}

	private BeanFormWidget<PersonMO> buildPersonEditForm() throws Exception {
    // get the current person data (retrieved from database by getGeneralDAO() in case person already has assigned ID); 
    PersonMO person = editMode ? (PersonMO) getGeneralDAO().getById(PersonMO.class, personId) : new PersonMO();
		
    /* Create the form, specifying the class of data that is binded to this form. */
		BeanFormWidget<PersonMO> form = new BeanFormWidget<PersonMO>(person);
		
		/* Adding the elements is done like in our SimpleFormWidget example, except
		 * that Data type is determined from bean class automatically and specifying
		 * it is not needed. */
		
		//BeanFormWidget.addBeanElement(String elementName, String labelId, Control control, boolean mandatory)
		form.addBeanElement("name", "#First name", new TextControl(), true);
		form.addBeanElement("surname", "#Last name", new TextControl(), false);
		form.addBeanElement("phone", "#Phone no", new TextControl(), true);
		form.addBeanElement("birthdate", "#Birthdate", new DateControl(), false);
		form.addBeanElement("salary", "#Salary", new FloatControl(), false);
		
		return form;
	}
	
	public void handleEventSave(String eventParameter) throws Exception {
		// if form data is found to be valid  
		if (form.convertAndValidate()) {
			// read the application user supplied data from form into model object.
		  PersonMO person = form.writeToBean();
			
			if (editMode) {
				// updates person object in database
				getGeneralDAO().edit(person);
			} else {
				// saves new person object to database
				personId = getGeneralDAO().add(person);      	      	
			}

			/* finish current flow execution and return to calling flow, returning database ID 
			 * of added or edited person */ 
			getFlowCtx().finish(personId);
		} else {
			/* Do nothing, error messages are applied to MessageContext by validating methods 
			 * so that application user receives immediate feedback about incorrectly filled
			 * form elements automatically. */
		}
	}
	
	/* Cancels the adding/editing of current person, returns to calling flow */
	public void handleEventCancel(String eventParameter) throws Exception {
		getFlowCtx().cancel();
	}
}
