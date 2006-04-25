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

package org.araneaframework.jsp.tag.uilib.form;

import javax.servlet.jsp.JspException;


/**
 * {@link org.araneaframework.jsp.tag.uilib.form.UiFormKeyboardHandlerTag UiFormKeyboardHandlerTag} with key="enter" by default.
 *
 * @see org.araneaframework.jsp.tag.uilib.form.UiFormKeyboardHandlerTag
 * @author Konstantin Tretyakov (kt@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "formEnterKeyboardHandler"
 *   body-content = "empty"
 *   description = "Equivalent of formKeyboardHandler, but key="enter" and event="onclick" by default."
 */
public final class UiFormEnterKeyboardHandlerTag extends UiFormKeyboardHandlerTag {
	{
		key = "enter";
		event = "onclick";
	}

	/**
	 * Throw an exception on attempt to set key. This tag supports "enter" only!
	 */
	public void setKey(String key) throws JspException {
		throw new JspException("You may not set key for the enter handler tag!");
	}

	/**
	 * @see #setKey
	 */
	public void setKeyCode(String keyCode) throws JspException {
		throw new JspException("You may not set keyCode for the enter handler tag!");
	}
}
