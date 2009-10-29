/*
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
 */

package org.araneaframework.jsp.tag.presentation;

import java.io.Writer;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Standard italic tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "italic"
 *   body-content = "JSP"
 *   description = "Acts as <i>&lt;i&gt;</i> HTML tag."
 */
public class ItalicHtmlTag extends BaseTag {
  
  //
  // Implementation
  //
    
  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    JspUtil.writeStartTag_SS(out, "i");
    
    // Continue
    return EVAL_BODY_INCLUDE;    
  }
  
  @Override
  protected int doEndTag(Writer out) throws Exception {   
    JspUtil.writeEndTag(out, "i");
      
    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;  
  }
}
