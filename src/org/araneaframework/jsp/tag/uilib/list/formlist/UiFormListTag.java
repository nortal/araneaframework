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
import org.araneaframework.jsp.UiException;
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
	
	public final static String FORM_LIST_ID_KEY = "formListId";
	public final static String FORM_LIST_VIEW_MODEL_KEY = "formList";	
	/**
	 *
	 */
	public int doStartTag(Writer out) throws Exception {
		if (id == null) {
			String listId = (String) UiUtil.requireContextEntry(pageContext, UiListTag.LIST_ID_KEY);
			id = listId + ".formList";
		}
		
		super.doStartTag(out);

		try {
			formListViewModel = (FormListWidget.ViewModel) viewModel;
		} catch (ClassCastException e) {
			throw new UiException("Could not acquire form list view model. <ui:formList> should have id specified or should be in context of real FormListWidget.", e);
		}
		
		// Set variables
		addContextEntry(FORM_LIST_ID_KEY, id);
		addContextEntry(FORM_LIST_VIEW_MODEL_KEY, formListViewModel);		

		return EVAL_BODY_INCLUDE; 
	}

	/* ***********************************************************************************
	 * FINALLY - reset some fields to allow safe reuse from tag pool.
	 * ***********************************************************************************/
	public void doFinally() {
		id = null;
		super.doFinally();
	}
}
