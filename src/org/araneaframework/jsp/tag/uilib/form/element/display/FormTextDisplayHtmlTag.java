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

package org.araneaframework.jsp.tag.uilib.form.element.display;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementDisplayTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.DisplayControl;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *  name = "textDisplay"
 *  body-content = "empty"
 *  description = "Display element value as string, represents UiLib 'DisplayControl'."
 */
public class FormTextDisplayHtmlTag extends BaseFormElementDisplayTag {

  /**
   * A boolean setting to translate shown text.
   * 
   * @since 2.0
   */
  protected boolean localizeText;

  @Override
  protected int doEndTag(Writer out) throws Exception {
    assertControlType("DisplayControl");

    DisplayControl.ViewModel viewModel = (DisplayControl.ViewModel) controlViewModel;

    if (getStyleClass() != null) {
      JspUtil.writeOpenStartTag(out, "span");
      JspUtil.writeAttribute(out, "class", getStyleClass());
      JspUtil.writeAttribute(out, "style", getStyle());
      JspUtil.writeAttributes(out, this.attributes);
      JspUtil.writeCloseStartTag(out);
    }

    if (viewModel.getValue() != null) {
      String value = ObjectUtils.toString(viewModel.getValue().toString(), "");
      if (this.localizeText && StringUtils.isNotBlank(value)) {
        value = JspUtil.getResourceString(this.pageContext, value);
      }
      JspUtil.writeEscaped(out, value);
    }

    if (getStyleClass() != null) {
      JspUtil.writeEndTag(out, "span");
    }

    return super.doEndTag(out);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Whether to localize the text. false by default. Does not depend on configuration."
   * @since 2.0
   */
  public void setLocalizeText(String localizeText) throws JspException {
    this.localizeText = evaluateNotNull("localizeText", localizeText, Boolean.class);
  }
}
