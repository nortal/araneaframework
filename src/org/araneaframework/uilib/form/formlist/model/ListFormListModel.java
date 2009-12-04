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

package org.araneaframework.uilib.form.formlist.model;

import org.araneaframework.core.Assert;

import org.araneaframework.uilib.form.formlist.FormListWidget;

import java.util.List;
import org.araneaframework.uilib.form.formlist.FormListModel;

/**
 * A <code>FormListModel</code> implementation that accepts list rows data in a {@link List}. This object can be
 * provided to {@link FormListWidget} to provide the model data to the list as the list rows.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 */
public class ListFormListModel<R> implements FormListModel<R> {

  private List<R> list;

  /**
   * Constructs a new <code>FormListModel</code> using the given <code>list</code>. Note that the parameter is required.
   * 
   * @param list The values of this list will be used as the rows of the {@link FormListWidget}.
   */
  public ListFormListModel(List<R> list) {
    Assert.notNullParam(list, "list");
    this.list = list;
  }

  public List<R> getRows() {
    return this.list;
  }
}
