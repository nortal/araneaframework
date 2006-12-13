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

import org.apache.log4j.Logger;
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
public class CompanyAddWidget extends TemplateBaseWidget {
  private static final long serialVersionUID = 1L;
  private static final Logger log = Logger.getLogger(CompanyAddWidget.class);
  private BeanFormWidget form;

  protected void init() throws Exception {
    setViewSelector("company/companyAdd");
    putViewData("formLabel", "company.add.form.label");

    form = new BeanFormWidget(CompanyMO.class);
    form.addBeanElement("name", "#Name", new TextControl(), true);
    form.addBeanElement("address", "#Address", new TextControl(), true);

    addWidget("form", form);
  }

  public void handleEventSave(String eventParameter) throws Exception {
    if (form.convertAndValidate()) {
      CompanyMO company = new CompanyMO();
      company = (CompanyMO) form.readBean(company);

      getFlowCtx().finish(company);           
    }
  }

  public void handleEventCancel(String eventParameter) throws Exception {
    getFlowCtx().cancel();
  }
}
