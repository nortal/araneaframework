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
import org.araneaframework.uilib.form.control.FloatControl;



/**
 * Standard number float form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "floatInput"
 *   body-content = "JSP"
 *   description = "Form floating-point number input field, represents UiLib "FloatControl"."
 */
public class FormFloatInputHtmlTag extends BaseFormTextInputHtmlTag {
  {
    baseStyleClass = "aranea-float";
  }
  protected int doEndTag(Writer out) throws Exception {
    assertControlType("FloatControl");

    writeTextInput(out, "text");

    super.doEndTag(out);
    return EVAL_PAGE;
  }
}
