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

import junit.framework.TestCase;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.BaseFieldConstraint;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.data.BigDecimalData;

/**
 * Tests general {@link org.araneaframework.uilib.form.Constraint} behaviour.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class GeneralConstraintContractTest extends TestCase {

  private FormWidget form;

  @Override
  public void setUp() throws Exception {
    this.form = new FormWidget();
    MockLifeCycle.begin(this.form, new MockEnvironment());
  }

  /** Test that field constraints cannot be set on various other widgets. */
  public void testInvalidFieldConstraintSetting() throws Exception {
    try {
      this.form.setConstraint(new NotEmptyConstraint<Object, Object>());
      this.form.validate();

      fail("Exception should have occured, because field constraint NotEmptyConstraint is not applicable to FormWidget");
    } catch (BaseFieldConstraint.FieldConstraintException e) {
      // OK
    }
  }

  /** Test that setting field constraint to null works and does not throw exception */
  public void testFieldNullConstraint() throws Exception {
    FormElement<BigDecimal, BigDecimal> el = this.form.createElement("#number", new FloatControl(), new BigDecimalData());
    el.setConstraint(new NotEmptyConstraint<BigDecimal, BigDecimal>());
    el.setConstraint(null);
    this.form.addElement("number", el);
    assertTrue("Form is supposed to valid because constraint is not set.", this.form.validate());
  }

  /** Test that setting {@link FormWidget} constraint to null works and does not throw exception */
  @SuppressWarnings("unchecked")
  public void testFormNullConstraint() throws Exception {
    this.form.addElement("number", "#number", new FloatControl(), new BigDecimalData(), false);
    this.form.getElement("number").setConstraint(new NotEmptyConstraint());
    this.form.getElement("number").setConstraint(null);

    this.form.setConstraint(new NotEmptyConstraint((FormElement) this.form.getElement("number")));
    this.form.setConstraint(null);

    assertTrue("Form is supposed to valid because constraint is not set.", this.form.validate());
  }

  /** Test environment propagation. */
  @SuppressWarnings("unchecked")
  public void testEnvironmentPropagation() throws Exception {
    // test propagation when both form widget and form element are initiated when constraint is set
    // and constraint is set on a form element
    this.form.addElement("number", "#number", new FloatControl(), new BigDecimalData(), false);
    NotEmptyConstraint constraint = new NotEmptyConstraint();
    this.form.getElement("number").setConstraint(constraint);
    assertTrue(EqualsBuilder.reflectionEquals(this.form.getElement("number").getConstraintEnvironment(), constraint
        .getEnvironment()));

    // test propagation when both form widget and form element are initiated when constraint is set
    // and constraint is set on a form widget
    this.form.getElement("number").setConstraint(null);
    constraint = new NotEmptyConstraint((FormElement) this.form.getElement("number"));
    this.form.setConstraint(constraint);
    assertTrue(EqualsBuilder.reflectionEquals(this.form.getElement("number").getConstraintEnvironment(), constraint
        .getEnvironment()));

    // test propagation when constraint is set on a form element that is not yet
    // initiated and is added to form widget after setting the constraint
    this.form.removeElement("number");
    FormElement element = this.form.createElement("#number", new FloatControl(), new BigDecimalData(), false);
    constraint = new NotEmptyConstraint();
    element.setConstraint(constraint);
    this.form.addElement("number", element);
    assertTrue(EqualsBuilder.reflectionEquals(this.form.getElement("number").getConstraintEnvironment(), constraint
        .getEnvironment()));

    // test propagation when constraint is set on a form widget while element is not yet initiated
    // and is added to form widget after setting the constraint
    this.form.removeElement("number");
    element = this.form.createElement("#number", new FloatControl(), new BigDecimalData(), false);
    constraint = new NotEmptyConstraint(element);
    this.form.setConstraint(constraint);
    this.form.addElement("number", element);
    assertTrue(EqualsBuilder.reflectionEquals(this.form.getElement("number").getConstraintEnvironment(), constraint
        .getEnvironment()));

    // test propagation when constraint is set on uninitiated form widget while element is not yet initiated
    // and is added to form widget after setting the constraint
    this.form = new FormWidget();
    element = this.form.createElement("#number", new FloatControl(), new BigDecimalData(), false);
    constraint = new NotEmptyConstraint(element);
    this.form.setConstraint(constraint);
    this.form.addElement("number", element);
    MockLifeCycle.begin(this.form, new MockEnvironment());
    assertTrue(EqualsBuilder.reflectionEquals(this.form.getElement("number").getConstraintEnvironment(), constraint
        .getEnvironment()));

    // test propagation when constraint is set on uninitiated form element belonging to uninitiated form widget
    this.form = new FormWidget();
    element = this.form.createElement("#number", new FloatControl(), new BigDecimalData(), false);
    constraint = new NotEmptyConstraint();
    element.setConstraint(constraint);
    this.form.addElement("number", element);
    MockLifeCycle.begin(this.form, new MockEnvironment());
    assertTrue(EqualsBuilder.reflectionEquals(this.form.getElement("number").getConstraintEnvironment(), constraint
        .getEnvironment()));
  }
}
