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

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.util.StringUtil;
import org.araneaframework.jsp.util.UiUtil;

/**
 * Standard button form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "button"
 *   body-content = "JSP"
 *   description = "Form button, represents UiLib "ButtonControl"."
 */
public class UiStdFormButtonTag extends UiStdFormButtonBaseTag {
	private static final String MOLD_BUTTON = "button";
	private static final String MOLD_INPUT = "input";
	
	public String mold;

	protected void init() {
		super.init();
		styleClass = "aranea-button";
		mold = UiStdFormButtonTag.MOLD_BUTTON;
	}

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = 
	 *   	"Allowed values are (button | input) - the corresponding HTML tag will be used for rendering. Default is button." 
	 */
	public void setMold(String mold) throws JspException {
		this.mold = (String) evaluate("mold", mold, String.class);
	}

	//
	// Implementation
	//  

	protected int before(Writer out) throws Exception {
		super.before(out);

		// Prepare
		String name = this.getScopedFullFieldId();
		
		UiUtil.writeOpenStartTag(out, mold.equals(UiStdFormButtonTag.MOLD_BUTTON) ? UiStdFormButtonTag.MOLD_BUTTON : UiStdFormButtonTag.MOLD_INPUT);

		if (mold.equals(UiStdFormButtonTag.MOLD_INPUT))
			UiUtil.writeAttribute(out, "type", "button");
		UiUtil.writeAttribute(out, "id", name);
		UiUtil.writeAttribute(out, "name", name);
		UiUtil.writeAttribute(out, "class", getStyleClass());
		if (showLabel && mold.equals(UiStdFormButtonTag.MOLD_INPUT)) {
			if (accessKey != null) {
				String escapedLabel = StringUtil
						.escapeHtmlEntities(localizedLabel);
				UiUtil.writeAttribute(out, "value", StringUtil
						.underlineAccessKey(escapedLabel, accessKey));
			} else {
				UiUtil.writeAttribute(out, "value", localizedLabel);			
			}
		}
		if (mold.equals(UiStdFormButtonTag.MOLD_BUTTON))
			UiUtil.writeAttribute(out, "label", localizedLabel);
		UiUtil.writeAttribute(out, "tabindex", tabindex);
		if (events) {
			writeEventAttribute(out);
		}
		UiUtil.writeAttributes(out, attributes);
		if (accessKey != null)
			UiUtil.writeAttribute(out, "accesskey", accessKey);
		if (mold.equals(UiStdFormButtonTag.MOLD_BUTTON))
			UiUtil.writeCloseStartTag_SS(out);			
		if (mold.equals(UiStdFormButtonTag.MOLD_INPUT))
			UiUtil.writeCloseStartEndTag(out);			

		// Continue
		return EVAL_BODY_INCLUDE;
	}

	protected int after(Writer out) throws Exception {

		if (mold.equals(UiStdFormButtonTag.MOLD_BUTTON)) {
			if (showLabel) {
				if (accessKey != null) {
					String escapedLabel = StringUtil
							.escapeHtmlEntities(localizedLabel);
					out.write(StringUtil
							.underlineAccessKey(escapedLabel, accessKey));
				} else {
					UiUtil.writeEscaped(out, localizedLabel);
				}
			}
			UiUtil.writeEndTag(out, UiStdFormButtonTag.MOLD_BUTTON);
		}

		// Continue
		super.after(out);
		return EVAL_PAGE;
	}

	protected boolean writeEventAttribute(Writer out) throws IOException,
			JspException {
		if (viewModel.isOnClickEventRegistered())
			this.writeEventAttributeForUiEvent(out, "onclick", id, "onClicked",
					validateOnEvent, onClickPrecondition, updateRegionNames);

		return viewModel.isOnClickEventRegistered();
	}
}
