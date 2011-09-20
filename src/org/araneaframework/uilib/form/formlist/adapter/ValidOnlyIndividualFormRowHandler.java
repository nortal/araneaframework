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
import org.araneaframework.uilib.form.formlist.FormRow;

/**
 * A more precise form row handler that narrows rows handling down to only adding, saving and deleting valid rows
 * <i>one-by-one</i>.
 * 
 * @param <K> The type of the form row key value.
 * @param <R> The type of the form row value.
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class ValidOnlyIndividualFormRowHandler<K, R> extends ValidOnlyFormRowHandler<K, R> {

  @Override
  public void saveValidRows(Map<K, FormRow<K, R>> rowForms) throws Exception {
    for (FormRow<K, R> formRow : rowForms.values()) {
      saveValidRow(formRow);
    }
  }

  @Override
  public void deleteRows(Set<K> keys) throws Exception {
    for (K key : keys) {
      deleteRow(key);
    }
  }

  /**
   * This method is called by {@link #saveValidRows(Map)} to save only one valid row. The implementation of this method
   * should save the form row data in a persistent data source.
   * 
   * @param formRow The form row data to add into a persistent data source.
   * @throws Exception Any exception that might occur during saving the data.
   */
  public void saveValidRow(FormRow<K, R> formRow) throws Exception {
  }

  /**
   * This method is called by {@link #deleteRows(Set)} to delete only one row. The implementation of this method should
   * delete the row identified by the given key from a persistent data source.
   * 
   * @param key The key (unique identifier) of the row that is to be deleted from the persistent data source.
   * @throws Exception Any exception that might occur during deleting the data.
   */
  public void deleteRow(K key) throws Exception {
  }
}
