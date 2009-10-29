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
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.tag.layout.support.CellClassProvider;

/**
 * Layout cell base tag.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class BaseLayoutCellTag extends PresentationTag {

  protected boolean overrideLayout = true;

  /**
   * HTML id of the cell.
   * 
   * @since 1.1
   */
  protected String id;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    return EVAL_BODY_INCLUDE;
  }

  @Override
  public String getStyleClass() {
    CellClassProvider cellClassProvider = (CellClassProvider) getContextEntry(CellClassProvider.KEY);
    String result = cellClassProvider != null ? cellClassProvider.getCellClass() : null;
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
   *    description ="Whether cell's styleClass completely overrides styleClass provided by surrounding layout (default behavior), or is appended to layout's styleClass."
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
