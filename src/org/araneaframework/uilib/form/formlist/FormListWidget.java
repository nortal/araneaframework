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

import org.araneaframework.uilib.form.formlist.model.MapFormListModel;

import org.araneaframework.uilib.form.formlist.model.ListWidgetFormListModel;

import org.araneaframework.uilib.form.formlist.model.ListFormListModel;

import org.araneaframework.uilib.form.FormWidget;

/**
 * Editable rows widget that is used to handle simultaneous editing of multiple forms with same structure.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class FormListWidget<K, R> extends BaseFormListWidget<K, R> {

  /**
   * Creates a new <code>FormListWidget</code> using the given <code>rowHandler</code>. Note that
   * <code>rowHandler</code> must not be null.
   * 
   * @param rowHandler The row handler to be used with this <code>FormListWidget</code>.
   */
  public FormListWidget(FormRowHandler<K, R> rowHandler) {
    super(rowHandler);
  }

  /**
   * Creates a new <code>FormListWidget</code> using the given <code>rowHandler</code> and <code>listModel</code>. Note
   * that <code>rowHandler</code> must not be null. The <code>listModel</code> can be used to fill the form with rows.
   * 
   * @param rowHandler The row handler to be used with this <code>FormListWidget</code>.
   * @param listModel An object that implements {@link FormListModel} and provides the rows to this
   *          <code>FormListWidget</code>.
   * @see ListFormListModel
   * @see ListWidgetFormListModel
   * @see MapFormListModel
   */
  public FormListWidget(FormRowHandler<K, R> rowHandler, FormListModel<R> listModel) {
    super(rowHandler, listModel);
  }

  @Override
  protected FormWidget buildAddForm() {
    return new FormWidget();
  }
}
