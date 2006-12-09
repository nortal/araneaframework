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
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.form.data.StringListData;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * Demonstrates use of multiselect control and markBaseState() isStateChanged() methods.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoMultiSelect extends TemplateBaseWidget {
	  private static final long serialVersionUID = 1L;
  private FormWidget form;
	
	protected void init() throws Exception {
		setViewSelector("demo/demoMultiSelect");
		
		MultiSelectControl control = new MultiSelectControl();
		
		control.addItem(new DisplayItem("1", "One"));
		control.addItem(new DisplayItem("2", "Two"));
		control.addItem(new DisplayItem("3", "Three"));
		control.addItem(new DisplayItem("4", "Four"));
		
		form = new FormWidget();
		addWidget("form", form);
		form.addElement("multiselect", "#multi", control, new StringListData(), false);
		
		form.markBaseState();
		
		StringBuffer sb = new StringBuffer().append("At the creation, multiselect values are : ");
		sb.append(form.getValueByFullName("multiselect") != null ? form.getValueByFullName("multiselect").toString() : "null");
		getMessageCtx().showInfoMessage(sb.toString());
	}
	
	  /**
	   * A test action, invoked when button is pressed. It adds the values of 
	   * formelements to message context, and they end up at the top of user screen
	   * at the end of the request.
	   */
	  public void handleEventTest() throws Exception {
        form.convert();
        if (form.isStateChanged())
        	getMessageCtx().showInfoMessage("State of multiselect control has changed.");

	    getMessageCtx().showInfoMessage("Multiselect values are " + form.getValueByFullName("multiselect"));
	    form.markBaseState();
	  }
}
