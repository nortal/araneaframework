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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.LinkedMap;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;
import org.araneaframework.uilib.form.visitor.FormElementVisitor;

/**
 *  Base class for editable rows widgets that are used to handle simultenous 
 *  editing of multiple forms with same structure.
 *   
 *  @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class BaseFormListWidget extends GenericFormElement {

	//*******************************************************************
	// FIELDS
	//*******************************************************************	

	protected FormListModel model = null;

	protected FormRowHandler formRowHandler;	
	protected Map formRows = new LinkedMap();


	protected int rowFormCounter = 0;

  public BaseFormListWidget(FormRowHandler formRowHandler) {
    setFormRowHandler(formRowHandler);
  }  
  
  public BaseFormListWidget(FormRowHandler formRowHandler, FormListModel model) {
    setFormRowHandler(formRowHandler);
    setModel(model);
  }
  
	//*******************************************************************
	// PUBLIC METHODS
	//*******************************************************************		

	/**
	 * Sets form list model. 
	 * @param model the form list model
	 */
	public void setModel(FormListModel model) {
		this.model = model;
	}
  
  private List getRows() {
    try {
      return model.getRows();
    }
    catch (Exception e) {
      throw new AraneaRuntimeException(e);
    }
  }

	/**
	 * Returns <code>Map&lt;Object key, EditableRow&gt;</code> of initialized editable rows.
	 * @return <code>Map&lt;Object key, EditableRow&gt;</code> of initialized editable rows.
	 */
	public Map getFormRows() {
    for (Iterator i = getRows().iterator(); i.hasNext();) {
      Object row = i.next();

      if (formRows.get(formRowHandler.getRowKey(row)) == null)
        addFormRow(row);
      else
        ((FormRow) formRows.get(formRowHandler.getRowKey(row))).setRow(row);
    }      
    
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
		return (FormRow) getFormRows().get(key);
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
  protected void addFormRow(Object newRow) {
    FormWidget rowForm;
    try {
      rowForm = buildAddForm();
    }
    catch (Exception e1) {
      throw ExceptionUtil.uncheckException(e1);
    }
    String rowFormId = "rowForm" + rowFormCounter++;
    FormRow newEditableRow = new FormRow(this, formRowHandler.getRowKey(newRow), newRow, rowFormId, rowForm, true);

    addWidget(rowFormId, rowForm);
    try {
      formRowHandler.initFormRow(newEditableRow, newRow);
    }
    catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }         

    formRows.put(formRowHandler.getRowKey(newRow), newEditableRow);
  }

	protected void init() throws Exception {
		super.init();		
    
		// XXX: restore the behaviour in 1.0.6. It was put back in 1.0.7 (a mistake)
		//processRows();    
		resetAddForm();		
	}

	/** Used to build instance of FormWidget belonging to this list. */
	protected abstract FormWidget buildAddForm() throws Exception;

	//*********************************************************************
	//* ROW HANDLING METHODS
	//*********************************************************************

	/**
	 * Saves all editable rows.
	 */
	public void saveAllRows() {
		Map rowsToSave = new HashMap();

		for (Iterator i = getFormRows().values().iterator(); i.hasNext();) {
			FormRow editableRow = (FormRow) i.next();
			rowsToSave.put(editableRow.getKey(), editableRow);
		}
    
    try {
      formRowHandler.saveRows(rowsToSave);      
    }
    catch (Exception e1) {
      throw ExceptionUtil.uncheckException(e1);
    }
	}

	/**
	 * Saves all editable rows that correspond to the current usual rows.
	 */
	public void saveCurrentRows()  {
		Map rowsToSave = new HashMap();

		for (Iterator i = getRows().iterator(); i.hasNext();) {
			Object row = i.next();

			FormRow editableRow = (FormRow) getFormRows().get(formRowHandler.getRowKey(row));
			rowsToSave.put(editableRow.getKey(), editableRow);
		}
    
    try {
      formRowHandler.saveRows(rowsToSave);      
    }
    catch (Exception e1) {
      throw ExceptionUtil.uncheckException(e1);
    }
	}	

	/**
	 * Saves row specified by key.
	 * @param key row key.
	 */
	public void saveRow(Object key) {
		Map rowsToSave = new HashMap();

		FormRow editableRow = (FormRow) getFormRows().get(key);
		rowsToSave.put(editableRow.getKey(), editableRow);

    try {
      formRowHandler.saveRows(rowsToSave);      
    }
    catch (Exception e1) {
      throw ExceptionUtil.uncheckException(e1);
    }
	}		

	/**
	 * Deletes row specified by key.
	 * @param key row key.
	 */
	public void deleteRow(Object key) {
		Set rowsToDelete = new HashSet(1);		

		rowsToDelete.add(key);
    getFormRows().remove(key);
    
    try {
      formRowHandler.deleteRows(rowsToDelete);
    }
    catch (Exception e1) {
      throw ExceptionUtil.uncheckException(e1);
    }		
	}	

	/**
	 * Adds row from given form.
	 * @param addForm add form.
	 */
	public void addRow(FormWidget addForm){		    
    try {
      formRowHandler.addRow(addForm);
    }
    catch (Exception e1) {
      throw ExceptionUtil.uncheckException(e1);
    } 
	}		

	/**
	 * Opens or closes the row specified by the key.
	 * @param key row key.
	 */
	public void openCloseRow(Object key) {
		FormRow currentRow = (FormRow) getFormRows().get(key);
    
    try {
      formRowHandler.openOrCloseRow(currentRow);
    }
    catch (Exception e1) {
      throw ExceptionUtil.uncheckException(e1);
    } 		
	}

	public void resetAddForm() {    
	  try {
	    FormWidget addForm = buildAddForm();
	    addWidget("addForm", addForm);   
	    formRowHandler.initAddForm(addForm);  
	  }
	  catch (Exception e1) {
	    throw ExceptionUtil.uncheckException(e1);
	  } 
	}

	public void resetFormRow(Object key) {
    getFormRows().remove(key);
	}

	public void resetFormRows() {
    getFormRows().clear();
	}


	// *********************************************************************
	//* VIEW MODEL
	//*********************************************************************

	public Object getViewModel() throws Exception {
		return new ViewModel();
	}	

	public class ViewModel extends BaseApplicationWidget.ViewModel {
		protected Map editableRows = new HashMap();
		protected List rows;

		public ViewModel() {
			for (Iterator i = BaseFormListWidget.this.getFormRows().entrySet().iterator(); i.hasNext();) {
				Map.Entry ent = (Map.Entry) i.next();

				editableRows.put(ent.getKey(), ((FormRow)ent.getValue()).getViewModel());
			}

			this.rows = BaseFormListWidget.this.getRows();
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


  protected void convertInternal() throws Exception {
    for (Iterator i = getFormRows().values().iterator(); i.hasNext();) {
      ((FormRow) i.next()).getForm().convert();
    }
  }
  
  protected boolean validateInternal() throws Exception {
    for (Iterator i = getFormRows().values().iterator(); i.hasNext();) {
      ((FormRow) i.next()).getForm().validate();
    }
    
    return super.validateInternal();
  }

  public boolean isStateChanged() {
    boolean result = false;
    for (Iterator i = getFormRows().values().iterator(); i.hasNext();)
      result |= ((FormRow) i.next()).getForm().isStateChanged();
    return result;
  }   
  
  public boolean isDisabled() {
    boolean result = false;
    for (Iterator i = getFormRows().values().iterator(); i.hasNext();)
      result &= ((FormRow) i.next()).getForm().isDisabled();
    return result;
  }
  
  public void accept(String id, FormElementVisitor visitor) {
    visitor.visit(id, this);

    visitor.pushContext(id, this);

    for (Iterator i = getFormRows().values().iterator(); i.hasNext();) {
      FormRow entry = (FormRow) i.next();

      String elementId = (String) entry.getKey();
      FormWidget element = entry.getForm();

      element.accept(elementId, visitor);
    }

    visitor.popContext();
  }
  public void markBaseState() {
    for (Iterator i = getFormRows().values().iterator(); i.hasNext();) {
      ((FormRow) i.next()).getForm().markBaseState();
    }
  }

  public void restoreBaseState() {
    for (Iterator i = getFormRows().values().iterator(); i.hasNext();) {
      ((FormRow) i.next()).getForm().restoreBaseState();
    }
  }

  public void setDisabled(boolean disabled) {
    for (Iterator i = getFormRows().values().iterator(); i.hasNext();) {
      ((FormRow) i.next()).getForm().setDisabled(disabled);
    }
  }	
  
  public void clearErrors() {
    super.clearErrors();
    for (Iterator i = getFormRows().values().iterator(); i.hasNext();) {
     ((FormRow) i.next()).getForm().clearErrors();
    }
  }
  
  public boolean isValid() {
    boolean result = super.isValid();
    
    for (Iterator i = getFormRows().values().iterator(); i.hasNext();) {
      if (!result) 
        break;
      result &= ((FormRow) i.next()).getForm().isValid();   
    }
    
    return result;
  }

}
