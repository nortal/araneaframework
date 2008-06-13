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

package org.araneaframework.jsp.tag.uilib.form.element.display;

import java.io.Writer;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementDisplayTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.DisplayControl;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 * @jsp.tag
 *   name = "textDisplay"
 *   body-content = "empty"
 *   description = "Display element value as string, represents UiLib "DisplayControl"."
 */
public class FormTextDisplayHtmlTag extends BaseFormElementDisplayTag {
  
  /**
   */
  protected int doEndTag(Writer out) throws Exception {
    assertControlType("DisplayControl");
    
    DisplayControl.ViewModel viewModel = (DisplayControl.ViewModel) controlViewModel;
    
    
    if (getStyleClass() != null) {
      JspUtil.writeOpenStartTag(out, "span");
      JspUtil.writeAttribute(out, "id", getFullFieldId());
      JspUtil.writeAttribute(out, "class", getStyleClass());
      JspUtil.writeAttribute(out, "style", getStyle());
      JspUtil.writeAttributes(out, attributes);
      JspUtil.writeCloseStartTag(out);
    }        
    
    if (viewModel.getValue() != null)
    	JspUtil.writeEscaped(out, viewModel.getValue().toString());
    
    if (getStyleClass() != null)
      JspUtil.writeEndTag(out, "span");
    
    super.doEndTag(out);    
    return EVAL_PAGE;
  }
}
