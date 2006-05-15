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
import org.araneaframework.jsp.tag.UiPresentationTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * Standard style tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "style"
 *   body-content = "JSP"
 *   description = "Sets a CSS class for the content, acts as a <i>&lt;span&gt;</i> HTML tag with the <i>class</i> atribute set."
 */
public class UiStdStyleTag extends UiPresentationTag {
  //
  // Implementation
  //  
    
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    UiUtil.writeOpenStartTag(out, "span");
    UiUtil.writeAttribute(out, "class", getStyleClass());
    UiUtil.writeCloseStartTag_SS(out);
    
    // Continue
    return EVAL_BODY_INCLUDE;    
  }
  
  protected int doEndTag(Writer out) throws Exception {   
    UiUtil.writeEndTag(out, "span");
      
    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;  
  }
}
