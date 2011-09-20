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
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.formlist.FormListUtil;
import org.araneaframework.uilib.form.formlist.FormRow;

/**
 * A more precise form row handler that narrows row data adding and saving down to only valid rows.
 * 
 * @param <K> The type of the form row key value.
 * @param <R> The type of the form row value.
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class ValidOnlyFormRowHandler<K, R> extends DefaultFormRowHandler<K, R> {

  @Override
  public final void addRow(FormWidget rowForm) throws Exception {
    if (rowForm.convertAndValidate()) {
      addValidRow(rowForm);
    }
  }

  @Override
  public final void saveRows(Map<K, FormRow<K, R>> formRows) throws Exception {
    if (FormListUtil.convertAndValidateRowForms(formRows)) {
      saveValidRows(formRows);
    }
  }

  /**
   * This method is called by {@link #addRow(FormWidget)} to add only one valid row. The implementation of this method
   * should add and save the row form data into a persistent data source.
   * 
   * @param rowForm The row form data to add into a persistent data source.
   * @throws Exception Any exception that might occur during saving the data.
   */
  public void addValidRow(FormWidget rowForm) throws Exception {
  }

  /**
   * This method is called by {@link #saveRows(Map)} to save only valid rows. The implementation of this method should
   * save the row form data into a persistent data source.
   * 
   * @param rowForms The forms data from rows to be save into a persistent data source.
   * @throws Exception Any exception that might occur during saving the data.
   */
  public void saveValidRows(Map<K, FormRow<K, R>> rowForms) throws Exception {
  }
}
