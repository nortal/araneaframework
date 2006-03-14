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

package org.araneaframework.jsp.tag.uilib.form;				

import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.util.UiWidgetUtil;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;


/**
 * Base form element data tag.
 * 
 * @author Jevgeni Kabanov
 * 
 * @jsp.tag
 *   name = "formElement"
 *   body-content = "JSP"
 *   description = "UiLib form element tag.  <br/>                 
           Makes available following page scope variables: 
           <ul>
             <li><i>formElement</i> - UiLib form element view model.
             <li><i>formElementId</i> - UiLib form element id.
           </ul> "
 */
public class UiFormElementTag extends UiBaseTag {
	public static final String ID_KEY_REQUEST = "formElementId";
	public static final String VIEW_MODEL_KEY_REQUEST = "formElement";
  public static final String VALUE_KEY_REQUEST = "formElementValue";
	
	protected String id;
	protected FormElement.ViewModel formElementViewModel;
  
  //
  // Attributes
  //
  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "true"
	 *   description = "UiLib form element id." 
	 */
  public void setId(String id) throws JspException {
    this.id = (String)evaluateNotNull("id", id, String.class);
  }
  
  //
  // Implementation
  //
  	
	public int before(Writer out) throws Exception {
    super.before(out);
    
    // Get form data    
    FormWidget form = (FormWidget)readAttribute(UiFormTag.FORM_KEY_REQUEST, PageContext.REQUEST_SCOPE);
    
    // Get form element
    formElementViewModel = 
      (FormElement.ViewModel) UiWidgetUtil.traverseToSubWidget(form, id)._getViewable().getViewModel();   
		   
		// Store data
    pushAttribute(VIEW_MODEL_KEY_REQUEST, formElementViewModel, PageContext.REQUEST_SCOPE);
    pushAttribute(ID_KEY_REQUEST, id, PageContext.REQUEST_SCOPE);
    pushAttribute(VALUE_KEY_REQUEST, formElementViewModel.getValue(), PageContext.REQUEST_SCOPE);
    				
		// Continue
	  return EVAL_BODY_INCLUDE;		
	}
  
  protected void init() {
    super.init();
    id = null;
  }    
}




