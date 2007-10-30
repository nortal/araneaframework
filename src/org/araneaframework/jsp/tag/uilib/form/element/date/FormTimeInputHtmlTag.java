package org.araneaframework.jsp.tag.uilib.form.element.date;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.Calendar;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.AraneaAttributes;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.control.TimeControl;

/**
 * Time input form element tag.
 * 
 * @author Marko Muts
 * @jsp.tag
 *   name = "timeInput"
 *   body-content = "JSP"
 *   description = "Form time input field (custom control), represents UiLib "TimeControl"."
 */
public class FormTimeInputHtmlTag extends BaseFormDateTimeInputHtmlTag {
  protected boolean showTimeSelect = true;
  protected boolean surroundingTable = true; 

  {
    baseStyleClass = "aranea-time";
  }
  protected int doEndTag(Writer out) throws Exception {
    assertControlType("TimeControl");

    // Prepare    
    String name = this.getFullFieldId();
    TimeControl.ViewModel viewModel = ((TimeControl.ViewModel) controlViewModel);

    Long timeInputSize = DEFAULT_TIME_INPUT_SIZE;

    if (surroundingTable)
    	out.write("<table border='0' cellpadding='0' cellspacing='0'><tr><td nowrap='true'>\n");
    this.writeTimeInput(out, name, name, viewModel.getSimpleValue(),
        localizedLabel, timeInputSize, viewModel.isDisabled(),
        accessKey);

    Integer minute = null, hour = null;
    try {
      if (viewModel.getSimpleValue() != null) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(viewModel.getCurrentSimpleDateTimeFormat().parse(viewModel.getSimpleValue()));

  	    hour = new Integer(calendar.get(Calendar.HOUR_OF_DAY));
   	    minute = new Integer(calendar.get(Calendar.MINUTE));
      }
    } catch (ParseException e) {
    	if (showTimeSelect) {
          // try to preserve the contents of selects anyway
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

    if (surroundingTable) out.write("</td></tr></table>\n");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  protected void writeMinuteSelect(Writer out, String name, boolean disabled, Integer minute) throws IOException {
    TimeControl.ViewModel viewModel = ((TimeControl.ViewModel) controlViewModel);
    out.write("<select id=\"" + name + ".select2\" name=\"" + name + ".select2\" onChange=\""
        + fillXJSCallConstructor("Aranea.UI.fillTimeText", name, name + ".select1", name + ".select2")
        + ";" + ((!disabled && events && viewModel.isOnChangeEventRegistered()) ? JspWidgetCallUtil.getSubmitScriptForEvent() : "") + "\"");

    if (disabled)
      out.write(" disabled=\"true\"");
    
    if (!disabled &&  events && viewModel.isOnChangeEventRegistered()) {
    	UiUpdateEvent event = new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT, name, null, updateRegionNames);
    	event.setEventPrecondition(getMinuteSelectOnChangePrecondition(name));
    	out.write(" ");
    	out.write(event.getEventAttributes().toString());
    }

    out.write(">\n");

    JspUtil.writeStartTag_SS(out, "script");
    out.write(getTimeSelectScript(name+".select2", minute, 60));
    
    if (!disabled && backgroundValidation) {
    	String s = name + ".select2";
    	String es = "$('" + s + "')";
    	String ns =  "$('" + name + "')";
    	String vcall = "formElementValidationActionCall(" + ns + ");";
    	out.write("Event.observe(" + es + ", 'change', function(event) {" + vcall + "});");
    }
    
    JspUtil.writeEndTag_SS(out, "script");

    JspUtil.writeEndTag_SS(out, "select");
  }

  protected void writeHourSelect(Writer out, String name, boolean disabled, Integer hour) throws IOException {
    TimeControl.ViewModel viewModel = ((TimeControl.ViewModel) controlViewModel);
    out.write("<select id=\"" + name + ".select1\" name=\"" + name + ".select1\" onChange=\""
        + fillXJSCallConstructor("Aranea.UI.fillTimeText", name, name + ".select1", name + ".select2")
        + ";" + ((!disabled && events && viewModel.isOnChangeEventRegistered()) ? JspWidgetCallUtil.getSubmitScriptForEvent() : "") + "\"");
    if (disabled)
      out.write(" disabled=\"true\"");

    if (!disabled &&  events && viewModel.isOnChangeEventRegistered()) {
    	UiUpdateEvent event = new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT, name, null, updateRegionNames);
    	event.setEventPrecondition(getHourSelectOnChangePrecondition(name));
    	out.write(" ");
    	out.write(event.getEventAttributes().toString());
    }
    out.write(">\n");

    JspUtil.writeStartTag_SS(out, "script");
    out.write(getTimeSelectScript(name+".select1", hour, 24));
    
    if (!disabled && backgroundValidation) {
    	String s = name + ".select1";
    	String es = "$('" + name + "')";
    	String ns =  "$('" + s + "')";
    	String vcall = "formElementValidationActionCall(" + es + ");";
    	out.write("Event.observe(" + ns + ", 'change', function(event) {" + vcall + "});");
    }
    
    JspUtil.writeEndTag_SS(out, "script");
    
    JspUtil.writeEndTag_SS(out, "select");
  }

  /**
   * Writes out time input
   */
  protected void writeTimeInput(Writer out, 
      String id, 
      String name,
      String value, 
      String label, 
      Long size, 
      boolean disabled,
      String accessKey) throws Exception {
    TimeControl.ViewModel viewModel = ((TimeControl.ViewModel) controlViewModel);
    // Write input tag
    JspUtil.writeOpenStartTag(out, "input");
    if (!StringUtils.isBlank(id))
      JspUtil.writeAttribute(out, "id", id);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "type", "text");
    JspUtil.writeAttribute(out, "value", value);
    JspUtil.writeAttribute(out, "size", size);
    JspUtil.writeAttribute(out, "tabindex", tabindex);
    
    writeBackgroundValidationAttribute(out);

    if (!disabled && events && viewModel.isOnChangeEventRegistered()) {
        JspUtil.writeAttribute(out, "onfocus", "Aranea.UI.saveValue(this)");
    	UiUpdateEvent event = new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT, name, null, updateRegionNames);
    	event.setEventPrecondition(getTimeInputOnChangePrecondition(name));
    	out.write(" ");
    	out.write(event.getEventAttributes().toString());
    }
    
    // validation won't occur with Event.observe registered in aranea-behaviour when date selected from calendar
    if (!viewModel.isOnChangeEventRegistered() && !disabled && backgroundValidation) {
    	JspUtil.writeAttribute(out, "onchange", "formElementValidationActionCall(this)");
    }

    StringBuffer onBlur = new StringBuffer();
    if (showTimeSelect)
    	onBlur.append(fillXJSCallConstructor("Aranea.UI.fillTimeSelect", name, name +".select1", name + ".select2") + ";");
    if (!disabled && events && viewModel.isOnChangeEventRegistered())
    	onBlur.append(JspWidgetCallUtil.getSubmitScriptForEvent());
    JspUtil.writeAttribute(out, "onblur", onBlur.toString());

    if (!StringUtils.isBlank(accessKey))
      JspUtil.writeAttribute(out, "accesskey", accessKey);
    if (disabled) 
      JspUtil.writeAttribute(out, "disabled", "true");

    JspUtil.writeAttributes(out, attributes);
    JspUtil.writeCloseStartEndTag_SS(out);
  }

  protected String fillXJSCallConstructor(String function, String timeInputEl, String hourSelectEl, String minuteSelectEl) {
    return FormTimeInputHtmlTag.staticFillXJSCall(function, timeInputEl, hourSelectEl, minuteSelectEl);
  }
  
  public static final String staticFillXJSCall(String function, String timeInputEl, String hourSelectEl, String minuteSelectEl) {
    return function + "('" + timeInputEl + "', '"  +  hourSelectEl + "', '" + minuteSelectEl + "')";
  }
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Boolean, specifying whether HTML &lt;select&;gt;'s should be shown for hour/minute selection."
   * 
   * @since 1.0.3
   */
  public void setShowTimeSelect(String showTimeSelect) throws JspException {
    this.showTimeSelect = ((Boolean) evaluate("showTimeSelect", showTimeSelect, Boolean.class)).booleanValue();
  }
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Boolean, specifying whether HTML &lt;table&;gt;'s should be rendered to around this time input. Default is true."
   * 
   * @since 1.1
   */
  public void setSurroundingTable(String surroundingTable) throws JspException {
    this.surroundingTable = ((Boolean) evaluate("surroundingTable", surroundingTable, Boolean.class)).booleanValue();
  }
}
