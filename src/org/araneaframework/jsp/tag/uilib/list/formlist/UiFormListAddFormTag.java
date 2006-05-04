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
import org.araneaframework.jsp.tag.uilib.form.UiFormTag;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "formListAddForm"
 *   body-content = "JSP"
 *   description = "UiLib editable list add form tag. <br/> 
           Makes available following page scope variables: 
           <ul>
             <li><i>form</i> - UiLib form view model.
             <li><i>formId</i> - UiLib form id.
           </ul> "
 */
public class UiFormListAddFormTag extends UiFormTag {

	public int doStartTag(Writer out) throws Exception {		
		String editableListId = (String)requireContextEntry(UiFormListTag.FORM_LIST_ID_KEY);
		
		id = editableListId + ".addForm";
		
		return super.doStartTag(out);
	}

	public void doFinally() {
		id = null;
		super.doFinally();
	}
}
