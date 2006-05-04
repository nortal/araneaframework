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
 * Layout row base tag.
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public abstract class LayoutRowBaseTag extends UiPresentationTag implements LayoutRowInterface {
  protected List cellClasses = new ArrayList(0);
  
  private LayoutInterface layout;
  private ResettableIterator cellIter;

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    layout = (LayoutInterface)requireContextEntry(LayoutInterface.KEY);
    cellIter = new LoopingIterator(cellClasses);

    addContextEntry(LayoutRowInterface.KEY, this);
    addContextEntry(CellClassProvider.KEY, this);

    return EVAL_BODY_INCLUDE;
  }
  
  public String getCellClass() throws JspException {
    String result = layout.getCellClass();
    if (cellIter.hasNext())
    	return (String)cellIter.next();

    return result;
  }

  public String getStyleClass() throws JspException {
    cellIter.reset();
    String result = layout.getRowClass();
    if (styleClass != null)
      return super.getStyleClass();
    return result;
  }
}
