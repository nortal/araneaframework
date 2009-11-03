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

package org.araneaframework.example.main.web.company;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.CompanyMO;

/**
 * This widget is for displaying a company. It only cancels current call when finishing.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>*
 */
public class CompanyViewWidget extends TemplateBaseWidget {

  private static final Log LOG = LogFactory.getLog(CompanyViewWidget.class);

  private Long id = null;

  /**
   * @param id Company's Id.
   */
  public CompanyViewWidget(Long id) {
    this.id = id;
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("company/companyView");
    LOG.debug("TemplateCompanyViewWidget init called");
    putViewData("company", getCompanyDAO().getById(CompanyMO.class, id));
  }

  public void handleEventReturn() throws Exception {
    getFlowCtx().cancel();
  }
}
