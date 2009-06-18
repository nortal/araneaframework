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

package org.araneaframework.example.main.web.demo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.time.DateUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.event.ProxyOnClickEventListener;
import org.araneaframework.uilib.flowcontext.transitionhandler.CancelConfirmingTransitionHandler;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
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
 * Demonstrates the basic usage of {@link FlowEventConfirmationContext} by registering a cancel()
 * precondition and appropriate doConfirm closure. onConfirm is left undefined, b/c defined doConfirm 
 * does not interact with application end-user.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoFlowEventConfirmationWidget extends TemplateBaseWidget {
  private static final long serialVersionUID = 1L;
  private FormWidget form;
  private boolean nested = false;

  public DemoFlowEventConfirmationWidget() {}

  private DemoFlowEventConfirmationWidget(boolean isNested) {
    this.nested = isNested;
  }

  private void registerCancelConfirmationHandler() {
    getFlowCtx().setTransitionHandler(new CancelConfirmingTransitionHandler(new UnsavedFlowDataPredicate(), "Form contains unsaved data. Continue?", true));
  }

  /**
   * Builds the form.
   */
  protected void init() throws Exception {
    setViewSelector("demo/flowEventConfirm");
    
    registerCancelConfirmationHandler();

    form = new FormWidget();

    FormElement el = form.createElement("common.Textbox", new TextControl(), new StringData(), false);
    form.addElement("textbox1", el);

    form.addElement("checkbox1", "Checkbox", new CheckboxControl(), new BooleanData(), false);
    form.addElement("dateTime", "common.datetime", new DateTimeControl(), new DateData(), false);
    form.addElement("time", "common.time", new TimeControl(), new DateData(), false);
    form.addElement("date", "common.date", new DateControl(), new DateData(), false);
    form.addElement("number", "common.float", new FloatControl(), new BigDecimalData(), false);
    // require the number input field to be filled. It could have been achieved already
    // on formelement creation by setting mandatory attribute to true
    form.getElement("number").setConstraint(new NotEmptyConstraint());
    // sets initial value of form element

    form.setValueByFullName("dateTime", DateUtils.truncate(new Date(), Calendar.MINUTE));

    // now we construct a button, that is also Control. Reason why we cannot just add it
    // to form is obvious, we want to add a specific listener to button before.
    ButtonControl button = new ButtonControl();
    button.addOnClickEventListener(new ProxyOnClickEventListener(this, "testSimpleForm"));
    // add the button to form. As the button does not hold any value, Data will be null.
    form.addElement("button", "common.Submit", button, null, false);

    form.markBaseState();
    
    // the usual, add the created widget to main widget.
    addWidget("form", form);
  }

  public void handleEventTestSimpleForm() throws Exception {
    form.convertAndValidate();
    form.markBaseState();
  }

  public void handleEventNextFlow() throws Exception {
    getFlowCtx().start(new DemoFlowEventConfirmationWidget(true));
  }

  public void handleEventReturn() throws Exception {
    if (isNested())
      getFlowCtx().cancel();
  }

  public boolean isNested() {
    return nested;
  }
  
  private class UnsavedFlowDataPredicate implements Predicate, Serializable {
    private static final long serialVersionUID = 1L;

    public boolean evaluate(Object obj) {
      form.convert();
      return form.isStateChanged();
    }
  }
}
