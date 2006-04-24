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

package org.araneaframework.jsp.tag.uilib.form.element;

import java.io.Writer;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;


/**
 * Represents a hidden input html control.
 * 
 * @author Konstantin Tretyakov
 * 
 * @jsp.tag
 *   name = "hiddenInput"
 *   body-content = "JSP"
 *   description = "Represents a "hidden" html input element mapped to an UiLib HiddenControl."
 */
public class UiStdFormHiddenInputTag extends UiFormElementBaseTag {
   
  //
  // Implementation
  //  

            
  protected int doEndTag(Writer out) throws Exception {
    // Type check
    assertControlType("HiddenControl");

    String name = this.getScopedFullFieldId();    
    StringArrayRequestControl.ViewModel viewModel = ((StringArrayRequestControl.ViewModel)controlViewModel);
    
    // Write
    UiUtil.writeOpenStartTag(out, "input");
    UiUtil.writeAttribute(out, "name", name);     
    UiUtil.writeAttribute(out, "id", name);         
    UiUtil.writeAttribute(out, "type", "hidden");
    UiUtil.writeAttribute(out, "value", viewModel.getSimpleValue());  
    UiUtil.writeCloseStartEndTag_SS(out);
    
    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;
  }
 
  
  protected void init() {
    super.init();

    // Hidden element may not be validated!
    this.validate = false;
    this.validateOnEvent = false;
  }
  
}




