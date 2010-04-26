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
import org.araneaframework.uilib.form.formlist.FormListUtil.ButtonOnClickEventListener.ListFormAction;

/**
 * Utility methods for adding buttons to {@link FormListWidget} rows and tracking state changes.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class FormListUtil {

  /**
   * Adds a button to the given form. The button will have an ID of <code>elementName</code> and invoke the provided
   * listener when clicked by the user.
   * 
   * @param labelId The ID of the label for the button.
   * @param rowForm The form (of a row) where this button will be added.
   * @param listener The on-click event listener to register for the button.
   * @param elementName The ID for the button.
   * @return The added button control.
   */
  public static ButtonControl addButtonToRowForm(String labelId, FormWidget rowForm, OnClickEventListener listener,
      String elementName) {
    ButtonControl button = new ButtonControl();
    if (listener != null) {
      button.addOnClickEventListener(listener);
    }
    rowForm.addElement(elementName, labelId, button);
    return button;
  }

  private static <K> ButtonControl addButtonToFormRow(String labelId, FormRow<K, ?> formRow, ListFormAction listenerAction) {
    OnClickEventListener listener = new ButtonOnClickEventListener<K>(formRow.getKey(), formRow.getFormList(),
        listenerAction);
    return addButtonToRowForm(labelId, formRow.getForm(), listener, listenerAction.getEventId());
  }

  private static <K> ButtonControl addButtonToFormRow(String labelId, BaseFormListWidget<K, ?> editableRows,
      FormWidget rowForm, K key, ListFormAction action) {
    String eventId = action.getEventId();
    return addButtonToRowForm(labelId, rowForm, new ButtonOnClickEventListener<K>(key, editableRows, action), eventId);
  }

  /**
   * Adds a "Save" button to the given row form. The button will have an ID of {@value #SAVE} and will save the
   * specified row when pressed by the user.
   * 
   * @param labelId The ID of the label for the "Save" button.
   * @param editableRows The widget with editable rows. A corresponding method of the widget will be called upon event.
   * @param rowForm The form of the row where this button will be added.
   * @param key The key to identify the row.
   * @return The added button control.
   * @deprecated Use {@link #addSaveButtonToRowForm(String, FormRow)} instead.
   */
  @Deprecated
  public static <K> ButtonControl addSaveButtonToRowForm(String labelId, BaseFormListWidget<K, ?> editableRows,
      FormWidget rowForm, K key) {
    return addButtonToFormRow(labelId, editableRows, rowForm, key, ListFormAction.SAVE);
  }

  /**
   * Adds a "Save" button to the given form (resolved through <code>formRow.getForm()</code>). The button will have an
   * ID of {@value #SAVE} and will save the specified row when pressed by the user.
   * 
   * @param labelId The ID of the label for the "Save" button.
   * @param formRow The <code>FormRow</code> where the button will be added.
   * @return The added button control.
   * @since 2.0
   */
  public static <K> ButtonControl addSaveButtonToRowForm(String labelId, FormRow<K, ?> formRow) {
    return addButtonToFormRow(labelId, formRow, ListFormAction.SAVE);
  }

  /**
   * Adds a "Delete" button to the given row form. The button will have an ID of {@value #DELETE} and will delete the
   * specified row when pressed by the user.
   * 
   * @param labelId The ID of the label for the "Delete" button.
   * @param editableRows The widget with editable rows. A corresponding method of the widget will be called upon event.
   * @param rowForm The form of the row where this button will be added.
   * @param key The key to identify the row.
   * @return The added button control.
   * @deprecated Use {@link #addDeleteButtonToRowForm(String, FormRow)} instead.
   */
  @Deprecated
  public static <K> ButtonControl addDeleteButtonToRowForm(String labelId, BaseFormListWidget<K, ?> editableRows,
      FormWidget rowForm, K key) {
    return addButtonToFormRow(labelId, editableRows, rowForm, key, ListFormAction.DELETE);
  }

  /**
   * Adds a "Delete" button to the given form (resolved through <code>formRow.getForm()</code>). The button will have an
   * ID of {@value #DELETE} and will save the specified row when pressed by the user.
   * 
   * @param labelId The ID of the label for the "Delete" button.
   * @param formRow The <code>FormRow</code> where the button will be added.
   * @return The added button control.
   * @since 2.0
   */
  public static <K> ButtonControl addDeleteButtonToRowForm(String labelId, FormRow<K, ?> formRow) {
    return addButtonToFormRow(labelId, formRow, ListFormAction.DELETE);
  }

  /**
   * Adds an "Open/Close" button to the given row form. The button will have an ID of {@value #OPEN_CLOSE} and will open
   * or close a row for editing when pressed by the user.
   * 
   * @param labelId The ID of the label for the "Open/Close" button.
   * @param editableRows The widget with editable rows. A corresponding method of the widget will be called upon event.
   * @param rowForm The form of the row where this button will be added.
   * @param key The key to identify the row.
   * @return The added button control.
   * @deprecated Use {@link #addOpenCloseButtonToRowForm(String, FormRow)} instead.
   */
  @Deprecated
  public static <K> ButtonControl addOpenCloseButtonToRowForm(String labelId, BaseFormListWidget<K, ?> editableRows,
      FormWidget rowForm, K key) {
    return addButtonToFormRow(labelId, editableRows, rowForm, key, ListFormAction.OPEN_CLOSE);
  }

  /**
   * Adds a "Open/Close" button to the given form (resolved through <code>formRow.getForm()</code>). The button will
   * have an ID of {@value #OPEN_CLOSE} and will save the specified row when pressed by the user.
   * 
   * @param labelId The ID of the label for the button.
   * @param formRow The <code>FormRow</code> where the button will be added.
   * @return The added button control.
   * @since 2.0
   */
  public static <K> ButtonControl addOpenCloseButtonToRowForm(String labelId, FormRow<K, ?> formRow) {
    return addButtonToFormRow(labelId, formRow, ListFormAction.OPEN_CLOSE);
  }

  /**
   * Adds an "Edit/Save" button to the given row form. The button will have an ID of {@value #EDIT_SAVE} and, when
   * pressed by the user, will open the row for editing when the row is not opened for editing, otherwise save the
   * opened row and closes it.
   * 
   * @param labelId The ID of the label for the "Edit/Save" button.
   * @param editableRows The widget with editable rows. A corresponding method of the widget will be called upon event.
   * @param rowForm The form of the row where this button will be added.
   * @param key The key to identify the row.
   * @return The added button control.
   * @deprecated Use {@link #addEditSaveButtonToRowForm(String, FormRow)} instead.
   */
  @Deprecated
  public static <K> ButtonControl addEditSaveButtonToRowForm(String labelId, BaseFormListWidget<K, ?> editableRows,
      FormWidget rowForm, K key) {
    return addButtonToFormRow(labelId, editableRows, rowForm, key, ListFormAction.EDIT_SAVE);
  }

  /**
   * Adds a "Edit/Save" button to the given form (resolved through <code>formRow.getForm()</code>). The button will have
   * an ID of {@value #EDIT_SAVE} and will save the specified row when pressed by the user.
   * 
   * @param labelId The ID of the label for the button.
   * @param formRow The <code>FormRow</code> where the button will be added.
   * @return The added button control.
   * @since 2.0
   */
  public static <K> ButtonControl addEditSaveButtonToRowForm(String labelId, FormRow<K, ?> formRow) {
    return addButtonToFormRow(labelId, formRow, ListFormAction.EDIT_SAVE);
  }

  /**
   * Adds an "Add" button to the given row form. The button will have an ID of {@value #ADD} and will add the
   * specified row form data to the list when pressed by the user.
   * 
   * @param labelId The ID of the label for the "Save" button.
   * @param editableRows The widget with editable rows. A corresponding method of the widget will be called upon event.
   * @param addForm The form of the row where this button will be added.
   * @return The added button control.
   */
  public static <K> ButtonControl addAddButtonToAddForm(String labelId, BaseFormListWidget<K, ?> editableRows,
      FormWidget addForm) {
    return addButtonToRowForm(labelId, addForm, new ButtonOnClickEventListener<K>(editableRows, addForm),
        ListFormAction.ADD.getEventId());
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

    public enum ListFormAction {
      SAVE("save"), OPEN_CLOSE("openClose"), EDIT_SAVE("editSave"), DELETE("delete"), ADD("add");

      private String eventId;

      ListFormAction(String eventId) {
        this.eventId = eventId;
      }

      public String getEventId() {
        return this.eventId;
      }
    }

    protected K key;

    protected BaseFormListWidget<K, ?> editableRows;

    protected FormWidget formToAdd;

    protected ListFormAction action;

    public ButtonOnClickEventListener(K key, BaseFormListWidget<K, ?> editableRows, ListFormAction action) {
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
      this.action = ListFormAction.ADD;
    }

    public void onClick() {
      if (ListFormAction.ADD.equals(this.action)) {
        this.editableRows.addRow(this.formToAdd);

      } else if (ListFormAction.SAVE.equals(this.action)) {
        this.editableRows.saveRow(this.key);

      } else if (ListFormAction.OPEN_CLOSE.equals(this.action)) {
        this.editableRows.openCloseRow(this.key);

      } else if (ListFormAction.EDIT_SAVE.equals(this.action)) {
        FormRow<K, ?> row = this.editableRows.getFormRow(this.key);

        if (row.isOpen()) {
          this.editableRows.saveRow(key);
        } else {
          this.editableRows.openCloseRow(this.key);
        }

      } else if (ListFormAction.DELETE.equals(this.action)) {
        this.editableRows.deleteRow(this.key);
      }
    }
  }

}
