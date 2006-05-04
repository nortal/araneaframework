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
import java.util.ArrayList;
import java.util.List;
import javax.servlet.jsp.JspException;
import org.apache.commons.collections.ResettableIterator;
import org.apache.commons.collections.iterators.LoopingIterator;
import org.araneaframework.jsp.tag.UiPresentationTag;

/**
 * Layout base tag.
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public abstract class LayoutBaseTag extends UiPresentationTag implements LayoutInterface {
  protected List rowClasses = new ArrayList(0);
  protected List cellClasses = new ArrayList(0);
  
  private ResettableIterator rowIter;
  private ResettableIterator cellIter;
  
  protected String width;
  
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    addContextEntry(LayoutInterface.KEY, this);
    addContextEntry(RowClassProvider.KEY, this);
    addContextEntry(CellClassProvider.KEY, this);

    rowIter = new LoopingIterator(rowClasses);
    cellIter = new LoopingIterator(cellClasses);

    return EVAL_BODY_INCLUDE;
  }
  
  public String getRowClass() throws JspException {
    cellIter.reset();
	return rowIter.hasNext() ? (String)rowIter.next() : null;
  }
  
  public String getCellClass() throws JspException {
    return cellIter.hasNext() ? (String)cellIter.next() : null;
  }
  
  public abstract void setRowClasses(String rowClasses) throws JspException;
  public abstract void setCellClasses(String cellClasses) throws JspException;
}
