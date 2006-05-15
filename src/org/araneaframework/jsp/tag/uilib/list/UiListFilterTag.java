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

package org.araneaframework.jsp.tag.uilib.list;				

import java.io.Writer;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.tag.uilib.form.UiFormTag;
import org.araneaframework.uilib.list.ListWidget;



/**
 * List widget filter tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "listFilter"
 *   body-content = "JSP"
 *   description = "Represents UiLib list filter. Introduces an implicit UiLib form, so one can place form elements under it."
 */
public class UiListFilterTag extends UiBaseTag {
  
  //
  // Implementation
  //
  
	public int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);

    // Get list data
    String listId = (String)requireContextEntry(UiListTag.LIST_ID_KEY_REQUEST);    
    
    // Include form tag
    formTag = new UiFormTag();
    this.registerSubtag(formTag);
    formTag.setId(listId + "." + ListWidget.FILTER_FORM_NAME);
    this.executeStartSubtag(formTag); 
	
		// Continue
	  return EVAL_BODY_INCLUDE;		
	}
  
  public int doEndTag(Writer out) throws Exception {   
    
    this.executeEndSubtag(formTag);
    this.unregisterSubtag(formTag);
          
    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;  
  }
  
  protected UiFormTag formTag;
}
