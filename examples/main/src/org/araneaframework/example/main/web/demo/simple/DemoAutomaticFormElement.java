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

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.jsp.support.TagAttr;
import org.araneaframework.jsp.util.AutomaticFormElementUtil;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * Demonstrates {@link FormElement} rendering with JSP &lt;ui:automaticFormElement&gt; tag, which allows dynamically
 * changing the tag with which {@link FormElement} is rendered.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoAutomaticFormElement extends TemplateBaseWidget {

  private FormWidget form;

  private FormElement<String, String> first;

  private boolean editable = false;

  @Override
  protected void init() throws Exception {
    setViewSelector("demo/demoAutomaticFormElement");
    this.form = new FormWidget();
    this.first = this.form.addElement("first", "#First", new TextControl(), new StringData(), "InitialFirst");
    changeFormTags();
    addWidget("form", this.form);
  }

  public void handleEventReverse() {
    changeFormTags();
  }

  private void changeFormTags() {
    this.editable = !this.editable;
    if (!this.editable) {
      AutomaticFormElementUtil.setFormElementTag(first, "textInputDisplay", new TagAttr("styleClass", "name"));
    } else {
      AutomaticFormElementUtil.setFormElementTag(first, "textInput", new TagAttr("styleClass", "inpt"));
    }
  }
}
