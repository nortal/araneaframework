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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import junit.framework.TestCase;
import org.araneaframework.core.StandardScope;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.tests.mock.MockFormElementContext;
import org.araneaframework.tests.mock.MockUiLibUtil;
import org.araneaframework.uilib.form.control.BigDecimalControl;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.support.DisplayItem;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class FormControlTest extends TestCase {

  /**
   * Tests that {@link MultiSelectControl} returns an empty <code>List</code> for an empty request.
   * 
   * @throws Exception
   */
  public void testMultiSelectOnEmptyRequest() throws Exception {

    MockHttpServletRequest emptyRequest = new MockHttpServletRequest();
    MultiSelectControl<DisplayItem> ms = new MultiSelectControl<DisplayItem>("label", "value");
    ms.setFormElementCtx(new MockFormElementContext<List<DisplayItem>, Object>());
    ms._getComponent().init(new StandardScope("myMultiSelect", null), new MockEnvironment());
    MockUiLibUtil.emulateHandleRequest(ms, "myMultiSelect", emptyRequest);
    ms.convertAndValidate();

    assertNotNull("MultiSelect must not return null if it's not present in request", ms.getRawValue());
    assertTrue("MultiSelect must return List if it's not present in request", ms.getRawValue() != null);
    assertTrue("MultiSelect must return empty List if it's not present in request", ms.getRawValue().isEmpty());

    ms._getComponent().destroy();
  }

  /**
   * Tests that {@link TextControl} return <code>null</code> on empty request.
   * 
   * @throws Exception
   */
  public void testTextboxOnEmptyRequest() throws Exception {
    MockHttpServletRequest emptyRequest = new MockHttpServletRequest();
    emptyRequest.addParameter("myTextBox", "");

    TextControl textControl = new TextControl();
    textControl.setFormElementCtx(new MockFormElementContext<String, Object>());
    textControl._getComponent().init(new StandardScope(null, null), new MockEnvironment());
    MockUiLibUtil.emulateHandleRequest(textControl, "myTextBox", emptyRequest);
    textControl.convertAndValidate();

    assertNull("TextBox must return null on empty request.", textControl.getRawValue());

    textControl._getComponent().destroy();
  }

  /**
   * Tests that {@link TextControl} return <code>null</code> on empty request.
   * 
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public void testControlRequestDataSaving() throws Exception {
    MockHttpServletRequest valueRequest = new MockHttpServletRequest();

    String DEV_NULL = "/dev/null";

    valueRequest.addParameter("myTextBox", DEV_NULL);

    TextControl textControl = new TextControl();
    textControl.setFormElementCtx(new MockFormElementContext<String, Object>());
    textControl._getComponent().init(new StandardScope("myTextBox", null), new MockEnvironment());
    MockUiLibUtil.emulateHandleRequest(textControl, "myTextBox", valueRequest);
    StringArrayRequestControl<?>.ViewModel vm = (StringArrayRequestControl.ViewModel) textControl._getViewable()
        .getViewModel();

    assertEquals("TextBox must contain the value from request!", DEV_NULL, vm.getSimpleValue());

    textControl._getComponent().destroy();
  }

  // ***********************************************************************
  // Client-side validation tests
  // ***********************************************************************
  /**
   * Tests that {@link NumberControl} lets only valid integers through.
   */
  public void testNumberControlSimpleValidationNew() throws Exception {
    MockHttpServletRequest correctValueRequest = new MockHttpServletRequest();
    correctValueRequest.addParameter("myNumberInput", "108");

    NumberControl numberControl = new NumberControl();

    MockFormElementContext<BigInteger, Object> mockFormElementContext = new MockFormElementContext<BigInteger, Object>(
        "TheLabel", false, false);

    numberControl.setFormElementCtx(mockFormElementContext);
    numberControl._getComponent().init(new StandardScope("myNumberInput", null), new MockEnvironment());

    MockUiLibUtil.emulateHandleRequest(numberControl, "myNumberInput", correctValueRequest);
    numberControl.convertAndValidate();

    assertTrue("Number control must be valid.", mockFormElementContext.isValid());
    assertTrue("Number control value must be a 'BigInteger'.", numberControl.getRawValue() != null);
    assertTrue("Number control value must be '108'.", (numberControl.getRawValue()).longValue() == 108L);

    MockHttpServletRequest incorrectValueRequest = new MockHttpServletRequest();
    incorrectValueRequest.addParameter("myNumberInput", "abcd");

    MockUiLibUtil.emulateHandleRequest(numberControl, "myNumberInput", incorrectValueRequest);
    numberControl.convertAndValidate();

    assertTrue("Number control mustn't be valid.", !mockFormElementContext.isValid());

    numberControl._getComponent().destroy();
  }

  /**
   * Tests that {@link NumberControl} uses the min/max value parameters for validation.
   */
  public void testNumberControlMinMaxValidation() throws Exception {
    MockHttpServletRequest correctValueRequest = new MockHttpServletRequest();
    correctValueRequest.addParameter("myNumberInput", "50");

    MockFormElementContext<BigInteger, Object> mockFormElementContext = new MockFormElementContext<BigInteger, Object>(
        "TheLabel", false, false);
    NumberControl numberControl = new NumberControl();
    numberControl.setFormElementCtx(mockFormElementContext);
    numberControl._getComponent().init(new StandardScope("myNumberInput", null), new MockEnvironment());

    numberControl.setMinValue(new BigInteger("25"));
    numberControl.setMaxValue(new BigInteger("75"));

    MockUiLibUtil.emulateHandleRequest(numberControl, "myNumberInput", correctValueRequest);
    numberControl.convertAndValidate();

    assertTrue("Number control must be valid.", mockFormElementContext.isValid());
    assertTrue("Number control value must be '50'.", (numberControl.getRawValue()).longValue() == 50L);

    MockHttpServletRequest tooLittleValueRequest = new MockHttpServletRequest();
    tooLittleValueRequest.addParameter("myNumberInput", "20");

    MockUiLibUtil.emulateHandleRequest(numberControl, "myNumberInput", tooLittleValueRequest);
    numberControl.convertAndValidate();

    assertTrue("Number control mustn't be valid.", !mockFormElementContext.isValid());

    MockHttpServletRequest tooBigValueRequest = new MockHttpServletRequest();
    tooBigValueRequest.addParameter("myNumberInput", "80");

    MockUiLibUtil.emulateHandleRequest(numberControl, "myNumberInput", tooBigValueRequest);
    numberControl.convertAndValidate();

    assertTrue("Number control mustn't be valid.", !mockFormElementContext.isValid());

    numberControl._getComponent().destroy();
  }

  /**
   * Tests that {@link BigDecimalControl} lets only valid decimals through.
   */
  public void testFloatControlSimpleValidation() throws Exception {
    MockHttpServletRequest correctValueRequest = new MockHttpServletRequest();
    correctValueRequest.addParameter("myFloatInput", "28.012");

    BigDecimalControl nc = new BigDecimalControl();
    MockFormElementContext<BigDecimal, Object> mockFormElementContext = new MockFormElementContext<BigDecimal, Object>(
        "TheLabel", false, false);
    nc.setFormElementCtx(mockFormElementContext);
    nc._getComponent().init(new StandardScope("myFloatInput", null), new MockEnvironment());
    MockUiLibUtil.emulateHandleRequest(nc, "myFloatInput", correctValueRequest);
    nc.convertAndValidate();

    assertTrue("Float control must be valid.", mockFormElementContext.isValid());
    assertTrue("Float control value must be a 'BigDecimal'.", nc.getRawValue() != null);
    assertTrue("Float control value must be '28.012'.", nc.getRawValue().doubleValue() == 28.012);

    MockHttpServletRequest incorrectValueRequest = new MockHttpServletRequest();
    incorrectValueRequest.addParameter("myFloatInput", "abcd");

    MockUiLibUtil.emulateHandleRequest(nc, "myFloatInput", incorrectValueRequest);
    nc.convertAndValidate();

    assertTrue("Float control mustn't be valid.", !mockFormElementContext.isValid());

    nc._getComponent().destroy();
  }

  /**
   * Tests that {@link BigDecimalControl} uses the min/max value parameters for validation.
   */
  public void testFloatControlMinMaxValidation() throws Exception {
    MockHttpServletRequest correctValueRequest = new MockHttpServletRequest();
    correctValueRequest.addParameter("myFloatInput", "50.0018");

    BigDecimalControl numberControl = new BigDecimalControl();
    MockFormElementContext<BigDecimal, Object> mockFormElementContext = new MockFormElementContext<BigDecimal, Object>(
        "TheLabel", false, false);
    numberControl.setFormElementCtx(mockFormElementContext);
    numberControl._getComponent().init(new StandardScope("myFloatInput", null), new MockEnvironment());

    numberControl.setMinValue(new BigDecimal("25.001"));
    numberControl.setMaxValue(new BigDecimal("75.002"));

    MockUiLibUtil.emulateHandleRequest(numberControl, "myFloatInput", correctValueRequest);
    numberControl.convertAndValidate();

    assertTrue("Float control must be valid.", mockFormElementContext.isValid());
    assertTrue("Float control value must be '50.0018'.", (numberControl.getRawValue()).doubleValue() == 50.0018);

    MockHttpServletRequest tooLittleValueRequest = new MockHttpServletRequest();
    tooLittleValueRequest.addParameter("myFloatInput", "20.1");

    MockUiLibUtil.emulateHandleRequest(numberControl, "myFloatInput", tooLittleValueRequest);
    numberControl.convertAndValidate();

    assertTrue("Float control mustn't be valid.", !mockFormElementContext.isValid());

    MockHttpServletRequest tooBigValueRequest = new MockHttpServletRequest();
    tooBigValueRequest.addParameter("myFloatInput", "80.2");

    MockUiLibUtil.emulateHandleRequest(numberControl, "myFloatInput", tooBigValueRequest);
    numberControl.convertAndValidate();

    assertTrue("Float control mustn't be valid.", !mockFormElementContext.isValid());
  }

  /**
   * Tests that {@link TextControl} uses the min/max length parameters for validation.
   */
  public void testTextboxControlMinMaxValidation() throws Exception {
    // Basic
    MockHttpServletRequest correctValueRequest = new MockHttpServletRequest();
    correctValueRequest.addParameter("myTextBox", "i love me");

    TextControl textControl = new TextControl();
    MockFormElementContext<String, Object> mockFormElementContext = new MockFormElementContext<String, Object>(
        "TheLabel", false, false);
    textControl.setFormElementCtx(mockFormElementContext);
    textControl._getComponent().init(new StandardScope("myTextBox", null), new MockEnvironment());

    textControl.setMinLength(new Long(5));
    textControl.setMaxLength(new Long(20));

    MockUiLibUtil.emulateHandleRequest(textControl, "myTextBox", correctValueRequest);
    textControl.convertAndValidate();

    assertTrue("Textbox control must be valid.", mockFormElementContext.isValid());
    assertTrue("Textbox control value must be 'i love me'.", (textControl.getRawValue()).equals("i love me"));

    // Too short

    MockHttpServletRequest tooShortValueRequest = new MockHttpServletRequest();
    tooShortValueRequest.addParameter("myTextBox", "boo");

    MockUiLibUtil.emulateHandleRequest(textControl, "myTextBox", tooShortValueRequest);
    textControl.convertAndValidate();

    assertTrue("Textbox control mustn't be valid.", !mockFormElementContext.isValid());

    // Too long

    MockHttpServletRequest tooLongValueRequest = new MockHttpServletRequest();
    tooLongValueRequest.addParameter("myTextBox", "i love myself and others very very much");

    MockUiLibUtil.emulateHandleRequest(textControl, "myTextBox", tooLongValueRequest);
    textControl.convertAndValidate();

    assertTrue("Textbox control mustn't be valid.", !mockFormElementContext.isValid());
    mockFormElementContext.clearErrors();

    // min=max correct

    textControl.setMinLength(new Long(10));
    textControl.setMaxLength(new Long(10));

    correctValueRequest = new MockHttpServletRequest();
    correctValueRequest.addParameter("myTextBox", "1234567890");

    MockUiLibUtil.emulateHandleRequest(textControl, "myTextBox", correctValueRequest);
    textControl.convertAndValidate();

    assertTrue("Textbox control must be valid.", mockFormElementContext.isValid());
    assertTrue("Textbox control value must be '1234567890'.", (textControl.getRawValue()).equals("1234567890"));

    // min=max too short

    tooShortValueRequest.addParameter("myTextBox", "123456789");

    MockUiLibUtil.emulateHandleRequest(textControl, "myTextBox", tooShortValueRequest);
    textControl.convertAndValidate();

    assertTrue("Textbox control mustn't be valid.", !mockFormElementContext.isValid());

    // min=max too long

    tooShortValueRequest.addParameter("myTextBox", "12345678901");

    MockUiLibUtil.emulateHandleRequest(textControl, "myTextBox", tooShortValueRequest);
    textControl.convertAndValidate();

    assertTrue("Textbox control mustn't be valid.", !mockFormElementContext.isValid());

    textControl._getComponent().destroy();
  }
}
