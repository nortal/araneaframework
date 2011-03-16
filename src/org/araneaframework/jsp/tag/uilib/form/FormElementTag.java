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

package org.araneaframework.jsp.tag.uilib.form;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;

/**
 * Specifies form element context for inner tags. Form element view model, plain ID, complete ID, control and value are
 * made accessible to inner tags as EL variables.
 * 
 * @author Jevgeni Kabanov
 * 
 * @jsp.tag
 *  name = "formElement"
 *  body-content = "JSP"
 *  description = "UiLib form element tag.<br/>Makes available following variables:<ul><li><i>formElement</i> - form element view model.</li><li><i>formElementId</i> - form element ID.</li><li><i>formElementFullId</i> - form element complete ID.</li><li><i>formElementValue</i> - form element value.</li></ul>"
 */
@SuppressWarnings("rawtypes")
public class FormElementTag extends BaseTag {

  /**
   * Attribute key for the plain ID of the form element in the context of given form. Attribute has same value as
   * <code>formElement.id</code>.
   */
  public static final String ID_KEY = "formElementId";

  /**
   * Attribute key for the complete ID of the form element. Attribute has same value as <code>formElement.scope</code>.
   * 
   * @since 2.0
   */
  public static final String FULL_ID_KEY = "formElementFullId";

  /**
   * Attribute key for the form element view-model.
   */
  public static final String VIEW_MODEL_KEY = "formElement";

  /**
   * Attribute key for the form element value. Attribute has same value as <code>formElement.value</code>.
   */
  public static final String VALUE_KEY = "formElementValue";

  /**
   * Attribute key for the form element control. Attribute has same value as <code>formElement.control</code>.
   * 
   * @since 2.0
   */
  public static final String CONTROL_KEY = "formElementControl";

  /**
   * The form element ID.
   */
  protected String id;

  /**
   * Specifies whether {@link FormElement#rendered()} was set to be called.
   */
  protected boolean markRendered;

  /**
   * The current form element view model.
   */
  protected FormElement.ViewModel formElementViewModel;

  /**
   * Optional control name that was expected from form element.
   */
  protected String expectedControl;

  @Override
  public int doStartTag(Writer out) throws Exception {
    // Get form data
    FormWidget form = (FormWidget) requireContextEntry(FormTag.FORM_KEY);

    // Get form element
    FormElement formElement = (FormElement) JspWidgetUtil.traverseToSubWidget(form, this.id);
    this.formElementViewModel = (FormElement.ViewModel) formElement._getViewable().getViewModel();
    Control.ViewModel control = this.formElementViewModel.getControl();

    if (control != null && StringUtils.isNotBlank(this.expectedControl)
        && !this.expectedControl.equals(control.getControlType())) {
      throw new AraneaJspException("Form element [" + this.id + "] control type [" + control.getControlType()
          + "] does not match the expected control type [" + this.expectedControl + "].");
    }

    if (this.markRendered) {
      formElement.rendered();
    }

    // Store data
    if (!this.id.equals(getContextEntry(ID_KEY))) {
      addContextEntry(ID_KEY, this.id);
      addContextEntry(FULL_ID_KEY, this.formElementViewModel.getScope().toString());
      addContextEntry(VIEW_MODEL_KEY, this.formElementViewModel);
      addContextEntry(VALUE_KEY, this.formElementViewModel.getValue());
      addContextEntry(CONTROL_KEY, control);
    }

    // Continue
    return EVAL_BODY_INCLUDE;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "true"
   *    description = "Required form element ID in the context of current form (set by the form tag)."
   */
  public void setId(String id) throws JspException {
    this.id = evaluateNotNull("id", id, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    description = "Optional: the expected control name for checking control correctness."
   */
  public void setExpectedControl(String expectedControl) {
    this.expectedControl = evaluate("expectedControl", expectedControl, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    description = "Optional: mark form element rendered (not rendered form element values won't be read during request)."
   */
  public void setMarkRendered(String markRendered) throws AraneaJspException {
    this.markRendered = evaluateNotNull("markRendered", markRendered, Boolean.class).booleanValue();
  }
}
