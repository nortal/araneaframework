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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.StandardWidget;
import org.araneaframework.uilib.core.StandardPresentationWidget;
import org.araneaframework.uilib.form.FormWidget;

/**
 *  Base class for editable rows widgets that are used to handle simultenous 
 *  editing of multiple forms with same structure.
 *   
 *  @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public abstract class BaseFormListWidget extends StandardPresentationWidget {
	
	//*******************************************************************
	// FIELDS
	//*******************************************************************	
	
	protected List rows;
	
	protected FormRowHandler formRowHandler;	
	protected Map formRows = new HashMap();
	
	protected int rowFormCounter = 0;
	
	//*******************************************************************
	// PUBLIC METHODS
	//*******************************************************************		
	
	/**
	 * Sets the list of row objects. 
	 * @param rows the list of row objects.
	 */
	public void setRows(List rows) {
		this.rows = rows;
	}
	
	/**
	 * Returns <code>Map&lt;Object key, EditableRow&gt;</code> of initialized editable rows.
	 * @return <code>Map&lt;Object key, EditableRow&gt;</code> of initialized editable rows.
	 */
	public Map getFormRows() {
		return formRows;
	}
	
	/**
	 * Returns add form of the widget.
	 * @return Add form of the widget.
	 */
	public FormWidget getAddForm() {
		return (FormWidget) getWidget("addForm");
	}
	
	/**
	 * Returns editable row corresponding to the row key.
	 * @param key row key.
	 * @return editable row corresponding to the row key.
	 */
	public FormRow getFormRow(Object key) {
		return (FormRow) formRows.get(key);
	}
	
	/**
	 * Returns current {@link FormRowHandler}.
	 * @return current {@link FormRowHandler}.
	 */
	public FormRowHandler getFormRowHandler() {
		return this.formRowHandler;
	}
	
	/**
	 * Sets new {@link FormRowHandler}.
	 * @param editableRowHandler new {@link FormRowHandler}.
	 */
	public void setFormRowHandler(FormRowHandler editableRowHandler) {
		this.formRowHandler = editableRowHandler;
	}
	
	//*******************************************************************
	// PROTECTED METHODS
	//*******************************************************************		
	
	/**
	 * Creates and adds an editable row from a usual row object.
	 */
	protected abstract void addFormRow(Object newRow) throws Exception; 
	
	protected void init() throws Exception {
		super.init();
		
		FormWidget addForm = buildAddForm();
		formRowHandler.initAddForm(addForm);
		addWidget("addForm", addForm);		
	}
	
	/** Used to build and right instance of FormWidget that belongs to list. */
	protected abstract FormWidget buildAddForm() throws Exception;
	
	protected void handleProcess() throws Exception {
		if (rows == null) throw new AraneaRuntimeException("You must set rows before using FormListWidget!");
		
		for (Iterator i = rows.iterator(); i.hasNext();) {
			Object row = i.next();
			
			if (formRows.get(formRowHandler.getRowKey(row)) == null)
				addFormRow(row);
			else
				((FormRow) formRows.get(formRowHandler.getRowKey(row))).setRow(row);
		}
		
	}
	
	//*********************************************************************
	//* ROW HANDLING METHODS
	//*********************************************************************
	
	/**
	 * Saves all editable rows.
	 */
	public void saveAllRows() throws Exception {
		Map rowsToSave = new HashMap();
		
		for (Iterator i = formRows.values().iterator(); i.hasNext();) {
			FormRow editableRow = (FormRow) i.next();
			rowsToSave.put(editableRow.getRowKey(), editableRow);
		}
		
		formRowHandler.saveRows(rowsToSave);
	}
	
	/**
	 * Saves all editable rows that correspond to the current usual rows.
	 */
	public void saveCurrentRows() throws Exception {
		Map rowsToSave = new HashMap();
		
		for (Iterator i = rows.iterator(); i.hasNext();) {
			Object row = i.next();
			
			FormRow editableRow = (FormRow) formRows.get(formRowHandler.getRowKey(row));
			rowsToSave.put(editableRow.getRowKey(), editableRow);
		}
		
		formRowHandler.saveRows(rowsToSave);
	}	
	
	/**
	 * Saves row specified by key.
	 * @param key row key.
	 */
	public void saveRow(Object key) throws Exception {
		Map rowsToSave = new HashMap();
		
		FormRow editableRow = (FormRow) formRows.get(key);
		rowsToSave.put(editableRow.getRowKey(), editableRow);
		
		formRowHandler.saveRows(rowsToSave);
	}		
	
	/**
	 * Deletes row specified by key.
	 * @param key row key.
	 */
	public void deleteRow(Object key) throws Exception {
		Set rowsToDelete = new HashSet();		
		
		rowsToDelete.add(key);
		formRows.remove(key);
		
		formRowHandler.deleteRows(rowsToDelete);
	}	
	
	/**
	 * Adds row from given form.
	 * @param addForm add form.
	 */
	public void addRow(FormWidget addForm) throws Exception {
		formRowHandler.addRow(addForm);
	}		
	
	/**
	 * Opens or closes the row specified by the key.
	 * @param key row key.
	 */
	public void openCloseRow(Object key) throws Exception {
		FormRow currentRow = (FormRow) formRows.get(key);		
		formRowHandler.openOrCloseRow(currentRow);
	}
	
	public void resetAddForm() throws Exception {    
		FormWidget addForm = buildAddForm();
		formRowHandler.initAddForm(addForm);
		addWidget("addForm", addForm);   
	}
	
	public void resetFormRow(Object key) throws Exception {
		formRows.remove(key);
	}
	
	public void resetFormRows() throws Exception {
		formRows.clear();
	}
	
	
	// *********************************************************************
	//* VIEW MODEL
	//*********************************************************************
	
	public Object getViewModel() throws Exception {
		return new ViewModel();
	}	
	
	public class ViewModel extends StandardWidget.ViewModel {
		protected Map editableRows = new HashMap();
		protected List rows;
		
		public ViewModel() {			
			for (Iterator i = BaseFormListWidget.this.formRows.entrySet().iterator(); i.hasNext();) {
				Map.Entry ent = (Map.Entry) i.next();
				
				editableRows.put(ent.getKey(), ((FormRow)ent.getValue()).getViewModel());
			}
			
			this.rows = BaseFormListWidget.this.rows;
		}
		
		
		/**
		 * 
		 * Returns <code>Map&lt;Object key, EditableRow&gt;</code>.
		 * @return <code>Map&lt;Object key, EditableRow&gt;</code>.
		 */
		public Map getFormRows() {
			return editableRows;
		}
		
		
		/**
		 * Returns row handler that is used to get row keys.
		 */
		public FormRowHandler getRowHandler() {
			return BaseFormListWidget.this.formRowHandler;
		}
		
		/**
		 * Returns rows.
		 * @return rows.
		 */
		public List getRows() {
			return rows;
		}
	}	
	
}
