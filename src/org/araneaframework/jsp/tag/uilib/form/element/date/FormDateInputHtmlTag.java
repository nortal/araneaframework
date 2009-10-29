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

package org.araneaframework.jsp.tag.uilib.form.element.date;

import java.io.Writer;
import org.araneaframework.uilib.form.control.DateControl;

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
public class FormDateInputHtmlTag extends BaseFormDateTimeInputHtmlTag {

  {
    baseStyleClass = "aranea-date";
  }
  @Override
  protected int doEndTag(Writer out) throws Exception {
    assertControlType("DateControl");    

    // Prepare
    String name = this.getFullFieldId();       
    DateControl.ViewModel viewModel = ((DateControl.ViewModel)controlViewModel);

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
        viewModel.isDisabled(),
        getStyleClass(),
        accessKey,
        viewModel);

    super.doEndTag(out);
    return EVAL_PAGE;
  }
}




