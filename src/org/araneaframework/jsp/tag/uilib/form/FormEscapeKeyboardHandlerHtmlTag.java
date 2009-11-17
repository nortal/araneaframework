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

package org.araneaframework.jsp.tag.uilib.form;

import javax.servlet.jsp.JspException;

/**
 * {@link org.araneaframework.jsp.tag.uilib.form.FormKeyboardHandlerHtmlTag FormKeyboardHandlerHtmlTag} with
 * key="escape" by default.
 * 
 * @see org.araneaframework.jsp.tag.uilib.form.FormKeyboardHandlerHtmlTag
 * @author Konstantin Tretyakov (kt@webmedia.ee)
 * 
 * @jsp.tag
 *  name = "formEscapeKeyboardHandler"
 *  body-content = "empty"
 *  description = "Equivalent of formKeyboardHandler, but key="escape" and event="onclick" by default."
 */
public final class FormEscapeKeyboardHandlerHtmlTag extends FormKeyboardHandlerHtmlTag {

  public FormEscapeKeyboardHandlerHtmlTag() {
    this.keyCode = 27;
    this.event = "onclick";
  }

  /**
   * Throw an exception on attempt to set key. This tag supports 27 ("escape") only!
   */
  @Override
  public void setKeyCode(String key) throws JspException {
    throw new JspException("You may not set key for the escape handler tag!");
  }

  /**
   * Throw an exception on attempt to set key. This tag supports "escape" (27) only!
   */
  @Override
  public void setKeyMetaCond(String keyCode) throws JspException {
    throw new JspException("You may not set keyCode for the escape handler tag!");
  }
}
