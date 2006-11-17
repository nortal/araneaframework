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

package org.araneaframework.tests;

import junit.framework.TestCase;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TextareaControl;
import org.araneaframework.uilib.form.converter.StringToLongConverter;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.form.data.StringData;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class FormElementTest extends TestCase {
	public void testDataCycling() throws Exception {
		MockHttpServletRequest emptyRequest = new MockHttpServletRequest();
		emptyRequest.addParameter("myTextBox", "");

		FormElement sfe = new FormElement();
		sfe.setLabel("textbox");

		sfe._getComponent().init(new MockEnvironment());

		TextControl tb = new TextControl();
		sfe.setMandatory(true);

		sfe.setControl(tb);
		sfe.setData(new LongData());
		sfe.setConverter(new StringToLongConverter());

		sfe._getWidget().update(new StandardServletInputData(emptyRequest));
		sfe.convertAndValidate();

		sfe.getData().setValue(new Long(110));

		sfe._getWidget().process();

		assertEquals("The textbox must have the data item value!",
				((StringArrayRequestControl.ViewModel) sfe.getControl()
						._getViewable().getViewModel()).getSimpleValue(), "110");

		sfe._getComponent().destroy();
	}

	/** Tests FormWidget getElement methods(); */
	public void testGetElement() throws Exception {
		FormWidget form = new FormWidget();
		FormElement button = form.createElement("my button", new ButtonControl(), null, false);
		form.addElement("myButton", button);
		
		FormWidget subForm = form.addSubForm("subForm");
		FormElement textArea = subForm.createElement("my text area", new TextareaControl(), new StringData(), true);
		subForm.addElement("myTextarea", textArea);

		form._getComponent().init(new MockEnvironment());

		// first level formElement
		assertEquals(button, form.getElement("myButton"));
		// the subForm
		assertEquals(subForm, form.getElement("subForm"));
		// second level formElement attached to subform
		assertEquals(textArea, form.getElement("subForm.myTextarea"));

		// trying to get nonexistant element should just return null 
		assertNull(form.getElement("nonexistant"));
		assertNull(form.getElement("really.nonexistant"));
	}
}
