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

package org.araneaframework.example.main.web.company;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.CompanyMO;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.TextControl;

/**
 * This widget is for adding new and editing existing companies.
 * It retruns the Id of stored company or cancels current call. 
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class CompanyEditWidget extends TemplateBaseWidget {
  private static final long serialVersionUID = 1L;
  private static final Log log = LogFactory.getLog(CompanyEditWidget.class);
  private Long id = null;
  private BeanFormWidget form;

  /**
   * Constructor for adding new company. 
   */
  public CompanyEditWidget() {}

  /**
   * Constructor for editing existing company with specified Id.
   * @param id Company's Id.
   */
  public CompanyEditWidget(Long id) {
    this.id = id;
  }

  protected void init() throws Exception {
    setViewSelector("company/companyAddEdit");
    putViewData("formLabel", id != null ? "company.edit.form.label" : "company.add.form.label");
    log.debug("CompanyEditWidget init called");

    form = new BeanFormWidget(CompanyMO.class);
    form.addBeanElement("name", "#Name", new TextControl(), true);
    form.addBeanElement("address", "#Address", new TextControl(), true);

    if (id != null) {
      CompanyMO company = (CompanyMO) getGeneralDAO().getById(CompanyMO.class, id);   
      form.readFromBean(company);
    }

    addWidget("form", form);
  }

  public void handleEventSave() throws Exception {
    if (form.convertAndValidate()) {
      CompanyMO company = id != null ? (CompanyMO) getGeneralDAO().getById(CompanyMO.class, id) : new CompanyMO();

      company = (CompanyMO) form.writeToBean(company);

      if (id != null) {
        getGeneralDAO().edit(company);
      } else {
        id = getGeneralDAO().add(company);                
      }
      log.debug("Company saved, id = " + id);
      getFlowCtx().finish(id);
    }
  }

  public void handleEventCancel() throws Exception {
    getFlowCtx().cancel();
  }
}
