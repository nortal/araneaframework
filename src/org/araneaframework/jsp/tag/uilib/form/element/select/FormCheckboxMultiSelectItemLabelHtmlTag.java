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

package org.araneaframework.jsp.tag.uilib.form.element.select;

import org.araneaframework.uilib.util.ConfigurationUtil;

import org.araneaframework.uilib.ConfigurationContext;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementLabelTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.MultiSelectControl;

/**
 * Form checkbox label, represents label of one item from
 * {@link org.araneaframework.uilib.form.control.MultiSelectControl}. It will be rendered with HTML &lt;span&gt; tag.
 * 
 * @author Marko Muts
 * @author Jevgeni Kabanov
 * 
 * @jsp.tag name = "checkboxMultiSelectItemLabel" body-content = "JSP" description = "Represents label to be localized."
 */
@SuppressWarnings("unchecked")
public class FormCheckboxMultiSelectItemLabelHtmlTag extends BaseFormElementLabelTag {

  protected String value;

  protected String checkboxId;

  /**
   * A boolean setting to override default configuration of {@link ConfigurationContext#LOCALIZE_FIXED_CONTROL_DATA}.
   * 
   * @since 2.0
   */
  protected Boolean localizeDisplayItems;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Prepare
    MultiSelectControl.ViewModel viewModel = ((MultiSelectControl.ViewModel) controlViewModel);

    if (this.value != null && viewModel.getSelectItem(this.value) == null) {
      throw new AraneaJspException("Value '" + this.value + "' not found in values list.");
    }

    writeLabel(out, viewModel.getSelectItem(this.value).getLabel());
    return EVAL_BODY_INCLUDE;
  }

  /** @since 1.1 */
  public void writeLabel(Writer out, String label) throws Exception {
    JspUtil.writeOpenStartTag(out, "span");
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeCloseStartTag_SS(out);

    JspUtil.writeOpenStartTag(out, "label");
    JspUtil.writeAttribute(out, "for", this.checkboxId);
    JspUtil.writeCloseStartTag_SS(out);
    JspUtil.writeEscaped(out, evaluateLabel(label));
    JspUtil.writeEndTag_SS(out, "label");

    JspUtil.writeEndTag(out, "span");
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "The HTML id of checkbox to which this label belongs."
   * @since 1.1
   */
  public void setCheckboxId(String checkboxId) {
    this.checkboxId = evaluate("checkboxId", checkboxId, String.class);
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Select item value."
   */
  public void setValue(String value) throws JspException {
    this.value = evaluateNotNull("value", value, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Whether to localize display items. Provides a way to override ConfigurationContext.LOCALIZE_FIXED_CONTROL_DATA."
   * @since 1.1
   */
  public void setLocalizeDisplayItems(String localizeDisplayItems) {
    this.localizeDisplayItems = evaluate("localizeDisplayItems", localizeDisplayItems, Boolean.class);
  }

  protected String evaluateLabel(String value) {
    this.localizeDisplayItems = ConfigurationUtil.isLocalizeControlData(getEnvironment(), this.localizeDisplayItems);
    if (this.localizeDisplayItems.booleanValue()) {
      value = JspUtil.getResourceString(this.pageContext, value);
    }
    return value;
  }
}
