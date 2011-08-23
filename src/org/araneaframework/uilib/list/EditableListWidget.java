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

import org.araneaframework.core.util.Assert;
import org.araneaframework.uilib.form.formlist.FormListWidget;
import org.araneaframework.uilib.form.formlist.FormRowHandler;
import org.araneaframework.uilib.form.formlist.model.ListWidgetFormListModel;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class EditableListWidget<K, R> extends ListWidget<R> {

  protected FormListWidget<K, R> formList;

  public EditableListWidget(FormRowHandler<K, R> rowHandler) {
    this.formList = new FormListWidget<K, R>(rowHandler, new ListWidgetFormListModel<R>(this));
  }

  // *********************************************************************
  // * PUBLIC METHODS
  // *********************************************************************

  /**
   * Returns the editable row manager.
   * 
   * @return the editable row manager.
   */
  public FormListWidget<K, R> getFormList() {
    return this.formList;
  }

  /**
   * Sets the <code>FormRowHandler</code> that this list widget will start to use.
   * 
   * @param rowHandler The new <code>FormRowHandler</code>. Must not be <code>null</code>.
   */
  public void setFormRowHandler(FormRowHandler<K, R> rowHandler) {
    this.formList.setFormRowHandler(rowHandler);
  }

  // *********************************************************************
  // * WIDGET METHODS
  // *********************************************************************

  @Override
  protected void init() throws Exception {
    super.init();
    Assert.notNull(this.formList, "You must provide a form row handler to the editable list!");
    addWidget("formList", this.formList);
  }
}
