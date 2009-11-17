/*
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
 */

package org.araneaframework.uilib.form.formlist;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;
import org.araneaframework.uilib.form.visitor.FormElementVisitor;

/**
 * Base class for editable rows widgets that are used to handle simultaneous editing of multiple forms with same
 * structure. The generic parameter K corresponds to the type of the key values, and the generic parameter R corresponds
 * to the type of the row values.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class BaseFormListWidget<K, R> extends GenericFormElement {

  /**
   * The key of add-form that is used for form adding and lookup.
   */
  public static final String ADD_FORM_KEY = "addForm";

  /**
   * The model object that provides the data to this form list.
   */
  protected FormListModel<R> model;

  /**
   * The form row handler that is called to invoke the callbacks when events occur.
   */
  protected FormRowHandler<K, R> formRowHandler;

  /**
   * The form rows used in the form. This dynamically changes when a row is opened or closed.
   */
  protected Map<K, FormRow<K, R>> formRows = new LinkedHashMap<K, FormRow<K, R>>();

  /**
   * TODO
   */
  protected int rowFormCounter = 0;

  /**
   * The default value for whether the list row forms are initiated as open by default or not. In the "old times" all
   * form rows were initiated as open. However, usually it was needed to render rows closed, and open them individually
   * as needed.
   */
  protected boolean defaultOpen;

  /**
   * Constructs a form list widget using the given row handler that will respond to form list events.
   * 
   * @param formRowHandler The handler that will respond to form list events.
   */
  public BaseFormListWidget(FormRowHandler<K, R> formRowHandler) {
    setFormRowHandler(formRowHandler);
  }

  /**
   * Constructs a form list widget using the given row handler (that will respond to form list events) and the given
   * model callback (that will provide the rows data to this form list).
   * 
   * @param formRowHandler The handler that will respond to form list events.
   * @param model The model callback that will provide the rows data to this form list.
   */
  public BaseFormListWidget(FormRowHandler<K, R> formRowHandler, FormListModel<R> model) {
    setFormRowHandler(formRowHandler);
    setModel(model);
  }

  /**
   * Sets the model callback that will provide the rows data to this form list. The model must not be <code>null</code>.
   * 
   * @param model The form list model callback that will provide the rows data to this form list.
   */
  public void setModel(FormListModel<R> model) {
    Assert.notNullParam(this, model, "model");
    this.model = model;
  }

  /**
   * Provides the rows data that this form list currently uses to display its rows.
   * 
   * @return The rows data that this form list currently uses to display its rows.
   */
  private List<R> getRows() {
    return this.model.getRows();
  }

  /**
   * Returns key-formRow pairs of initialized editable rows. If a form row is not yet initialized, it will be
   * initialized here.
   * 
   * @return Key-formRow pairs of initialized editable rows.
   */
  public Map<K, FormRow<K, R>> getFormRows() {
    for (R row : getRows()) {
      FormRow<K, R> formRow = this.formRows.get(this.formRowHandler.getRowKey(row));
      if (formRow == null) {
        addFormRow(row);
      } else {
        formRow.setRow(row);
      }
    }
    return this.formRows;
  }

  /**
   * Provides the adding form of this form list widget.
   * 
   * @return The adding form of this form list widget.
   */
  public FormWidget getAddForm() {
    return (FormWidget) getWidget(ADD_FORM_KEY);
  }

  /**
   * Returns the editable row corresponding to the row key.
   * 
   * @param key The key that uniquely defines the row.
   * @return The editable row corresponding to the row key, or <code>null</code> if not found.
   */
  public FormRow<K, R> getFormRow(K key) {
    return getFormRows().get(key);
  }

  /**
   * Provides the current <code>FormRowHandler</code> of this form list widget.
   * 
   * @return The current <code>FormRowHandler</code> of this form list widget.
   */
  public FormRowHandler<K, R> getFormRowHandler() {
    return this.formRowHandler;
  }

  /**
   * Sets the <code>FormRowHandler</code> that this form list widget will start to use.
   * 
   * @param editableRowHandler The new <code>FormRowHandler</code>. Must not be <code>null</code>.
   */
  public void setFormRowHandler(FormRowHandler<K, R> editableRowHandler) {
    Assert.notNullParam(this, editableRowHandler, "editableRowHandler");
    this.formRowHandler = editableRowHandler;
  }

  // *******************************************************************
  // PROTECTED METHODS
  // *******************************************************************

  @Override
  protected void init() throws Exception {
    super.init();
    resetAddForm();
  }

  /**
   * Creates and adds an editable row for adding new rows. This method also generates a new ID for the row form and
   * invokes the form row handler callbacks.
   * 
   * @param newRow The row data object to add.
   */
  protected void addFormRow(R newRow) {
    FormWidget rowForm;

    try {
      rowForm = buildAddForm();
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }

    String rowFormId = "rowForm" + this.rowFormCounter++;
    K newRowKey = this.formRowHandler.getRowKey(newRow);

    FormRow<K, R> newEditableRow = createNewEditableRow(newRowKey, newRow, rowFormId, rowForm);

    addWidget(rowFormId, rowForm);

    try {
      this.formRowHandler.initFormRow(newEditableRow, newRow);
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }

    this.formRows.put(this.formRowHandler.getRowKey(newRow), newEditableRow);
  }

  /**
   * Creates the editable row object. Can be customized by child-classes.
   * 
   * @param key The key that uniquely identifies the row.
   * @param row The row data object.
   * @param rowFormId The row form ID.
   * @param rowForm The row form.
   * @return The form row object.
   * @since 2.0
   */
  protected FormRow<K, R> createNewEditableRow(K key, R row, String rowFormId, FormWidget rowForm) {
    return new FormRow<K, R>(this, key, row, rowFormId, rowForm, this.defaultOpen);
  }

  /**
   * The underlying implementation must build an instance of <code>FormWidget</code> that will be used for adding a new
   * row to the list.
   */
  protected abstract FormWidget buildAddForm() throws Exception;

  // *********************************************************************
  // * ROW HANDLING METHODS
  // *********************************************************************

  /**
   * Saves all editable rows.
   */
  public void saveAllRows() {
    Map<K, FormRow<K, R>> rowsToSave = new HashMap<K, FormRow<K, R>>();

    for (FormRow<K, R> editableRow : getFormRows().values()) {
      rowsToSave.put(editableRow.getKey(), editableRow);
    }

    try {
      this.formRowHandler.saveRows(rowsToSave);
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

  /**
   * Saves all editable rows that correspond to the current usual rows.
   */
  public void saveCurrentRows() {
    Map<K, FormRow<K, R>> rowsToSave = new HashMap<K, FormRow<K, R>>();

    for (R row : getRows()) {
      FormRow<K, R> editableRow = getFormRows().get(this.formRowHandler.getRowKey(row));
      rowsToSave.put(editableRow.getKey(), editableRow);
    }

    try {
      this.formRowHandler.saveRows(rowsToSave);
    } catch (Exception e1) {
      throw ExceptionUtil.uncheckException(e1);
    }
  }

  /**
   * Saves the row that is specified by the given <code>key</code>.
   * 
   * @param key The key to use to search for the row.
   */
  public void saveRow(Object key) {
    FormRow<K, R> editableRow = getFormRows().get(key);
    try {
      this.formRowHandler.saveRows(Collections.singletonMap(editableRow.getKey(), editableRow));
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

  /**
   * Deletes row specified by key.
   * 
   * @param key row key.
   */
  public void deleteRow(K key) {
    getFormRows().remove(key);
    try {
      this.formRowHandler.deleteRows(Collections.singleton(key));
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

  /**
   * Adds a row from given form.
   * 
   * @param addForm add form.
   */
  public void addRow(FormWidget addForm) {
    try {
      this.formRowHandler.addRow(addForm);
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

  /**
   * Opens or closes the row specified by the key.
   * 
   * @param key row key.
   */
  public void openCloseRow(K key) {
    FormRow<K, R> currentRow = getFormRows().get(key);

    try {
      this.formRowHandler.openOrCloseRow(currentRow);
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

  public void resetAddForm() {
    try {
      FormWidget addForm = buildAddForm();
      addWidget(ADD_FORM_KEY, addForm);
      this.formRowHandler.initAddForm(addForm);
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

  public void resetFormRow(Object key) {
    getFormRows().remove(key);
  }

  public void resetFormRows() {
    getFormRows().clear();
  }

  // *********************************************************************
  // * GenericFormElement METHODS
  // *********************************************************************

  @Override
  protected void convertInternal() throws Exception {
    for (FormRow<K, R> element : getFormRows().values()) {
      element.getForm().convert();
    }
  }

  @Override
  protected boolean validateInternal() throws Exception {
    for (FormRow<K, R> element : getFormRows().values()) {
      element.getForm().validate();
    }
    return super.validateInternal();
  }

  @Override
  public boolean isStateChanged() {
    boolean result = false;
    for (FormRow<K, R> formRow : getFormRows().values()) {
      result = result || formRow.getForm().isStateChanged();
      if (result) {
        break;
      }
    }
    return result;
  }

  @Override
  public boolean isDisabled() {
    boolean result = false;
    for (FormRow<K, R> formRow : getFormRows().values()) {
      result = result && formRow.getForm().isDisabled();
      if (result) {
        break;
      }
    }
    return result;
  }

  @Override
  public void accept(String id, FormElementVisitor visitor) {
    visitor.visit(id, this);
    visitor.pushContext(id, this);

    for (FormRow<K, R> formRow : getFormRows().values()) {
      formRow.getForm().accept(formRow.getFormId(), visitor);
    }

    visitor.popContext();
  }

  @Override
  public void markBaseState() {
    for (FormRow<K, R> element : getFormRows().values()) {
      element.getForm().markBaseState();
    }
  }

  @Override
  public void restoreBaseState() {
    for (FormRow<K, R> element : getFormRows().values()) {
      element.getForm().restoreBaseState();
    }
  }

  @Override
  public void setDisabled(boolean disabled) {
    for (FormRow<K, R> element : getFormRows().values()) {
      element.getForm().setDisabled(disabled);
    }
  }

  @Override
  public void clearErrors() {
    super.clearErrors();
    for (FormRow<K, R> element : getFormRows().values()) {
      element.getForm().clearErrors();
    }
  }

  @Override
  public boolean isValid() {
    boolean result = super.isValid();

    for (FormRow<K, R> formRow : getFormRows().values()) {
      result = result && formRow.getForm().isValid();
      if (!result) {
        break;
      }
    }

    return result;
  }

  // *********************************************************************
  // * VIEW MODEL
  // *********************************************************************

  @Override
  public Object getViewModel() {
    return new ViewModel();
  }

  public class ViewModel extends BaseApplicationWidget.ViewModel {

    protected Map<K, FormRow<K, R>.ViewModel> editableRows = new HashMap<K, FormRow<K, R>.ViewModel>();

    protected List<R> rows;

    public ViewModel() {
      for (Map.Entry<K, FormRow<K, R>> ent : BaseFormListWidget.this.getFormRows().entrySet()) {
        this.editableRows.put(ent.getKey(), ent.getValue().getViewModel());
      }
      this.rows = BaseFormListWidget.this.getRows();
    }

    /**
     * 
     * Returns <code>Map&lt;Object key, EditableRow&gt;</code>.
     * 
     * @return <code>Map&lt;Object key, EditableRow&gt;</code>.
     */
    public Map<K, FormRow<K, R>.ViewModel> getFormRows() {
      return this.editableRows;
    }

    /**
     * Returns row handler that is used to get row keys.
     */
    public FormRowHandler<K, R> getRowHandler() {
      return BaseFormListWidget.this.formRowHandler;
    }

    /**
     * Returns rows.
     * 
     * @return rows.
     */
    public List<R> getRows() {
      return this.rows;
    }
  }
}
