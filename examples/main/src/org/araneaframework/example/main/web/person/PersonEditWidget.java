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

import org.apache.log4j.Logger;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.BaseWidget;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.TextControl;

/**
 * This widget is for adding new and editing existing persons.
 * It retruns the Id of stored person or cancels current call. 
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class PersonEditWidget extends BaseWidget {
	
	private static final Logger log = Logger.getLogger(PersonEditWidget.class);
	
  private Long id = null;
  private boolean editMode;
  
  private BeanFormWidget form; 
  
  /**
   * Constructor for adding new person. 
   */
  public PersonEditWidget() {
	  
  }
    
  /**
   * Constructor for editing existing person with specified Id.
   * @param id Person's Id.
   */
  public PersonEditWidget(Long id) {
  	this.id = id;
  	editMode = true;
  }
	
  protected void init() throws Exception {
    super.init();

    addGlobalEventListener(new ProxyEventListener(this));
    putViewData("label", editMode ? "Edit person" : "Add person");
    setViewSelector(id != null ? "person/personEdit" : "person/personAdd");
    
    form = new BeanFormWidget(PersonMO.class);
    form.addBeanElement("name", "#First name", new TextControl(), true);
    form.addBeanElement("surname", "#Last name", new TextControl(), false);
    form.addBeanElement("phone", "#Phone no", new TextControl(), true);
    form.addBeanElement("birthdate", "#Birthdate", new DateControl(), false);

    if (id != null) {
      PersonMO person = (PersonMO) getGeneralDAO().getById(PersonMO.class, id);
      form.writeBean(person);
    }

    addWidget("form", form);
  }
  
	public void handleEventSave(String eventParameter) throws Exception {
    log.debug("Event 'save' received!");
    if (form.convertAndValidate()) {
    	PersonMO person = id != null ? (PersonMO) getGeneralDAO().getById(PersonMO.class, id) : new PersonMO();
      person = (PersonMO) form.readBean(person);
      
      if (id != null) {
      	getGeneralDAO().edit(person);
      } else {
        id = getGeneralDAO().add(person);      	      	
      }
      log.debug("Person saved, id = " + id);
      getFlowCtx().finish(id);
    }
	}
  
	public void handleEventCancel(String eventParameter) throws Exception {
    log.debug("Event 'cancel' received!");
    getFlowCtx().cancel();
	}
}
