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

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;


/**
 * Standard text input form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "textarea"
 *   body-content = "JSP"
 *   description = "Form text input field (textarea), represents UiLib "TextareaControl"."
 */
public class UiStdFormTextareaTag extends UiFormElementBaseTag {
  protected Long cols;
  protected Long rows;
  
  public UiStdFormTextareaTag() {
    styleClass = "aranea-textarea";
  }
  
  protected int doEndTag(Writer out) throws Exception {
    assertControlType("TextareaControl");    
    
    String name = this.getScopedFullFieldId();     
    StringArrayRequestControl.ViewModel viewModel = ((StringArrayRequestControl.ViewModel)controlViewModel);
    
    UiUtil.writeOpenStartTag(out, "textarea");
    UiUtil.writeAttribute(out, "id", name);
    UiUtil.writeAttribute(out, "name", name);
    UiUtil.writeAttribute(out, "class", getStyleClass());
    
    UiUtil.writeAttribute(out, "cols", cols);
    UiUtil.writeAttribute(out, "rows", rows);
    UiUtil.writeAttribute(out, "label", localizedLabel);
    UiUtil.writeAttribute(out, "tabindex", tabindex);
    if (viewModel.isDisabled())
      UiUtil.writeAttribute(out, "disabled", "true");
    UiUtil.writeAttributes(out, attributes);
    UiUtil.writeCloseStartTag(out);
    UiUtil.writeEscaped(out, viewModel.getSimpleValue());
    UiUtil.writeEndTag_SS(out, "textarea");
    if (!StringUtils.isBlank(accessKey))
      UiUtil.writeAttribute(out, "accesskey", accessKey);
    if (validate) 
      writeValidationScript(out, viewModel);
    
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
  
  /**
   * Write validation javascript
   * @author Konstantin Tretyakov
   */
  protected void writeValidationScript(Writer out, StringArrayRequestControl.ViewModel viewModel) throws IOException {
    UiUtil.writeStartTag(out, "script");
    out.write("uiAddTextAreaValidator(");
    UiUtil.writeScriptString(out, getScopedFullFieldId());
    out.write(", ");
    UiUtil.writeScriptString(out, localizedLabel);
    out.write(", ");
    out.write(viewModel.isMandatory() ? "true" : "false");
    out.write(");\n");
    UiUtil.writeEndTag_SS(out, "script");
  }    
}