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
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.event.OnChangeEventListener;
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
public class FormCheckboxHtmlTag extends BaseFormElementHtmlTag {
	protected String onChangePrecondition;

	{
		baseStyleClass = "aranea-checkbox";
	}

	protected int doEndTag(Writer out) throws Exception {
		assertControlType("CheckboxControl");	

		// Prepare
		String name = this.getScopedFullFieldId();
		StringArrayRequestControl.ViewModel viewModel = ((StringArrayRequestControl.ViewModel)controlViewModel);

		// Write input tag							
		JspUtil.writeOpenStartTag(out, "input");
		JspUtil.writeAttribute(out, "id", name);
		JspUtil.writeAttribute(out, "name", name);
		JspUtil.writeAttribute(out, "class", getStyleClass());
		JspUtil.writeAttribute(out, "style", getStyle());
		JspUtil.writeAttribute(out, "type", "checkbox");

		if ("true".equals(viewModel.getSimpleValue()))
			JspUtil.writeAttribute(out, "checked", "true");

		if (viewModel.isDisabled())
			JspUtil.writeAttribute(out, "disabled", "true");
		JspUtil.writeAttribute(out, "label", localizedLabel);
		JspUtil.writeAttribute(out, "tabindex", tabindex);
		if (accessKey != null)
			JspUtil.writeAttribute(out, "accesskey", accessKey);

		if (events && viewModel.isOnChangeEventRegistered()) {
			this.writeSubmitScriptForUiEvent(out, "onclick", derivedId, OnChangeEventListener.ON_CHANGE_EVENT, onChangePrecondition, updateRegionNames);
		}

		JspUtil.writeAttributes(out, attributes);   
		JspUtil.writeCloseStartEndTag_SS(out);

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
