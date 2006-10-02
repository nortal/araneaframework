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

package org.araneaframework.tests.constraints.helper;

import org.araneaframework.uilib.form.Constraint;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;

/**
 * Helper for testing {@link Constraint}s on a {@link FormWidget} with exactly one
 * {@link FormElement}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ConstraintTestHelper {
  private FormWidget form;
  private FormElement element;
  
  public ConstraintTestHelper(FormWidget form, FormElement element) {
    org.araneaframework.core.Assert.notNullParam(this, form, "form");
    org.araneaframework.core.Assert.notNullParam(this, element, "element");

    this.form = form;
    this.element = element;
  }
  
  /**
   * Tests validness of {@link FormWidget} and {@link FormElement}, after 
   * {@link FormElement} value is set.
   */
  public void testConstraintValidness(Constraint constraint, Object value, boolean valid) throws Exception {
    element.setConstraint(constraint);
    element.setValue(value);
    element.getControl().setRawValue(value);

    junit.framework.Assert.assertEquals(valid, element.validate());
    form.clearErrors();
    junit.framework.Assert.assertEquals(valid, form.validate());
    form.clearErrors(); // allows using more than once per setUp() 
  }
}
