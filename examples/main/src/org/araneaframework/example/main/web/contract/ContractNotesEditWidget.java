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

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import org.apache.log4j.Logger;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.util.DecimalPattern;

/**
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class ContractNotesEditWidget extends TemplateBaseWidget {
	
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
	
	public void setTotal(BigDecimal total) {
		form.setValueByFullName("total", total);
	}
	
	public BigDecimal getTotal() {
		return (BigDecimal) form.getValueByFullName("total");
	}
	
	protected void init() throws Exception {
		super.init();
		setViewSelector("contract/contractNotesEdit");
		log.debug("TemplateContractNotesEditWidget init called");
		
		form = new FormWidget();
		form.addElement("notes", "#Notes", new TextControl(), new StringData(), false);
		form.addElement("total", "#Total", buildFloatControl(), new BigDecimalData(), false);
		addWidget("form", form);
	}
	
	// Builds FloatControl that accepts points and commas as decimal separator.
	private FloatControl buildFloatControl() {		
		DecimalFormatSymbols symbols1 = new DecimalFormatSymbols();
		symbols1.setDecimalSeparator(',');
		DecimalPattern format1 = new DecimalPattern("0.#", symbols1);
		
		DecimalFormatSymbols symbols2 = new DecimalFormatSymbols();
		symbols2.setDecimalSeparator('.');
		DecimalPattern format2 = new DecimalPattern("0.#", symbols2);		
		
		return new FloatControl(new DecimalPattern[] {format1, format2}, format1);
	}
}
