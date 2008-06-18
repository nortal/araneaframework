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
import java.util.ListIterator;
import org.araneaframework.jsp.tag.uilib.form.FormTag;
import org.araneaframework.jsp.tag.uilib.list.BaseListRowsTag;
import org.araneaframework.uilib.form.formlist.FormListWidget;
import org.araneaframework.uilib.form.formlist.FormRow;


/**
 * List widget rows tag.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 * @jsp.tag
 *   name = "formListRows"
 *   body-content = "JSP"
 *   description = "Iterating tag that gives access to each row and row form on the UiLib editable list current page.
					The editable row is accessible as "formRow" variable."
 */
public class FormListRowsTag extends BaseListRowsTag {
  
	public static final String EDITABLE_ROW_KEY = "formRow";
	
	protected FormListWidget.ViewModel editableListViewModel;
	protected String editableListId;
	
	protected FormTag rowForm = new FormTag();
	
	protected String var = "row";
	
	@Override
  public int doStartTag(Writer out) throws Exception {
		editableListViewModel = (FormListWidget.ViewModel)requireContextEntry(FormListTag.FORM_LIST_VIEW_MODEL_KEY);
		editableListId = (String)requireContextEntry(FormListTag.FORM_LIST_ID_KEY);
		return super.doStartTag(out);
	}
  
  //
  // Attributes
  //

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Name of variable that represents individual row (by default "row")." 
	 */
  public void setVar(String var) {
    this.var = var;
  }
	
  //
  // Implementation
  //
	
	@Override
  protected ListIterator<?> getIterator() {
		return editableListViewModel.getRows().listIterator();
	}

	@Override
  protected void doForEachRow(Writer out) throws Exception {
		super.doForEachRow(out);		
		
	  	Object currentRowKey = editableListViewModel.getRowHandler().getRowKey(currentRow);
	  	FormRow.ViewModel currentEditableRow = editableListViewModel.getFormRows().get(currentRowKey);
	  	
	  	addContextEntry(EDITABLE_ROW_KEY, currentEditableRow);
	  	addContextEntry(var, currentRow);	
	  	
	    registerSubtag(rowForm);
	    rowForm.setId(editableListId + "." + currentEditableRow.getRowFormId());
	    executeStartSubtag(rowForm);
	}
    
  @Override
  protected int afterBody(Writer out) throws Exception {
  	executeEndTagAndUnregister(rowForm);
  	
  	return super.afterBody(out);   
  } 
}
