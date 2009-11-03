/*
 * Copyright 2007 Webmedia Group Ltd.
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

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.inputfilter.InputFilter;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class FilteredInputDemoWidget extends TemplateBaseWidget {

  private FormWidget form;

  /**
   * Builds the form.
   */
  @Override
  protected void init() throws Exception {
    setViewSelector("demo/filteredInput");

    // creation of new form
    this.form = new FormWidget();

    final InputFilter inputFilter = new InputFilter();
    final TextControl filter = new TextControl();
    final TextControl filtered = new TextControl();

    filtered.setInputFilter(inputFilter);
    filter.addOnChangeEventListener(new OnChangeEventListener() {

      public void onChange() throws Exception {
        form.convert();
        inputFilter.setCharacterFilter((String) form.getValueByFullName("filter"));
        form.setValueByFullName("filtered", null);
      }
    });

    this.form.addElement("filter", "demo.filteredinput.filterchars", filter, new StringData());
    this.form.addElement("filtered", "demo.filteredinput.input", filtered, new StringData());

    // the usual, add the created widget to main widget.
    addWidget("form", form);
  }
}
