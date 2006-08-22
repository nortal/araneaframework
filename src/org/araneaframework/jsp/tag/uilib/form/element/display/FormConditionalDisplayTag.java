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
import org.araneaframework.uilib.form.control.DisplayControl;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 * @jsp.tag
 *   name = "conditionalDisplay"
 *   body-content = "JSP"
 *   description = "Depending whether element Boolean value is TRUE or FALSE display one or other content, represents UiLib "DisplayControl"."
 */
public class FormConditionalDisplayTag extends BaseFormElementDisplayTag {
  public final static String CONDITION_KEY = "org.araneaframework.jsp.tag.uilib.form.element.display.FormConditionalDisplayTag.CONDITION";    
  protected Boolean condition = Boolean.FALSE;
  
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    assertControlType("DisplayControl");
    
    DisplayControl.ViewModel viewModel = (DisplayControl.ViewModel) controlViewModel;
        
    if (viewModel.getValue() != null) 
      condition = ((Boolean) viewModel.getValue());
     
    addContextEntry(CONDITION_KEY, condition);
    
    return EVAL_BODY_INCLUDE;   
  }
}
