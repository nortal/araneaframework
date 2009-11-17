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

package org.araneaframework.example.main.web.release;

import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.time.DateUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.core.OverlayRootWidget;
import org.araneaframework.uilib.event.ProxyOnClickEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.FileUploadControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.FileInfoData;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoModalDialogWidget extends TemplateBaseWidget {

  private FormWidget form;

  private boolean nested;

  public DemoModalDialogWidget() {}

  private DemoModalDialogWidget(boolean isNested) {
    this.nested = isNested;
  }

  /**
   * Builds the form.
   */
  @Override
  protected void init() throws Exception {
    setViewSelector("release/demos/modalDialog");
    addWidget("form", buildForm());
  }

  private FormWidget buildForm() {
    this.form = new FormWidget();
    this.form.addElement("textbox1", form.createElement("common.Textbox", new TextControl(), new StringData()));
    this.form.addElement("checkbox1", "common.Checkbox", new CheckboxControl(), new BooleanData());
    this.form.addElement("dateTime", "common.datetime", new DateTimeControl(), new DateData());
    this.form.addElement("date", "common.date", new DateControl(), new DateData());
    this.form.addElement("number", "common.float", new FloatControl(), new BigDecimalData());
    this.form.addElement("upload", "common.file", new FileUploadControl(), new FileInfoData());

    // Require the number input field to be filled. It could have been achieved
    // already on form element creation by setting mandatory attribute to true.
    this.form.getElement("number").setConstraint(new NotEmptyConstraint<Object, Object>());

    // Sets initial value of the form element:
    this.form.setValueByFullName("dateTime", DateUtils.truncate(new Date(), Calendar.MINUTE));

    // Now we construct a button, that is also Control. Reason why we cannot just add it to form is obvious, we want to
    // add a specific listener to the button before:
    ButtonControl button = new ButtonControl();
    button.addOnClickEventListener(new ProxyOnClickEventListener(this, "testSimpleForm"));

    // Add the button to form. As the button does not hold any value, Data will be null.
    this.form.addElement("button", "button.submit", button);
    return this.form;
  }

  public void handleEventTestSimpleForm() throws Exception {
    this.form.convertAndValidate();
  }

  public void handleEventNextFlowOverlay() throws Exception {
    getOverlayCtx().start(new OverlayRootWidget(new DemoModalDialogWidget(true)));
  }

  public void handleEventNextFlow() throws Exception {
    getFlowCtx().start(new DemoModalDialogWidget(true));
  }

  public void handleEventReturn() throws Exception {
    if (isNested()) {
      getFlowCtx().cancel();
    }
  }

  public void handleEventClose() throws Exception {
    if (isOverlay()) {
      getOverlayCtx().cancel();
    }
  }

  public boolean isNested() {
    return this.nested;
  }

  public boolean isOverlay() {
    return getOverlayCtx().isOverlayActive();
  }

}
