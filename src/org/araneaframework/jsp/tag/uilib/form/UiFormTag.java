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
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.uilib.UiWidgetTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.FormWidget;


/**
 * FormWidget widget tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "form"
 *   body-content = "JSP"
 *   description = "UiLib form tag. <br/> 
           Makes available following page scope variables: 
           <ul>
             <li><i>form</i> - UiLib form view model.
             <li><i>formId</i> - UiLib form id.
           </ul> "
 */
public class UiFormTag extends UiWidgetTag {
	public final static String FORM_ID_KEY_REQUEST = "formId";
	public final static String FORM_SCOPED_FULL_ID_KEY_REQUEST = "org.araneaframework.jsp.ui.uilib.form.UiFormTag.SCOPED_FULL_ID";
	public final static String FORM_FULL_ID_KEY_REQUEST = "org.araneaframework.jsp.ui.uilib.form.UiFormTag.FULL_ID";
	public final static String FORM_VIEW_MODEL_KEY_REQUEST = "form";
	public final static String FORM_KEY_REQUEST = "org.araneaframework.jsp.ui.uilib.form.UiFormTag.FORM";    
  
  //
  // Implementation
  //
  
	public int before(Writer out) throws Exception {
		super.before(out);
		
		// Get form data		
		formViewModel = (FormWidget.ViewModel) viewModel;		

		// Set variables
		pushAttribute(FORM_ID_KEY_REQUEST, id, PageContext.REQUEST_SCOPE);
		pushAttribute(FORM_SCOPED_FULL_ID_KEY_REQUEST, scopedFullId, PageContext.REQUEST_SCOPE);
		pushAttribute(FORM_FULL_ID_KEY_REQUEST, fullId, PageContext.REQUEST_SCOPE);
		pushAttribute(FORM_VIEW_MODEL_KEY_REQUEST, formViewModel, PageContext.REQUEST_SCOPE);
		pushAttribute(FORM_KEY_REQUEST, widget, PageContext.REQUEST_SCOPE);
   
		writeJavascript(out);
	
		// Continue
	  return EVAL_BODY_INCLUDE;		
	}

   
  /**
   * Write javascript that accompanies this tag.
   * Validation-related stuff mostly.
   * @author Konstantin Tretyakov
   */
  protected void writeJavascript(Writer out) throws Exception{
    // Append a property to the global uiSystemFormProperties object
    // this property's property "validator" will be our form's validator
    UiUtil.writeStartTag(out, "script");
    out.write("uiFormContext(");
    UiUtil.writeScriptString(out, fullId);
    out.write(");\n");
    UiUtil.writeEndTag(out, "script");
  }
  
	protected FormWidget.ViewModel formViewModel;		
}
