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
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.ButtonControl;


/**
 * Standard button form element base tag.
 * 
 * @author Oleg Mürk
 */
public class UiStdFormButtonBaseTag extends UiFormElementBaseTag {
  protected boolean showLabel;
    
  protected String onClickPrecondition;
  protected ButtonControl.ViewModel viewModel;
	
  
  //
  // Attributes
  //  

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Whether button shows its label." 
	 */
  public void setShowLabel(String showLabel) {
    this.showLabel = showLabel.equalsIgnoreCase("true");
  }

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Precondition for deciding whether go to server side or not." 
	 */
  public void setOnClickPrecondition(String onClickPrecondition) throws JspException {
    this.onClickPrecondition = (String) evaluate("onClickPrecondition", onClickPrecondition, String.class);
  }
  //
  // Implementation
  //  
  
  protected int before(Writer out) throws Exception {
    super.before(out);
    
    // Type check
    assertControlType("ButtonControl");
    
    // Prepare    
    viewModel = ((ButtonControl.ViewModel)controlViewModel);
    
 
    
    // Check whether access key was specified in the resources
    if (accessKey == null) {
      	accessKey = UiUtil.getResourceStringOrNull(pageContext, controlViewModel.getLabel() + ".access-key");
      	if (accessKey != null && accessKey.length() != 1) accessKey = null;
    }
    
    // Continue
    return EVAL_BODY_INCLUDE;    
  }
  
  protected void init() {
    super.init();
    showLabel = true;
    onClickPrecondition = "return true;";
  }
}




