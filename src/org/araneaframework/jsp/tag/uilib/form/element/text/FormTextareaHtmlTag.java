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

package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;


/**
 * Standard text input form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "textarea"
 *   body-content = "JSP"
 *   description = "Form text input field (textarea), represents UiLib 'TextareaControl'."
 */
public class FormTextareaHtmlTag extends BaseFormElementHtmlTag {
  protected Long cols;
  protected Long rows;

  {
    baseStyleClass = "aranea-textarea";
  }
  
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    return EVAL_BODY_INCLUDE;
  }

protected int doEndTag(Writer out) throws Exception {
    assertControlType("TextareaControl");    
    
    String name = this.getScopedFullFieldId();     
    StringArrayRequestControl.ViewModel viewModel = ((StringArrayRequestControl.ViewModel)controlViewModel);
    
    JspUtil.writeOpenStartTag(out, "textarea");
    JspUtil.writeAttribute(out, "id", name);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    
    JspUtil.writeAttribute(out, "cols", cols);
    JspUtil.writeAttribute(out, "rows", rows);
    JspUtil.writeAttribute(out, "label", localizedLabel);
    JspUtil.writeAttribute(out, "tabindex", tabindex);
    if (viewModel.isDisabled())
      JspUtil.writeAttribute(out, "disabled", "true");
    JspUtil.writeAttributes(out, attributes);
    JspUtil.writeCloseStartTag(out);
    JspUtil.writeEscaped(out, viewModel.getSimpleValue());
    JspUtil.writeEndTag_SS(out, "textarea");
    if (!StringUtils.isBlank(accessKey))
      JspUtil.writeAttribute(out, "accesskey", accessKey);
    
    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false" 
   *   description = "Number of visible columns."
   */
  public void setCols(String size) throws JspException {
    this.cols = (Long)evaluate("cols", size, Long.class);
  }
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false" 
   *   description = "Number of visible rows."
   */
  public void setRows(String size) throws JspException {
    this.rows = (Long)evaluate("rows", size, Long.class);
  }
}
