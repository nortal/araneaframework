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

package org.araneaframework.example.main.web.demo.simple;

import java.util.Date;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.event.ProxyOnClickEventListener;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TextareaControl;
import org.araneaframework.uilib.form.control.TimeControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.StringData;

/**
 * Simple form component. A form with one checkbox, one textbox and three kinds of different TimeInputs (DateInput,
 * TimeInput and DateTimeInput) and a button.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class SimpleFormWidget extends TemplateBaseWidget {

  private FormWidget simpleForm;

  /**
   * Builds a simple form with some controls of different data types.
   */
  @Override
  protected void init() throws Exception {
    setViewSelector("demo/simple/simpleForm");

    // creation of new form
    this.simpleForm = new FormWidget();

    // Now that we have created a form, we will need to add form elements. form elements consist of four basic things -
    // label, Control that implements form element functionality and Data holding values that form element can have.
    // Note that the first sample with FormWidget's createElement method is not the way form elements are usually added
    // to the form, but rather emphasizes the fact that everything you add to FormWidget is a FormElement.

    // createElement(String labelId, Control control, Data data, boolean mandatory)
    FormElement<String, String> el = this.simpleForm.createElement("common.Textbox", new TextControl(), new StringData());
    this.simpleForm.addElement("textbox1", el);

    // and here we add form elements to form without the extra step taken previously.
    this.simpleForm.addElement("checkbox1", "common.Checkbox", new CheckboxControl(), new BooleanData(), false);
    this.simpleForm.addElement("dateTime", "common.datetime", new DateTimeControl(), new DateData(), false);
    this.simpleForm.addElement("time", "common.time", new TimeControl(), new DateData(), false);
    this.simpleForm.addElement("date", "common.date", new DateControl(), new DateData(), false);
    this.simpleForm.addElement("number", "common.float", new FloatControl(), new BigDecimalData(), false);

    // require the number input field to be filled. It could have been achieved already on form element creation by
    // setting mandatory attribute to true
    this.simpleForm.getElement("number").setConstraint(new NotEmptyConstraint<Object, Object>());

    // sets initial value of form element
    this.simpleForm.setValueByFullName("dateTime", new Date());

    // here are two controls that are either disabled or read-only:
    this.simpleForm.addElement("disabledCtrl", "common.disabled", new TextareaControl(), new StringData(),
        t("common.disabled"), false).setDisabled(true);
    this.simpleForm.addElement("readOnlyCtrl", "common.readOnly", new TextControl(), new StringData(),
        t("common.readOnly"), false).setDisabled(true);

    // Now we construct a button, that is also Control. Reason why we cannot just add it to form is obvious, we want to
    // add a specific listener to button before.
    ButtonControl button = new ButtonControl(new ProxyOnClickEventListener(this, "testSimpleForm"));
    // Add the button to form. As the button does not hold any value, Data will be null.
    this.simpleForm.addElement("button", "common.Button", button);

    // The usual, add the created widget to main widget.
    addWidget("simpleForm", this.simpleForm);

  }

  /**
   * A test action, invoked when button is pressed. It adds the values of formelements to message context, and they end
   * up at the top of user screen at the end of the request.
   */
  public void handleEventTestSimpleForm() throws Exception {
    // if form is not invalid, do not try to show form element values (error messages are added automatically to the
    // message context though, user will not be without feedback).
    if (this.simpleForm.convertAndValidate()) {
      showMsg(this.simpleForm.getElementByFullName("checkbox1"));
      showMsg(this.simpleForm.getElementByFullName("textbox1"));
      showMsg(this.simpleForm.getElementByFullName("dateTime"));
      showMsg(this.simpleForm.getElementByFullName("time"));
      showMsg(this.simpleForm.getElementByFullName("date"));
      showMsg(this.simpleForm.getElementByFullName("number"));
    }
  }

  private void showMsg(FormElement<?, Object> element) {
    getMessageCtx().showInfoMessage("simpleForm.msg", t(element.getLabel()), element.getValue());
  }
}
