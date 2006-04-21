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

package org.araneaframework.jsp.tag.presentation;			

import java.io.Writer;

import javax.servlet.jsp.JspException;

import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.tag.uilib.form.element.UiStdFormButtonTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * Standard button tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "basicButton"
 *   body-content = "JSP"
 *   description = "Represents an HTML form button."
 */
public class UiStdButtonTag extends UiButtonBaseTag {
	  //
	  // Implementation
	  //  		
	
	private static final String RENDER_BUTTON = "button";
	private static final String RENDER_INPUT = "input";
	
	protected String renderMode;
	
	protected void init() {
		super.init();
		styleClass = "aranea-button";
		renderMode = UiStdButtonTag.RENDER_BUTTON;
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = 
	 *   	"Allowed values are (button | input) - the corresponding HTML tag will be used for rendering. Default is button." 
	 */
	public void setRenderMode(String renderMode) throws JspException {
		if (!(renderMode.equals(UiStdButtonTag.RENDER_BUTTON) || renderMode.equals(UiStdButtonTag.RENDER_INPUT)))
			throw new UiException("<ui:basicButton> 'renderMode' attribute must be '" + UiStdButtonTag.RENDER_BUTTON + "' or '"+ UiStdButtonTag.RENDER_INPUT+"'");
		this.renderMode = (String) evaluate("renderMode", renderMode, String.class);
	}	
  
  protected int before(Writer out) throws Exception {
    super.before(out);

    UiUtil.writeOpenStartTag(out, renderMode.equals(UiStdButtonTag.RENDER_BUTTON) ? UiStdButtonTag.RENDER_BUTTON : UiStdButtonTag.RENDER_INPUT);
	if (renderMode.equals(UiStdButtonTag.RENDER_INPUT))
		UiUtil.writeAttribute(out, "type", "button");    
    UiUtil.writeAttribute(out, "id", id);
    UiUtil.writeAttribute(out, "class", getStyleClass());
    UiUtil.writeAttribute(out, "onclick", onclick);
	if (labelId != null && renderMode.equals(UiStdButtonTag.RENDER_INPUT)) {
		UiUtil.writeAttribute(out, "value", UiUtil.getResourceString(pageContext, labelId));			
	}
	if (renderMode.equals(UiStdButtonTag.RENDER_BUTTON))
		UiUtil.writeCloseStartTag_SS(out);			
	if (renderMode.equals(UiStdButtonTag.RENDER_INPUT))
		UiUtil.writeCloseStartEndTag(out);			
    
    // Continue
    return EVAL_BODY_INCLUDE;    
  }
  
  protected int after(Writer out) throws Exception {	
	  if (renderMode.equals(UiStdButtonTag.RENDER_BUTTON)) {
		  if (labelId != null)						
			  UiUtil.writeEscaped(out, UiUtil.getResourceString(pageContext, labelId));
		  UiUtil.writeEndTag(out, "button"); 		  
	  }
	  
	  // Continue
	  super.after(out);
	  return EVAL_PAGE;      
  }
}




