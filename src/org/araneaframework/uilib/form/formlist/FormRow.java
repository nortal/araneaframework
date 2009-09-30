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
 **/

package org.araneaframework.uilib.form.formlist;

import java.io.Serializable;
import org.araneaframework.uilib.form.FormWidget;

/**
 * Represents one editable row.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class FormRow<K, R> implements Serializable {

  protected K rowKey;

  protected R row;

  protected FormWidget rowForm;

  protected String rowFormId;

  protected boolean open;

  protected BaseFormListWidget<K, R> formList;

  public FormRow(BaseFormListWidget<K, R> formList, K rowKey, R row, String rowFormId, FormWidget rowForm, boolean open) {
    this.rowKey = rowKey;
    this.row = row;
    this.rowForm = rowForm;
    this.rowFormId = rowFormId;
    this.open = open;
    this.formList = formList;
  }

  /**
   * Opens the current list row for editing.
   */
  public void open() {
    this.open = true;
  }

  /**
   * Closes the current list row from editing.
   */
  public void close() {
    this.open = false;
  }

  /**
   * Resets the current row by removing it from the current form.
   */
  public void reset() {
    getFormList().resetFormRow(getKey());
  }

  /**
   * Returns editable row form.
   * 
   * @return editable row form.
   */
  public FormWidget getForm() {
    return this.rowForm;
  }

  /**
   * Returns editable row key.
   * 
   * @return editable row key.
   */
  public K getKey() {
    return this.rowKey;
  }

  /**
   * Returns whether the row is open.
   * 
   * @return whether the row is open.
   */
  public boolean isOpen() {
    return this.open;
  }

  /**
   * Sets whether the row is open.
   * 
   * @param open whether the row is open.
   */
  public void setOpen(boolean open) {
    this.open = open;
  }

  /**
   * Returns editable row form id.
   * 
   * @return editable row form id.
   */
  public String getFormId() {
    return this.rowFormId;
  }

  /**
   * Returns associated list row object.
   * 
   * @return associated list row object.
   */
  public R getRow() {
    return this.row;
  }

  /**
   * Sets associated list row object.
   */
  public void setRow(R row) {
    this.row = row;
  }

  /**
   * Returns editable list.
   * 
   * @return editable list.
   */
  public BaseFormListWidget<K, R> getFormList() {
    return this.formList;
  }

  /**
   * Returns editable row view model.
   * 
   * @return editable row view model.
   */
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  public class ViewModel {

    protected FormWidget rowForm;

    protected String rowFormId;

    protected Boolean open;

    public ViewModel() {
      this.rowForm = FormRow.this.rowForm;
      this.rowFormId = FormRow.this.rowFormId;
      this.open = FormRow.this.open ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * Returns editable row form view model.
     * 
     * @return editable row form view model.
     */
    public FormWidget getRowForm() {
      return this.rowForm;
    }

    /**
     * Gets whether the row is open.
     */
    public Boolean getOpen() {
      return this.open;
    }

    /**
     * Returns editable row form id.
     * 
     * @return editable row form id.
     */
    public String getRowFormId() {
      return this.rowFormId;
    }
  }
}
