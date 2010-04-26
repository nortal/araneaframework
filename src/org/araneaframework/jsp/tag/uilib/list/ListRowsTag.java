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

package org.araneaframework.jsp.tag.uilib.list;

import java.io.Writer;
import java.util.ListIterator;
import org.araneaframework.uilib.list.ListWidget;

/**
 * List widget rows tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *  name = "listRows"
 *  body-content = "JSP"
 *  description = "Iterating tag that gives access to each row on the UiLib list current page."
 */
public class ListRowsTag extends BaseListRowsTag {

  protected String var = "row";

  protected ListWidget<?>.ViewModel viewModel;

  @Override
  public int doStartTag(Writer out) throws Exception {
    // Get list data:
    this.viewModel = ListWidget.ViewModel.class.cast(requireContextEntry(ListTag.LIST_VIEW_MODEL_KEY));
    return super.doStartTag(out);
  }

  @Override
  protected void doForEachRow(Writer out) throws Exception {
    super.doForEachRow(out);
    addContextEntry(this.var, this.currentRow);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Name of variable that represents individual row (by default "row")."
   */
  public void setVar(String var) {
    this.var = var;
  }

  @Override
  protected ListIterator<?> getIterator() {
    return this.viewModel.getItemRange().listIterator();
  }
}
