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

import org.araneaframework.uilib.form.formlist.BeanFormRow;

import org.araneaframework.uilib.form.formlist.FormRow;

import org.araneaframework.uilib.form.BeanFormWidget;

import org.araneaframework.uilib.form.FormWidget;

@SuppressWarnings("unchecked")
public abstract class ValidOnlyIndividualBeanFormRowHandler<K, R> extends ValidOnlyIndividualFormRowHandler<K, R> {

  @Override
  public void initAddForm(FormWidget addForm) throws Exception {
    initAddForm((BeanFormWidget<R>) addForm);
  }

  @Override
  public final void initFormRow(FormRow<K, R> editableRow, R row) throws Exception {
    initFormRow((BeanFormRow<K, R>) editableRow, row);
  }

  @Override
  public final void addValidRow(FormWidget rowForm) throws Exception {
    addValidRow((BeanFormWidget<R>) rowForm);
  }

  @Override
  public final void saveValidRow(FormRow<K, R> formRow) throws Exception {
    saveValidRow((BeanFormRow<K, R>) formRow);
  }

  /**
   * The underlying implementation should initialize the given bean form that will be used to add new rows.
   * 
   * @param addForm An instance of bean form that will be used to add new bean rows to the list.
   * @throws Exception Any exception that may occur.
   */
  public void initAddForm(BeanFormWidget<R> addForm) throws Exception {}

  /**
   * The underlying implementation should initialize the <code>editableForm</code> that will be used to edit given
   * <code>row</code>.
   * 
   * @param editableRow The form of the row to edit the row. Use it to add controls and buttons, for example.
   * @param row The row that is about to be edited.
   * @throws Exception Any exception that may occur.
   */
  public void initFormRow(BeanFormRow<K, R> editableRow, R row) throws Exception {}

  /**
   * The implementation of this method should add and save the row form data into a persistent data source.
   * 
   * @param formRow The valid row form data to be added.
   * @throws Exception Any exception that may occur.
   */
  public void addValidRow(BeanFormWidget<R> formRow) throws Exception {}

  /**
   * The implementation of this method should save the modified row form data into a persistent data source.
   * 
   * @param formRow The valid row form data to be saved.
   * @throws Exception Any exception that may occur.
   */
  public void saveValidRow(BeanFormRow<K, R> formRow) throws Exception {}

}
