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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.util.JspUpdateRegionUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;
/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 * @jsp.tag
 *   name = "eventKeyboardHandler"
 *   body-content = "empty"
 *   description = "Registers a 'server-side' keyboard handler that sends an event to the specified widget."
 */
public class ServerSideKeyboardHandlerHtmlTag extends BaseKeyboardHandlerTag{
	  protected String scope;
	  protected String updateRegions;
	  protected String globalUpdateRegions;

      protected UiUpdateEvent event = new UiUpdateEvent();
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
      event.setTarget((String) evaluate("widgetId", widgetId, String.class));
  }
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "" 
	 */
	public void setEventId(String eventId) throws JspException {
		event.setId((String) evaluate("eventId", eventId, String.class));
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "" 
	 */
	public void setEventParam(String eventParam) throws JspException {
		event.setParam((String) evaluate("eventParam", eventParam, String.class));
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
  * Sets the precondition, default value is <code>null</code>
  * @see org.araneaframework.jsp.tag.uilib.form.element.BaseFormButtonTag#setOnClickPrecondition
  */
  public void setPrecondition(String precondition) throws JspException {
    event.setEventPrecondition((String) evaluate("precondition", precondition, String.class));
  }	
	
	
	//
	// Implementation
	//
		
	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);

		event.setUpdateRegionNames(JspUpdateRegionUtil.getUpdateRegionNames(pageContext, updateRegions, globalUpdateRegions));
		String handler = createHandlerToCallEvent(pageContext, event);

		// Write out.
		if (intKeyCode != null)
			KeyboardHandlerHtmlTag.writeRegisterKeypressHandlerScript(out, scope, intKeyCode, handler);
		else if (keyCombo != null)
			writeRegisterKeycomboHandlerScript(out, scope, keyCombo, handler);

		return SKIP_BODY;
	}
  
	

	public static final String createHandlerToCallEvent(PageContext pageContext, UiUpdateEvent event) throws JspException{
		if (event.getTarget() == null) {
          event.setTarget(JspWidgetUtil.getContextWidgetFullId(pageContext));
		}

    // submit_6 : function(systemForm, eventId, eventTarget, eventParam, eventPrecondition, eventUpdateRegions)
		StringBuffer result = new StringBuffer("function (event, elementId) { ");
		result.append("_ap.event_6(");
		result.append("_ap.getSystemForm(),");
		if (event.getId() != null)
			result.append("'").append(event.getId()).append("',");
		else
			result.append(" null, ");
		
		if (event.getTarget() != null)
			result.append("'").append(event.getTarget()).append("',");
		else
			result.append(" null, ");
		
		if (event.getParam() != null)
			result.append("'").append(event.getParam()).append("',");
		else
			result.append(" null, ");
		
		if (event.getEventPrecondition() != null)
			result.append("'").append(event.getEventPrecondition()).append("',");
		else
			result.append(" null, ");

		result.append("'").append(JspUpdateRegionUtil.formatUpdateRegionsJS(event.getUpdateRegionNames())).append("'");

		result.append(')');
		result.append('}');
		
		return result.toString();
	}
}
