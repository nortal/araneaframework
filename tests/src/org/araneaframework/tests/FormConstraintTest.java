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

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import junit.framework.TestCase;
import org.araneaframework.servlet.core.StandardServletInputData;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.tests.mock.MockUiLibUtil;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.AfterTodayConstraint;
import org.araneaframework.uilib.form.constraint.ConstraintGroupHelper;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.constraint.NumberInRangeConstraint;
import org.araneaframework.uilib.form.constraint.OptionalConstraint;
import org.araneaframework.uilib.form.constraint.RangeConstraint;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.LongData;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class FormConstraintTest extends TestCase {
  boolean eventsWork = false;
  
  public FormConstraintTest(String name) {
    super(name);
  }

  private FormWidget makeUsualForm() throws Exception {

    //Creating form :-)
    FormWidget testForm = new FormWidget();
    testForm._getComponent().init(new MockEnvironment());
    
    //Adding elements to form
    testForm.addElement("myCheckBox", "my checkbox", new CheckboxControl(), new BooleanData(), true);
    testForm.addElement("myLongText", "my long text", new TextControl(), new LongData(), false);
    testForm.addElement("myDateTime", "my date and time", new DateTimeControl(), new DateData(), false);
    testForm.addElement("myButton", "my button", new ButtonControl(), null, false);

    return testForm;
  }

  /**
   * Testing reading from request with a primitive constraint set.
   */
  public void testFormPrimitiveConstraint() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest request = new MockHttpServletRequest();

    request.addParameter("testForm.__present", "true");
    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", "108");
	request.addParameter("testForm.myDateTime", (String) null);

    //Testing primitive constraint
    testForm.getElement("myDateTime").setConstraint(new NotEmptyConstraint());
    
    StandardServletInputData input = new StandardServletInputData(request);
    input.pushScope("testForm");
    testForm._getWidget().update(input);
    assertTrue("Test form must not be valid after reading from request", !testForm.convertAndValidate());
  }

  /**
   * Testing reading from request with a grouped constraint set.
   */
  public void testFormActiveGroupedConstraintInvalidates() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest request = new MockHttpServletRequest();

    request.addParameter("testForm.__present", "true");
    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", "108");
    request.addParameter("testForm.myDateTime", (String) null);

    // create helper
    ConstraintGroupHelper groupHelper = new ConstraintGroupHelper();
    testForm.getElement("myDateTime").setConstraint(
        groupHelper.createGroupedConstraint(new NotEmptyConstraint(), "active"));

    StandardServletInputData input = new StandardServletInputData(request);
    input.pushScope("testForm");
    testForm._getWidget().update(input);
    
    groupHelper.setActiveGroup("active");
    assertTrue("Test form must not be valid after reading from request", !testForm.convertAndValidate());
  }
  
  /**
   * Testing reading from request with a grouped constraint set.
   */
  public void testFormActiveGroupedConstraintValidates() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest request = new MockHttpServletRequest();

    request.addParameter("testForm.__present", "true");
    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", "108");    
    request.addParameter("testForm.myDateTime.date", "11.10.2015");
    request.addParameter("testForm.myDateTime.time", "01:01");  

    // create helper
    ConstraintGroupHelper groupHelper = new ConstraintGroupHelper();    
    testForm.getElement("myDateTime").setConstraint(groupHelper.createGroupedConstraint(new NotEmptyConstraint(), "active"));
    
    StandardServletInputData input = new StandardServletInputData(request);
    input.pushScope("testForm");
    testForm._getWidget().update(input);
    
    groupHelper.setActiveGroup("active");
    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());       
  }
  
  /**
   * Testing reading from request with a grouped constraint set.
   */
  public void testFormInactiveGroupedConstraintValidates() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest request = new MockHttpServletRequest();

    request.addParameter("testForm.__present", "true");
    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", "108");    
    request.addParameter("testForm.myDateTime.date", "11.10.2015");
    request.addParameter("testForm.myDateTime.time", "01:01");  

    // create helper
    ConstraintGroupHelper groupHelper = new ConstraintGroupHelper();    
    testForm.getElement("myDateTime").setConstraint(groupHelper.createGroupedConstraint(new NotEmptyConstraint(), "active"));
    
    StandardServletInputData input = new StandardServletInputData(request);
    input.pushScope("testForm");
    testForm._getWidget().update(input);
    
    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());       
  }
  
  /**
   * Testing reading from request with a primitive constraint set.
   */
  public void testFormOptionalConstraint() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest request = new MockHttpServletRequest();

    //invalid
    
    request.addParameter("testForm.__present", "true");
    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", "12345");
    request.addParameter("testForm.myDateTime", (String) null);

    //Testing primitive constraint
    testForm.getElement("myLongText").setConstraint(
        new OptionalConstraint(
            new NumberInRangeConstraint(BigInteger.valueOf(20000), null)));

    StandardServletInputData input = new StandardServletInputData(request);
    input.pushScope("testForm");
    testForm._getWidget().update(input);
    input.popScope();
    
    assertTrue("Test form must not be valid after reading from request", !testForm.convertAndValidate());
            
    //valid
    
    request = new MockHttpServletRequest();
    
    request.addParameter("testForm.__present", "true");
    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", "40000");
    request.addParameter("testForm.myDateTime", (String) null);
    
    input = new StandardServletInputData(request);
    input.pushScope("testForm");
    testForm._getWidget().update(input); 
    input.popScope();
    
    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());
    
    //off
    
    request = new MockHttpServletRequest();
    
    request.addParameter("testForm.__present", "true");
    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", (String) null);
    request.addParameter("testForm.myDateTime", (String) null);
    
    input = new StandardServletInputData(request);
    input.pushScope("testForm");
    testForm._getWidget().update(input);
    input.popScope();
    
    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());
    
  }

  public void testFormAfterTodayConstraint() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest request = new MockHttpServletRequest();

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    
    //invalid
    
    request.addParameter("testForm.__present", "true");
    request.addParameter("testForm.myDateTime.date", "11.10.1015");
    request.addParameter("testForm.myDateTime.time", "01:01");  

    testForm.getElement("myDateTime").setConstraint(new AfterTodayConstraint(false));

    StandardServletInputData input = new StandardServletInputData(request);
    input.pushScope("testForm");
    testForm._getWidget().update(input);
    input.popScope();
    
    assertTrue("Test form must not be valid after reading from request", !testForm.convertAndValidate());
            
    request = new MockHttpServletRequest();
    
    //invalid
    request.addParameter("testForm.__present", "true");
    request.addParameter("testForm.myDateTime.date", sdf.format(new Date()));
    request.addParameter("testForm.myDateTime.time", "00:00");  

    testForm.getElement("myDateTime").setConstraint(new AfterTodayConstraint(false));
    
    input = new StandardServletInputData(request);
    input.pushScope("testForm");
    testForm._getWidget().update(input);
    input.popScope();
    
    assertTrue("Test form must not be valid after reading from request", !testForm.convertAndValidate());

    request = new MockHttpServletRequest();
    
    //invalid    
    request.addParameter("testForm.__present", "true");
    request.addParameter("testForm.myDateTime.date", "11.10.2015");
    request.addParameter("testForm.myDateTime.time", "01:01");  

    //Testing primitive constraint
    testForm.getElement("myDateTime").setConstraint(new AfterTodayConstraint(true));

    input = new StandardServletInputData(request);
    input.pushScope("testForm");
    testForm._getWidget().update(input);
    input.popScope();
    
    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());
    
    request = new MockHttpServletRequest();
    
    //valid
    request.addParameter("testForm.__present", "true");
    request.addParameter("testForm.myDateTime.date", sdf.format(new Date()));
    request.addParameter("testForm.myDateTime.time", "00:01");  

    //Testing primitive constraint
    testForm.getElement("myDateTime").setConstraint(new AfterTodayConstraint(true));

    input = new StandardServletInputData(request);
    input.pushScope("testForm");
    testForm._getWidget().update(input);  
    input.popScope();
    
    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());            
  }

  public void testFormRangeConstraint() throws Exception {

    FormWidget testForm = new FormWidget();
    testForm._getComponent().init(new MockEnvironment());
    
    //Adding elements to form
    FormElement lo = testForm.createElement("my date and time", new DateTimeControl(), new DateData(), false);
    FormElement hi = testForm.createElement("my date and time", new DateTimeControl(), new DateData(), false);
    FormWidget date = testForm.addSubForm("date");
    date.addElement("myDateLo", lo);
    date.addElement("myDateHi", hi);

    MockHttpServletRequest request = new MockHttpServletRequest();

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    
    //valid
    
    request.addParameter("testForm.__present", "true");
    request.addParameter("testForm.date.myDateLo.date", sdf.format(new java.sql.Date(System.currentTimeMillis()-1000*60*60*24)));
    request.addParameter("testForm.date.myDateHi.date", sdf.format(new java.util.Date()));
    
    testForm.getElement("date").setConstraint(new RangeConstraint((FormElement)testForm.getGenericElementByFullName("date.myDateLo"),
                                                                  (FormElement)testForm.getGenericElementByFullName("date.myDateHi"), 
                                                                  true)
                                              );

    StandardServletInputData input = new StandardServletInputData(request);
    input.pushScope("testForm");
    testForm._getWidget().update(input);
    input.popScope();
    
    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());            
  }
  
  /**
   * Test reading from request with a grouped constraint set, date is invalid.
   */
  public void testFormInactiveGroupedConstraintInValidates() throws Exception {
    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest request = new MockHttpServletRequest();

    request.addParameter("testForm.__present", "true");
    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", "108");
    request.addParameter("testForm.myDateTime.date", "11.10.20151");
    request.addParameter("testForm.myDateTime.time", "01:01");

    // create helper
    ConstraintGroupHelper groupHelper = new ConstraintGroupHelper();    
    testForm.getElement("myDateTime").setConstraint(groupHelper.createGroupedConstraint(new NotEmptyConstraint(), "active"));
    
    MockUiLibUtil.emulateHandleRequest(testForm, "testForm", request);

    assertFalse("Test form must be invalid after reading from request", testForm.convertAndValidate());       
  }

  /**
   * Test reading from request with a grouped constraint set, date is invalid.
   */
  public void anotherTestFormInactiveGroupedConstraintInValidates() throws Exception {
    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest request = new MockHttpServletRequest();

    request.addParameter("testForm.__present", "true");
    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", "108");
    request.addParameter("testForm.myDateTime.date", "11.10.20151278901");
    request.addParameter("testForm.myDateTime.time", "01:01");

    // create helper
    ConstraintGroupHelper groupHelper = new ConstraintGroupHelper();    
    testForm.getElement("myDateTime").setConstraint(groupHelper.createGroupedConstraint(new NotEmptyConstraint(), "active"));
    
    MockUiLibUtil.emulateHandleRequest(testForm, "testForm", request);
    assertFalse("Test form must be invalid after reading from request", testForm.convertAndValidate());       
  }
}
