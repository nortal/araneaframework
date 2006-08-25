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

package org.araneaframework.uilib.form.formlist;

import java.util.Iterator;
import java.util.Map;
import org.araneaframework.uilib.event.OnClickEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.formlist.adapters.MemoryBasedListFormRowHandlerDecorator;
import org.araneaframework.uilib.list.EditableListWidget;
import org.araneaframework.uilib.list.dataprovider.MemoryBasedListDataProvider;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class FormListUtil {

	public static void addButtonToRowForm(String labelId, FormWidget rowForm, OnClickEventListener listener, String elementName) throws Exception {
		ButtonControl button = new ButtonControl();
		button.addOnClickEventListener(listener);
		rowForm.addElement(elementName, labelId, button, null, false);
	}

	/**
	 * Adds a save button to the given row form. Save button has id 
	 * "save" and will save the specified row when pressed by user. 
	 * 
	 * @param labelId button label id.
	 * @param editableRows editable rows widget.
	 * @param rowForm row form.
	 * @param key row key.
	 * @throws Exception 
	 */
	public static void addSaveButtonToRowForm(String labelId, BaseFormListWidget editableRows, FormWidget rowForm, Object key) throws Exception {
		addButtonToRowForm(labelId, rowForm, new ButtonSaveOnClickEventListener(key, editableRows), "save");
	}
	
	/**
	 * Adds a delete button to the given row form. Delete button has id 
	 * "delete" and will delete the specified row when pressed by user.
	 * 
	 * @param labelId button label id.
	 * @param editableRows editable rows widget.
	 * @param rowForm row form.
	 * @param key row key.
	 * @throws Exception 
	 */
	public static void addDeleteButtonToRowForm(String labelId, BaseFormListWidget editableRows, FormWidget rowForm, Object key) throws Exception {
		addButtonToRowForm(labelId, rowForm, new ButtonDeleteOnClickEventListener(key, editableRows), "delete");
	}
	
	/**
	 * Adds an open/close button to the given row form. Open/close button has id "openClose" 
	 * and will open or close (negate the current status) the specified row when pressed by user.
	 * 
	 * @param labelId button label id.
	 * @param editableRows editable rows widget.
	 * @param rowForm row form.
	 * @param key row key.
	 * @throws Exception 
	 */
	public static void addOpenCloseButtonToRowForm(String labelId, BaseFormListWidget editableRows, FormWidget rowForm, Object key) throws Exception {
		addButtonToRowForm(labelId, rowForm, new ButtonOpenCloseOnClickEventListener(key, editableRows), "openClose");
	}	
	
	
	/**
	 * Adds an edit/save button to the given row form. Edit/save button has id "editSave" 
	 * and will open or close (negate the current status) the specified row when pressed by user.
	 * Additionally when the row is closed it will save the row.
	 * 
	 * @param labelId button label id.
	 * @param editableRows editable rows widget.
	 * @param rowForm row form.
	 * @param key row key.
	 * @throws Exception 
	 */
	public static void addEditSaveButtonToRowForm(String labelId, BaseFormListWidget editableRows, FormWidget rowForm, Object key) throws Exception {
		addButtonToRowForm(labelId, rowForm, new ButtonEditSaveOnClickEventListener(key, editableRows), "editSave");
	}
	
	/**
	 * Adds an add button to the given add form. Add button has id 
	 * "add" and will add the row from the add form when pressed by user.
	 * 
	 * @param labelId button label id.
	 * @param editableRows editable rows widget.
	 * @param addForm add form.
	 * @throws Exception 
	 */
	public static void addAddButtonToAddForm(String labelId, BaseFormListWidget editableRows, FormWidget addForm) throws Exception {
		addButtonToRowForm(labelId, addForm, new ButtonAddOnClickEventListener(editableRows, addForm), "add");
	}		
	
	/**
	 * Returns whether the editable rows forms have been edited since last save. Note that for
	 * this method to work correctly programmer must call <code>FormWidget.markBaseState()</code> method when 
	 * initializing and saving rows.
	 * 
	 * @param editableRows editable rows.
	 * @return whether the editable rows have been edited since last save.
	 */
	public static boolean isRowFormsStateChanged(Map editableRows) {
		boolean result = false;
		
		for (Iterator i = editableRows.values().iterator(); i.hasNext();) {
			FormRow editableRow = (FormRow) i.next();
			result |= editableRow.getForm().isStateChanged();
		}
		
		return result;
	}
	
	/**
	 * Converts and validates all editable row forms and returns wtether they were all valid.
	 * 
	 * @param editableRows editable rows.
	 * @return Converts and validates all editable row forms and returns wtether they were all valid.
	 * @throws Exception 
	 */
	public static boolean convertAndValidateRowForms(Map editableRows) throws Exception {
		boolean result = true;
		
		for (Iterator i = editableRows.values().iterator(); i.hasNext();) {
			FormRow editableRow = (FormRow) i.next();
			result &= editableRow.getForm().convertAndValidate();
		}
		
		return result;
	}		
	
	/**
	 * Decorates the current {@link FormRowHandler}
	 * propagating all changes to the specified {@link MemoryBasedListDataProvider} making them 
	 * visible in the {@link EditableListWidget}. 
	 * 
	 * @param editableRows editable rows widget.
	 * @param data data <code>Map</code>.
	 */	
	public static void associateFormListWithMemoryBasedList(FormListWidget editableRows, MemoryBasedListDataProvider listDataProvider) {
		editableRows.setFormRowHandler(
						new MemoryBasedListFormRowHandlerDecorator(
							listDataProvider, 
							editableRows.getFormRowHandler()));
	}		
	
	public static class ButtonSaveOnClickEventListener implements OnClickEventListener {
		protected Object key;
		protected BaseFormListWidget editableRows;
		
		public ButtonSaveOnClickEventListener(Object key, BaseFormListWidget editableRows) {
			this.key = key;
			this.editableRows = editableRows;
		}
		
		public void onClick() throws Exception {
			editableRows.saveRow(key);
		}				
	}
	
	public static class ButtonOpenCloseOnClickEventListener implements OnClickEventListener {
		protected Object key;
		protected BaseFormListWidget editableRows;
		
		public ButtonOpenCloseOnClickEventListener(Object key, BaseFormListWidget editableRows) {
			this.key = key;
			this.editableRows = editableRows;
		}
		
		public void onClick() throws Exception {
			editableRows.openCloseRow(key);
		}				
	}	
	
	public static class ButtonEditSaveOnClickEventListener implements OnClickEventListener {
		protected Object key;
		protected BaseFormListWidget editableRows;
		
		public ButtonEditSaveOnClickEventListener(Object key, BaseFormListWidget editableRows) {
			this.key = key;
			this.editableRows = editableRows;			
		}
		
		public void onClick() throws Exception {
			FormRow row = editableRows.getFormRow(key);
			if (row.isOpen()) 
				editableRows.saveRow(key);
			else
				editableRows.openCloseRow(key);
		}				
	}		
	
	public static class ButtonDeleteOnClickEventListener implements OnClickEventListener {
		protected Object key;
		protected BaseFormListWidget editableRows;
		
		public ButtonDeleteOnClickEventListener(Object key, BaseFormListWidget editableRows) {
			this.key = key;
			this.editableRows = editableRows;
		}
		
		public void onClick() throws Exception {
			editableRows.deleteRow(key);
		}				
	}	
	
	public static class ButtonAddOnClickEventListener implements OnClickEventListener {
		protected BaseFormListWidget editableRows;
        protected FormWidget form;
		
		public ButtonAddOnClickEventListener(BaseFormListWidget editableRows, FormWidget form) {
			this.editableRows = editableRows;
            this.form = form;
		}
		
		public void onClick() throws Exception {
			editableRows.addRow(form);
		}				
	}		
}
