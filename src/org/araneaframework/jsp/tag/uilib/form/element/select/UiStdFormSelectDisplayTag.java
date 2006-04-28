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
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseDisplayTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.SelectControl;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "selectDisplay"
 *   body-content = "JSP"
 *   description = "Form select display field, represents UiLib "SelectControl"."
 */
public class UiStdFormSelectDisplayTag extends UiFormElementBaseDisplayTag {
	public UiStdFormSelectDisplayTag() {
		baseStyleClass = "aranea-select-display";
	}

	protected int doEndTag(Writer out) throws Exception {				
		SelectControl.ViewModel viewModel = ((SelectControl.ViewModel)controlViewModel);
		
		UiUtil.writeOpenStartTag(out, "span");
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeCloseStartTag(out);

		UiUtil.writeEscaped(out, viewModel.getLabelForValue(viewModel.getSimpleValue()));

		return super.doEndTag(out);  
	}
}
