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
import org.araneaframework.http.util.FileImportUtil;
import org.araneaframework.jsp.AraneaAttributes;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
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
	protected String disabledRenderMode = RENDER_DISABLED_DISABLED;

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
     *   rtexprvalue = "true"
	 *   required = "false"
	 *   description = "Precondition for deciding whether go to server side or not." 
	 */
	public void setOnChangePrecondition(String onChangePrecondition)throws JspException {
		this.onChangePrecondition = (String) evaluate("onChangePrecondition", onChangePrecondition, String.class);
	}

  /**
   * @jsp.attribute type = "java.lang.String"
   *                rtexprvalue = "true"
   *                required = "false"
   *                description = "Specifies how to render a disabled input. Valid options are <code>'disabled'</code> and <code>'read-only'</code>. Default is <code>'disabled'</code>."
   * @since 1.1.3
   */
  public void setDisabledRenderMode(String disabledRenderMode) throws JspException {
    this.disabledRenderMode = evaluateDisabledRenderMode(disabledRenderMode);
  }

	/**
     * @jsp.attribute type = "java.lang.String"
     *                rtexprvalue = "true"
     *                required = "false"
     *                description = "Alignment for popup calendar. In form 'zx' where z is in {TBCtb} and x in {LRClr}. Default is 'Br' (Bottom, right)."
     */
	public void setCalendarAlignment(String calendarAlignment)throws JspException {
		this.calendarAlignment = (String) evaluate("calendarAlignment", calendarAlignment, String.class);
	}

	/**
	 * Writes out date input
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
			boolean disabled,
			String styleClass,
			String accessKey,
			DateControl.ViewModel viewModel) throws Exception {

		if (viewModel.getInputFilter() != null) {
			attributes.put(AraneaAttributes.FilteredInputControl.CHARACTER_FILTER,
					viewModel.getInputFilter().getCharacterFilter());
		}

		// Write input tag
		JspUtil.writeOpenStartTag(out, "input");
		if (StringUtils.isNotBlank(id)) JspUtil.writeAttribute(out, "id", id);
		JspUtil.writeAttribute(out, "name", name);
		JspUtil.writeAttribute(out, "class", getStyleClass());
		JspUtil.writeAttribute(out, "style", getStyle());
		JspUtil.writeAttribute(out, "type", "text");
		JspUtil.writeAttribute(out, "value", value);	
		JspUtil.writeAttribute(out, "size", size);
		JspUtil.writeAttribute(out, "tabindex", tabindex);
		if (StringUtils.isNotBlank(accessKey)) JspUtil.writeAttribute(out, "accesskey", accessKey);
		
		if (disabled) {
		  if (viewModel.isDisabled()) {
		    JspUtil.writeAttribute(out, this.disabledRenderMode,
		        this.disabledRenderMode);
		  }
		} else if (events && viewModel.isOnChangeEventRegistered()) {
			writeSubmitScriptForUiEvent(out, "onchange", this.derivedId, "onChanged",
					onChangePrecondition, updateRegionNames);
		}

		// validation won't occur with Event.observe registered in aranea-behaviour when date selected from calendar
		if (!viewModel.isOnChangeEventRegistered() && backgroundValidation) {
			JspUtil.writeAttribute(out, "onchange", "formElementValidationActionCall(this)");
		}

		writeBackgroundValidationAttribute(out);

		JspUtil.writeAttributes(out, attributes);
		JspUtil.writeCloseStartEndTag_SS(out);

		if (!disabled) {

			JspUtil.writeOpenStartTag(out, "a");
			JspUtil.writeAttribute(out, "href", "javascript:;");
			JspUtil.writeCloseStartTag_SS(out);

			String calendarImgId = id + CALENDAR_BUTTON_ID_SUFFIX;
			JspUtil.writeOpenStartTag(out, "img");
			out.write(" src=\"");
			out.write(FileImportUtil.getImportString("gfx/ico_calendar.gif", pageContext.getRequest()));
			out.write("\" ");
			JspUtil.writeAttribute(out, "id", calendarImgId);
			JspUtil.writeAttribute(out, "class", calendarIconClass);
			JspUtil.writeCloseStartEndTag_SS(out);
	
			JspUtil.writeEndTag_SS(out, "a");
		
			writeCalendarScript(out, id, "%d.%m.%Y");
		}
	}

	/**
	 * @since 1.0.3
	 */
	protected String getTimeSelectScript(String selectId, Integer value, int valueCount) {
	    StringBuffer sb = new StringBuffer();
	    sb.append("Aranea.UI.addOptions('"+selectId+"'," + String.valueOf(valueCount)+ ",");
	    sb.append(value != null ? value.toString():"null").append(");");

	    return sb.toString();
	}

	/**
	 * @since 1.0.3
	 */
	protected String getTimeInputOnChangePrecondition(String timeInputId) {
		if (onChangePrecondition != null) {
			return onChangePrecondition;
		}

		String timeInputRef = new StringBuffer("document.getElementById('")
				.append(timeInputId)
				.append("')")
				.toString();

		StringBuffer precondition = new StringBuffer();
		precondition.append("return Aranea.UI.isChanged('");
		precondition.append(timeInputId);
		precondition.append("') && ((");
		precondition.append(timeInputRef);
		precondition.append(".value.length==5) || (");
		precondition.append(timeInputRef);
		precondition.append(".value.length==0))");
		return precondition.toString();
	}

	/**
	 * @since 1.0.3
	 */
	protected String getHourSelectOnChangePrecondition(String timeInputId) {
		return getSelectOnChangePrecondition(timeInputId);
	}

	/**
	 * @since 1.0.3
	 */
	protected String getMinuteSelectOnChangePrecondition(String timeInputId) {
		return getSelectOnChangePrecondition(timeInputId);
	}

	/**
	 * @since 1.0.3
	 */
	protected String getSelectOnChangePrecondition(String timeInputId) {
		String precondition = onChangePrecondition;
		if (precondition == null) {
			precondition = "return (document.getElementById('" + timeInputId
					+ "').value.length==5)";
		}
		return precondition;
	}

	/**
	 * Writes out time input The id and accessKey parameters may be null. Note
	 * that the ID attribute is [for the moment, at least: 28.12.2004] only needed
	 * for the access keys to function. That is, the typical pattern as seen in
	 * HTML is:
	 * 
	 * <pre>
	 * &lt;label for=&quot;some.control.id&quot; accesskey=&quot;some.key&quot;&gt; ...
	 * &lt;input id=&quot;some.control.id&quot; ... &gt;
	 * </pre>
	 * 
	 * As you see, the <code>input</code> tag outputs its ID so that the
	 * <code>label</code> tag could reference it.
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

		if (viewModel.getInputFilter() != null) {
	    	attributes.put(AraneaAttributes.FilteredInputControl.CHARACTER_FILTER,
					viewModel.getInputFilter().getCharacterFilter());
		}

		// Write input tag
		JspUtil.writeOpenStartTag(out, "input");
		if (!StringUtils.isBlank(id)) JspUtil.writeAttribute(out, "id", id);
		JspUtil.writeAttribute(out, "name", name);
		JspUtil.writeAttribute(out, "class", styleClass);
		JspUtil.writeAttribute(out, "type", "text");
		JspUtil.writeAttribute(out, "value", value);	
		JspUtil.writeAttribute(out, "size", size);
		JspUtil.writeAttribute(out, "tabindex", tabindex);

		if (!StringUtils.isBlank(accessKey)) {
			JspUtil.writeAttribute(out, "accesskey", accessKey);
		}

		if (disabled) {
		  if (viewModel.isDisabled()) {
		    JspUtil.writeAttribute(out, this.disabledRenderMode,
		        this.disabledRenderMode);
		  }
		} else if (events && viewModel.isOnChangeEventRegistered()) {
			writeSubmitScriptForUiEvent(out, "onchange", this.derivedId, "onChanged",
					onChangePrecondition, updateRegionNames);
		}

		writeBackgroundValidationAttribute(out);

		JspUtil.writeAttributes(out, attributes);
		JspUtil.writeCloseStartEndTag_SS(out);
	}

	protected void writeCalendarScript (Writer out, String id, String format) throws Exception {
		JspUtil.writeOpenStartTag(out, "script");
		JspUtil.writeAttribute(out, "type", "text/javascript");
		JspUtil.writeCloseStartTag(out);

		StringBuffer script = new StringBuffer();
		script.append("Aranea.UI.calendarSetup('");
		script.append(id);
		script.append("', '");
		script.append(format);
		script.append("', ");

		if (calendarAlignment == null) {
			script.append("null");
		} else {
			script.append("'");
			script.append(calendarAlignment);
			script.append("'");
		}

		script.append(");");

		out.write(script.toString());
		JspUtil.writeEndTag_SS(out, "script");
	}

}
