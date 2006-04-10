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

package org.araneaframework.jsp.tag.layout;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.util.UiUtil;


/**
 * ETEA layout row tag.
 * 
 * @author Oleg Mürk
 */
public class UiStdLayoutCellTag extends UiLayoutCellBaseTag {
	
  //
  // Implementation
  //
	
	public UiStdLayoutCellTag(String styleClass) throws JspException {
		this.styleClass = styleClass;
	}
  
    
	protected int before(Writer out) throws Exception {
		super.before(out);
		
		UiUtil.writeOpenStartTag(out, "td");
		UiUtil.writeAttribute(out, "colspan", colSpan);
		UiUtil.writeAttribute(out, "rowspan", rowSpan);
		UiUtil.writeAttribute(out, "width", width);	
		UiUtil.writeAttribute(out, "height", height);
		UiUtil.writeAttribute(out, "class", styleClass);
		UiUtil.writeCloseStartTag(out);
						
		// Continue
	  return EVAL_BODY_INCLUDE;
	}		

	protected int after(Writer out) throws Exception {
		UiUtil.writeEndTag(out, "td");
    	
		// Continue
		super.after(out);
		return EVAL_PAGE;     
	}
}
