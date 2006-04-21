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
import org.araneaframework.jsp.util.UiStdWidgetCallUtil;
import org.araneaframework.jsp.util.UiUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "eventButton"
 *   body-content = "JSP"
 *   description = "Represents an HTML form button."
 */
public class UiStdEventButtonTag extends UiEventButtonBaseTag {
	
	private static final String RENDER_BUTTON = "button";
	private static final String RENDER_INPUT = "input";
	
	protected String renderMode;	
	
	protected void init() {
		super.init();
		styleClass = "aranea-button";
		renderMode = UiStdEventButtonTag.RENDER_BUTTON;
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = 
	 *   	"Allowed values are (button | input) - the corresponding HTML tag will be used for rendering. Default is button." 
	 */
	public void setRenderMode(String renderMode) throws JspException {
		if (!(renderMode.equals(UiStdEventButtonTag.RENDER_BUTTON) || renderMode.equals(UiStdEventButtonTag.RENDER_INPUT)))
			throw new UiException("<ui:eventButton> 'renderMode' attribute must be '" + UiStdEventButtonTag.RENDER_BUTTON + "' or '"+ UiStdEventButtonTag.RENDER_INPUT+"'");
		this.renderMode = (String) evaluate("renderMode", renderMode, String.class);
	}	
	
	protected int before(Writer out) throws Exception {
		super.before(out);                
		// Write button tag
		UiUtil.writeOpenStartTag(out, renderMode.equals(UiStdEventButtonTag.RENDER_BUTTON) ? UiStdEventButtonTag.RENDER_BUTTON : UiStdEventButtonTag.RENDER_INPUT);
		if (renderMode.equals(UiStdEventButtonTag.RENDER_INPUT))
			UiUtil.writeAttribute(out, "type", "button");		
		UiUtil.writeAttribute(out, "id", id);
		UiUtil.writeAttribute(out, "class", getStyleClass());
		if(disabled != null) 
			out.write(" DISABLED ");
		if (eventId != null)
			UiStdWidgetCallUtil.writeEventAttributeForEvent(
					pageContext,
					out, 
					"onclick", 
					systemFormId, 
					contextWidgetId, 
					eventId, 
					eventParam, 
					onClickPrecondition,
					updateRegionNames);
		if (labelId != null && renderMode.equals(UiStdEventButtonTag.RENDER_INPUT)) {
			UiUtil.writeAttribute(out, "value", localizedLabel);			
		}
		if (renderMode.equals(UiStdEventButtonTag.RENDER_BUTTON))
			UiUtil.writeCloseStartTag_SS(out);			
		if (renderMode.equals(UiStdEventButtonTag.RENDER_INPUT))
			UiUtil.writeCloseStartEndTag(out);	
		
		// Continue
		return EVAL_BODY_INCLUDE;    
	}    
	
	protected int after(Writer out) throws Exception {
		if (renderMode.equals(UiStdEventButtonTag.RENDER_BUTTON)) {
			if (localizedLabel != null)
				UiUtil.writeEscaped(out, localizedLabel);
			
			UiUtil.writeEndTag(out, "button"); 		  
		}
		
		// Continue
		super.after(out);
		return EVAL_PAGE;      
	}  
}
