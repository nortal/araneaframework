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

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementLabelTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.util.ConfigurationUtil;

/**
 * Standard form element label tag.
 * 
 * @author Marko Muts
 * @jsp.tag
 *  name = "radioSelectItemLabel"
 *  body-content = "JSP"
 *  description = "Represents label to be localized."
 */
public class FormRadioSelectItemLabelHtmlTag extends BaseFormElementLabelTag {

  protected String value;

  /** @since 1.1 */
  protected String radioId;

  /** @since 2.0 */
  protected Boolean localizeLabel = false;

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    writeLabel(out, getSelectItemLabel());
    return EVAL_BODY_INCLUDE;
  }

  protected String getSelectItemLabel() throws Exception {
    assertControlType("SelectControl");
    String label = this.value;
    if (ConfigurationUtil.isLocalizeControlData(getEnvironment(), this.localizeLabel)) {
      label = JspUtil.getResourceStringOrNull(this.pageContext, label);
    }
    return StringUtils.defaultString(label);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Select item value."
   */
  public void setValue(String value) throws JspException {
    this.value = evaluateNotNull("value", value, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "The HTML id radio button to which this label belongs."
   * @since 1.1
   */
  public void setRadioId(String radioId) {
    this.radioId = evaluate("radioId", radioId, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "The HTML id radio button to which this label belongs."
   * @since 2.0
   */
  public void setLocalizeLabel(String localizeLabel) {
    this.localizeLabel = evaluate("localizeLabel", localizeLabel, Boolean.class);
  }

  /**
   * @since 1.1
   */
  public void writeLabel(Writer out, String label) throws Exception {
    out.write("\n");
    
    JspUtil.writeOpenStartTag(out, "span");
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeCloseStartTag_SS(out);

    JspUtil.writeOpenStartTag(out, "label");
    JspUtil.writeAttribute(out, "for", this.radioId);
    JspUtil.writeCloseStartTag_SS(out);
    JspUtil.writeEscaped(out, label);
    JspUtil.writeEndTag_SS(out, "label");

    JspUtil.writeEndTag(out, "span");
  }
}
