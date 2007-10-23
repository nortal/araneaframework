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

package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.ComboTextControl;
import org.araneaframework.uilib.form.control.TextControl.ViewModel;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 * 
 * @jsp.tag
 *   name = "comboTextInput"
 *   body-content = "JSP"
 *   description = "Form text input field, represents UiLib &quot;ComboTextControl&quot;."
 */
public class FormComboTextInputHtmlTag extends FormTextInputHtmlTag {
	{
		baseStyleClass = "aranea-comboselect";
	}
	
	protected int doEndTag(Writer out) throws Exception {
		super.doEndTag(out);

		JspUtil.writeOpenStartTag(out, "script");
		JspUtil.writeAttribute(out, "type", "text/javascript");
		JspUtil.writeCloseStartTag(out);

		writeSelectInitializationScript(out);

		JspUtil.writeEndTag(out, "script");

        return EVAL_PAGE;
	}
	
	protected void writeSelectInitializationScript(Writer out) throws Exception {
		out.write("ieCompatCreateEditableSelect('" + this.getFullFieldId() + "');");
	}

	protected Map getCustomAttributes(ViewModel viewModel) {
		ComboTextControl.ViewModel model = (org.araneaframework.uilib.form.control.ComboTextControl.ViewModel) viewModel;
		Map r = super.getCustomAttributes(model);

		Collection l = model.getPredefinedInputs();
		StringBuffer sb = new StringBuffer();
		for (Iterator i=l.iterator(); i.hasNext(); ) {
			sb.append(i.next());
			if (i.hasNext())
				sb.append(';');
		}

		if (StringUtils.isNotBlank(sb.toString()))
			r.put("selectBoxOptions", sb.toString());
		return r;
	}

	protected void assertType() throws JspException {
		assertControlType("ComboTextControl");
	}
}
