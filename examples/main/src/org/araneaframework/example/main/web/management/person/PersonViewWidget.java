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

package org.araneaframework.example.main.web.management.person;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.PersonMO;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DisplayControl;
import org.araneaframework.uilib.form.control.FloatControl;

/**
 * This widget is for displaying a person. It cancels current call only.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 */
public class PersonViewWidget extends TemplateBaseWidget {

  private Long personId = null;

  /**
   * @param personId Person's Id.
   */
  public PersonViewWidget(Long id) {
    this.personId = id;
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("management/person/personView");
    PersonMO person = getGeneralDAO().getById(PersonMO.class, this.personId);
    BeanFormWidget<PersonMO> personForm = new BeanFormWidget<PersonMO>(PersonMO.class, person);

    personForm.addBeanElement("name", "common.firstname", new DisplayControl(), true);
    personForm.addBeanElement("surname", "common.lastname", new DisplayControl());
    personForm.addBeanElement("phone", "persons.address", new DisplayControl(), true);
    personForm.addBeanElement("birthdate", "persons.birthdate", new DateControl());
    personForm.addBeanElement("salary", "persons.salary", new FloatControl());

    addWidget("personForm", personForm);
  }

  public void handleEventReturn() throws Exception {
    getFlowCtx().cancel();
  }
}
