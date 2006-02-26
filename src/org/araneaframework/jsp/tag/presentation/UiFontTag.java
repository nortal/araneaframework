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
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * Standard font tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "font"
 *   body-content = "JSP"
 *   description = "Acts as <i>&lt;font&gt;</i> HTML tag."
 */
public class UiFontTag extends UiBaseTag {
  
  //
  // Attributes
  //
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Font face." 
	 */
  public void setFace(String face) throws JspException {
    this.face = (String)evaluate("face", face, String.class);
  }
  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Font size." 
	 */
  public void setSize(String size) throws JspException {
    this.size = (String)evaluate("size", size, String.class);
  }
  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Font color (any HTML valid color)." 
	 */
  public void setColor(String color) throws JspException {
    this.color = (String)evaluate("color", color, String.class);
  }
  
  //
  // Implementation
  //
    
  protected int before(Writer out) throws Exception {
    super.before(out);
    
    UiUtil.writeOpenStartTag(out, "font");
    UiUtil.writeAttribute(out, "face", face);
    UiUtil.writeAttribute(out, "size", size);
    UiUtil.writeAttribute(out, "color", color);
    UiUtil.writeCloseStartTag_SS(out);
    
    // Continue
    return EVAL_BODY_INCLUDE;    
  }
  
  protected int after(Writer out) throws Exception {   
    UiUtil.writeEndTag(out, "font");
      
    // Continue
    super.after(out);
    return EVAL_PAGE;  
  }

  protected void init() {
    super.init();
    this.face = null;
    this.size = null;
    this.color = null;
  }
  
  protected String face;
  protected String size;
  protected String color;
   
}
