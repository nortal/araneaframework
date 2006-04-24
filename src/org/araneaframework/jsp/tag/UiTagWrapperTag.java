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

package org.araneaframework.jsp.tag;

import java.io.Writer;
import javax.servlet.jsp.JspException;


/**
 * Tag wrapper tag.
 * 
 * @author Oleg Mürk
 */
public abstract class UiTagWrapperTag extends UiBaseTag {
  
  //
  // Implementation
  //
  
	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);	

		// Get wrapped tag		
		tag = getTag(); 
		
		// Prepare
		this.registerSubtag(tag);
		
		// Configure
		this.configureTag(tag);
		
		// Execute start tag	
		this.executeStartSubtag(tag);
		
		return EVAL_BODY_INCLUDE;
	}

	/**
	 * After tag.
	 */
	protected int doEndTag(Writer out) throws Exception {				
		this.executeEndSubtag(tag);
    
		// Complete
		super.doEndTag(out);
		return EVAL_PAGE;
	}
	
	/**
	 * Callback: get tag
	 */
	protected abstract UiContainedTagInterface getTag() throws JspException;

	/**
	 * Callback: configure tag
	 */
	protected abstract void configureTag(UiContainedTagInterface tag) throws JspException;	


	protected UiContainedTagInterface tag;
}
