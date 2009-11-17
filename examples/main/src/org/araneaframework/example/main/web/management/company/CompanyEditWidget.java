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

package org.araneaframework.example.main.web.management.company;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.CompanyMO;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.TextControl;

/**
 * This widget is for adding new and editing existing companies. It retruns the Id of stored company or cancels current
 * call.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class CompanyEditWidget extends TemplateBaseWidget {

  private static final Log LOG = LogFactory.getLog(CompanyEditWidget.class);

  private Long id = null;

  private BeanFormWidget<CompanyMO> form;

  /**
   * Constructor for adding new company.
   */
  public CompanyEditWidget() {}

  /**
   * Constructor for editing existing company with specified ID.
   * 
   * @param id Existing company's ID.
   */
  public CompanyEditWidget(Long id) {
    this.id = id;
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("company/companyAddEdit");
    putViewData("formLabel", this.id != null ? "company.edit.form.label" : "company.add.form.label");
    LOG.debug("CompanyEditWidget init called");

    CompanyMO company = this.id != null ? getCompanyDAO().getById(CompanyMO.class, this.id) : new CompanyMO();

    this.form = new BeanFormWidget<CompanyMO>(CompanyMO.class, company);
    this.form.addBeanElement("name", "#Name", new TextControl(), true);
    this.form.addBeanElement("address", "#Address", new TextControl(), true);
    addWidget("form", this.form);
  }

  public void handleEventSave() throws Exception {
    if (this.form.convertAndValidate()) {
      CompanyMO company = this.form.writeToBean();

      if (this.id != null) {
        getCompanyDAO().edit(company);
      } else {
        this.id = getCompanyDAO().add(company);
      }
      LOG.debug("Company saved, id = " + this.id);
      getFlowCtx().finish(this.id);
    }
  }

  public void handleEventCancel() throws Exception {
    getFlowCtx().cancel();
  }
}
