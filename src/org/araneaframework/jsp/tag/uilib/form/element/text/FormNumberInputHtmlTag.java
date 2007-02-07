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
public class FormNumberInputHtmlTag extends BaseFormTextInputHtmlTag {
  {
    size = null;
    baseStyleClass = "aranea-number";
  }

  protected int doEndTag(Writer out) throws Exception {
    assertControlType("NumberControl");  
    writeTextInput(out, "text");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

}
