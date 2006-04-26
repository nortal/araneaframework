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
import org.araneaframework.jsp.util.UiStdScriptUtil;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.NumberControl;

/**
 * Standard number input form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "numberInput"
 *   body-content = "JSP"
 *   description = "Form number input field, represents UiLib "NumberControl"."
 */
public class UiStdFormNumberInputTag extends UiStdFormTextInputBaseTag {
  public UiStdFormNumberInputTag() {
    size = null;
    baseStyleClass = "aranea-number";
    onChangePrecondition = "return true;";
  }

  protected int doEndTag(Writer out) throws Exception {
    assertControlType("NumberControl");  
    NumberControl.ViewModel viewModel = ((NumberControl.ViewModel)controlViewModel);
    writeTextInput(out, "text");

    if (validate) 
      writeValidationScript(out, viewModel);

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /**
   * Write validation javascript
   * @author Konstantin Tretyakov
   */
  protected void writeValidationScript(Writer out, NumberControl.ViewModel viewModel) throws IOException {
    UiUtil.writeStartTag(out, "script");
    out.write("uiAddIntegerValidator(");
    UiUtil.writeScriptString(out, getScopedFullFieldId());
    out.write(", ");
    UiUtil.writeScriptString(out, localizedLabel);
    out.write(", ");
    out.write(viewModel.isMandatory() ? "true" : "false");    
    out.write(", ");
    UiStdScriptUtil.writeObject(out, viewModel.getMinValue());
    out.write(", ");
    UiStdScriptUtil.writeObject(out, viewModel.getMaxValue());
    out.write(");\n");
    UiUtil.writeEndTag_SS(out, "script");
  }  
}
