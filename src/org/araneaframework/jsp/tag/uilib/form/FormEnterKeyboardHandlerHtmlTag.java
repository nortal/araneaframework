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
 * keyCode="13" by default.
 * 
 * @see org.araneaframework.jsp.tag.uilib.form.FormKeyboardHandlerHtmlTag
 * @author Konstantin Tretyakov (kt@webmedia.ee)
 * 
 * @jsp.tag
 *  name = "formEnterKeyboardHandler"
 *  body-content = "empty"
 *  description = "Equivalent of formKeyboardHandler, but keyCode="13" and event='onclick' by default. As usually, the parameters elementId/fullElementId/handler must be provided."
 */
public final class FormEnterKeyboardHandlerHtmlTag extends FormKeyboardHandlerHtmlTag {

  public FormEnterKeyboardHandlerHtmlTag() {
    this.keyCode = 13;
    this.event = "onclick";
  }

  /**
   * Throw an exception on attempt to set key. This tag supports 13 ("enter") only!
   */
  @Override
  public void setKeyCode(String keyCode) throws JspException {
    throw new JspException("You may not set 'keyCode' for the enter handler tag!");
  }

  /**
   * Throw an exception on attempt to set key meta condition. This tag supports "enter" (13) only!
   */
  @Override
  public void setKeyMetaCond(String keyMetaCond) throws JspException {
    throw new JspException("You may not set 'keyMetaCond' for the enter handler tag!");
  }
}
