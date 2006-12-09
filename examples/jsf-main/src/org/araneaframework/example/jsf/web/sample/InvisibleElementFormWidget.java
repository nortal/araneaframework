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

package org.araneaframework.example.jsf.web.sample;

import org.araneaframework.example.jsf.TemplateBaseWidget;
import org.araneaframework.uilib.event.ProxyOnChangeEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.StringData;

public class InvisibleElementFormWidget extends TemplateBaseWidget {
  private static final long serialVersionUID = 1L;
  private FormWidget form;
  private String initialName;
  
  public InvisibleElementFormWidget() {
  }
  
  public InvisibleElementFormWidget(String initialName) {
    this.initialName = initialName;
  }
  
  protected void init() throws Exception {
    setViewSelector("sample/invisibleElementForm");
  
    form = new FormWidget();

    //Adding form controls
    form.addElement("firstName", "#First name", new TextControl(), new StringData(), initialName, true);
    form.addElement("lastName", "#Last name", new TextControl(), new StringData(), true);
    
    CheckboxControl showTitleCtl = new CheckboxControl();
    showTitleCtl.addOnChangeEventListener(new ProxyOnChangeEventListener(this, "showTitle"));
    form.addElement("showTitle", "#", showTitleCtl, new BooleanData(), false);
    
    form.addElement("title", "#Title", new TextControl(), new StringData(), false);
    
    //Putting the widget
    addWidget("form", form);    
  }

  public void handleEventShowTitle() throws Exception {
    //It's enough to convert and validate since we'll be using the element value in JSP
    form.getElementByFullName("showTitle").convertAndValidate();
  }
}
