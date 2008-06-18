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
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class GeneralConstraintContractTest extends TestCase {
  private FormWidget form;

  @Override
  public void setUp() throws Exception {
    form = new FormWidget();
    MockLifeCycle.begin(form, new MockEnvironment());
  }

  /** Test that field constraints cannot be set on various other widgets. */
  public void testInvalidFieldConstraintSetting() throws Exception {
    try {
      form.setConstraint(new NotEmptyConstraint());
      form.validate();

      fail("Exception should have occured, because field constraint NotEmptyConstraint is not applicable to FormWidget");
    } catch (BaseFieldConstraint.FieldConstraintException e) {
      // ok
    }
  }
  
  /** Test that setting field constraint to null works and does not throw exception */
  public void testFieldNullConstraint() throws Exception {
    FormElement el = form.createElement("#number", new FloatControl(), new BigDecimalData(), false);
    el.setConstraint(new NotEmptyConstraint());
    el.setConstraint(null);
    form.addElement("number", el);

    assertTrue("Form is supposed to valid because constraint is not set.", form.validate());
  }
  
  /** Test that setting {@link FormWidget} constraint to null works and does not throw exception */
  public void testFormNullConstraint() throws Exception {
    form.addElement("number","#number", new FloatControl(), new BigDecimalData(), false);
    form.getElement("number").setConstraint(new NotEmptyConstraint());
    form.getElement("number").setConstraint(null);

    form.setConstraint(new NotEmptyConstraint((FormElement)form.getElement("number")));
    form.setConstraint(null);

    assertTrue("Form is supposed to valid because constraint is not set.", form.validate());
  }
  
  /** Test environment propagation. */
  public void testEnvironmentPropagation() throws Exception {
    // test propagation when both formwidget and formelement are inited when constraint is set
    // and constraint is set on a formelement
	form.addElement("number","#number", new FloatControl(), new BigDecimalData(), false);
	NotEmptyConstraint constraint = new NotEmptyConstraint();
	form.getElement("number").setConstraint(constraint);
	assertTrue(EqualsBuilder.reflectionEquals(form.getElement("number").getConstraintEnvironment(), constraint.getEnvironment()));

    // test propagation when both formwidget and formelement are inited when constraint is set
    // and constraint is set on a formwidget
	form.getElement("number").setConstraint(null);
	constraint = new NotEmptyConstraint((FormElement)form.getElement("number"));
	form.setConstraint(constraint);
	assertTrue(EqualsBuilder.reflectionEquals(form.getElement("number").getConstraintEnvironment(), constraint.getEnvironment()));
	
    // test propagation when constraint is set on a formelement that is not yet
	// inited and is added to formwidget after setting the constraint
    form.removeElement("number");
    FormElement element = form.createElement("#number", new FloatControl(), new BigDecimalData(), false);
    constraint = new NotEmptyConstraint();
    element.setConstraint(constraint);
    form.addElement("number", element);
    assertTrue(EqualsBuilder.reflectionEquals(form.getElement("number").getConstraintEnvironment(), constraint.getEnvironment()));
    
    // test propagation when constraint is set on a formwidget while element is not yet inited
	// and is added to formwidget after setting the constraint
    form.removeElement("number");
    element = form.createElement("#number", new FloatControl(), new BigDecimalData(), false);
    constraint = new NotEmptyConstraint(element);
    form.setConstraint(constraint);
    form.addElement("number", element);
    assertTrue(EqualsBuilder.reflectionEquals(form.getElement("number").getConstraintEnvironment(), constraint.getEnvironment()));
    
    // test propagation when constraint is set on uninited formwidget while element is not yet inited
	// and is added to formwidget after setting the constraint
    form = new FormWidget();
    element = form.createElement("#number", new FloatControl(), new BigDecimalData(), false);
    constraint = new NotEmptyConstraint(element);
    form.setConstraint(constraint);
    form.addElement("number", element);
    MockLifeCycle.begin(form, new MockEnvironment());
    assertTrue(EqualsBuilder.reflectionEquals(form.getElement("number").getConstraintEnvironment(), constraint.getEnvironment()));
    
    // test propagation when constraint is set on uninited formelement belonging to uninited formwidget
    form = new FormWidget();
    element = form.createElement("#number", new FloatControl(), new BigDecimalData(), false);
    constraint = new NotEmptyConstraint();
    element.setConstraint(constraint);
    form.addElement("number", element);
    MockLifeCycle.begin(form, new MockEnvironment());
    assertTrue(EqualsBuilder.reflectionEquals(form.getElement("number").getConstraintEnvironment(), constraint.getEnvironment()));
  }
}
