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

package org.araneaframework.jsp.tag.uilib.form.element.date;

import java.io.Writer;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseDisplayTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.DateTimeControl;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "dateTimeInputDisplay"
 *   body-content = "JSP"
 *   description = "Form date-time display field, represents UiLib "DateTimeControl"."
 */
public class UiStdFormDateTimeInputDisplayTag extends UiFormElementBaseDisplayTag {
	{
		baseStyleClass = "aranea-datetime-display";
	}
	protected int doEndTag(Writer out) throws Exception {
		assertControlType("DateTimeControl");	
		
		DateTimeControl.ViewModel viewModel = ((DateTimeControl.ViewModel)controlViewModel);
		
		UiUtil.writeOpenStartTag(out, "span");
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeCloseStartTag(out);
		
		UiUtil.writeEscaped(out, viewModel.getDate());
		out.write("&nbsp;");
		UiUtil.writeEscaped(out, viewModel.getTime());
		
		UiUtil.writeEndTag(out, "span");		
		
		return super.doEndTag(out);
	}
}
