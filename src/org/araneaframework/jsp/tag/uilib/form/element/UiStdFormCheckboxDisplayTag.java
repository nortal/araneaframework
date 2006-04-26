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
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseDisplayTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "checkboxDisplay"
 *   body-content = "JSP" 
 *   description = "Form checkbox display field, represents UiLib "CheckboxControl"."
 */
public class UiStdFormCheckboxDisplayTag extends UiFormElementBaseDisplayTag {
	protected String imageCode;
	
	protected void init() {
		super.init();
		baseStyleClass = "aranea-checkbox-display";
	}
	
	//
	// Attributes
	//
	//XXX: why is it here, without attributes
	protected void setImageCode(String imageCode) throws JspException {
		this.imageCode = (String) evaluateNotNull("imageCode", imageCode, String.class);
	}
	
	//
	// Implementation
	//
	
	protected int after(Writer out) throws Exception {				
		StringArrayRequestControl.ViewModel viewModel = ((StringArrayRequestControl.ViewModel) controlViewModel);
		
		UiUtil.writeOpenStartTag(out, "span");
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeCloseStartTag(out);
		
		if ("true".equals(viewModel.getSimpleValue())) //TODO: image?
			UiUtil.writeEscaped(out, "x");
		
		return super.after(out);  
	}
	
}
