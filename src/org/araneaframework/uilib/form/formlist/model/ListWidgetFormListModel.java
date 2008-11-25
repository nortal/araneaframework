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

import java.util.List;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.formlist.FormListModel;
import org.araneaframework.uilib.list.ListWidget;

public class ListWidgetFormListModel implements FormListModel {

  private static final long serialVersionUID = 1L;

  private ListWidget listWidget;

  public ListWidgetFormListModel(ListWidget listWidget) {
    Assert.notNullParam(listWidget, "listWidget");
    this.listWidget = listWidget;
  }

  public List getRows() {
    return listWidget.getItemRange();
  }
}
