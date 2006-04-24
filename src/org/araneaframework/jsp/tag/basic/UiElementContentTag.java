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
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.util.UiUtil;


/**
 * Element content tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "elementContent"
 *   body-content = "JSP"
 *   description = "Defines an HTML element content, meaning the body of the HTML element where text and other tags go."
 */
public class UiElementContentTag extends UiBaseTag {
  
  //
  // Implementation
  //
	
	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		UiElementTag parent = (UiElementTag)requireContextEntry(UiElementTag.KEY_REQUEST);
		parent.onContent();
    
		parent.writeAttributes(out);
		UiUtil.writeCloseStartTag(out);
		
		// Continue
	  return EVAL_BODY_INCLUDE;
	}
}
