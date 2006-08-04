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
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.tag.layout.support.CellClassProvider;
import org.araneaframework.jsp.tag.layout.support.NullIterator;
import org.araneaframework.jsp.tag.layout.support.RowClassProvider;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Layout row base tag.
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public abstract class BaseLayoutRowTag extends PresentationTag implements CellClassProvider {
  protected List cellClasses;
  private ResettableIterator cellIter;

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    requireContextEntry(RowClassProvider.KEY);
    requireContextEntry(CellClassProvider.KEY);

    cellIter = cellClasses != null ? (ResettableIterator)new LoopingIterator(cellClasses) : new NullIterator();
    if (cellClasses != null)
      addContextEntry(CellClassProvider.KEY, this);

    return EVAL_BODY_INCLUDE;
  }
  
  public String getCellClass() throws JspException {
    return cellIter.hasNext() ? (String)cellIter.next() : null;
  }

  public String getStyleClass() throws JspException {
    cellIter.reset();
    String result = ((RowClassProvider)requireContextEntry(RowClassProvider.KEY)).getRowClass();
    if (styleClass != null)
      return super.getStyleClass();
    return (result != null && result.length() == 0) ? null : result;
  }
  
  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Default styleclass of cells inside this row. This is multi-valued attribute and overwrites cell styleclasses defined by surrounding layout."
   */
  public void setCellClasses(String cellClasses) throws JspException {
    this.cellClasses = JspUtil.parseMultiValuedAttribute((String)evaluate("cellClasses", cellClasses, String.class));
  }
}
