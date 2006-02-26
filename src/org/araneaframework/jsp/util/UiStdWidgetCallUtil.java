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

package org.araneaframework.jsp.util;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.container.UiWidgetContainer;

/**
 * Standard util for producing calls to UiLib widgets in various
 * container frameworks. 
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class UiStdWidgetCallUtil {

  public static UiWidgetContainer getContainer(PageContext pageContext) throws JspException {
    return (UiWidgetContainer) UiUtil.readAttribute(pageContext, UiWidgetContainer.REQUEST_CONTEXT_KEY, PageContext.REQUEST_SCOPE);
  }

  /**
   * Write standard event handling attribute which submits widget event with given id to the system form.
   * @throws JspException 
   */
  public static void writeEventAttributeForEvent(PageContext pageContext, Writer out, String attributeName, String systemFormId, String widgetId, String eventId, String eventParam, List updateRegions) throws IOException, JspException {
    writeEventAttributeForEvent(pageContext, out, attributeName, systemFormId, widgetId, eventId, eventParam, "return true", updateRegions);
  }

  /**
   * Write standard event handling attribute which submits widget event with given id to the system form.
   * @throws JspException 
   */
  public static void writeEventAttributeForEvent(PageContext pageContext, Writer out, String attributeName, String systemFormId, String widgetId, String eventId, String eventParam, String precondition, List updateRegions) throws IOException, JspException {
    UiUtil.writeOpenAttribute(out, attributeName);
    
    out.write("javascript:");
    out.write("return uiStandardSubmitEvent("); 
    out.write("document.");
    UiUtil.writeEscapedAttribute(out, systemFormId);
    out.write(", ");    
    UiUtil.writeScriptString(out, widgetId);    
    out.write(", ");    
    UiUtil.writeScriptString(out, eventId);
    out.write(", ");
    UiUtil.writeScriptString(out, eventParam);
    out.write(", function() {");
    UiUtil.writeEscaped(out, getContainer(pageContext).buildWidgetCall(systemFormId, widgetId, eventId, eventParam, updateRegions));    
    out.write("}, function() {");
      if (StringUtils.isBlank(precondition)) out.write("return true;");
      else UiUtil.writeEscaped(out, precondition);
    out.write("});");

    UiUtil.writeCloseAttribute(out);
  }

  /**
   * Writes standard event handling attribute which validates the form, if neccessary, and submits 
   * event of form element with given id to the system form.
   * @throws JspException 
   */
  public static void writeEventAttributeForFormEvent(
		  PageContext pageContext, 
		  Writer out, 
		  String attributeName, 
		  String systemFormId, 
		  String formId, 
		  String elementId, 
		  String eventId, 
		  String eventParam, 
		  boolean validate, 
		  String precondition, 
		  List updateRegions) throws IOException, JspException {
	  
	  
    UiUtil.writeOpenAttribute(out, attributeName);
    
    out.write("javascript:");
    writeEventScriptForFormEvent(pageContext, out, systemFormId, formId, elementId, eventId, eventParam, validate, precondition, updateRegions);
    UiUtil.writeCloseAttribute(out);
  }

  /**
   * Writes standard event handling script content which validates the form, if neccessary, and submits 
   * event of form element with given id to the system form.
   * @author <a href='mailto:margus@webmedia.ee'>Margus Väli</a> 6.05.2005 extracted from {@link #writeEventAttributeForFormEvent(Writer, String, String, String, String, String, String, String, boolean, String)}
   * @throws JspException 
   */  
  public static void writeEventScriptForFormEvent(PageContext pageContext, Writer out, String systemFormId, String formId, String elementId, String eventId, String eventParam, boolean validate, String precondition, List updateRegions) throws IOException, JspException {
    out.write("return uiStandardSubmitFormEvent(");
    out.write("document.");
    UiUtil.writeEscapedAttribute(out, systemFormId);
    out.write(", ");    
    UiUtil.writeScriptString(out, formId);    
    out.write(", ");    
    UiUtil.writeScriptString(out, elementId);    
    out.write(", ");        
    UiUtil.writeScriptString(out, eventId);
    out.write(", ");
    UiUtil.writeScriptString(out, eventParam);
    out.write(", ");    
    out.write(validate ? "true" : "false");

    out.write(", function() {");
    UiUtil.writeEscaped(out, getContainer(pageContext).buildWidgetCall(systemFormId, formId + "." + elementId, eventId, eventParam, updateRegions));
    out.write("}, function() {");
    if (StringUtils.isBlank(precondition)) out.write("return true;");
    else UiUtil.writeEscaped(out, precondition);    
    out.write("});"); 
  }

}
