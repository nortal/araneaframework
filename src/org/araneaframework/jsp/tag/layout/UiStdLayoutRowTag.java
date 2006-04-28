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
public class UiStdLayoutRowTag extends UiLayoutRowBaseTag {
	protected String cellClass;
	
	//
	// Implementation
	//  
	
	public UiStdLayoutRowTag(String styleClass, String cellClass) {		
		this.styleClass = styleClass;
		this.cellClass = cellClass;
	}
	
	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		UiUtil.writeOpenStartTag(out, "tr");
		UiUtil.writeAttribute(out, "class", styleClass);
		UiUtil.writeAttribute(out, "height", height);
		UiUtil.writeCloseStartTag(out);
		
		// Continue
		return EVAL_BODY_INCLUDE;			
	}		
	
	protected int doEndTag(Writer out) throws Exception {
		UiUtil.writeEndTag(out, "tr");
		
		// Continue
		super.doEndTag(out);
		return EVAL_PAGE;     
	}
	
	public UiLayoutCellTagInterface getCellTag(String styleClass) throws JspException {
		return new UiStdLayoutCellTag(styleClass);
	} 
}
