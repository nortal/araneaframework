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

package org.araneaframework.example.main.web.sample;

import org.apache.log4j.Logger;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TimeControl;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.StringData;


/**
 * Simple form component. A form with one checkbox, one textbox and a button.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 */
public class SimpleFormWidget extends TemplateBaseWidget {
  private static final Logger log = Logger.getLogger(SimpleFormWidget.class);
  
  private FormWidget simpleForm;
  
  /**
   * Builds the form with one checkbox, one textbox and a button.
   */
  protected void init() throws Exception {
	super.init();
	
	setViewSelector("sample/simpleForm");

    simpleForm = new FormWidget();
    
    FormElement el = simpleForm.createElement("#Textbox", new TextControl(), new StringData(), false);
    
    simpleForm.addElement("checkbox1", "#Checkbox", new CheckboxControl(), new BooleanData(), false);
    simpleForm.addElement("textbox1", el);
    simpleForm.addElement("button1", "#Button", new ButtonControl(), null, false);
    simpleForm.addElement("dateTime", "#DateTime", new DateTimeControl(), new DateData(), false);
    simpleForm.addElement("time", "#Time", new TimeControl(), new DateData(), false);
    simpleForm.addElement("date", "#Date", new DateControl(), new DateData(), false);
    addWidget("simpleForm", simpleForm);
    
    addGlobalEventListener(new ProxyEventListener(this));
  }
  
  /**
   * A test action.
   */
  public void handleEventTestSimpleForm() throws Exception {
    if (simpleForm.convertAndValidate()) {
    	getMessageCtx().showInfoMessage("Checkbox value is: " + ((FormElement) simpleForm.getElement("checkbox1")).getData().getValue());
    	getMessageCtx().showInfoMessage("Textbox value is: " + simpleForm.getValueByFullName("textbox1"));
    	getMessageCtx().showInfoMessage("DateTime value is: " + simpleForm.getValueByFullName("dateTime"));
    	getMessageCtx().showInfoMessage("Time value is: " + simpleForm.getValueByFullName("time"));
    	getMessageCtx().showInfoMessage("Date value is: " + simpleForm.getValueByFullName("date"));
    }
  }
  
  public void handleEventReturn(String eventParameter) throws Exception {
	  log.debug("Event 'return' received!");
	  getFlowCtx().cancel();
  }	
}
