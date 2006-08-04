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
import org.araneaframework.jsp.util.JspScriptUtil;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.TextControl;


/**
 * Standard validating text input form element base tag.
 * 
 * @author Oleg Mürk
 */
public class BaseFormValidatingTextInputHtmlTag extends BaseFormTextInputHtmlTag {
  
  //
  // Implementation
  //  
  
  protected void writeTextInputValidation(Writer out) throws Exception {
    // Prepare
    TextControl.ViewModel viewModel = ((TextControl.ViewModel)controlViewModel);

    // Write
    if (validate) writeValidationScript(out, viewModel);
  }
  
  /**
   * Write validation javascript
   * @author Konstantin Tretyakov
   */
  protected void writeValidationScript(Writer out, TextControl.ViewModel viewModel) throws IOException {
    JspUtil.writeStartTag(out, "script");    
    
    // Simple text validator
    out.write("uiAddTextInputValidator(");
    JspUtil.writeScriptString(out, getScopedFullFieldId());
    out.write(", ");
    JspUtil.writeScriptString(out, localizedLabel);
    out.write(", ");
    out.write(viewModel.isMandatory() ? "true" : "false");
    out.write(", ");
    JspUtil.writeScriptString(out, viewModel.getTextType());
    out.write(", ");    
    JspScriptUtil.writeObject(out, viewModel.getMinLength());
    out.write(", ");
    JspScriptUtil.writeObject(out, viewModel.getMaxLength()); 
    out.write(");\n");
    
    JspUtil.writeEndTag_SS(out, "script");
  }  
  
}




