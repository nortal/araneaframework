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
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public abstract class UiStdFormSimpleElementDisplayBaseTag extends UiFormElementBaseDisplayTag {	
	
	protected int doEndTag(Writer out) throws Exception {				
		StringArrayRequestControl.ViewModel viewModel = ((StringArrayRequestControl.ViewModel) controlViewModel);
		
		UiUtil.writeOpenStartTag(out, "span");
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeAttribute(out, "style", getStyle());
		UiUtil.writeAttributes(out, attributes);
		UiUtil.writeCloseStartTag(out);
		
		String s = viewModel.getSimpleValue();
		if (s != null)
			UiUtil.writeEscaped(out, s);
		
		UiUtil.writeEndTag(out, "span");		
		
		return super.doEndTag(out);
	}
}
