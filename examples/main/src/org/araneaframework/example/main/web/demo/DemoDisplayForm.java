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

import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.BaseWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.DisplayControl;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.form.data.StringData;


/**
 * Simple form component. A form with one checkbox, one textbox and a button.
 *
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class DemoDisplayForm extends BaseWidget {

	private FormWidget displayForm;

	/**
	 * Builds the form with one checkbox, one textbox and a button.
	 */
	public void init() throws Exception {
		super.init();
		
		addGlobalEventListener(new ProxyEventListener(this));
    setViewSelector("demo/DemoDisplayForm/main");
		
		displayForm = new FormWidget();

		displayForm.addElement("condDisplay", "#Condition", new DisplayControl(), new BooleanData(), false);
		displayForm.addElement("textDisplay", "#Text", new DisplayControl(), new StringData(), false);
		displayForm.addElement("valueDisplay", "#Value", new DisplayControl(), new LongData(), false);

		displayForm.setValueByFullName("condDisplay", Boolean.TRUE);
		displayForm.setValueByFullName("textDisplay", "Test string");
		displayForm.setValueByFullName("valueDisplay", new Long(11));

		addWidget("displayForm", displayForm);
	}
	
	public void handleEventReturn(String param) throws Exception {
		getFlowCtx().cancel();
	}
}
