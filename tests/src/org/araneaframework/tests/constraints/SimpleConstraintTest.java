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
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.tests.util.RequestUtil;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.BaseFieldConstraint;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class SimpleConstraintTest extends TestCase {
  private FormWidget form;

  public void setUp() throws Exception {
    form = new FormWidget();
    MockLifeCycle.begin(form, new MockEnvironment());
  }

  // test that field constraints cannot be set on various other widgets.
  public void testInvalidConstraintSetting() throws Exception {
    try {
      form.setConstraint(new NotEmptyConstraint());
      
      MockHttpServletRequest request = RequestUtil.markSubmitted(new MockHttpServletRequest());
      
      StandardServletInputData input = new StandardServletInputData(request);
      input.pushScope("form");
      form._getWidget().update(input);
      input.popScope();
      
      form.convertAndValidate();
      
      fail("Exception should have occured, because NotEmptyConstraint is not applicable to FormWidget");
    } catch (BaseFieldConstraint.FieldConstraintException e) {
    }
  }
  
  // test that setting field constraint to null works and does not throw exception
  public void testFieldNullConstraintSetting() throws Exception {
    FormElement el = form.createElement("#number", new FloatControl(), new BigDecimalData(), false);
    el.setConstraint(null);
    form.addElement("number", el);

    MockHttpServletRequest request = RequestUtil.markSubmitted(new MockHttpServletRequest());
      
    StandardServletInputData input = new StandardServletInputData(request);
    input.pushScope("form");
    form._getWidget().update(input);
    input.popScope();
      
    assertTrue("Form is supposed to valid because constraint is not set.", form.convertAndValidate());
  }
  
  // test that setting formwidget constraint to null works and does not throw exception
  public void testFormNullConstraintSetting() throws Exception {
    form.setConstraint(null);

    MockHttpServletRequest request = RequestUtil.markSubmitted(new MockHttpServletRequest());

    StandardServletInputData input = new StandardServletInputData(request);
    input.pushScope("form");
    form._getWidget().update(input);
    input.popScope();

    assertTrue("Form is supposed to valid because constraint is not set.", form.convertAndValidate());
  }
}
