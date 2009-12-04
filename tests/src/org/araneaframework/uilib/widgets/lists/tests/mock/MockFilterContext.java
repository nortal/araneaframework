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

package org.araneaframework.uilib.widgets.lists.tests.mock;

import java.util.Comparator;
import java.util.Locale;
import org.araneaframework.tests.mock.MockConfiguration;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.list.structure.filter.FilterContext;
import org.araneaframework.uilib.list.util.ComparatorFactory;
import org.araneaframework.uilib.util.Event;

public class MockFilterContext implements FilterContext {

  private FormWidget form = new FormWidget();

  private ConfigurationContext configuration = new MockConfiguration();

  public FormWidget getForm() {
    return this.form;
  }

  public boolean isStrict() {
    return false;
  }

  public ConfigurationContext getConfiguration() {
    return this.configuration;
  }

  public Comparator<?> getFieldComparator(String fieldId) {
    return ComparatorFactory.getDefault();
  }

  public String getFieldLabel(String fieldId) {
    return null;
  }

  public Class<?> getFieldType(String fieldId) {
    return null;
  }

  public Locale getLocale() {
    return Locale.getDefault();
  }

  public boolean isIgnoreCase() {
    return true;
  }

  public void setLabelForElement(FormElement<?, ?> formElement, String fieldId) {
    formElement.setLabel(fieldId);
  }

  public void addInitEvent(Event event) {
    event.run();
  }

}
