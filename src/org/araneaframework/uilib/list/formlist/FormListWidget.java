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

package org.araneaframework.uilib.list.formlist;

import org.araneaframework.uilib.form.FormWidget;


/**
 * Editable rows widget that is used to handle simultenous editing of multiple forms with same structure.
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class FormListWidget extends BaseFormListWidget {
	
	
	//*******************************************************************
	// CONSTRUCTORS
	//*******************************************************************		
	
	/**
	 * @param id widget id.
	 * @param enviroment resource provider.
	 * @param rowHandler row handler.
	 */
	public FormListWidget(FormRowHandler rowHandler) {
		this.formRowHandler = rowHandler;
	}
	
	protected FormWidget buildAddForm() throws Exception {
		return new FormWidget();
	}
	
	protected void addFormRow(Object newRow) throws Exception {
		FormWidget rowForm = buildAddForm();
		String rowFormId = "rowForm" + rowFormCounter++;
		FormRow newEditableRow = new FormRow(formRowHandler.getRowKey(newRow), newRow, rowFormId, rowForm, true);
		
		formRowHandler.initFormRow(newEditableRow, newRow);     
		addWidget(rowFormId, rowForm);
		
		formRows.put(formRowHandler.getRowKey(newRow), newEditableRow);
	}
}
