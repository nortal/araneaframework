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

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.servlet.util.FileImportUtil;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.TimeControl;


/**
 * Standard date time input form element base tag.
 * 
 * @author Oleg Mürk
 * @author <a href='mailto:margus@webmedia.ee'>Margus Väli</a> 2.05.2005 - precondition and onblur event notifying listeners
 */
public class BaseFormDateTimeInputHtmlTag extends BaseFormElementHtmlTag {
	public final static String CALENDAR_BUTTON_ID_SUFFIX = "_cbutton";
	
	public final static Long DEFAULT_DATE_INPUT_SIZE = new Long(11);
	public final static Long DEFAULT_TIME_INPUT_SIZE = new Long(5);
	
	protected String onChangePrecondition;
	protected String calendarAlignment;
	protected String calendarIconClass = "middle";
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Precondition for deciding whether go to server side or not." 
	 */
	public void setOnChangePrecondition(String onChangePrecondition)throws JspException {
		this.onChangePrecondition = (String) evaluate("onChangePrecondition", onChangePrecondition, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Alignment for popup calendar. In form 'zx' where z is in {TBCtb} and x in {LRClr}. Default is 'Br' (Bottom, right)." 
	 */
	public void setCalendarAlignment(String calendarAlignment)throws JspException {
		this.calendarAlignment = (String) evaluate("calendarAlignment", calendarAlignment, String.class);
	}

	/**
	 * Writes out date input
	 * The id and accessKey parameters may be null.
	 * @see #writeTimeInput
	 * @author <a href='mailto:margus@webmedia.ee'>Margus Väli</a> 6.05.2005 -- added callback function argument to popup-calendar
	 */
	protected void writeDateInput(
			Writer out, 
			String id,
			String name, 
			String value,
			String label,
			boolean isMandatory,
			boolean isValid,
			Long size,
			boolean validate,
			boolean disabled,
      String styleClass,
			String accessKey,      
			DateControl.ViewModel viewModel) throws Exception {
		// Write input tag
		JspUtil.writeOpenStartTag(out, "input");
		if (!StringUtils.isBlank(id)) JspUtil.writeAttribute(out, "id", id);
		JspUtil.writeAttribute(out, "name", name);
		JspUtil.writeAttribute(out, "class", getStyleClass());
		JspUtil.writeAttribute(out, "style", getStyle());
		JspUtil.writeAttribute(out, "type", "text");
		JspUtil.writeAttribute(out, "value", value);	
		JspUtil.writeAttribute(out, "size", size);
		JspUtil.writeAttribute(out, "label", label);
		JspUtil.writeAttribute(out, "tabindex", tabindex);
		if (!StringUtils.isBlank(accessKey)) JspUtil.writeAttribute(out, "accesskey", accessKey);
		
		if (disabled) {
			JspUtil.writeAttribute(out, "disabled", "true");
		}
		else if (events && viewModel.isOnChangeEventRegistered()) {
			writeEventAttributeForUiEvent(out, "onchange", this.derivedId, "onChanged", validateOnEvent, onChangePrecondition, 
					updateRegionNames);
		}
		
		JspUtil.writeAttributes(out, attributes);    
		JspUtil.writeCloseStartEndTag_SS(out);
		
		if (!disabled) {
			JspUtil.writeOpenStartTag(out, "a");
			JspUtil.writeAttribute(out, "href", "javascript:;");
			JspUtil.writeCloseStartTag_SS(out);

			String calendarImgId = id + CALENDAR_BUTTON_ID_SUFFIX;
			JspUtil.writeOpenStartTag(out, "img");
			out.write(" src=\"");
			out.write(FileImportUtil.getImportString("gfx/ico_calendar.gif", pageContext.getRequest(), pageContext.getResponse()));
			out.write("\" ");
			JspUtil.writeAttribute(out, "id", calendarImgId);
			JspUtil.writeAttribute(out, "class", calendarIconClass);
			JspUtil.writeCloseStartTag_SS(out);
	
			JspUtil.writeEndTag_SS(out, "a");
		
			writeCalendarScript(out, id, "%d.%m.%Y");
		}
	}
	
	
	/**
	 * Writes out time input
	 * The id and accessKey parameters may be null.
	 * Note that the ID attribute is [for the moment, at least: 28.12.2004] only needed
	 * for the access keys to function. That is, the typical pattern as seen in HTML is:
	 * <pre>
	 * &lt;label for="some.control.id" accesskey="some.key"&gt; ...
	 * &lt;input id="some.control.id" ... &gt;
	 * </pre>
	 * As you see, the <code>input</code> tag outputs its ID so that the <code>label</code> tag
	 * could reference it. 
	 */
	// XXX: not used ANYWHERE in Aranea classes.
	protected void writeTimeInput(
			Writer out, 
			String id,
			String name, 
			String value,
			String label,
			Long size,
			boolean disabled,
      String styleClass,
			String accessKey,
			TimeControl.ViewModel viewModel) throws Exception {
		// Write input tag
		JspUtil.writeOpenStartTag(out, "input");
		if (!StringUtils.isBlank(id)) JspUtil.writeAttribute(out, "id", id);
		JspUtil.writeAttribute(out, "name", name);
		JspUtil.writeAttribute(out, "class", styleClass);
		JspUtil.writeAttribute(out, "type", "text");
		JspUtil.writeAttribute(out, "value", value);	
		JspUtil.writeAttribute(out, "size", size);
		JspUtil.writeAttribute(out, "label", label);
		JspUtil.writeAttribute(out, "tabindex", tabindex);
		if (!StringUtils.isBlank(accessKey)) JspUtil.writeAttribute(out, "accesskey", accessKey);
		if (disabled) {
			JspUtil.writeAttribute(out, "disabled", "true");
		}
		else if (events && viewModel.isOnChangeEventRegistered()) {
			writeEventAttributeForUiEvent(out, "onchange", this.derivedId, "onChanged", validateOnEvent, onChangePrecondition,
					updateRegionNames);
		}
		
		JspUtil.writeAttributes(out, attributes);
		JspUtil.writeCloseStartEndTag_SS(out);
	}
	
	protected void writeCalendarScript (Writer out, String id, String format) throws Exception {
		JspUtil.writeOpenStartTag(out, "script");
		JspUtil.writeAttribute(out, "type", "text/javascript");
		JspUtil.writeCloseStartTag(out);
		
		StringBuffer script = new StringBuffer();
		script.append("Calendar.setup({\n");
		script.append("inputField : \"");
		script.append(id);
		script.append("\",\nifFormat : \"");
		script.append(format);
		script.append("\",\nshowsTime : false, ");
		script.append("\nbutton : \"");
		script.append(id);
		script.append(CALENDAR_BUTTON_ID_SUFFIX);
		script.append("\",\nsingleClick : true, ");
		script.append("\nstep: 1, ");
		script.append("\nfirstDay: 1");
		if (calendarAlignment != null)
			script.append(",\nalign:\"").append(calendarAlignment).append("\"");
		script.append("\n});");

		out.write(script.toString());
		JspUtil.writeEndTag_SS(out, "script");
	}
}
