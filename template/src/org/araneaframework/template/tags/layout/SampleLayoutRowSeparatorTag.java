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

package org.araneaframework.template.tags.layout;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.util.UiUtil;




/**
 * SAMPLE layout row separator tag.
 * 
 * @author Marko Muts
 * 
 * @jsp.tag
 *   name = "rowSeparator"
 *   body-content = "empty"
 */
public class SampleLayoutRowSeparatorTag extends UiBaseTag {
  
  //
  // Attributes
  //  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 */
  public void setColSpan(String colSpan) throws JspException {
    this.colSpan = (String)evaluate("colSpan", colSpan, String.class);
  }
  
  //
  // Implementation
  //  
  
	protected int before(Writer out) throws Exception {
		super.before(out);
		UiUtil.writeStartTag(out, "tr");
    UiUtil.writeOpenStartTag(out, "td");
    UiUtil.writeAttribute(out, "colspan", colSpan);
    UiUtil.writeCloseStartTag(out);    
    UiUtil.writeOpenStartTag(out, "img");
		UiUtil.writeAttribute(out, "src",  "gfx/1.gif");
		UiUtil.writeAttribute(out, "width", "1");	
		UiUtil.writeAttribute(out, "height", "10");	
		UiUtil.writeCloseStartEndTag_SS(out);	
		UiUtil.writeEndTag(out, "td");
		UiUtil.writeEndTag(out, "tr");		
    
    return EVAL_BODY_INCLUDE;
	}
  
  protected void init() {
    super.init();
    this.colSpan = null;
  }  
  protected String colSpan;			
}
