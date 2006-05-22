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
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;


/**
 * Standard button form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "checkbox"
 *   body-content = "JSP"
 *   description = "Form checkbox input field, represents UiLib "CheckboxControl"."
 */
public class UiStdFormCheckboxTag extends UiFormElementBaseTag {
	protected String onChangePrecondition;

	protected int doEndTag(Writer out) throws Exception {
		assertControlType("CheckboxControl");		

		// Prepare
		String name = this.getScopedFullFieldId(); 		
		StringArrayRequestControl.ViewModel viewModel = ((StringArrayRequestControl.ViewModel)controlViewModel);

		// Write input tag							
		UiUtil.writeOpenStartTag(out, "input");
		//UiUtil.writeAttribute(out, "id", name);
		UiUtil.writeAttribute(out, "name", name);
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeAttribute(out, "type", "checkbox");

		if ("true".equals(viewModel.getSimpleValue()))
			UiUtil.writeAttribute(out, "checked", "true");

		if (viewModel.isDisabled())
			UiUtil.writeAttribute(out, "disabled", "true");
		UiUtil.writeAttribute(out, "label", localizedLabel);
		UiUtil.writeAttribute(out, "tabindex", tabindex);
		if (accessKey != null)
			UiUtil.writeAttribute(out, "accesskey", accessKey);

		if (events && viewModel.isOnChangeEventRegistered())
			this.writeEventAttributeForUiEvent(out, "onclick", derivedId, "onChanged", validateOnEvent, onChangePrecondition,
					updateRegionNames);

		UiUtil.writeAttributes(out, attributes);   
		UiUtil.writeCloseStartEndTag_SS(out);

		super.doEndTag(out);
		return EVAL_PAGE;      
	}

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Precondition for deciding whether go to server side or not." 
	 */	  
	public void setOnChangePrecondition(String onChangePrecondition)throws JspException {
		this.onChangePrecondition = (String) evaluate("onChangePrecondition", onChangePrecondition, String.class);
	}
}
