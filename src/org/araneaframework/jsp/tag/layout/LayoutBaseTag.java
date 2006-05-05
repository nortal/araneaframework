/**
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
 **/

package org.araneaframework.jsp.tag.layout;

import java.io.Writer;
import java.util.List;
import javax.servlet.jsp.JspException;
import org.apache.commons.collections.ResettableIterator;
import org.apache.commons.collections.iterators.LoopingIterator;
import org.araneaframework.jsp.tag.UiPresentationTag;
import org.araneaframework.jsp.tag.layout.support.NullIterator;
import org.araneaframework.jsp.util.UiUtil;

/**
 * Layout base tag.
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public abstract class LayoutBaseTag extends UiPresentationTag implements RowClassProvider, CellClassProvider {
  protected List rowClasses;
  protected List cellClasses;
  
  protected ResettableIterator rowIter;
  protected ResettableIterator cellIter;
  
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    addContextEntry(RowClassProvider.KEY, this);
    addContextEntry(CellClassProvider.KEY, this);

    rowIter = rowClasses != null ? (ResettableIterator)new LoopingIterator(rowClasses) : new NullIterator();
    cellIter = cellClasses != null ?(ResettableIterator) new LoopingIterator(cellClasses) : new NullIterator();

    return EVAL_BODY_INCLUDE;
  }
  
  public String getRowClass() throws JspException {
    cellIter.reset();
	return rowIter.hasNext() ? (String)rowIter.next() : null;
  }
  
  public String getCellClass() throws JspException {
    return cellIter.hasNext() ? (String)cellIter.next() : null;
  }
  
  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Default style of rows in this layout. This is multi-valued attribute." 
   */
  public void setRowClasses(String rowClasses) throws JspException {
    this.rowClasses = UiUtil.parseMultiValuedAttribute((String)evaluate("rowClasses", rowClasses, String.class));
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Default styleclass of cells in this layout. This is multi-valued attribute."
   */
  public void setCellClasses(String cellClasses) throws JspException {
    this.cellClasses = UiUtil.parseMultiValuedAttribute((String)evaluate("cellClasses", cellClasses, String.class));
  }
}
