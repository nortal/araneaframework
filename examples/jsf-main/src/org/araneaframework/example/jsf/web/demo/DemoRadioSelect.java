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

package org.araneaframework.example.jsf.web.demo;

import org.araneaframework.example.jsf.TemplateBaseWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * Demonstrates use of SelectControl rendered with radiobuttons.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoRadioSelect extends TemplateBaseWidget {
	  private static final long serialVersionUID = 1L;
  private FormWidget form;
	private SelectControl control;

	protected void init() throws Exception {
		setViewSelector("demo/demoRadioSelect");
		
		form = new FormWidget();

		control = new SelectControl();
		control.addItem(new DisplayItem("1", "First"));
		control.addItem(new DisplayItem("2", "Second"));
		control.addItem(new DisplayItem("3", "Third"));
		control.addItem(new DisplayItem("4", "Fourth"));
		control.addItem(new DisplayItem("5", "Fifth"));
		
		form.addElement("select", "#Boring number", control, new StringData(), false);
		addWidget("form", form);
	}
	
	public void handleEventTest(String param) throws Exception {
		if (form.convertAndValidate()) {
			String value = (String) form.getValueByFullName("select");
			getMessageCtx().showInfoMessage(value != null ? value : "null");
		}
	}
}
