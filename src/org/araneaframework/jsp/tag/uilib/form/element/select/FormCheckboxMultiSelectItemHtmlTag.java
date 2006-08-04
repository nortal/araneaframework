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
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.MultiSelectControl;


/**
 * Checkbox multi select item form element tag.
 * 
 * @author Oleg Murk
 * @author Jevgeni Kabanov
 * 
 * @jsp.tag
 *   name = "checkboxMultiSelectItem"
 *   body-content = "JSP"
 *   description = "Form radio button, represents one item from UiLib "MultiSelectControl"."
 */
public class FormCheckboxMultiSelectItemHtmlTag extends BaseFormElementHtmlTag {
	protected String value;

	{
		baseStyleClass = "aranea-multi-checkbox";
	}
	
	protected int doEndTag(Writer out) throws Exception {
		// Type check
		assertControlType("MultiSelectControl");		

		// Prepare
		String name = this.getScopedFullFieldId(); 		
		MultiSelectControl.ViewModel viewModel = ((MultiSelectControl.ViewModel)controlViewModel);

		if (viewModel.getSelectItemByValue(value) == null) 
			throw new AraneaJspException("Value '" + value + "' not found in values list.");

		JspUtil.writeOpenStartTag(out, "input");
		JspUtil.writeAttribute(out, "name", name);
		JspUtil.writeAttribute(out, "class", getStyleClass());
		JspUtil.writeAttribute(out, "style", getStyle());
		JspUtil.writeAttribute(out, "type", "checkbox");
		JspUtil.writeAttribute(out, "label", localizedLabel);
		JspUtil.writeAttribute(out, "value", value);
		JspUtil.writeAttribute(out, "tabindex", tabindex);

		if (viewModel.isDisabled() || viewModel.getSelectItemByValue(value).isDisabled())
			JspUtil.writeAttribute(out, "disabled", "true");

		if (viewModel.getValueSet().contains(value))
			JspUtil.writeAttribute(out, "checked", "true");

		JspUtil.writeAttributes(out, attributes);

		if (events && viewModel.isOnChangeEventRegistered())
			this.writeEventAttributeForUiEvent(out, "onclick", derivedId, "onChanged", validateOnEvent, "",
					updateRegionNames);

		JspUtil.writeCloseStartEndTag_SS(out);

		// Continue
		super.doEndTag(out);
		return EVAL_PAGE;
	}


	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "The value of this checkbox that will be submitted with form if this checkbox is selected." 
	 */
	public void setValue(String value) throws JspException  {
		this.value = (String)evaluateNotNull("value", value, String.class);
	}
}




