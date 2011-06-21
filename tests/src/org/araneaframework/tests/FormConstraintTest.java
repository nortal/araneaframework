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

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import junit.framework.TestCase;
import org.araneaframework.InputData;
import org.araneaframework.core.StandardScope;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.tests.mock.MockUiLibUtil;
import org.araneaframework.tests.util.RequestUtil;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
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
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 */
public class FormConstraintTest extends TestCase {

  public FormConstraintTest(String name) {
    super(name);
  }

  private FormWidget makeUsualForm() throws Exception {
    // Creating form :-)
    FormWidget testForm = new FormWidget();
    testForm._getComponent().init(new StandardScope("testForm", null), new MockEnvironment());

    // Adding elements to form
    testForm.addElement("myCheckBox", "my checkbox", new CheckboxControl(), new BooleanData(), true);
    testForm.addElement("myLongText", "my long text", new TextControl(), new LongData(), false);
    testForm.addElement("myDateTime", "my date and time", new DateTimeControl(), new DateData(), false);
    testForm.addElement("myButton", "my button", new ButtonControl(), null, false);

    markElementsRendered(testForm);

    return testForm;
  }

  private void markElementsRendered(FormWidget testForm) {
    for (Object element : testForm.getElements().values()) {
      if (element instanceof FormWidget) {
        markElementsRendered((FormWidget) element);
      } else {
        ((FormElement<?, ?>) element).rendered();
      }
    }
  }

  protected void processRequest(InputData input, FormWidget testForm) {
    markElementsRendered(testForm);
    testForm._getWidget().update(input);
  }

  /**
   * Testing reading from request with a grouped constraint set.
   */
  public void testFormActiveGroupedConstraintInvalidates() throws Exception {

    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest request = new MockHttpServletRequest();

    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", "108");
    request.addParameter("testForm.myDateTime", (String) null);

    // create helper
    ConstraintGroupHelper groupHelper = new ConstraintGroupHelper();
    testForm.getElement("myDateTime").setConstraint(groupHelper.createGroupedConstraint(new NotEmptyConstraint<Object, Object>(), "active"));

    processRequest(new StandardServletInputData(request), testForm);

    groupHelper.setActiveGroup("active");
    assertTrue("Test form must not be valid after reading from request", !testForm.convertAndValidate());
  }

  /**
   * Testing reading from request with a primitive constraint set.
   */
  public void testFormOptionalConstraint() throws Exception {
    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest request = RequestUtil.markSubmitted(new MockHttpServletRequest());

    // invalid 1, integer should be greater than 20000
    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", "12345");
    request.addParameter("testForm.myDateTime", (String) null);

    // Testing primitive constraint
    testForm.getElement("myLongText").setConstraint(
        new OptionalConstraint(new NumberInRangeConstraint<String, Long>(BigInteger.valueOf(20000), null)));

    processRequest(new StandardServletInputData(request), testForm);

    assertTrue("Test form must not be valid after reading from request", !testForm.convertAndValidate());

    // valid 1, long field is not read from request and should not be validated too
    request = RequestUtil.markSubmitted(new MockHttpServletRequest());

    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myDateTime", (String) null);

    // Testing primitive constraint
    testForm.getElement("myLongText").setConstraint(
        new OptionalConstraint(new NumberInRangeConstraint<String, Long>(BigInteger.valueOf(20000), null)));

    processRequest(new StandardServletInputData(request), testForm);

    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());

    // valid

    request = RequestUtil.markSubmitted(new MockHttpServletRequest());
    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", "40000");
    request.addParameter("testForm.myDateTime", (String) null);

    processRequest(new StandardServletInputData(request), testForm);

    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());

    // off

    request = RequestUtil.markSubmitted(new MockHttpServletRequest());

    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", (String) null);
    request.addParameter("testForm.myDateTime", (String) null);

    processRequest(new StandardServletInputData(request), testForm);

    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());

  }

  @SuppressWarnings("unchecked")
  public void testFormRangeConstraint() throws Exception {

    FormWidget testForm = new FormWidget();
    testForm._getComponent().init(new StandardScope("testForm", null), new MockEnvironment());

    // Adding elements to form
    FormElement<Timestamp, Date> lo = testForm.createElement("my date and time", new DateTimeControl(), new DateData(),
        false);
    FormElement<Timestamp, Date> hi = testForm.createElement("my date and time", new DateTimeControl(), new DateData(),
        false);
    FormWidget date = testForm.addSubForm("date");
    date.addElement("myDateLo", lo);
    date.addElement("myDateHi", hi);

    MockHttpServletRequest request = new MockHttpServletRequest();

    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    // valid
    request.addParameter("testForm.date.myDateLo.date", sdf.format(new java.sql.Date(System.currentTimeMillis() - 1000
        * 60 * 60 * 24)));
    request.addParameter("testForm.date.myDateHi.date", sdf.format(new java.util.Date()));

    testForm.getElement("date").setConstraint(
        new RangeConstraint<Object, Object>((FormElement<Object, Object>) testForm
            .getGenericElementByFullName("date.myDateLo"), (FormElement<Object, Object>) testForm
            .getGenericElementByFullName("date.myDateHi"), true));
    processRequest(new StandardServletInputData(request), testForm);

    assertTrue("Test form must be valid after reading from request", testForm.convertAndValidate());
  }

  /**
   * Test reading from request with a grouped constraint set, date is invalid.
   */
  public void testFormInactiveGroupedConstraintInValidates() throws Exception {
    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest request = new MockHttpServletRequest();

    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", "108");
    request.addParameter("testForm.myDateTime.date", "11.10.20151");
    request.addParameter("testForm.myDateTime.time", "01:01");

    // create helper
    ConstraintGroupHelper groupHelper = new ConstraintGroupHelper();
    testForm.getElement("myDateTime").setConstraint(
        groupHelper.createGroupedConstraint(new NotEmptyConstraint<Object, Object>(), "active"));

    MockUiLibUtil.emulateHandleRequest(testForm, "testForm", request);

    assertFalse("Test form must be invalid after reading from request", testForm.convertAndValidate());
  }

  /**
   * Test reading from request with a grouped constraint set, date is invalid.
   */
  public void anotherTestFormInactiveGroupedConstraintInValidates() throws Exception {
    FormWidget testForm = makeUsualForm();

    MockHttpServletRequest request = new MockHttpServletRequest();

    request.addParameter("testForm.myCheckBox", "true");
    request.addParameter("testForm.myLongText", "108");
    request.addParameter("testForm.myDateTime.date", "11.10.20151278901");
    request.addParameter("testForm.myDateTime.time", "01:01");

    // create helper
    ConstraintGroupHelper groupHelper = new ConstraintGroupHelper();
    testForm.getElement("myDateTime").setConstraint(
        groupHelper.createGroupedConstraint(new NotEmptyConstraint<Object, Object>(), "active"));

    MockUiLibUtil.emulateHandleRequest(testForm, "testForm", request);
    assertFalse("Test form must be invalid after reading from request", testForm.convertAndValidate());
  }
}
