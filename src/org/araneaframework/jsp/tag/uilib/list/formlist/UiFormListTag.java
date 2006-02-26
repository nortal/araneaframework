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

package org.araneaframework.jsp.tag.uilib.list.formlist;

import java.io.Writer;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.uilib.UiWidgetTag;
import org.araneaframework.jsp.tag.uilib.list.UiListTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.list.formlist.FormListWidget;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "formList"
 *   body-content = "JSP"
 *   description = "UiLib form list tag. <br/> 
           Makes available following page scope variables: 
           <ul>
             <li><i>formList</i> - UiLib editable list view model.
             <li><i>formListId</i> - UiLib editable list id.
           </ul> "
 */
public class UiFormListTag extends UiWidgetTag {
	protected FormListWidget.ViewModel formListViewModel;
	
	public final static String FORM_LIST_ID_KEY_REQUEST = "formListId";
	public final static String FORM_LIST_VIEW_MODEL_KEY_REQUEST = "formList";	
	/**
	 *
	 */
	public int before(Writer out) throws Exception {
		if (id == null) {
			String listId = (String) UiUtil.readAttribute(pageContext, UiListTag.LIST_ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);
			id = listId + ".formList";
		}
		
		super.before(out);
						
		formListViewModel = (FormListWidget.ViewModel) viewModel;	
		
		// Set variables
		pushAttribute(FORM_LIST_ID_KEY_REQUEST, id, PageContext.REQUEST_SCOPE);
		pushAttribute(FORM_LIST_VIEW_MODEL_KEY_REQUEST, formListViewModel, PageContext.REQUEST_SCOPE);		
		
		return EVAL_BODY_INCLUDE; 
	}		
}
