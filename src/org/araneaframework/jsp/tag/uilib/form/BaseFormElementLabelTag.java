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

import org.araneaframework.jsp.exception.AraneaJspException;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.exception.MissingFormElementIdAraneaJspException;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;

/**
 * Base form element label tag.
 * 
 * @author Oleg Mürk
 */
@SuppressWarnings("unchecked")
public class BaseFormElementLabelTag extends PresentationTag {

  protected FormWidget.ViewModel formViewModel;

  protected FormElement.ViewModel formElementViewModel;

  protected Control.ViewModel controlViewModel;

  protected String localizedLabel;

  protected String accessKeyId;

  protected String derivedId;

  protected String id;

  protected boolean showMandatory = true;

  protected boolean showColon = true;

  protected String accessKey;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Get form data
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

    this.formElementViewModel = (FormElement.ViewModel) JspWidgetUtil.traverseToSubWidget(form, this.derivedId)
        ._getViewable().getViewModel();

    // Get control
    this.controlViewModel = this.formElementViewModel.getControl();
    this.localizedLabel = JspUtil.getResourceString(this.pageContext, this.formElementViewModel.getLabel());

    if (this.accessKeyId == null) {
      // If controlViewModel.getLabel() did not specify a resource, we assume that
      // controlViewModel.getLabel() + ".access-key" will also _not_ specify a legal resource.
      this.accessKey = JspUtil.getResourceStringOrNull(this.pageContext, this.formElementViewModel.getLabel()
          + ".access-key");
    } else {
      this.accessKey = JspUtil.getResourceStringOrNull(this.pageContext, this.accessKeyId);
    }

    if (this.accessKey != null && this.accessKey.length() != 1) {
      this.accessKey = null;
    }

    // Continue
    return EVAL_BODY_INCLUDE;
  }

  protected void assertControlType(String type) throws JspException {
    if (!this.controlViewModel.getControlType().equals(type)) {
      throw new AraneaJspException("Control of type '" + type + "' expected in form element '" + this.derivedId
          + "' instead of '" + this.controlViewModel.getControlType() + "'");
    }
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Element id."
   */
  public void setId(String id) throws JspException {
    this.id = evaluateNotNull("id", id, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Whether an asterisk is shown when the element is mandatory."
   */
  public void setShowMandatory(String showMandatory) throws JspException {
    this.showMandatory = evaluateNotNull("showMandatory", showMandatory, Boolean.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Whether a colon (":") is draw after the label."
   */
  public void setShowColon(String showColon) throws JspException {
    this.showColon = evaluateNotNull("showColon", showColon, Boolean.class);
  }

  /**
   * Set the id of the resource containing the access key for this label. It may be null. In this case the default
   * resource id is used: &lt;label-id&gt;.access-key (where label-id is the resource id containing the label for the
   * element. if it exists). If such resource exists and specifies a single character, the tag outputs an additional
   * access key attribute for the HTML a &lt;label&gt; element. If given resource does not exist, it's also OK. No one
   * will die.
   */
  public void setAccessKeyId(String accessKeyId) {
    this.accessKeyId = evaluate("accessKeyId", accessKeyId, String.class);
  }
}
