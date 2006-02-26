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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import junit.framework.TestCase;
import org.araneaframework.tests.mock.MockEnviroment;
import org.araneaframework.tests.mock.MockUiLibUtil;
import org.araneaframework.uilib.constants.TextType;
import org.araneaframework.uilib.widgets.forms.controls.FloatControl;
import org.araneaframework.uilib.widgets.forms.controls.MultiSelectControl;
import org.araneaframework.uilib.widgets.forms.controls.NumberControl;
import org.araneaframework.uilib.widgets.forms.controls.StringArrayRequestControl;
import org.araneaframework.uilib.widgets.forms.controls.TextControl;
import org.springframework.mock.web.MockHttpServletRequest;


/**
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class FormControlTest extends TestCase {

  /**
   * Tests that {@link MultiSelectControl} returns an empty <code>List</code> for an empty request.
   * @throws Exception
   */
  public void testMultiSelectOnEmptyRequest() throws Exception {

    MockHttpServletRequest emptyRequest = new MockHttpServletRequest();
    MultiSelectControl ms = new MultiSelectControl();
    ms._getComponent().init(new MockEnviroment());
    MockUiLibUtil.emulateHandleRequest(ms, "myMultiSelect", emptyRequest);    
    ms.convertAndValidate();

    assertNotNull("MultiSelect must not return null if it's not present in request", ms.getRawValue());
    assertTrue("MultiSelect must return List if it's not present in request", ms.getRawValue() instanceof List);
    assertTrue("MultiSelect must return empty List if it's not present in request", ((List) ms.getRawValue()).size() == 0);
    
    ms._getComponent().destroy();
  }

  /**
   * Tests that {@link TextControl} return <code>null</code> on empty request.
   * @throws Exception
   */
  public void testTextboxOnEmptyRequest() throws Exception {
    MockHttpServletRequest emptyRequest = new MockHttpServletRequest();
    emptyRequest.addParameter("myTextBox", "");

    TextControl tb = new TextControl();   
    tb._getComponent().init(new MockEnviroment());
    MockUiLibUtil.emulateHandleRequest(tb, "myTextBox", emptyRequest);
    tb.convertAndValidate();

    assertNull("TextBox must return null on empty request.", tb.getRawValue());

    tb._getComponent().destroy();
  }
  
  /**
   * Tests that {@link TextControl} return <code>null</code> on empty request.
   * @throws Exception
   */
  public void testControlRequestDataSaving() throws Exception {
    MockHttpServletRequest valueRequest = new MockHttpServletRequest();
    
    String DEV_NULL = "/dev/null"; 
    
    valueRequest.addParameter("myTextBox", DEV_NULL);

    TextControl tb = new TextControl();
    tb._getComponent().init(new MockEnviroment());
    MockUiLibUtil.emulateHandleRequest(tb, "myTextBox", valueRequest);
    StringArrayRequestControl.ViewModel vm = (StringArrayRequestControl.ViewModel) tb._getViewable().getViewModel();
    
    assertEquals("TextBox must contain the value from request!", DEV_NULL, vm.getSimpleValue());
    
    tb._getComponent().destroy();
  }  
  
  //***********************************************************************
  // Client-side validation tests
  //***********************************************************************
  
  /**
   * Tests that {@link NumberControl} lets only valid integers through.
   */
  public void testNumberControlSimpleValidation() throws Exception {
    MockHttpServletRequest correctValueRequest = new MockHttpServletRequest();
    correctValueRequest.addParameter("myNumberInput", "108");
    
    NumberControl nc = new NumberControl();
    nc._getComponent().init(new MockEnviroment());
    MockUiLibUtil.emulateHandleRequest(nc, "myNumberInput", correctValueRequest);
    nc.convertAndValidate();
    
    assertTrue("Number control must be valid.", nc.isValid());
    assertTrue("Number control value must be a 'BigInteger'.", nc.getRawValue() instanceof BigInteger);
    assertTrue("Number control value must be '108'.", ((BigInteger) nc.getRawValue()).longValue() == 108L);

    MockHttpServletRequest incorrectValueRequest = new MockHttpServletRequest();
    incorrectValueRequest.addParameter("myNumberInput", "abcd");
    
    MockUiLibUtil.emulateHandleRequest(nc, "myNumberInput", incorrectValueRequest);
    nc.convertAndValidate();    
    
    assertTrue("Number control mustn't be valid.", !nc.isValid());
    
    nc._getComponent().destroy();
  }
  
  /**
   * Tests that {@link NumberControl} uses the min/max value parameters
   * for validation. 
   */
  public void testNumberControlMinMaxValidation() throws Exception {
    MockHttpServletRequest correctValueRequest = new MockHttpServletRequest();
    correctValueRequest.addParameter("myNumberInput", "50");
    
    NumberControl nc = new NumberControl();
    nc._getComponent().init(new MockEnviroment());
    
    nc.setMinValue(new BigInteger("25"));
    nc.setMaxValue(new BigInteger("75"));
    
    MockUiLibUtil.emulateHandleRequest(nc, "myNumberInput", correctValueRequest);
    nc.convertAndValidate();
    
    assertTrue("Number control must be valid.", nc.isValid());    
    assertTrue("Number control value must be '50'.", ((BigInteger) nc.getRawValue()).longValue() == 50L);
    
    MockHttpServletRequest tooLittleValueRequest = new MockHttpServletRequest();
    tooLittleValueRequest.addParameter("myNumberInput", "20");
    
    MockUiLibUtil.emulateHandleRequest(nc, "myNumberInput", tooLittleValueRequest);
    nc.convertAndValidate();    
    
    assertTrue("Number control mustn't be valid.", !nc.isValid());
       
    MockHttpServletRequest tooBigValueRequest = new MockHttpServletRequest();
    tooBigValueRequest.addParameter("myNumberInput", "80");
    
    MockUiLibUtil.emulateHandleRequest(nc, "myNumberInput", tooBigValueRequest);
    nc.convertAndValidate();    
    
    assertTrue("Number control mustn't be valid.", !nc.isValid());
    
    nc._getComponent().destroy();
  }
  
  /**
   * Tests that {@link FloatControl} lets only valid decimals through.
   */
  public void testFloatControlSimpleValidation() throws Exception {
    MockHttpServletRequest correctValueRequest = new MockHttpServletRequest();
    correctValueRequest.addParameter("myFloatInput", "28.012");
    
    FloatControl nc = new FloatControl();
    nc._getComponent().init(new MockEnviroment());
    MockUiLibUtil.emulateHandleRequest(nc, "myFloatInput", correctValueRequest);
    nc.convertAndValidate();
    
    assertTrue("Float control must be valid.", nc.isValid());
    assertTrue("Float control value must be a 'BigDecimal'.", nc.getRawValue() instanceof BigDecimal);
    assertTrue("Float control value must be '28.012'.", ((BigDecimal) nc.getRawValue()).doubleValue() == 28.012);
    
    MockHttpServletRequest incorrectValueRequest = new MockHttpServletRequest();
    incorrectValueRequest.addParameter("myFloatInput", "abcd");
    
    MockUiLibUtil.emulateHandleRequest(nc, "myFloatInput", incorrectValueRequest);
    nc.convertAndValidate();    
    
    assertTrue("Float control mustn't be valid.", !nc.isValid());
    
    nc._getComponent().destroy();
  }
  
  /**
   * Tests that {@link FloatControl} uses the min/max value parameters
   * for validation. 
   */
  public void testFloatControlMinMaxValidation() throws Exception {
    MockHttpServletRequest correctValueRequest = new MockHttpServletRequest();
    correctValueRequest.addParameter("myFloatInput", "50.0018");
    
    FloatControl nc = new FloatControl();
    nc._getComponent().init(new MockEnviroment());
    
    nc.setMinValue(new BigDecimal("25.001"));
    nc.setMaxValue(new BigDecimal("75.002"));
    
    MockUiLibUtil.emulateHandleRequest(nc, "myFloatInput", correctValueRequest);
    nc.convertAndValidate();
    
    assertTrue("Float control must be valid.", nc.isValid());    
    assertTrue("Float control value must be '50.0018'.", ((BigDecimal) nc.getRawValue()).doubleValue() == 50.0018);
    
    MockHttpServletRequest tooLittleValueRequest = new MockHttpServletRequest();
    tooLittleValueRequest.addParameter("myFloatInput", "20.1");
    
    MockUiLibUtil.emulateHandleRequest(nc, "myFloatInput", tooLittleValueRequest);
    nc.convertAndValidate();    
    
    assertTrue("Float control mustn't be valid.", !nc.isValid());
    
    MockHttpServletRequest tooBigValueRequest = new MockHttpServletRequest();
    tooBigValueRequest.addParameter("myFloatInput", "80.2");
    
    MockUiLibUtil.emulateHandleRequest(nc, "myFloatInput", tooBigValueRequest);    
    nc.convertAndValidate();    
    
    assertTrue("Float control mustn't be valid.", !nc.isValid());    
  }
  
  /**
   * Tests that {@link TextControl} with content type set to personal id
   * lets only valid Estonian personal ids through. 
   */
  public void testPersonalIdControlSimpleValidation() throws Exception {
    MockHttpServletRequest correctValueRequest = new MockHttpServletRequest();
    correctValueRequest.addParameter("myPersonalIdInput", "38304280235");
    
    TextControl pic = new TextControl(TextType.EST_PERSONAL_ID);
    pic._getComponent().init(new MockEnviroment());
    MockUiLibUtil.emulateHandleRequest(pic, "myPersonalIdInput", correctValueRequest);
    pic.convertAndValidate();
    
    assertTrue("Personal id control must be valid.", pic.isValid());
    assertTrue("Personal id control value must be a 'String'.", pic.getRawValue() instanceof String);
    assertTrue("Personal id control value must be '38304280235'.", ((String) pic.getRawValue()).equals("38304280235"));
     
    MockHttpServletRequest incorrectValueRequest = new MockHttpServletRequest();
    incorrectValueRequest.addParameter("myPersonalIdInput", "abcd");
    
    MockUiLibUtil.emulateHandleRequest(pic, "myPersonalIdInput", incorrectValueRequest);
    pic.convertAndValidate();    
    
    assertTrue("Personal id control mustn't be valid.", !pic.isValid());
    
    pic._getComponent().destroy();
  }
  
  /**
   * Tests that {@link TextControl} uses the min/max length parameters
   * for validation. 
   */
  public void testTextboxControlMinMaxValidation() throws Exception {
    //Basic
    MockHttpServletRequest correctValueRequest = new MockHttpServletRequest();
    correctValueRequest.addParameter("myTextBox", "i love me");
    
    TextControl tc = new TextControl();
    tc._getComponent().init(new MockEnviroment());
    
    tc.setMinLength(new Long(5));
    tc.setMaxLength(new Long(20));
    
    MockUiLibUtil.emulateHandleRequest(tc, "myTextBox", correctValueRequest);
    tc.convertAndValidate();
    
    assertTrue("Textbox control must be valid.", tc.isValid());    
    assertTrue("Textbox control value must be 'i love me'.", ((String) tc.getRawValue()).equals("i love me"));
     
    //Too short

    MockHttpServletRequest tooShortValueRequest = new MockHttpServletRequest();
    tooShortValueRequest.addParameter("myTextBox", "boo");
    
    MockUiLibUtil.emulateHandleRequest(tc, "myTextBox", tooShortValueRequest);
    tc.convertAndValidate();    
    
    assertTrue("Textbox control mustn't be valid.", !tc.isValid());
    
    //Too long
    
    MockHttpServletRequest tooLongValueRequest = new MockHttpServletRequest();
    tooLongValueRequest.addParameter("myTextBox", "i love myself and others very very much");
    
    MockUiLibUtil.emulateHandleRequest(tc, "myTextBox", tooLongValueRequest);   
    tc.convertAndValidate();    
    
    assertTrue("Textbox control mustn't be valid.", !tc.isValid());  
          
    //min=max correct
    
    tc.setMinLength(new Long(10));
    tc.setMaxLength(new Long(10));

    correctValueRequest = new MockHttpServletRequest();
    correctValueRequest.addParameter("myTextBox", "1234567890");
    
    MockUiLibUtil.emulateHandleRequest(tc, "myTextBox", correctValueRequest);
    tc.convertAndValidate();
        
    assertTrue("Textbox control must be valid.", tc.isValid());    
    assertTrue("Textbox control value must be '1234567890'.", ((String) tc.getRawValue()).equals("1234567890"));
    
    //min=max too short

    tooShortValueRequest.addParameter("myTextBox", "123456789");
    
    MockUiLibUtil.emulateHandleRequest(tc, "myTextBox", tooShortValueRequest);
    tc.convertAndValidate();
        
    assertTrue("Textbox control mustn't be valid.", !tc.isValid());    
    
    //min=max too long

    tooShortValueRequest.addParameter("myTextBox", "12345678901");
    
    MockUiLibUtil.emulateHandleRequest(tc, "myTextBox", tooShortValueRequest);   
    tc.convertAndValidate();
        
    assertTrue("Textbox control mustn't be valid.", !tc.isValid());  
    
    tc._getComponent().destroy();
  }  
}
