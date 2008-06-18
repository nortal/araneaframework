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
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.AraneaAttributes;
import org.araneaframework.uilib.form.control.TextControl;

/**
 * Standard text input form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "textInput"
 *   body-content = "JSP"
 *   description = "Form text input field, represents UiLib &quot;TextControl&quot;."
 */
public class FormTextInputHtmlTag extends BaseFormTextInputHtmlTag {
  @Override
  protected int doEndTag(Writer out) throws Exception {
    assertType();

    TextControl.ViewModel viewModel = ((TextControl.ViewModel)controlViewModel);

    // Write
    Map<String, String> attributes = getCustomAttributes(viewModel);
    writeTextInput(out, "text", true, attributes);

    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /** @since 1.1 */
  protected void assertType() throws JspException {
    assertControlType("TextControl");
  }

  /** @since 1.1 */
  protected Map<String, String> getCustomAttributes(TextControl.ViewModel viewModel) {
    Map<String, String> attributes = new HashMap<String, String>();
    attributes.put("maxLength", String.valueOf(viewModel.getMaxLength()));
    if (viewModel.getInputFilter() != null) {
      attributes.put(AraneaAttributes.FilteredInputControl.CHARACTER_FILTER, viewModel.getInputFilter().getCharacterFilter());
    }
    return attributes;
  }
}




