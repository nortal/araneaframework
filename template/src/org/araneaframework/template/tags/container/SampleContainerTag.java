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

package org.araneaframework.template.tags.container;


import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * Template standard container base tag.
 * 
 * @author Oleg Mürk
 * @author Taimo Peelo (taimo@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "container"
 *   body-content = "JSP"
 */
public class SampleContainerTag extends UiBaseTag {
	public final static String STYLE_CLASS = "component";
	
	protected String width;
	protected String height;
	protected String styleClass;
	
	protected void init() {
		super.init();
		styleClass = SampleContainerTag.STYLE_CLASS;
	}
	
	/************************************************************
	 * ATTRIBUTES
	 ************************************************************/
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Container width"
	 */
	public void setWidth(String width) throws JspException {
		this.width = (String)evaluate("width", width, String.class);    
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Container width"
	 */
	public void setHeight(String height) throws JspException {
		this.height = (String)evaluate("height", height, String.class);
	}
	
	protected int before(Writer out) throws Exception {
		super.before(out);
		
		UiUtil.writeOpenStartTag(out, "div");
		UiUtil.writeAttribute(out, "class", styleClass);
		UiUtil.writeCloseStartTag(out);
		
		UiUtil.writeOpenStartTag(out, "div");
		UiUtil.writeAttribute(out, "class", "w100p");
		UiUtil.writeCloseStartTag(out);
		
		return EVAL_BODY_INCLUDE;			
	}		
	
	protected int after(Writer out) throws Exception {

		UiUtil.writeEndTag(out, "div");
		UiUtil.writeEndTag(out, "div");

		super.after(out);
		return EVAL_PAGE;
	}
}
