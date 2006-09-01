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
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.uilib.BaseWidgetTag;
import org.araneaframework.uilib.form.FormWidget;


/**
 * {@link org.araneaframework.uilib.form.FormWidget} tag.
 * Specifies form context for inner tags, makes 
 * {@link org.araneaframework.uilib.form.FormWidget.ViewModel} and 
 * widget id accessible to inner tags as EL variables.
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
           </ul> "
 */
public class FormTag extends BaseWidgetTag {
	public final static String FORM_SCOPED_FULL_ID_KEY = "org.araneaframework.jsp.tag.uilib.form.FormTag..SCOPED_FULL_ID";
	public final static String FORM_FULL_ID_KEY = "org.araneaframework.jsp.tag.uilib.form.FormTag.FULL_ID";
	public final static String FORM_VIEW_MODEL_KEY = "form";
	public final static String FORM_KEY = "org.araneaframework.jsp.tag.uilib.form.FormTag.FORM";
	
	protected FormWidget.ViewModel formViewModel;

	public int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		// Get form data
		try {
			formViewModel = (FormWidget.ViewModel) viewModel;
		} catch (ClassCastException e) {
			throw new AraneaJspException("Could not acquire form view model. <ui:form> should have an id specified or should be in context of real FormWidget.", e);
		}

		// Set variables
		addContextEntry(FORM_SCOPED_FULL_ID_KEY, scopedFullId);
		addContextEntry(FORM_FULL_ID_KEY, fullId);
		addContextEntry(FORM_VIEW_MODEL_KEY, formViewModel);
		addContextEntry(FORM_KEY, widget);
	
		// Continue
	  return EVAL_BODY_INCLUDE;		
	}
}
