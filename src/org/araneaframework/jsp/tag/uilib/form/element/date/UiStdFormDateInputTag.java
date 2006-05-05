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

package org.araneaframework.jsp.tag.uilib.form.element.date;

import java.io.IOException;
import java.io.Writer;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;



/**
 * Standard date input form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "dateInput"
 *   body-content = "JSP"
 *   description = "Form date input field (custom control), represents UiLib "DateControl"."
 */
public class UiStdFormDateInputTag extends UiStdFormDateTimeInputBaseTag {
  public UiStdFormDateInputTag() {
    baseStyleClass = "aranea-date";
  }

  protected int doEndTag(Writer out) throws Exception {
    assertControlType("DateControl");    

    // Prepare
    String name = this.getScopedFullFieldId();       
    StringArrayRequestControl.ViewModel viewModel = ((StringArrayRequestControl.ViewModel)controlViewModel);

    Long dateInputSize = DEFAULT_DATE_INPUT_SIZE;

    this.writeDateInput(
        out,
        name,
        name, 
        viewModel.getSimpleValue(), 
        localizedLabel,
        viewModel.isMandatory(), 
        formElementViewModel.isValid(),
        dateInputSize,
        validate,
        viewModel.isDisabled(),
        getStyleClass(),
        accessKey,
        viewModel);

    if (validate) 
      writeValidationScript(out, viewModel);

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /**
   * Write validation javascript
   * @author Konstantin Tretyakov
   */
  protected void writeValidationScript(Writer out, StringArrayRequestControl.ViewModel viewModel) throws IOException {
    UiUtil.writeStartTag(out, "script");
    out.write("uiAddDateValidator(");
    UiUtil.writeScriptString(out, getScopedFullFieldId());
    out.write(", ");
    UiUtil.writeScriptString(out, localizedLabel);
    out.write(", ");
    out.write(viewModel.isMandatory() ? "true" : "false");
    out.write(");\n");
    UiUtil.writeEndTag_SS(out, "script");
  }     
}




