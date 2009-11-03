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

package org.araneaframework.example.main.web.demo;

import java.util.List;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.form.data.StringListData;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * Demonstrates use of a multi-select control and the use of markBaseState() and isStateChanged() methods.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoMultiSelect extends TemplateBaseWidget {

  private static final String SELECT = "multiselect";

  private FormWidget form;

  @Override
  @SuppressWarnings("unchecked")
  protected void init() throws Exception {
    setViewSelector("demo/demoMultiSelect");
    addWidget("form", createForm());

    List<DisplayItem> values = (List<DisplayItem>) this.form.getValueByFullName(SELECT);
    StringBuffer sb = new StringBuffer("At the creation, multiselect values are : ").append(values);
    getMessageCtx().showInfoMessage(sb.toString());
  }

  private FormWidget createForm() {
    MultiSelectControl<DisplayItem> control = new MultiSelectControl<DisplayItem>(DisplayItem.class, "label", "value");
    control.addItem("1", "One");
    control.addItem("2", "Two");
    control.addItem("3", "Three");
    control.addItem("4", "Four");

    this.form = new FormWidget();
    this.form.addElement(SELECT, "#multi", control, new StringListData(), false);
    this.form.markBaseState();
    return this.form;
  }

  /**
   * A test action, invoked when button is pressed. It adds the values of form elements to message context, and they end
   * up at the top of user screen at the end of the request.
   */
  public void handleEventTest() {
    this.form.convertAndValidate();
    if (this.form.isStateChanged()) {
      getMessageCtx().showInfoMessage("State of multiselect control has changed.");
    }
    getMessageCtx().showInfoMessage("Multiselect values are " + this.form.getValueByFullName(SELECT));
    this.form.markBaseState();
  }
}
