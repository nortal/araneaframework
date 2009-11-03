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

package org.araneaframework.example.main.web.sample;

import org.araneaframework.uilib.form.FormElement;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.AndConstraint;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.constraint.OrConstraint;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * A demo widget that shows different ways how complex constraints are created and assigned to forms.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class FormComplexConstraintDemoWidget extends TemplateBaseWidget {

  private FormWidget searchForm;

  /**
   * This method creates the form and also shows how the complex constraint is created and assigned to the form.
   */
  @Override
  protected void init() throws Exception {
    setViewSelector("sample/searchForm");

    // Adding form controls
    this.searchForm = new FormWidget();
    FormElement<String, String> firstName = addElement("clientFirstName", "#Client first name");
    FormElement<String, String> lastName = addElement("clientLastName", "#Client last name");
    FormElement<String, String> personalId = addElement("clientPersonalId", "#Client personal id");
    FormElement<String, String> town = addElement("clientAddressTown", "#Town");
    FormElement<String, String> street = addElement("clientAddressStreet", "#Street");
    FormElement<String, String> house = addElement("clientAddressHouse", "#House");
    this.searchForm.addElement("search", "#Search", new ButtonControl());

    //
    // Now creating a complex constraint that must fulfill to validate.
    //

    // First searching scenario
    AndConstraint clientNameConstraint = new AndConstraint();
    clientNameConstraint.addConstraint(notEmpty(firstName));
    clientNameConstraint.addConstraint(notEmpty(lastName));

    // Second searching scenario
    NotEmptyConstraint<String, String> clientPersonalIdConstraint = notEmpty(personalId);

    // Third searching scenario
    AndConstraint clientAddressConstraint = new AndConstraint();
    clientAddressConstraint.addConstraint(notEmpty(town));
    clientAddressConstraint.addConstraint(notEmpty(street));
    clientAddressConstraint.addConstraint(notEmpty(house));

    // Combining scenarios
    OrConstraint searchConstraint = new OrConstraint();
    searchConstraint.addConstraint(clientPersonalIdConstraint);
    searchConstraint.addConstraint(clientNameConstraint);
    searchConstraint.addConstraint(clientAddressConstraint);
    // Setting custom error message:
    searchConstraint.setCustomErrorMessage("Not enough data! Please fill in either (client first and last name) or (client personal id) or (client town, street and number)");

    // Setting constraint:
    this.searchForm.setConstraint(searchConstraint);

    addWidget("searchForm", this.searchForm);
  }

  /**
   * Since the controls and data are mostly the same for form elements, this method takes the ID and label for each form
   * element to be created, specifies the same control and data, and returns newly created form element.
   * 
   * @param id The ID that should be assigned to the new form element.
   * @param label The label that should be assigned to the new form element.
   * @return The new form element.
   */
  private FormElement<String, String> addElement(String id, String label) {
    return this.searchForm.addElement(id, label, new TextControl(), new StringData(), false);
  }

  /**
   * A short-named method that creates a {@link NotEmptyConstraint} for given <code>formElement</code>.
   * 
   * @param formElement The form element that must fulfill the not empty condition.
   * @return The not-empty-constraint for given form element.
   */
  private NotEmptyConstraint<String, String> notEmpty(FormElement<String, String> formElement) {
    return new NotEmptyConstraint<String, String>(formElement);
  }

  /**
   * The event handler for the "Search" button.
   */
  public void handleEventSearch() {
    if (this.searchForm.convertAndValidate()) {
      getMessageCtx().showMessage(MessageContext.INFO_TYPE, "Search allowed!");
    }
  }

  /**
   * The event handler for the "Back" button.
   */
  public void handleEventReturn() {
    getFlowCtx().cancel();
  }
}
