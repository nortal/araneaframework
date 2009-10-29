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

package org.araneaframework.uilib.list;

import org.araneaframework.uilib.form.formlist.FormListUtil;

import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.form.formlist.FormRowHandler;
import org.araneaframework.uilib.form.formlist.model.ListWidgetFormListModel;

/**
 * A list widget that extends the functionality of {@link BeanListWidget} by adding the possibility of editing the list
 * rows. The list forms are distinct from <code>BeanListWidget</code> because their functionality is handled by
 * {@link BeanFormListWidget} that is provided with access to the data of this <code>(Editable)BeanListWidget</code>
 * through {@link ListWidgetFormListModel}. In addition, the <code>BeanFormListWidget</code> also requires a
 * {@link FormRowHandler} implementation that is invoked when list row events occur.
 * <p>
 * If you use <code>EditableBeanListWidget</code> then you also might want to add buttons to your form that would invoke
 * events (e.g. to start editing a row or to save a row). To add those buttons, use methods in {@link FormListUtil}.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class EditableBeanListWidget<K, R> extends BeanListWidget<R> {

  /**
   * An instance of form list widget that takes care of the form logic in the list. The form logic can be accessed
   * through {@link #getFormList()}.
   */
  protected BeanFormListWidget<K, R> formList;

  /**
   * Creates a new editable {@link BeanListWidget} for row objects of class <code>beanClass</code>. The
   * <code>rowHandler</code> is used to notify about the row form event that occur.
   * 
   * @param rowHandler The handler that will respond to form list events.
   * @param beanClass The list row data object type.
   */
  public EditableBeanListWidget(FormRowHandler<K, R> rowHandler, Class<R> beanClass) {
    super(beanClass);
    this.formList = new BeanFormListWidget<K, R>(rowHandler, new ListWidgetFormListModel<R>(this), beanClass);
  }

  /**
   * Returns the editable row manager.
   * 
   * @return the editable row manager.
   */
  public BeanFormListWidget<K, R> getFormList() {
    return this.formList;
  }

  /**
   * Provides the possibility of changing the form row handler of the editable list.
   * 
   * @param rowHandler A rowHandler that responds to the list row events.
   */
  public void setFormRowHandler(FormRowHandler<K, R> rowHandler) {
    this.formList.setFormRowHandler(rowHandler);
  }

  @Override
  protected void init() throws Exception {
    super.init();
    Assert.notNull(this.formList, "You must provide a form row handler to the editable list!");
    addWidget("formList", this.formList);
  }
}
