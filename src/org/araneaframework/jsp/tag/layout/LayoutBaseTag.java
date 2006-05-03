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
import org.apache.commons.collections.iterators.LoopingIterator;
import org.araneaframework.jsp.tag.UiPresentationTag;

/**
 * Layout base tag.
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public abstract class LayoutBaseTag extends UiPresentationTag {
  protected List rowClasses = new ArrayList(0);
  protected List cellClasses = new ArrayList(0);
  
  protected String width;
  
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    addContextEntry(LayoutInterface.KEY, this);
    addContextEntry(LayoutInterface.ROWCLASS_KEY, new LoopingIterator(rowClasses));
    addContextEntry(LayoutInterface.CELLCLASS_KEY, new LoopingIterator(cellClasses));

    return EVAL_BODY_INCLUDE;
  }
  
  public abstract void setRowClasses(String rowClasses) throws JspException;
  public abstract void setCellClasses(String cellClasses) throws JspException;
}
