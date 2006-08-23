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
import org.araneaframework.Widget;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.tests.mock.MockUiLibUtil;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TextareaControl;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.support.DisplayItem;
import org.springframework.mock.web.MockHttpServletRequest;


/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class WidgetTest extends TestCase {
  boolean eventsWork = false;

  private FormWidget makeUsualForm() throws Exception {

    //Creating form :-)
    FormWidget testForm = new FormWidget();
    
    //Adding elements to form
    testForm.addElement("myCheckBox", "my checkbox", new CheckboxControl(), new BooleanData(), true);
    testForm.addElement("myLongText", "my long text", new TextControl(), new LongData(), true);
    testForm.addElement("myDateTime", "my date and time", new DateTimeControl(), new DateData(), false);
    testForm.addElement("myButton", "my button", new ButtonControl(), null, false);

    //Adding a composite element
    FormWidget hierarchyTest = testForm.addSubForm("hierarchyTest");
    hierarchyTest.addElement("myTextarea", "my text area", new TextareaControl(), new StringData(), true);

    //Filling in select control (which is under a composite element)
    FormElement mySelectElement = hierarchyTest.addElement("mySelect", "my drop down", new SelectControl(), new LongData(), true);
    SelectControl mySelect = (SelectControl) mySelectElement.getControl();
    mySelect.addItem(new DisplayItem("1", "one"));
    mySelect.addItem(new DisplayItem("2", "two"));
    mySelect.addItem(new DisplayItem("3", "three"));
    mySelect.addItem(new DisplayItem("4", "four"));
    
    testForm._getComponent().init(new MockEnvironment());

    return testForm;
  }

  /**
   * Testing reading from valid request.
   */
  public void testFormRequestHandling() throws Exception {
    FormWidget testForm = makeUsualForm();
    Widget currentWidget = testForm;
    MockHttpServletRequest validRequest = new MockHttpServletRequest();

    validRequest.addParameter("testForm.myCheckBox", "true");
    ((FormElement)testForm.getElement("myCheckBox")).rendered();

    validRequest.addParameter("testForm.myLongText", "108");
    ((FormElement)testForm.getElement("myLongText")).rendered();
    
    ((FormElement)testForm.getElement("myDateTime")).rendered();
    validRequest.addParameter("testForm.myDateTime.date", "11.10.2015");
    validRequest.addParameter("testForm.myDateTime.time", "01:01");
    
    (((FormElement)((FormWidget)testForm.getElement("hierarchyTest")).getElement("myTextarea"))).rendered();
    validRequest.addParameter("testForm.hierarchyTest.myTextarea", "blah");
    (((FormElement)((FormWidget)testForm.getElement("hierarchyTest")).getElement("mySelect"))).rendered();
    validRequest.addParameter("testForm.hierarchyTest.mySelect", "2");
    
    MockUiLibUtil.emulateHandleRequest(currentWidget, "testForm", validRequest);
    currentWidget._getWidget().process();

    assertTrue(((StringArrayRequestControl.ViewModel) testForm.getControlByFullName("myCheckBox")._getViewable().getViewModel()).getSimpleValue().equals("true"));
    assertTrue(((StringArrayRequestControl.ViewModel) testForm.getControlByFullName("myLongText")._getViewable().getViewModel()).getSimpleValue().equals("108"));
    assertTrue(((StringArrayRequestControl.ViewModel) testForm.getControlByFullName("hierarchyTest.myTextarea")._getViewable().getViewModel()).getSimpleValue().equals("blah"));
    assertTrue(((StringArrayRequestControl.ViewModel) testForm.getControlByFullName("hierarchyTest.mySelect")._getViewable().getViewModel()).getSimpleValue().equals("2"));     
  }
}
