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

import org.araneaframework.uilib.form.BeanFormWidget;

/**
 * Represents one editable row that is a bean of type <code>R</code>. The form row has a key of type <code>K</code>. A
 * {@link BeanFormListWidget} may contain multiple rows but all of them have common row key and row data types.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public class BeanFormRow<K, R> extends FormRow<K, R> {

  
  public BeanFormRow(BeanFormListWidget<K, R> formList, K rowKey, R row, String rowFormId, BeanFormWidget<R> formWidget, boolean open) {
    super(formList, rowKey, row, rowFormId, formWidget, open);
  }

  @Override
  @SuppressWarnings("unchecked")
  public BeanFormWidget<R> getForm() {
    return (BeanFormWidget<R>) super.getForm();
  }

  @Override
  public BeanFormListWidget<K, R> getFormList() {
    return (BeanFormListWidget<K, R>) super.getFormList();
  }
}
