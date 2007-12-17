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

import java.util.Date;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.event.ProxyOnClickEventListener;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.AfterTodayConstraint;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.constraint.ReverseConstraint;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TimeControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.StringData;


/**
 * Simple form component. A form with one checkbox, one textbox and 
 * three kinds of different timeinputs (DateInput, Timeinput and 
 * DateTimeInput) and a button.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class SimpleFormWidget extends TemplateBaseWidget {
  private static final long serialVersionUID = 1L;
  private FormWidget simpleForm;
  
  /**
   * Builds the form.
   */
  protected void init() throws Exception {
	setViewSelector("sample/simpleForm");

    // creation of new form
	simpleForm = new FormWidget();
    
	// Now that we have created a form, we will need to add form elements.
	// form elements consist of four basic things - label, Control that implements
	// form element functionality and Data holding values that form element can have.
	// Note that the first sample with FormWidget's createElement method is not the
	// way form elements are usually added to the form, but rather emphasises the
	// fact that everything you add to FormWidget is a FormElement.

	// createElement(String labelId, Control control, Data data, boolean mandatory)
    FormElement el = simpleForm.createElement("common.Textbox", new TextControl(), new StringData(), false);
    simpleForm.addElement("textbox1", el);
    
    // and here we add form elements to form without the extra step taken previously. 
    simpleForm.addElement("checkbox1", "Checkbox", new CheckboxControl(), new BooleanData(), false);
    simpleForm.addElement("dateTime", "common.datetime", new DateTimeControl(), new DateData(), false);
    simpleForm.addElement("time", "common.time", new TimeControl(), new DateData(), false);
    simpleForm.addElement("date", "common.date", new DateControl(), new DateData(), false);
    simpleForm.addElement("number", "common.float", new FloatControl(), new BigDecimalData(), false);
    // require the number input field to be filled. It could have been achieved already
    // on formelement creation by setting mandatory attribute to true
    simpleForm.getElement("number").setConstraint(new NotEmptyConstraint());
    // sets initial value of form element
    simpleForm.setValueByFullName("dateTime", new Date());
    
    simpleForm.getElementByFullName("date").setConstraint(new ReverseConstraint(new AfterTodayConstraint(false), "dadadadad"));

	// now we construct a button, that is also Control. Reason why we cannot just add it
    // to form is obvious, we want to add a specific listener to button before.
    ButtonControl button = new ButtonControl();
	button.addOnClickEventListener(new ProxyOnClickEventListener(this, "testSimpleForm"));
	// add the button to form. As the button does not hold any value, Data will be null.
	simpleForm.addElement("button", "#Button", button, null, false);
    
    // the usual, add the created widget to main widget.
	addWidget("simpleForm", simpleForm);
  }

  /**
   * A test action, invoked when button is pressed. It adds the values of 
   * formelements to message context, and they end up at the top of user screen
   * at the end of the request.
   */
  public void handleEventTestSimpleForm() throws Exception {
    // if form is not invalid, do not try to show form element values 
    // (error messages are added automatically to the messagecontext 
    // though, user will not be without feedback)
    if (simpleForm.convertAndValidate()) {
    	// long way to check form element value ...
    	getMessageCtx().showInfoMessage("Checkbox value is: " + ((FormElement) simpleForm.getElement("checkbox1")).getData().getValue());
    	// and a shorter one
    	getMessageCtx().showInfoMessage("Textbox value is: " + simpleForm.getValueByFullName("textbox1"));
    	getMessageCtx().showInfoMessage("DateTime value is: " + simpleForm.getValueByFullName("dateTime"));
    	getMessageCtx().showInfoMessage("Time value is: " + simpleForm.getValueByFullName("time"));
    	getMessageCtx().showInfoMessage("Date value is: " + simpleForm.getValueByFullName("date"));
    	getMessageCtx().showInfoMessage("Number value is: " + simpleForm.getValueByFullName("number"));
    }
  }
}