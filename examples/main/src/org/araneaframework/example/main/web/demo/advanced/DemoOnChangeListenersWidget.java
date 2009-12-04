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

package org.araneaframework.example.main.web.demo.advanced;

import org.apache.commons.beanutils.MethodUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.release.DemoInputSuggestWidget;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElementContext;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.AutoCompleteTextControl;
import org.araneaframework.uilib.form.control.BaseControl;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.TimeControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.StringData;

/**
 * Interactive "onchange" event listener test for the troublesome date/time controls and their JSP tags -- which should
 * be buried.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoOnChangeListenersWidget extends TemplateBaseWidget {

  private FormWidget simpleForm;

  @Override
  protected void init() throws Exception {
    setViewSelector("demo/advanced/onChangeListeners");

    this.simpleForm = new FormWidget();
    this.simpleForm.addElement("dateTime1", "common.datetime", buildControl(DateTimeControl.class), new DateData());
    this.simpleForm.addElement("time1", "common.time", buildControl(TimeControl.class), new DateData());
    this.simpleForm.addElement("date1", "common.date", buildControl(DateControl.class), new DateData());
    this.simpleForm.addElement("dateTime2", "common.datetime", buildControl(DateTimeControl.class), new DateData());
    this.simpleForm.addElement("time2", "common.time", buildControl(TimeControl.class), new DateData());
    this.simpleForm.addElement("date2", "common.date", buildControl(DateControl.class), new DateData());
    this.simpleForm.addElement("suggestBox", "onChangeListener.autocomplete",
        buildControl(AutoCompleteTextControl.class), new StringData());
    this.simpleForm.addElement("float1", "common.float", buildControl(FloatControl.class), new BigDecimalData());
    this.simpleForm.addElement("float2", "common.float", buildControl(FloatControl.class), new BigDecimalData());

    AutoCompleteTextControl control = (AutoCompleteTextControl) this.simpleForm.getControlByFullName("suggestBox");
    control.setDataProvider(new DemoInputSuggestWidget.DemoACDataProvider(
        new DemoInputSuggestWidget.LocalizationContextProvider() {

          public LocalizationContext getL10nCtx() {
            return DemoOnChangeListenersWidget.this.getL10nCtx();
          }
        }));

    addWidget("listenerForm", this.simpleForm);
  }

  @SuppressWarnings("unchecked")
  private <T> Control<T> buildControl(Class<T> clazz) throws Exception {
    Control<T> c = (Control<T>) clazz.newInstance();
    MethodUtils.invokeMethod(c, "addOnChangeEventListener", new DemoChangeEventListener(c));
    return c;
  }

  private class DemoChangeEventListener<T> implements OnChangeEventListener {

    private Control<T> eventSource;

    public DemoChangeEventListener(Control<T> eventSource) {
      this.eventSource = eventSource;
    }

    public void onChange() throws Exception {
      FormElementContext<T, Object> element = ((BaseControl<T>) this.eventSource).getFormElementCtx();
      Object oldValue = element.getValue();
      simpleForm.convert();
      Object newValue = element.getValue();
      getMessageCtx().showInfoMessage("onChangeListener.msg", t(element.getLabel()), oldValue, newValue);
    }
  }
}
