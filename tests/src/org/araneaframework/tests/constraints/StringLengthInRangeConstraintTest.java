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

  private FormElement<String, String> stringElement;

  private ConstraintTestHelper helper;

  @Override
  public void setUp() throws Exception {
    this.form = new FormWidget();
    this.stringElement = this.form.createElement("#text", new TextControl(), new StringData(), false);
    this.form.addElement("string", this.stringElement);
    MockLifeCycle.begin(this.form, new MockEnvironment());
    this.helper = new ConstraintTestHelper(this.form, this.stringElement);
  }

  // test that null value validates if minLength == 0 and invalidates otherwise
  public void testEmptyValidation() throws Exception {
    this.helper.testConstraintValidness(new StringLengthInRangeConstraint(0, 20), null, true);
    this.helper.testConstraintValidness(new StringLengthInRangeConstraint(1, 20), null, false);
  }

  public void testValidation() throws Exception {
    this.helper.testConstraintValidness(new StringLengthInRangeConstraint(1, 1), "1", true);
    this.helper.testConstraintValidness(new StringLengthInRangeConstraint(2, 2), "1", false);
    this.helper.testConstraintValidness(new StringLengthInRangeConstraint(2, 2), "123", false);
    this.helper.testConstraintValidness(new StringLengthInRangeConstraint(0, 5), "1", true);
    this.helper.testConstraintValidness(new StringLengthInRangeConstraint(0, 5), "1234", true);
    this.helper.testConstraintValidness(new StringLengthInRangeConstraint(0, 5), "123456", false);
  }
}
