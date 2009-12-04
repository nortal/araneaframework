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
import java.util.Set;
import org.araneaframework.uilib.form.FormWidget;

/**
 * This class represents the callback interface provided by the programmer to the
 * {@link org.araneaframework.uilib.form.formlist.FormListWidget}. This callback allows to respond form list events. The
 * generic parameter K corresponds to the type of the key values, and the generic parameter R corresponds to the type of
 * the row values.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface FormRowHandler<K, R> extends RowHandler<K, R> {

  /**
   * The underlying implementation should initialize (e.g. setting the correct open/close status) the editable row (
   * <code>formRow)</code>) that represents the given <code>row</code> model object. This method is usually called when
   * building the form.
   * 
   * @param formRow The editable row to initialize.
   * @param row The row data object that the editable row represents.
   */
  public void initFormRow(FormRow<K, R> formRow, R row) throws Exception;

  /**
   * The underlying implementation should initialize the form that will be used to add new rows.
   * 
   * @param addForm An instance of form that will be used to add new rows to the list.
   */
  public void initAddForm(FormWidget addForm) throws Exception;

  /**
   * This method is called when a new row was ordered to be added. The row data should be read from the supplied form
   * (after converting and validating).
   * 
   * @param addForm The form that will be used to add new rows.
   */
  public void addRow(FormWidget addForm) throws Exception;

  /**
   * This method is called when the specified form rows were ordered to be saved to a file, database, etc.
   * 
   * @param formRows A map of row keys that correspond to appropriate {@link FormRow}s to be saved.
   */
  public void saveRows(Map<K, FormRow<K, R>> formRows) throws Exception;

  /**
   * This method is called when the rows (specified by the keys in the supplied set) should be deleted.
   * 
   * @param keys A set of row keys that correspond to form rows that were ordered to be deleted.
   */
  public void deleteRows(Set<K> keys) throws Exception;

  /**
   * This method is called when the supplied row has been opened or closed. You can check current status of
   * <code>formRow</code> by invoking {@link FormRow#isOpen()}.
   * 
   * @param formRow The editable form row that was opened or closed.
   */
  public void openOrCloseRow(FormRow<K, R> formRow) throws Exception;
}
