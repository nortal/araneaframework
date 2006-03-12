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

import org.araneaframework.jsp.tag.uilib.form.element.UiStdFormButtonTag;
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
	
	private static final String MOLD_BUTTON = "button";
	private static final String MOLD_INPUT = "input";
	
	public String mold;	
	
	protected void init() {
		super.init();
		styleClass = "aranea-button";
		mold = UiStdEventButtonTag.MOLD_BUTTON;
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = 
	 *   	"Allowed values are (button | input) - the corresponding HTML tag will be used for rendering. Default is button." 
	 */
	public void setMold(String mold) throws JspException {
		this.mold = (String) evaluate("mold", mold, String.class);
	}	
	
	protected int before(Writer out) throws Exception {
		super.before(out);                
		// Write button tag
		UiUtil.writeOpenStartTag(out, mold.equals(UiStdEventButtonTag.MOLD_BUTTON) ? UiStdEventButtonTag.MOLD_BUTTON : UiStdEventButtonTag.MOLD_INPUT);
		if (mold.equals(UiStdEventButtonTag.MOLD_INPUT))
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
		if (labelId != null && mold.equals(UiStdEventButtonTag.MOLD_INPUT)) {
			UiUtil.writeAttribute(out, "value", localizedLabel);			
		}
		if (mold.equals(UiStdEventButtonTag.MOLD_BUTTON))
			UiUtil.writeCloseStartTag_SS(out);			
		if (mold.equals(UiStdEventButtonTag.MOLD_INPUT))
			UiUtil.writeCloseStartEndTag(out);	
		
		// Continue
		return EVAL_BODY_INCLUDE;    
	}    
	
	protected int after(Writer out) throws Exception {
		if (mold.equals(UiStdEventButtonTag.MOLD_BUTTON)) {
			if (localizedLabel != null)
				UiUtil.writeEscaped(out, localizedLabel);
			
			UiUtil.writeEndTag(out, "button"); 		  
		}
		
		// Continue
		super.after(out);
		return EVAL_PAGE;      
	}  
}
