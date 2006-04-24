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

package org.araneaframework.jsp.tag.uilib.form.element.select;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementLabelBaseTag;
import org.araneaframework.jsp.tag.uilib.form.UiStdFormSimpleLabelTag;
import org.araneaframework.uilib.form.control.SelectControl;


/**
 * Standard form element label tag.
 * 
 * @author Marko Muts
 * 
 * @jsp.tag
 *   name = "radioSelectItemLabel"
 *   body-content = "JSP" 
 *   description = "Represents localizable label."
 */
public class UiStdFormRadioSelectItemLabelTag extends UiFormElementLabelBaseTag {
  //
  // Attributes
  //  
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Select item value." 
	 */
  public void setValue(String value) throws JspException  {
    this.value = (String)evaluateNotNull("value", value, String.class);
  }
    
	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
    // Type check
		//assertControlType("SelectControl");		
		
		// Prepare	
		SelectControl.ViewModel viewModel = ((SelectControl.ViewModel)controlViewModel);
    localizedLabel = viewModel.getLabelForValue(value);
    
    
    UiStdFormSimpleLabelTag.writeSelectLabel(
                                out, 
                                localizedLabel, 
                                getStyleClass()
                              );
							
		// Continue
	  return EVAL_BODY_INCLUDE;		
	}
	
  protected void init() {
    super.init();
    value = null;
  }	
  
  protected String value;
}




