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

package org.araneaframework.jsp.tag.basic;

import java.io.Writer;

import javax.servlet.jsp.JspException;

/**
 * Invokes a "uiRegisterKeyboardHandler" javascript.
 * 
 * @author Konstantin Tretyakov (kt@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "keyboardHandler"
 *   body-content = "empty"
 *   description = "Registers a simple javascript keyboard handler."
 *   
 */
public class UiKeyboardHandlerTag extends UiKeyboardHandlerBaseTag{
  
	  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "When a keyboard event happens, it is usually associated with 
          a certain form element / form / widget / etc. The object with which an event is associated
          is identified by a hierarchical id (e.g. there may be widget 'somelist', containing
          form 'somelist.form', containing textbox 'somelist.form.textbox'.
          The scope is a prefix of that id that must match in order for the handler to be triggered.
          For example, the handler with scope='somelist.form.textbox' will be triggered only when
          the event in the textbox occurs, but the handler with scope="somelist" will be triggered when
          any event in any of the elements inside any of the forms of "somelist" occurs. I.e. for any
          element with ID beginning with 'somelist'.
          When scope is not specified, a global handler is registered, that reacts to an event in any form/widget"
	 */
	public void setScope(String scope) throws JspException{
	  this.scope = (String) evaluate("scope", scope, String.class);
	}
	
	/**
	 * The handler to invoke when an event happens.
	 * @param handler must be a javascript function taking 2 arguments: the event object and the id of the
	 *  element where an event occured. For example:
	 *  <pre>
	 *    function (event, elementId) { alert('this is an event for element ' + elementId); }
	 *  </pre>
	 *  There must be no semicolon at the end.
	 * 
	 * @jsp.attribute
	 *   type = "java.lang.String" 
	 *   required = "true"
	 *   description = "A javascript handler function that takes two parameters - the event object and the element id for which 
          the event was fired. 
          Example: function(event, elementId) { alert(elementId); }" 
	 */
	public void setHandler(String handler) throws JspException {
		this.handler = (String) evaluate("handler", handler, String.class);
	}
	

	protected int before(Writer out) throws Exception {
		super.before(out);
		writeRegisterKeypressHandlerScript(out, scope, keyCode, handler);
		return SKIP_BODY;
	}
	
	protected void init() {
		super.init();
		scope = handler = null;
	}
	
	// Tag attributes
	protected String scope;
	protected String handler;
}
