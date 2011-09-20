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
 * A row handler that simplifies responding to list form events by just implementing methods to handle rows
 * individually. The methods are {@link #deleteRow(Object)} and {@link #saveRow(FormRow)}.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class IndividualFormRowHandler<K, R> extends DefaultFormRowHandler<K, R> {

  @Override
  public void saveRows(Map<K, FormRow<K, R>> rowForms) throws Exception {
    for (Map.Entry<K, FormRow<K, R>> entry : rowForms.entrySet()) {
      saveRow(entry.getValue());
    }
  }

  @Override
  public void deleteRows(Set<K> keys) throws Exception {
    for (K object : keys) {
      deleteRow(object);
    }
  }

  /**
   * This method, when implemented, should save the given form row.
   * 
   * @param formRow The form row data to be saved.
   * @throws Exception Any exception that may occur during saving.
   */
  public void saveRow(FormRow<K, R> formRow) throws Exception {
  }

  /**
   * This method, when implemented, should delete the given form row.
   * 
   * @param key The unique identifier of the row as defined by {@link FormRow#getKey()}.
   * @throws Exception Any exception that may occur during deleting.
   */
  public void deleteRow(K key) throws Exception {
  }
}
