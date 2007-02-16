package org.araneaframework.jsp.tag.uilib.form.element.date;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.Calendar;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.http.util.FileImportUtil;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.TimestampControl.ViewModel;

/**
 * Date/time input form element tag.
 * 
 * @author Marko Muts
 * @jsp.tag
 *   name = "dateTimeInput"
 *   body-content = "JSP"
 *   description = "Form date and time input field (custom control), represents UiLib "DateTimeControl"."
 */
public class FormDateTimeInputHtmlTag extends BaseFormDateTimeInputHtmlTag {
  protected String timeStyleClass;
  protected String dateStyleClass;
  
  protected boolean showTimeSelect = true;

  public FormDateTimeInputHtmlTag() {
    timeStyleClass = "aranea-time";
    dateStyleClass = "aranea-date";
  }

  public String getDateStyleClass() {
    return dateStyleClass;
  }

  protected int doEndTag(Writer out) throws Exception {
    assertControlType("DateTimeControl");

    // Prepare
    String name = this.getScopedFullFieldId();
    DateTimeControl.ViewModel viewModel = ((DateTimeControl.ViewModel) controlViewModel);

    Long timeInputSize = DEFAULT_TIME_INPUT_SIZE;
    Long dateInputSize = DEFAULT_DATE_INPUT_SIZE;

    // Write
    out.write("<table border='0' cellpadding='0' cellspacing='0'><tr><td nowrap='true'>\n");

    this.writeDateInput(
        out,
        name,
        name + ".date", 
        viewModel.getDate(), 
        localizedLabel,
        viewModel.isMandatory(), 
        formElementViewModel.isValid(),
        dateInputSize,
        viewModel.isDisabled(),
        getDateStyleClass(),
        accessKey,
        viewModel.getDateViewModel());

    out.write("&nbsp;");
    writeTimeInput(out, name, viewModel.getTime(), localizedLabel,
        timeInputSize, viewModel.isDisabled());

    Integer minute = null, hour = null;
    try {
      ViewModel timeViewModel = viewModel.getTimeViewModel();
      if (timeViewModel.getSimpleValue() != null) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timeViewModel.getCurrentSimpleDateTimeFormat().parse(timeViewModel.getSimpleValue()));

  	    hour = new Integer(calendar.get(Calendar.HOUR_OF_DAY));
   	    minute = new Integer(calendar.get(Calendar.MINUTE));
      }
    } catch (ParseException e) {
        // try to preserve the contents of selects anyway
    	if (showTimeSelect) {
    	  String strHour =  ServletUtil.getRequest(getOutputData().getInputData()).getParameter(name+".select1");
    	  if (strHour != null && !(strHour.trim().length() == 0))
     	    hour = Integer.valueOf(strHour.trim());
    	  String strMinute = ServletUtil.getRequest(getOutputData().getInputData()).getParameter(name+".select2");
    	  if (strMinute != null && !(strMinute.trim().length() == 0))
    	    minute = Integer.valueOf(strMinute);
    	}
    }

    if (showTimeSelect) {
      writeHourSelect(out, name, viewModel.isDisabled(), hour);
      writeMinuteSelect(out, name, viewModel.isDisabled(), minute);
    }

    out.write("</td></tr></table>\n");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  public String getTimeStyleClass() {
    return timeStyleClass;
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Css class for date." 
   */
  public void setDateStyleClass(String dateCssClass) throws JspException {
    this.dateStyleClass = (String) evaluate("dateStyleClass", dateCssClass, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Css class for time." 
   */
  public void setTimeStyleClass(String timeCssClass) throws JspException {
    this.timeStyleClass = (String) evaluate("timeStyleClass", timeCssClass, String.class);
  }

  /* ***********************************************************************************
   * Helper functions, mostly javascript.
   * ***********************************************************************************/

  protected void writeMinuteSelect(Writer out, String name, boolean disabled, Integer minute) throws IOException {
	DateTimeControl.ViewModel viewModel = ((DateTimeControl.ViewModel) controlViewModel);
    out.write("<select name='"
        + name
        + ".select2' onChange=\"" +
        fillXJSCallConstructor("fillTimeText", systemFormId, name)
        + ";" + ((!disabled && events && viewModel.isOnChangeEventRegistered()) ? JspWidgetCallUtil.getSubmitScriptForEvent() : "") + "\"");

    if (disabled)
      out.write(" disabled=\"true\"");
    
    if (!disabled &&  events && viewModel.isOnChangeEventRegistered()) {
    	UiUpdateEvent event = new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT, name, null, updateRegionNames);
    	event.setEventPrecondition(getMinuteSelectOnChangePrecondition(name + ".time", systemFormId));
    	out.write(" ");
    	out.write(event.getEventAttributes().toString());
    }
    out.write(">\n");
    
    JspUtil.writeStartTag_SS(out, "script");
    out.write(getTimeSelectScript(name+".select2", minute, 60));
    JspUtil.writeEndTag_SS(out, "script");

    JspUtil.writeEndTag_SS(out, "select");
  }

  protected void writeHourSelect(Writer out, String name, boolean disabled, Integer hour) throws IOException {
    DateTimeControl.ViewModel viewModel = ((DateTimeControl.ViewModel) controlViewModel);
    out.write("<select name='"
        + name
        + ".select1' onChange=\"" + 
        fillXJSCallConstructor("fillTimeText", systemFormId, name)
        + ";" + ((!disabled && events && viewModel.isOnChangeEventRegistered()) ? JspWidgetCallUtil.getSubmitScriptForEvent() : "") + "\"");
    if (disabled)
      out.write(" disabled=\"true\"");

    if (!disabled &&  events && viewModel.isOnChangeEventRegistered()) {
    	UiUpdateEvent event = new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT, name, null, updateRegionNames);
    	event.setEventPrecondition(getHourSelectOnChangePrecondition(name + ".time", systemFormId));
    	out.write(" ");
    	out.write(event.getEventAttributes().toString());
    }
    out.write(">\n");
    
    JspUtil.writeStartTag_SS(out, "script");
    out.write(getTimeSelectScript(name+".select1", hour, 24));
    JspUtil.writeEndTag_SS(out, "script");

    JspUtil.writeEndTag_SS(out, "select");
  }

  /**
   * Writes out time input
   */
  protected void writeTimeInput(Writer out, String name, String value,
      String label, Long size, boolean disabled) throws Exception {
    DateTimeControl.ViewModel viewModel = ((DateTimeControl.ViewModel) controlViewModel);
	  
    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "id", name + ".time");
    JspUtil.writeAttribute(out, "name", name + ".time");
    JspUtil.writeAttribute(out, "class", getTimeStyleClass());
    JspUtil.writeAttribute(out, "type", "text");
    JspUtil.writeAttribute(out, "value", value);
    JspUtil.writeAttribute(out, "size", size);
    JspUtil.writeAttribute(out, "tabindex", tabindex);
    
    if (!disabled && events && viewModel.isOnChangeEventRegistered()) {
        JspUtil.writeAttribute(out, "onfocus", "saveValue(this)");
    	UiUpdateEvent event = new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT, name, null, updateRegionNames);
    	event.setEventPrecondition(getTimeInputOnChangePrecondition(name+".time", systemFormId));
    	out.write(" ");
    	out.write(event.getEventAttributes().toString());
    }

    StringBuffer onBlur = new StringBuffer();
    if (showTimeSelect) 
      onBlur.append(fillXJSCallConstructor("fillTimeSelect", systemFormId, name) + ";");
    if (!disabled && events && viewModel.isOnChangeEventRegistered())
      onBlur.append(JspWidgetCallUtil.getSubmitScriptForEvent());
    JspUtil.writeAttribute(out, "onBlur", onBlur.toString());

    if (disabled)
      JspUtil.writeAttribute(out, "disabled", "true");
    JspUtil.writeAttributes(out, attributes);
    JspUtil.writeCloseStartEndTag_SS(out);
  }
  
	/**
	 * Writes out date input.
	 * 
	 * If just super.writeDateInput is called then date's viewmodel does not have
     * correct information about whether onchange listeners are registered and who has registered them.
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
		DateTimeControl.ViewModel dateTimeViewModel = ((DateTimeControl.ViewModel) controlViewModel);
		// Write input tag
		JspUtil.writeOpenStartTag(out, "input");
		if (!StringUtils.isBlank(id)) JspUtil.writeAttribute(out, "id", id);
		JspUtil.writeAttribute(out, "name", name);
		JspUtil.writeAttribute(out, "class", getStyleClass());
		JspUtil.writeAttribute(out, "style", getStyle());
		JspUtil.writeAttribute(out, "type", "text");
		JspUtil.writeAttribute(out, "value", value);	
		JspUtil.writeAttribute(out, "size", size);
		JspUtil.writeAttribute(out, "tabindex", tabindex);
		if (!StringUtils.isBlank(accessKey)) JspUtil.writeAttribute(out, "accesskey", accessKey);
		
		if (disabled) {
			JspUtil.writeAttribute(out, "disabled", "true");
		}
		// dateTimeViewModel's event listeners are these to look out for
		else if (events && dateTimeViewModel.isOnChangeEventRegistered()) {
			writeSubmitScriptForUiEvent(out, "onchange", this.derivedId, "onChanged", onChangePrecondition, updateRegionNames);
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
			out.write(FileImportUtil.getImportString("gfx/ico_calendar.gif", pageContext.getRequest()));
			out.write("\" ");
			JspUtil.writeAttribute(out, "id", calendarImgId);
			JspUtil.writeAttribute(out, "class", calendarIconClass);
			JspUtil.writeCloseStartEndTag_SS(out);
	
			JspUtil.writeEndTag_SS(out, "a");
		
			writeCalendarScript(out, id, "%d.%m.%Y");
		}
	}

  protected String fillXJSCallConstructor(String function, String formId, String element) {
    return FormTimeInputHtmlTag.staticFillXJSCall(function, formId, element +".time", element + ".select1", element + ".select2");
  }
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Boolean, specifying whether HTML &lt;select&;gt;'s should be shown for foolproof hour/minute selection."
   * 
   * @since 1.0.3
   */
  public void setShowTimeSelect(String showTimeSelect) throws JspException {
    this.showTimeSelect = ((Boolean) evaluate("showTimeSelect", showTimeSelect, Boolean.class)).booleanValue();
  }
}
