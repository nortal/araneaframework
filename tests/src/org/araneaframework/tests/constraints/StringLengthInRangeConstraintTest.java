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
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.tests.constraints.helper.ConstraintTestHelper;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.StringLengthInRangeConstraint;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class StringLengthInRangeConstraintTest extends TestCase {
  private FormWidget form;
  private FormElement stringElement;
  private ConstraintTestHelper helper;

  public void setUp() throws Exception {
    form = new FormWidget();
    stringElement = form.createElement("#text", new TextControl(), new StringData(), false);
    form.addElement("string", stringElement);
    MockLifeCycle.begin(form, new MockEnvironment());

    helper = new ConstraintTestHelper(form, stringElement);
  }
  
  // test that null value validates if minLength == 0 and invalidates otherwise
  public void testEmptyValidation() throws Exception {
    helper.testConstraintValidness(new StringLengthInRangeConstraint(0, 20), null, true);
    helper.testConstraintValidness(new StringLengthInRangeConstraint(1, 20), null, false);
  }
  
  public void testValidation() throws Exception {
    helper.testConstraintValidness(new StringLengthInRangeConstraint(1, 1), "1", true);
    helper.testConstraintValidness(new StringLengthInRangeConstraint(2, 2), "1", false);
    helper.testConstraintValidness(new StringLengthInRangeConstraint(2, 2), "123", false);
    helper.testConstraintValidness(new StringLengthInRangeConstraint(0, 5), "1", true);
    helper.testConstraintValidness(new StringLengthInRangeConstraint(0, 5), "1234", true);
    helper.testConstraintValidness(new StringLengthInRangeConstraint(0, 5), "123456", false);
  }
}
