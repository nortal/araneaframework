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

package org.araneaframework.tests;

import junit.framework.TestCase;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardScope;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.tests.mock.MockUiLibUtil;
import org.araneaframework.tests.util.RequestUtil;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.DefaultSelectControl;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TextareaControl;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.form.data.StringData;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 */
public class WidgetTest extends TestCase {

  private FormWidget makeUsualForm() throws Exception {

    // Creating form :-)
    FormWidget testForm = new FormWidget();

    // Adding elements to form
    testForm.addElement("myCheckBox", "my checkbox", new CheckboxControl(), new BooleanData(), true);
    testForm.addElement("myLongText", "my long text", new TextControl(), new LongData(), true);
    testForm.addElement("myDateTime", "my date and time", new DateTimeControl(), new DateData(), false);
    testForm.addElement("myButton", "my button", new ButtonControl(), null, false);

    // Adding a composite element
    FormWidget hierarchyTest = testForm.addSubForm("hierarchyTest");
    hierarchyTest.addElement("myTextarea", "my text area", new TextareaControl(), new StringData(), true);

    // Filling in select control (which is under a composite element)
    DefaultSelectControl mySelect = new DefaultSelectControl();
    mySelect.addItem("one", "1");
    mySelect.addItem("two", "2");
    mySelect.addItem("three", "3");
    mySelect.addItem("four", "4");
    hierarchyTest.addElement("mySelect", "my drop down", mySelect, new LongData(), true);

    testForm._getComponent().init(new StandardScope("testForm", null), new MockEnvironment());

    return testForm;
  }

  /**
   * Testing reading from valid request.
   */
  public void testFormRequestHandling() throws Exception {
    FormWidget testForm = makeUsualForm();
    Widget currentWidget = testForm;
    MockHttpServletRequest validRequest = RequestUtil.markSubmitted(new MockHttpServletRequest());

    validRequest.addParameter("testForm.myCheckBox", "true");
    setRendered(testForm, "myCheckBox");

    validRequest.addParameter("testForm.myLongText", "108");
    setRendered(testForm, "myLongText");

    setRendered(testForm, "myDateTime");
    validRequest.addParameter("testForm.myDateTime.date", "11.10.2015");
    validRequest.addParameter("testForm.myDateTime.time", "01:01");

    setRendered(testForm, "myDateTime");
    setRendered(testForm, "hierarchyTest", "myTextarea");
    validRequest.addParameter("testForm.hierarchyTest.myTextarea", "blah");
    setRendered(testForm, "hierarchyTest", "mySelect");
    validRequest.addParameter("testForm.hierarchyTest.mySelect", "2");

    MockUiLibUtil.emulateHandleRequest(currentWidget, "testForm", validRequest);

    assertTrue(equals(testForm, "myCheckBox", "true"));
    assertTrue(equals(testForm, "myLongText", "108"));
    assertTrue(equals(testForm, "hierarchyTest.myTextarea", "blah"));
    assertTrue(equals(testForm, "hierarchyTest.mySelect", "2"));
  }

  @SuppressWarnings("unchecked")
  private void setRendered(FormWidget form, String element) {
    ((FormElement) form.getElement(element)).rendered();
  }

  @SuppressWarnings("unchecked")
  private void setRendered(FormWidget form, String element, String element2) {
    (((FormElement) ((FormWidget) form.getElement(element)).getElement(element2))).rendered();
  }

  @SuppressWarnings("unchecked")
  private boolean equals(FormWidget form, String element, Object value) {
    return ((StringArrayRequestControl.ViewModel) form.getControlByFullName(element)._getViewable().getViewModel())
        .getSimpleValue().equals(value);
  }
}
