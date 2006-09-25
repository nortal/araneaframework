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
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.tag.layout.support.CellClassProvider;

/**
 * Layout cell base tag.
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class BaseLayoutCellTag extends PresentationTag {
  protected boolean overrideLayout = true;

  protected int doStartTag(Writer out) throws Exception {
    requireContextEntry(CellClassProvider.KEY);

    return EVAL_BODY_INCLUDE;
  }

  public String getStyleClass() throws JspException {
    String result = ((CellClassProvider)requireContextEntry(CellClassProvider.KEY)).getCellClass();
    result = (result != null && result.length() == 0) ? null : result;
    
    String superStyleClass = super.getStyleClass();
    if (superStyleClass != null) {
      StringBuffer sb = new StringBuffer(superStyleClass);
      if (!overrideLayout && result != null)
        sb.append(' ').append(result);
      
      result = sb.toString();
    }
    return result;
  }
  
  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Whether cell's styleClass completely overrides styleClass provided by surrounding layout (default behaviour), or is appended to layout's styleClass."
   */
  public void setOverrideLayout(String overrideLayout) throws JspException {
    this.overrideLayout = ((Boolean)evaluate("overrideLayout", overrideLayout, Boolean.class)).booleanValue();
  }
}
