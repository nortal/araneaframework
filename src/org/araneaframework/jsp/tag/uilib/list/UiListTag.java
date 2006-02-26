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

import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.uilib.UiWidgetTag;
import org.araneaframework.uilib.list.ListWidget;



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
  //
  // Implementation
  //
  
	public int before(Writer out) throws Exception {
		super.before(out);
		
		// Get list data		
		listViewModel = (ListWidget.ViewModel)viewModel;		

		// Set variables		
		pushAttribute(LIST_ID_KEY_REQUEST, id, PageContext.REQUEST_SCOPE);
		pushAttribute(LIST_FULL_ID_KEY_REQUEST, fullId, PageContext.REQUEST_SCOPE);
		pushAttribute(LIST_VIEW_MODEL_KEY_REQUEST, listViewModel, PageContext.REQUEST_SCOPE);		
	
		// Continue
	  return EVAL_BODY_INCLUDE;		
	}

	protected ListWidget.ViewModel listViewModel;		
}
