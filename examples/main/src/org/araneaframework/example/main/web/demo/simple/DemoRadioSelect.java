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
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.DefaultSelectControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * Demonstrates use of SelectControl rendered with radio-buttons.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoRadioSelect extends TemplateBaseWidget {

  private FormWidget form;

  private DefaultSelectControl control;

  @Override
  protected void init() throws Exception {
    setViewSelector("demo/simple/radioSelect");

    this.control = new DefaultSelectControl();
    this.control.addItem("select.one", "1");
    this.control.addItem("select.two", "2");
    this.control.addItem("select.three", "3");
    this.control.addItem("select.four", "4");
    this.control.addItem("select.five", "5");

    this.form = new FormWidget();
    this.form.addElement("select", "radioselect.label", this.control, new StringData());
    addWidget("form", this.form);
  }

  public void handleEventTest() throws Exception {
    if (this.form.convertAndValidate()) {
      String value = (String) this.form.getValueByFullName("select");
      getMessageCtx().showInfoMessage("radioselect.values.msg", value != null ? value : "null");
    }
  }
}
