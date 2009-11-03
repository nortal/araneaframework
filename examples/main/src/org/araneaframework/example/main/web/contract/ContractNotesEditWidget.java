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

package org.araneaframework.example.main.web.contract;

import java.math.BigDecimal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Rein Raudjärv (rein@araneaframework.org)
 */
public class ContractNotesEditWidget extends TemplateBaseWidget {

  private static final Log LOG = LogFactory.getLog(ContractNotesEditWidget.class);

  private FormWidget form;

  public FormWidget getForm() {
    return this.form;
  }

  public void setForm(FormWidget form) {
    this.form = form;
  }

  public void setNotes(String notes) {
    this.form.setValueByFullName("notes", notes);
  }

  public String getNotes() {
    return (String) this.form.getValueByFullName("notes");
  }

  public void setTotal(BigDecimal total) {
    this.form.setValueByFullName("total", total);
  }

  public BigDecimal getTotal() {
    return (BigDecimal) this.form.getValueByFullName("total");
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("contract/contractNotesEdit");
    LOG.debug("TemplateContractNotesEditWidget init called");

    this.form = new FormWidget();
    this.form.addElement("notes", "#Notes", new TextControl(), new StringData());
    this.form.addElement("total", "#Total", new FloatControl(), new BigDecimalData());
    addWidget("form", this.form);
  }
}
