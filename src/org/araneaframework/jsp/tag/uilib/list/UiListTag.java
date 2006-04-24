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


import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.tag.uilib.UiWidgetTag;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.formlist.FormListWidget;

/**
 * List widget tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "list"
 *   body-content = "JSP"
 *   description = "Makes available following page scope variables: 
           <ul>
             <li><i>list</i> - UiLib list view model.
             <li><i>listId</i> - UiLib list id.
           </ul> "
 */
public class UiListTag extends UiWidgetTag {
	public final static String LIST_ID_KEY_REQUEST = "listId";
	public final static String LIST_VIEW_MODEL_KEY_REQUEST = "list";  
	public final static String LIST_FULL_ID_KEY_REQUEST = "listFullId";
	
	protected ListWidget.ViewModel listViewModel;
	protected String varSequence;
	
  //
  // Implementation
  //
	public void init() {
		super.init();
		varSequence = "listSequence";
	}	
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Name of variable that represents list sequence info (by default "listSequence")." 
	 */
	public void setVarSequence(String varSequence) {
		this.varSequence = varSequence;
	}
  
	public int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		// Get list data
		try {
			listViewModel = (ListWidget.ViewModel)viewModel;
		} catch (ClassCastException e) {
			throw new UiException("Could not acquire list view model. <ui:list> should have id specified or should be in context of real ListWidget.", e);
		}

		// Set variables		
		addContextEntry(LIST_ID_KEY_REQUEST, id);
		addContextEntry(LIST_FULL_ID_KEY_REQUEST, fullId);
		addContextEntry(LIST_VIEW_MODEL_KEY_REQUEST, listViewModel);

		addContextEntry(varSequence, listViewModel.getSequence());
	
		// Continue
		return EVAL_BODY_INCLUDE;		
	}
	
	public int doEndTag(Writer out) throws Exception {
		addContextEntry(varSequence, null);
		
		return EVAL_PAGE;		
	}
}
