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
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.exception.MissingFormElementIdAraneaJspException;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;

/**
 * Base form element tag.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
@SuppressWarnings("rawtypes")
public class BaseFormElementDisplayTag extends PresentationTag implements FormElementTagInterface {

  protected FormWidget.ViewModel formViewModel;

  protected FormElement.ViewModel formElementViewModel;

  protected Control.ViewModel controlViewModel;

  private String id;

  protected String derivedId;

  protected String tabindex;

  protected boolean events = true;

  protected boolean validate = true;

  protected boolean validateOnEvent;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    this.formViewModel = (FormWidget.ViewModel) requireContextEntry(FormTag.FORM_VIEW_MODEL_KEY);
    FormWidget form = (FormWidget) requireContextEntry(FormTag.FORM_KEY);

    // In case the tag is in formElement tag
    this.derivedId = this.id;

    if (this.derivedId == null && getContextEntry(FormElementTag.ID_KEY) != null) {
      this.derivedId = (String) getContextEntry(FormElementTag.ID_KEY);
    }

    if (this.derivedId == null) {
      throw new MissingFormElementIdAraneaJspException(this);
    }

    // get control and form element view models
    this.formElementViewModel = (FormElement.ViewModel) JspWidgetUtil.traverseToSubWidget(form, this.derivedId)
        ._getViewable().getViewModel();
    this.controlViewModel = this.formElementViewModel.getControl();

    return EVAL_BODY_INCLUDE;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Element ID, can also be inherited."
   */
  public void setId(String id) throws JspException {
    this.id = evaluateNotNull("id", id, String.class);
  }

  public void setEvents(String events) throws JspException {
    this.events = (evaluateNotNull("events", events, Boolean.class)).booleanValue();
  }

  public void setValidate(String validate) throws JspException {
    this.validate = (evaluateNotNull("validate", validate, Boolean.class)).booleanValue();
  }

  public void setValidateOnEvent(String validateOnEvent) throws JspException {
    this.validateOnEvent = (evaluateNotNull("validateOnEvent", validateOnEvent, Boolean.class)).booleanValue();
  }

  public void setTabindex(String tabindex) throws JspException {
    this.tabindex = evaluateNotNull("tabindex", tabindex, String.class);
  }

  public void setUpdateRegions(String updateRegions) {}

  public void setGlobalUpdateRegions(String globalUpdateRegions) {}

  protected boolean isType(String type) {
    return this.controlViewModel.getControlType().equals(type);
  }

  /**
   * Asserts that associated control is of given type. If the condition does not hold, throws exception.
   */
  protected void assertControlType(String type) throws JspException {
    if (!this.controlViewModel.getControlType().equals(type)) {
      throw new AraneaJspException("Control of type '" + type + "' expected in form element '" + this.id
          + "' instead of '" + this.controlViewModel.getControlType() + "'");
    }
  }

  /**
   * Asserts that associated control is one of given types. If the condition does not hold, throws exception.
   * 
   * @since 2.0
   */
  protected void assertControlTypes(String... types) throws JspException {
    if (types != null) {
      for (String type : types) {
        assertControlType(type);
      }
    }
  }
}
