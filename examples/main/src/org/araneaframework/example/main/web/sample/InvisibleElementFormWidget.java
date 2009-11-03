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

package org.araneaframework.example.main.web.sample;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.event.ProxyOnChangeEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.StringData;

public class InvisibleElementFormWidget extends TemplateBaseWidget {

  private FormWidget form;

  private String initialName;

  public InvisibleElementFormWidget() {}

  public InvisibleElementFormWidget(String initialName) {
    this.initialName = initialName;
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("sample/invisibleElementForm");

    // Creating form and adding form controls
    this.form = new FormWidget();
    this.form.addElement("firstName", "#First name", new TextControl(), new StringData(), this.initialName, true);
    this.form.addElement("lastName", "#Last name", new TextControl(), new StringData(), true);
    this.form.addElement("showTitle", "#", createCheckbox(), new BooleanData(), false);
    this.form.addElement("title", "#Title", new TextControl(), new StringData(), false);
    addWidget("form", this.form); // Adds the form as sub-widget that can be accessed from JSP.
  }

  private CheckboxControl createCheckbox() {
    CheckboxControl showTitleCtl = new CheckboxControl();
    showTitleCtl.addOnChangeEventListener(new ProxyOnChangeEventListener(this, "showTitle"));
    return showTitleCtl;
  }

  public void handleEventShowTitle() throws Exception {
    // It's enough to convert and validate since we'll be using the element value in JSP
    this.form.getElementByFullName("showTitle").convertAndValidate();
  }
}
