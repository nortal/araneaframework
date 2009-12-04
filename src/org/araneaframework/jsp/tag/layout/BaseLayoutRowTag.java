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
import org.apache.commons.collections.iterators.LoopingIterator;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.tag.layout.support.CellClassProvider;
import org.araneaframework.jsp.tag.layout.support.NullIterator;
import org.araneaframework.jsp.tag.layout.support.RowClassProvider;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Layout row base tag.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class BaseLayoutRowTag extends PresentationTag implements CellClassProvider {

  protected boolean overrideLayout = true;

  /**
   * HTML id of the row.
   * 
   * @since 1.1
   */
  protected String id;

  protected List<String> cellClasses;

  private ResettableIterator cellIter;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    this.cellIter = this.cellClasses != null ? new LoopingIterator(this.cellClasses) : new NullIterator();
    if (this.cellClasses != null) {
      addContextEntry(CellClassProvider.KEY, this);
    }

    return EVAL_BODY_INCLUDE;
  }

  public String getCellClass() {
    return this.cellIter.hasNext() ? (String) this.cellIter.next() : null;
  }

  @Override
  public String getStyleClass() {
    this.cellIter.reset();
    RowClassProvider rowClassProvider = (RowClassProvider) getContextEntry(RowClassProvider.KEY);
    String result = rowClassProvider != null ? rowClassProvider.getRowClass() : null;
    result = StringUtils.defaultIfEmpty(result, null);

    String superStyleClass = super.getStyleClass();
    if (superStyleClass != null) {
      StringBuffer sb = new StringBuffer(superStyleClass);
      if (!this.overrideLayout && result != null) {
        sb.append(' ').append(result);
      }

      result = sb.toString();
    }

    return result;
  }

  // Tag attributes

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description ="Default CSS class of cells inside this row. This is multi-valued attribute and overwrites cell CSS classes defined by surrounding layout."
   */
  public void setCellClasses(String cellClasses) {
    this.cellClasses = JspUtil.parseMultiValuedAttribute(evaluate("cellClasses", cellClasses, String.class));
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description ="Whether row's styleClass completely overrides CSS class provided by surrounding layout (default behavior), or is appended to layout's CSS class."
   */
  public void setOverrideLayout(String overrideLayout) {
    this.overrideLayout = evaluate("overrideLayout", overrideLayout, Boolean.class);
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "HTML id of this row."
   * @since 1.1
   */
  public void setId(String id) {
    this.id = evaluate("id", id, String.class);
  }
}
