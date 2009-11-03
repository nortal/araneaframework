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

import org.araneaframework.uilib.form.reader.ListFormReader;

import org.araneaframework.uilib.form.BeanFormWidget;

/**
 * Editable rows list widget that is used to handle simultaneous editing of multiple forms with same structure. The
 * generic parameter K corresponds to the type of the key values, and the generic parameter R corresponds to the type of
 * the row values.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class BeanFormListWidget<K, R> extends BaseFormListWidget<K, R> {

  /**
   * The class of the list row data.
   */
  protected Class<R> beanClass;

  /**
   * Constructs a form list widget using the given row handler that will respond to form list events. The
   * <code>beanClass</code> is used when the list has also add-form for adding new rows to list.
   * 
   * @param formRowHandler The handler that will respond to form list events.
   * @param beanClass The class of form and list row data.
   */
  public BeanFormListWidget(FormRowHandler<K, R> formRowHandler, Class<R> beanClass) {
    super(formRowHandler);
    this.beanClass = beanClass;
  }

  /**
   * Constructs a form list widget using the given row handler (that will respond to form list events) and the given
   * model callback (that will provide the rows data to this form list). The <code>beanClass</code> is used when the
   * list has also add-form for adding new rows to list.
   * 
   * @param rowHandler The handler that will respond to form list events.
   * @param model The model callback that will provide the rows data to this form list.
   * @param beanClass The class of form and list row data.
   */
  public BeanFormListWidget(FormRowHandler<K, R> rowHandler, FormListModel<R> model, Class<R> beanClass) {
    super(rowHandler, model);
    this.beanClass = beanClass;
  }

  /**
   * Provides the bean class of list forms and list row data. This is usually used by {@link ListFormReader}.
   * 
   * @return The class of the list row data as specified to the constructor of this class.
   */
  public Class<R> getBeanClass() {
    return this.beanClass;
  }

  @Override
  protected BeanFormWidget<R> buildAddForm() {
    return new BeanFormWidget<R>(this.beanClass);
  }
}
