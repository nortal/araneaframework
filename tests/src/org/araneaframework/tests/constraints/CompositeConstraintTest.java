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

package org.araneaframework.tests.constraints;

import java.math.BigDecimal;
import java.math.BigInteger;
import junit.framework.TestCase;
import org.araneaframework.InputData;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.tests.util.RequestUtil;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.AndConstraint;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.constraint.NumberInRangeConstraint;
import org.araneaframework.uilib.form.constraint.OrConstraint;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.araneaframework.uilib.form.data.StringData;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class CompositeConstraintTest extends TestCase {

  protected FormWidget form;

  protected FormElement<String, String> textInput1, textInput2;

  protected FormElement<BigDecimal, BigDecimal> numberInput;

  @Override
  public void setUp() throws Exception {
    this.form = new FormWidget();
    this.textInput1 = this.form.createElement("#text1", new TextControl(), new StringData(), false);
    this.textInput2 = this.form.createElement("#text2", new TextControl(), new StringData(), false);
    this.numberInput = this.form.createElement("#number", new FloatControl(), new BigDecimalData(), false);
    this.form.addElement("text1", this.textInput1);
    this.form.addElement("text2", this.textInput2);
    this.form.addElement("number", this.numberInput);
    MockLifeCycle.begin(this.form, new MockEnvironment());
  }

  protected void markElementsRendered() {
    this.textInput1.rendered();
    this.textInput2.rendered();
    this.numberInput.rendered();
  }

  protected InputData createRequestWithText(String text1, String text2) {
    MockHttpServletRequest request = RequestUtil.markSubmitted(new MockHttpServletRequest());
    request.addParameter("text1", text1);
    request.addParameter("text2", text2);
    return new StandardServletInputData(request);
  }

  protected InputData createRequestWithNumber(String number) {
    MockHttpServletRequest request = RequestUtil.markSubmitted(new MockHttpServletRequest());
    request.addParameter("number", number);
    return new StandardServletInputData(request);
  }

  protected void processRequest(InputData input) {
    markElementsRendered();
    this.form._getWidget().update(input);
  }

  protected void processRequireAll(String text1, String text2) {
    AndConstraint requireAllConstraint = new AndConstraint();
    requireAllConstraint.setCustomErrorMessage("AND constraint violated.");
    requireAllConstraint.addConstraint(new NotEmptyConstraint<String, String>(this.textInput1));
    requireAllConstraint.addConstraint(new NotEmptyConstraint<String, String>(this.textInput2));
    this.form.setConstraint(requireAllConstraint);

    processRequest(createRequestWithText(text1, text2));
  }

  protected void processRequireAny(String text1, String text2) {
    OrConstraint requireAnyConstraint = new OrConstraint();
    requireAnyConstraint.setCustomErrorMessage("OR constraint violated.");
    requireAnyConstraint.addConstraint(new NotEmptyConstraint<String, String>(this.textInput1));
    requireAnyConstraint.addConstraint(new NotEmptyConstraint<String, String>(this.textInput2));
    this.form.setConstraint(requireAnyConstraint);

    processRequest(createRequestWithText(text1, text2));
  }

  /** Tests that composite AND constraint set on FORM validates with input. */
  public void testValidRequireAll() throws Exception {
    processRequireAll("some text", "more text");
    assertTrue("Should be valid as both textinputs are present.", this.form.convertAndValidate());
  }

  /** Tests that composite AND constraint set on FORM does not validate with input. */
  public void testInvalidRequireAll() throws Exception {
    processRequireAll("some text", null);
    assertFalse("Should be invalid as one textinput is missing.", this.form.convertAndValidate());
  }

  /** Tests that composite OR constraint set on FORM validates with input. */
  public void testValidRequireAny_AllPresent() throws Exception {
    processRequireAny("some text", "more text");
    assertTrue("Should be valid as both textinputs are present.", this.form.convertAndValidate());
  }

  /** Tests that composite OR constraint set on FORM validates with input. */
  public void testValidRequireAny_OnePresent() throws Exception {
    processRequireAny("some text", null);
    assertTrue("Should be valid as one textinput is present.", this.form.convertAndValidate());
  }

  /** Tests that composite OR constraint set on FORM invalidates with input. */
  public void testInvalidRequireAny_OnePresent() throws Exception {
    processRequireAny(null, null);
    assertFalse("Should be invalid as no textinputs are present.", this.form.convertAndValidate());
  }

  /** test that setting composite constraint directly on form element works */
  public void testCompositeSettingOnFormElementAndConstraintClearance() throws Exception {
    OrConstraint constraint = new OrConstraint();
    constraint.addConstraint(new NumberInRangeConstraint<Object, BigInteger>(new BigInteger("100"), new BigInteger("200")));
    constraint.addConstraint(new NumberInRangeConstraint<Object, BigInteger>(new BigInteger("800"), new BigInteger("900")));
    this.numberInput.setConstraint(constraint);

    processRequest(createRequestWithNumber("400"));
    assertFalse("Should be invalid as number does not fall into valid ranges.", this.form.convertAndValidate());

    processRequest(createRequestWithNumber("150"));
    this.numberInput.getValue();
    assertTrue("Should be valid as number is in valid range.", this.form.convertAndValidate());

    processRequest(createRequestWithNumber("872"));
    assertTrue("Should be valid as number is in valid range.", this.form.convertAndValidate());

    // test that clearing composite constraint works
    constraint.clearConstraints();
    processRequest(createRequestWithNumber("400"));
    assertTrue("Should be valid as composite constraint should be empty.", this.form.convertAndValidate());
  }
}
