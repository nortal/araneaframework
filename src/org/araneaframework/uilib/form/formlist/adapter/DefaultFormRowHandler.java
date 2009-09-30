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

package org.araneaframework.uilib.form.formlist.adapter;

import java.util.Map;
import java.util.Set;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.formlist.FormRow;
import org.araneaframework.uilib.form.formlist.FormRowHandler;

/**
 * The default implementation of <code>FormRowHandler</code>. It is sometimes more sensible to extend this class than to
 * implement all methods of <code>FormRowHandler</code>. The generic parameter K corresponds to the type of the key
 * values, and the generic parameter R corresponds to the type of the row values.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class DefaultFormRowHandler<K, R> implements FormRowHandler<K, R> {

  public void initFormRow(FormRow<K, R> editableRow, R row) throws Exception {}

  public void initAddForm(FormWidget addForm) throws Exception {}

  public void addRow(FormWidget rowForm) throws Exception {}

  public void saveRows(Map<K, FormRow<K, R>> editableRows) throws Exception {}

  public void deleteRows(Set<K> keys) throws Exception {}

  /**
   * Opens a closed row or resets and closes open row.
   * 
   * @see FormRowHandler#openOrCloseRow(FormRow)
   */
  public void openOrCloseRow(FormRow<K, R> editableRow) throws Exception {
    if (editableRow.isOpen()) {
      editableRow.reset();
    }
    editableRow.setOpen(!editableRow.isOpen());
  }

}
