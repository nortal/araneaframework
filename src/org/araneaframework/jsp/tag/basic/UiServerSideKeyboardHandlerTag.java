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

package org.araneaframework.jsp.tag.basic;

import java.io.Writer;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.form.UiSystemFormTag;
import org.araneaframework.jsp.util.UiStdWidgetCallUtil;
import org.araneaframework.jsp.util.UiUpdateRegionUtil;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.jsp.util.UiWidgetUtil;
/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "eventKeyboardHandler"
 *   body-content = "empty"
 *   description = "Registers a 'server-side' keyboard handler that sends an event to the specified widget."
 */
public class UiServerSideKeyboardHandlerTag extends UiKeyboardHandlerBaseTag{
  
  
  //
  // Attributes
  //
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "See keyboardHandler tag." 
	 */
	public void setScope(String scope) throws JspException{
		this.scope = (String) evaluate("scope", scope, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "" 
	 */
  public void setWidgetId(String widgetId) throws JspException {
      this.widgetId = (String) evaluate("widgetId", widgetId, String.class);
  }
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "" 
	 */
	public void setEventId(String eventId) throws JspException {
		this.eventId = (String) evaluate("eventId", eventId, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "" 
	 */
	public void setEventParam(String eventParam) throws JspException {
		this.eventParam = (String) evaluate("eventParam", eventParam, String.class);
	}
  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Enumerates the regions of markup to be updated in this widget scope. Please see <code><ui:updateRegion></code> for details." 
	 */
  public void setUpdateRegions(String updateRegions) throws JspException {
    this.updateRegions = (String) evaluate("updateRegions", updateRegions, String.class);
  }
  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Enumerates the regions of markup to be updated globally. Please see <code><ui:updateRegion></code> for details." 
	 */
  public void setGlobalUpdateRegions(String globalUpdateRegions) throws JspException {
    this.globalUpdateRegions = (String) evaluate("globalUpdateRegions", globalUpdateRegions, String.class);
  }  
  

	
	/**
	 * The default value is <code>"return true;"</code>
	 * @see org.araneaframework.jsp.tag.uilib.form.element.UiStdFormButtonBaseTag#setOnClickPrecondition
	 * @see #setElementId
	 */
	public void setPrecondition(String precondition) throws JspException {
		this.precondition = (String) evaluate("precondition", precondition, String.class);
	}
	
	
	//
	// Implementation
	//
		
	protected int before(Writer out) throws Exception {
		super.before(out);
		
		String handler = null;
    
    updateRegionNames = UiUpdateRegionUtil.getUpdateRegionNames(pageContext, updateRegions, globalUpdateRegions);
		
		// This is a call to a form event
		handler = createHandlerToCallEvent(pageContext, widgetId, eventId, eventParam, precondition, updateRegionNames);				

		// Write out.
		UiKeyboardHandlerTag.writeRegisterKeypressHandlerScript(out, scope, keyCode, handler);				
		return SKIP_BODY;
	}
  
	

	public static final String createHandlerToCallEvent(PageContext pageContext, String widgetId,
			String eventId, String eventParam, String precondition, List updateRegionNames) throws JspException{
		
		if (widgetId == null) {
          widgetId = UiWidgetUtil.getContextWidgetFullId(pageContext);		
		}
		if (eventId == null) eventId = "";
		if (eventParam == null) eventParam = "";
		if (StringUtils.isBlank(precondition)) precondition = "return true;";
		
		String systemFormId = (String)UiUtil.readAttribute(pageContext, UiSystemFormTag.SYSTEM_FORM_ID_KEY, PageContext.REQUEST_SCOPE);	
		
		return "function(event, elementId) { " +
		"uiStandardSubmitEvent(" + "document.forms['" + systemFormId + "'], '" + 
        widgetId + "', '" + eventId + "', '" + eventParam +  
			 "', function() { " + UiStdWidgetCallUtil.getContainer(pageContext).buildWidgetCall(systemFormId, widgetId, eventId, eventParam,
           updateRegionNames)
            + "}, function() { " + precondition + "}); }";
	}
	
	
 
  protected void init() {
  	super.init();
    scope = eventId = eventParam = precondition = keyCode = updateRegions = globalUpdateRegions = null;
  }
		
  protected String scope;
  protected String widgetId;
  protected String eventId;
  protected String eventParam;
  protected String precondition;
  protected String updateRegions;
  protected String globalUpdateRegions;  
  
  protected List updateRegionNames;
}
