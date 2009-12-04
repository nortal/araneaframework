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
import org.araneaframework.jsp.tag.BaseIterationTag;

/**
 * List widget rows tag.
 * 
 * @author Oleg Mürk
 */
@SuppressWarnings("unchecked")
public abstract class BaseListRowsTag extends BaseIterationTag {

  public static final String ROW_REQUEST_ID_KEY = "rowRequestId";

  public static final String ROW_KEY = "org.araneaframework.jsp.tag.uilib.list.BaseListRowsTag.ROW";

  protected Object currentRow;

  protected ListIterator rowIterator;

  @Override
  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Get list row iterator
    this.rowIterator = getIterator();

    // Get first row & continue if needed
    if (this.rowIterator.hasNext()) {
      doForEachRow(out);
      return EVAL_BODY_INCLUDE;
    } else {
      return SKIP_BODY;
    }
  }

  /**
   * This method must handle rendering each row. The {@link BaseListRowsTag#doForEachRow(Writer)} handles setting the
   * context variables, subclasses should extend that functionality with actual rendering.
   * 
   * @param out The output steam to write to the rendering result.
   */
  protected void doForEachRow(Writer out) throws Exception {
    this.currentRow = this.rowIterator.next();
    addContextEntry(ROW_KEY, this.currentRow);
    addContextEntry(ROW_REQUEST_ID_KEY, Integer.toString(this.rowIterator.previousIndex()));
  }

  @Override
  protected int afterBody(Writer out) throws Exception {
    // Get next row & continue if needed
    if (this.rowIterator.hasNext()) {
      doForEachRow(out);
      return EVAL_BODY_AGAIN;
    } else {
      return SKIP_BODY;
    }
  }

  protected abstract ListIterator getIterator();
}
