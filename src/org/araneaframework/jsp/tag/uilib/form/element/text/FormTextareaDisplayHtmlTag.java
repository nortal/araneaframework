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

package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.io.Writer;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementDisplayTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *  name = "textareaDisplay"
 *  body-content = "JSP"
 *  description = "Form textarea display field, represents UiLib 'TextareaControl'."
 */
public class FormTextareaDisplayHtmlTag extends BaseFormElementDisplayTag {

  /** @since 1.0.6 */
  protected boolean escapeSingleSpaces = true;

  public FormTextareaDisplayHtmlTag() {
    this.baseStyleClass = "aranea-textarea-display";
  }

  @Override
  @SuppressWarnings("unchecked")
  protected int doEndTag(Writer out) throws Exception {
    StringArrayRequestControl<?>.ViewModel viewModel = ((StringArrayRequestControl.ViewModel) this.controlViewModel);

    JspUtil.writeOpenStartTag(out, "span");
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttributes(out, this.attributes);
    JspUtil.writeCloseStartTag(out);

    if (viewModel.getSimpleValue() != null) {
      String text = StringEscapeUtils.escapeHtml(viewModel.getSimpleValue());
      text = StringUtils.replace(text, "\n", "<br/>\n");
      text = StringUtils.replace(text, "  ", " &nbsp;");
      if (this.escapeSingleSpaces) {
        text = StringUtils.replace(text, " ", "&nbsp;");
      }
      out.write(text);
    }

    JspUtil.writeEndTag(out, "span");
    return super.doEndTag(out);
  }

  /**
   * @since 1.0.6
   * 
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Whether all spaces (blanks) should be replace with &amp;nbsp; entities in output. Usually, two spaces are replaced to ' &amp;nbsp;'."
   */
  public void setEscapeSingleSpaces(String escapeSingleSpaces) throws Exception {
    this.escapeSingleSpaces = evaluate("escapeSingleSpaces", escapeSingleSpaces, Boolean.class);
  }
}
