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

package org.araneaframework.example.main.web.form;

import java.math.BigInteger;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.LocalFormElementValidationErrorRenderer;
import org.araneaframework.uilib.form.constraint.BaseFieldConstraint;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.data.IntegerData;

/**
 * This widget demonstrates different options for customizing form element validation:
 * <ol>
 * <li>the first form element has default behaviour
 * <li>the second form element validates its value immediately with AJAX when focus leaves from the input
 * <li>the third form element adds custom prime number constraint
 * <li>the fourth form element has custom error message renderer.
 * </ol>
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 */
public final class ValidationFormWidget extends TemplateBaseWidget {

  // Here is our form used in this demo:
  private FormWidget validationForm = new FormWidget();

  /**
   * Since all four elements are very similar, this method handles the creation of each element. NumberControl is used
   * for gathering input, and at the control level, the input value is limited to values range from 1 to 999.
   * 
   * @param id The ID for the new element.
   * @param label The label ID for the new element.
   * @return The created element.
   */
  private FormElement<?, ?> addElement(String id, String label) {
    return this.validationForm.addElement(id, label, new NumberControl(BigInteger.ONE, BigInteger.valueOf(999)),
        new IntegerData(), true);
  }

  @Override
  protected void init() {
    // We need to define four form elements for gathering and validating input.

    // The first element has default behaviour
    addElement("first", "form.valid.default");

    // The second element immediately validates when its value changes (JavaScript "onchange" event is triggered).
    // Note that this feature can also be enabled on all form elements by calling:
    // this.validationForm.setBackgroundValidation(true);
    // or by setting it globally through configuration context (see the reference manual for more information).
    FormElement<?, ?> second = addElement("second", "form.valid.background");
    second.setBackgroundValidation(true);
    // Background validation can be further customized through:
    // second.setBackgroundValidationListener(actionListener);

    // The third element has additional validation logic (constraint) making sure the input is a prime number.
    FormElement<?, ?> third = addElement("third", "form.valid.constraint");
    third.setConstraint(new PrimeNumberValidator());

    // The fourth element has a custom error message renderer.
    FormElement<?, ?> fourth = addElement("fourth", "form.valid.renderer");
    fourth.setFormElementValidationErrorRenderer(new LocalFormElementValidationErrorRenderer());

    // Now we need to only register the form and the JSP page.
    addWidget("validationForm", this.validationForm);
    setViewSelector("form/validationForm");
  }

  /**
   * Event handler that just validates the form.
   */
  public void handleEventValidate() {
    this.validationForm.convertAndValidate();
  }

  /**
   * A prime number validator that checks whether provided integer is a prime number. Note that the control is mandatory
   * and that it accepts values from 1 to 999. Therefore we don't have to worry about negative number or null values.
   */
  private static final class PrimeNumberValidator extends BaseFieldConstraint<BigInteger, Integer> {

    @Override
    protected void validateConstraint() {
      int num = getValue(); // The null value never reaches this line, because form element value is mandatory.
      boolean prime = true;

      for (int i = 2; i < num; i++) {
        if (num % i == 0) {
          prime = false;
          break;
        }
      }

      if (!prime) {
        addError("form.msg.invalidPrimeNum", t(getLabel()));
      }
    }
  }

}
