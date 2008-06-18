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

package org.araneaframework.tests.constraints;

import junit.framework.TestCase;
import org.araneaframework.InputData;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.tests.util.RequestUtil;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class NotEmptyConstraintTest extends TestCase {
  protected FormWidget form;
  protected FormElement textInput;

  @Override
  public void setUp() throws Exception {
    form = new FormWidget();
    textInput = form.createElement("#text", new TextControl(), new StringData(), false);
    form.addElement("text", textInput);
    textInput.rendered();
    MockLifeCycle.begin(form, new MockEnvironment());
  }
  
  protected InputData createRequestWithText(String text) {
    MockHttpServletRequest request = RequestUtil.markSubmitted(new MockHttpServletRequest());
    request.addParameter("text", text);
    return new StandardServletInputData(request);
  }
  
  protected void processRequestWithText(String text) {
    InputData input = createRequestWithText(text);
    form._getWidget().update(input);
  }
  
  public void testValidFormElement() throws Exception {
    textInput.setConstraint(new NotEmptyConstraint());
    processRequestWithText("some text");
    assertTrue("Textinput value should have been read from request.", form.convertAndValidate());
  }
  
  public void testValidForm() throws Exception {
    form.setConstraint(new NotEmptyConstraint(textInput));
    processRequestWithText("some text");
    assertTrue("Textinput value should have been read from request.", form.convertAndValidate());
  }
  
  public void testInvalidFormElement() throws Exception {
    textInput.setConstraint(new NotEmptyConstraint());
    processRequestWithText((String)null);
    assertFalse("Textinput value should have not been read from request.", form.convertAndValidate());
  }
  
  /** 
   * TODO: this test fails now, because we are emulating previous broken behaviour of forms,
   * unfortunately some cases of validation (isRead() of controls) fails otherwise.
   * Test that constraint correctly detects that formelement was submitted as empty, even after
   * FormElement.setValue is called. */
  public void testFormElementMissingInRequestExplicitSetValue() throws Exception {
    textInput.setConstraint(new NotEmptyConstraint());
    processRequestWithText((String)null);
    textInput.setValue("some text");
    assertFalse("Textinput value should have not been read from request (it was set only explicitly).", form.convertAndValidate());
  }
  
  public void testInvalidForm() throws Exception {
    form.setConstraint(new NotEmptyConstraint(textInput));
    processRequestWithText((String)null);
    assertFalse("Textinput value should have not been read from request.", form.convertAndValidate());
  }
}
