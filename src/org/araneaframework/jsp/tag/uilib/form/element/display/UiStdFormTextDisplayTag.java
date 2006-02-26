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
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseDisplayTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.DisplayControl;

/**
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 * @jsp.tag
 *   name = "textDisplay"
 *   body-content = "empty"
 *   description = "Display element value as string, represents UiLib "DisplayControl"."
 */
public class UiStdFormTextDisplayTag extends UiFormElementBaseDisplayTag {
  
  /**
   */
  protected int after(Writer out) throws Exception {
    assertControlType("DisplayControl");
    
    DisplayControl.ViewModel viewModel = (DisplayControl.ViewModel) controlViewModel;
    
    
    if (getStyleClass() != null) {
      UiUtil.writeOpenStartTag(out, "span");
      UiUtil.writeAttribute(out, "class", getStyleClass());
      UiUtil.writeCloseStartTag(out);
    }        
    
    if (viewModel.getValue() != null)
    	UiUtil.writeEscaped(out, viewModel.getValue().toString());
    
    if (getStyleClass() != null)
      UiUtil.writeEndTag(out, "span");
    
    super.after(out);    
    return EVAL_PAGE;
  }
}
