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

import java.util.Map;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.event.OnClickEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.formlist.FormListUtil.ButtonOnClickEventListener.Action;

/**
 * Utility methods for adding buttons to {@link FormListWidget} rows and tracking state changes.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class FormListUtil {

  public static final String ADD = "add";
  public static final String SAVE = "save";
  public static final String DELETE = "delete";
  public static final String OPEN_CLOSE = "openClose";
  public static final String EDIT_SAVE = "editSave";

  public static ButtonControl addButtonToRowForm(String labelId, FormWidget rowForm, OnClickEventListener listener,
      String elementName) {
    ButtonControl button = new ButtonControl();
    button.addOnClickEventListener(listener);
    rowForm.addElement(elementName, labelId, button, null, false);
    return button;
  }

  /**
   * Adds a save button to the given row form. Save button has id "save" and will save the specified row when pressed by
   * user.
   * 
   * @param labelId button label id.
   * @param editableRows editable rows widget.
   * @param rowForm row form.
   * @param key row key.
   * @throws Exception
   */
  public static <K> ButtonControl addSaveButtonToRowForm(String labelId, BaseFormListWidget<K, ?> editableRows,
      FormWidget rowForm, K key) {
    return addButtonToRowForm(labelId, rowForm, new ButtonOnClickEventListener<K>(key, editableRows, Action.Save), SAVE);
  }

  /**
   * Adds a delete button to the given row form. Delete button has id "delete" and will delete the specified row when
   * pressed by user.
   * 
   * @param labelId button label id.
   * @param editableRows editable rows widget.
   * @param rowForm row form.
   * @param key row key.
   * @throws Exception
   */
  public static <K> ButtonControl addDeleteButtonToRowForm(String labelId, BaseFormListWidget<K, ?> editableRows,
      FormWidget rowForm, K key) {
    return addButtonToRowForm(labelId, rowForm, new ButtonOnClickEventListener<K>(key, editableRows, Action.Delete),
        DELETE);
  }

  /**
   * Adds an open/close button to the given row form. Open/close button has id "openClose" and will open or close
   * (negate the current status) the specified row when pressed by user.
   * 
   * @param labelId button label id.
   * @param editableRows editable rows widget.
   * @param rowForm row form.
   * @param key row key.
   * @throws Exception
   */
  public static <K> ButtonControl addOpenCloseButtonToRowForm(String labelId, BaseFormListWidget<K, ?> editableRows,
      FormWidget rowForm, K key) {
    return addButtonToRowForm(labelId, rowForm, new ButtonOnClickEventListener<K>(key, editableRows, Action.OpenClose),
        OPEN_CLOSE);
  }

  /**
   * Adds an edit/save button to the given row form. Edit/save button has id "editSave" and will open or close (negate
   * the current status) the specified row when pressed by user. Additionally when the row is closed it will save the
   * row.
   * 
   * @param labelId button label id.
   * @param editableRows editable rows widget.
   * @param rowForm row form.
   * @param key row key.
   */
  public static <K> ButtonControl addEditSaveButtonToRowForm(String labelId, BaseFormListWidget<K, ?> editableRows,
      FormWidget rowForm, K key) {
    return addButtonToRowForm(labelId, rowForm, new ButtonOnClickEventListener<K>(key, editableRows, Action.EditSave),
        EDIT_SAVE);
  }

  /**
   * Adds an add button to the given add form. Add button has id "add" and will add the row from the add form when
   * pressed by user.
   * 
   * @param labelId button label id.
   * @param editableRows editable rows widget.
   * @param addForm add form.
   * @throws Exception
   */
  public static <K> ButtonControl addAddButtonToAddForm(String labelId, BaseFormListWidget<K, ?> editableRows,
      FormWidget addForm) {
    return addButtonToRowForm(labelId, addForm, new ButtonOnClickEventListener<K>(editableRows, addForm), ADD);
  }

  /**
   * Returns whether the editable rows forms have been edited since last save. Note that for this method to work
   * correctly programmer must call <code>FormWidget.markBaseState()</code> method when initializing and saving rows.
   * 
   * @param editableRows editable rows.
   * @return whether the editable rows have been edited since last save.
   */
  public static <K, R> boolean isRowFormsStateChanged(Map<K, FormRow<K, R>> editableRows) {
    boolean result = false;
    for (FormRow<?, ?> editableRow : editableRows.values()) {
      result = result || editableRow.getForm().isStateChanged();
    }
    return result;
  }

  /**
   * Converts and validates all editable row forms and returns whether they were all valid.
   * 
   * @param formRows editable rows.
   * @return Converts and validates all editable row forms and returns whether they were all valid.
   * @throws Exception
   */
  public static <K, R> boolean convertAndValidateRowForms(Map<K, FormRow<K, R>> formRows) {
    boolean result = true;

    for (FormRow<?, ?> editableRow : formRows.values()) {
      result = result && editableRow.getForm().convertAndValidate();
    }

    return result;
  }

  public static class ButtonOnClickEventListener<K> implements OnClickEventListener {

    public enum Action {
      Save, OpenClose, EditSave, Delete, AddForm
    }

    protected K key;

    protected BaseFormListWidget<K, ?> editableRows;

    protected FormWidget formToAdd;

    protected Action action;

    public ButtonOnClickEventListener(K key, BaseFormListWidget<K, ?> editableRows, Action action) {
      Assert.notNullParam(this, key, "key");
      Assert.notNullParam(this, editableRows, "editableRows");
      Assert.notNullParam(this, action, "action");

      this.key = key;
      this.editableRows = editableRows;
      this.action = action;
    }

    public ButtonOnClickEventListener(BaseFormListWidget<K, ?> editableRows, FormWidget formToAdd) {
      Assert.notNullParam(this, editableRows, "editableRows");
      Assert.notNullParam(this, formToAdd, "formToAdd");

      this.editableRows = editableRows;
      this.formToAdd = formToAdd;
      this.action = Action.AddForm;
    }

    public void onClick() {
      if (Action.AddForm.equals(this.action)) {
        this.editableRows.addRow(this.formToAdd);

      } else if (Action.Save.equals(this.action)) {
        this.editableRows.saveRow(this.key);

      } else if (Action.OpenClose.equals(this.action)) {
        this.editableRows.openCloseRow(this.key);

      } else if (Action.EditSave.equals(this.action)) {
        FormRow<K, ?> row = this.editableRows.getFormRow(this.key);

        if (row.isOpen()) {
          this.editableRows.saveRow(key);
        } else {
          this.editableRows.openCloseRow(this.key);
        }

      } else if (Action.Delete.equals(this.action)) {
        this.editableRows.deleteRow(this.key);
      }
    }
  }

}
