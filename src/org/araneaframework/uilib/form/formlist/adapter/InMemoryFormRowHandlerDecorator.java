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
import org.araneaframework.uilib.form.formlist.InMemoryFormListHelper;

/**
 * Decorator that uses the {@link InMemoryFormListHelper} to assign temporary keys to new objects.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class InMemoryFormRowHandlerDecorator<K, R> implements FormRowHandler<K, R> {

  protected FormRowHandler<K, R> rowHandler;

  protected InMemoryFormListHelper<K, R> inMemoryRowHelper;

  public InMemoryFormRowHandlerDecorator(FormRowHandler<K, R> rowHandler,
      InMemoryFormListHelper<K, R> editableMemoryBasedHelper) {
    this.rowHandler = rowHandler;
    this.inMemoryRowHelper = editableMemoryBasedHelper;
  }

  public K getRowKey(R row) {
    K result = this.rowHandler.getRowKey(row);
    if (result != null) {
      return result;
    }

    return this.inMemoryRowHelper.getKey(row);
  }

  public void saveRows(Map<K, FormRow<K, R>> rowForms) throws Exception {
    this.rowHandler.saveRows(rowForms);
  }

  public void deleteRows(Set<K> keys) throws Exception {
    this.rowHandler.deleteRows(keys);
  }

  public void initFormRow(FormRow<K, R> editableRow, R row) throws Exception {
    this.rowHandler.initFormRow(editableRow, row);
  }

  public void initAddForm(FormWidget addForm) throws Exception {
    this.rowHandler.initAddForm(addForm);
  }

  public void addRow(FormWidget rowForm) throws Exception {
    this.rowHandler.addRow(rowForm);
  }

  public void openOrCloseRow(FormRow<K, R> editableRow) throws Exception {
    this.rowHandler.openOrCloseRow(editableRow);
  }
}
