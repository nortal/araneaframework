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

import java.lang.reflect.Method;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.release.demos.DemoAutoCompletionWidget;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
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
 * Interactive onchange event listener test for the troublesome date/time controls 
 * and their JSP tags -- which should be buried. 
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoOnChangeListenersWidget extends TemplateBaseWidget {
	private static final long serialVersionUID = 1L;
	private FormWidget simpleForm;

	protected void init() throws Exception {
		setViewSelector("demo/demoOnChangeListeners");

		simpleForm = new FormWidget();

		simpleForm.addElement("dateTime1", "common.datetime", buildControl(DateTimeControl.class), new DateData(), false);
		simpleForm.addElement("time1", "common.time", buildControl(TimeControl.class), new DateData(), false);
		simpleForm.addElement("date1", "common.date", buildControl(DateControl.class), new DateData(), false);
		
		simpleForm.addElement("dateTime2", "common.datetime", buildControl(DateTimeControl.class), new DateData(), false);
		simpleForm.addElement("time2", "common.time", buildControl(TimeControl.class), new DateData(), false);
		simpleForm.addElement("date2", "common.date", buildControl(DateControl.class), new DateData(), false);
		
		simpleForm.addElement("suggestBox", "demo.suggestive.textinput", buildControl(AutoCompleteTextControl.class), new StringData(), false);
		((AutoCompleteTextControl)
				simpleForm.getControlByFullName("suggestBox"))
					.setDataProvider(new DemoAutoCompletionWidget.DemoACDataProvider(new DemoAutoCompletionWidget.LocalizationContextProvider() {
						public LocalizationContext getL10nCtx() {
							return DemoOnChangeListenersWidget.this.getL10nCtx();
						}
					}));
		
		simpleForm.addElement("float1", "common.float", buildControl(FloatControl.class), new BigDecimalData(), false);
		simpleForm.addElement("float2", "common.float", buildControl(FloatControl.class), new BigDecimalData(), false);

		addWidget("listenerForm", simpleForm);
	}
	
	private Control buildControl(Class clazz) throws Exception {
		Control c = (Control) clazz.newInstance();
		Method m = clazz.getMethod("addOnChangeEventListener", new Class[] {OnChangeEventListener.class});
		m.invoke(c, new Object[] {new DemoChangeEventListener(c)});
		return c;
	}

	private class DemoChangeEventListener implements OnChangeEventListener {
		private static final long serialVersionUID = 1L;
		private Control eventSource;

		public DemoChangeEventListener(Control eventSource) {
			this.eventSource = eventSource;
		}

		public void onChange() throws Exception {
			FormElement element = (FormElement) ((BaseControl) eventSource).getFormElementCtx();
			Object oldValue = element.getValue();
			simpleForm.convert();
			Object newValue = element.getValue();
			getMessageCtx().showInfoMessage("'" + t(element.getLabel()) + "' triggered onChange: '" + oldValue + "' --> '" + newValue + "'");
		}
	}
}
