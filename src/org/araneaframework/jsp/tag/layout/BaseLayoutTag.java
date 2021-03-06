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

package org.araneaframework.jsp.tag.layout;

import java.io.Writer;
import java.util.List;
import org.apache.commons.collections.ResettableIterator;
import org.apache.commons.collections.iterators.EmptyIterator;
import org.apache.commons.collections.iterators.LoopingIterator;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.tag.layout.support.CellClassProvider;
import org.araneaframework.jsp.tag.layout.support.RowClassProvider;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Layout base tag.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class BaseLayoutTag extends PresentationTag implements RowClassProvider, CellClassProvider {

  protected List<String> rowClasses;

  protected List<String> cellClasses;

  protected ResettableIterator rowIter;

  protected ResettableIterator cellIter;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    addContextEntry(RowClassProvider.KEY, this);
    addContextEntry(CellClassProvider.KEY, this);

    this.rowIter = this.rowClasses != null ? new LoopingIterator(this.rowClasses) : EmptyIterator.RESETTABLE_INSTANCE;
    this.cellIter = this.cellClasses != null ? new LoopingIterator(this.cellClasses) : EmptyIterator.RESETTABLE_INSTANCE;

    return EVAL_BODY_INCLUDE;
  }

  public String getRowClass() {
    this.cellIter.reset();
    return this.rowIter.hasNext() ? (String) this.rowIter.next() : null;
  }

  public String getCellClass() {
    return this.cellIter.hasNext() ? (String) this.cellIter.next() : null;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Default style of rows in this layout. This is multi-valued attribute."
   */
  public void setRowClasses(String rowClasses) {
    this.rowClasses = JspUtil.parseMultiValuedAttribute(evaluate("rowClasses", rowClasses, String.class));
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Default CSS class of cells in this layout. This is multi-valued attribute."
   */
  public void setCellClasses(String cellClasses) {
    this.cellClasses = JspUtil.parseMultiValuedAttribute(evaluate("cellClasses", cellClasses, String.class));
  }
}
