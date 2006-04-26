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

package org.araneaframework.template.tags.presentation;


import java.io.Writer;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.util.UiUtil;


/**
 * SAMPLE path item tag.
 * 
 * @author Marko Muts
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "pathItem"
 *   body-content = "JSP"
 */
public class SamplePathItemTag extends UiBaseTag {
  
  //
  // Implementation
  //
      
	protected int before(Writer out) throws Exception {		
    super.before(out);
    
    parent = (SamplePathTag)readAttribute(SamplePathTag.KEY);
    
    if (parent.getHadItems()) {
      UiUtil.writeOpenStartTag(out, "img");
      UiUtil.writeAttribute(out, "src", "gfx/dot07.gif");
      UiUtil.writeAttribute(out, "width", "17");
      UiUtil.writeAttribute(out, "height", "9");
      UiUtil.writeCloseStartEndTag_SS(out);
    }
    
		// Continue
    return EVAL_BODY_INCLUDE;
	}	
  
  protected int after(Writer out) throws Exception {
    parent.onItem();
      
		// Continue
		super.after(out);
		return EVAL_PAGE;  
  }
  
  protected SamplePathTag parent;
}
