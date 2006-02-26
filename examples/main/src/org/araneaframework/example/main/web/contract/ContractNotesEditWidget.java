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

package org.araneaframework.example.main.web.contract;

import org.apache.log4j.Logger;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.BaseWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class ContractNotesEditWidget extends BaseWidget {
	
	private static final Logger log = Logger.getLogger(ContractNotesEditWidget.class);
	private FormWidget form;
	
	public FormWidget getForm() {
		return form;
	}

	public void setForm(FormWidget form) {
		this.form = form;
	}

	public void setNotes(String notes) {
		form.setValueByFullName("notes", notes);
	}
	
	public String getNotes() {
		return (String) form.getValueByFullName("notes");
	}
	
  protected void init() throws Exception {
    super.init();
    setViewSelector("contract/contractNotesEdit");
    log.debug("TemplateContractNotesEditWidget init called");
    addGlobalEventListener(new ProxyEventListener(this));    
    
    form = new FormWidget();
    form.addElement("notes", "#Notes", new TextControl(), new StringData(), true);
    addWidget("form", form);
  }
}
