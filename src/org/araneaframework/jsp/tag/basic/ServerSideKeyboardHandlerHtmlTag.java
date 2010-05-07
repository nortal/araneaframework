/*
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
 */

package org.araneaframework.jsp.tag.basic;

import java.io.Writer;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.util.JspUpdateRegionUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *  name = "eventKeyboardHandler"
 *  body-content = "empty"
 *  description = "Registers a 'server-side' keyboard handler that sends an event to the specified widget."
 */
public class ServerSideKeyboardHandlerHtmlTag extends BaseKeyboardHandlerTag {

  protected String scope;

  protected String updateRegions;

  protected String globalUpdateRegions;

  protected UiUpdateEvent event = new UiUpdateEvent();

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    this.event.setUpdateRegionNames(JspUpdateRegionUtil.getUpdateRegionNames(this.pageContext, this.updateRegions,
        this.globalUpdateRegions));

    String handler = createHandlerToCallEvent(this.pageContext, this.event);

    // Write out.
    writeKeypressHandlerScript(out, this.scope, handler);

    return SKIP_BODY;
  }

  public static final String createHandlerToCallEvent(PageContext pageContext, UiUpdateEvent event) {
    if (event.getTarget() == null) {
      event.setTarget(JspWidgetUtil.getContextWidgetFullId(pageContext));
    }

    // Let's call JS function: event(eventId, eventTarget, eventParam, eventPrecondition, eventUpdateRegions)
    StringBuffer result = new StringBuffer("function(event, elementId) { Aranea.Page.event(");
    append(result, event.getId(), true);
    append(result, event.getTarget(), true);
    append(result, event.getParam(), true);
    append(result, event.getEventPrecondition(), true);
    append(result, JspUpdateRegionUtil.formatUpdateRegionsJS(event.getUpdateRegionNames()), false);
    result.append(")}");

    return result.toString();
  }

  protected static void append(StringBuffer sb, String value, boolean comma) {
    if (value != null) {
      sb.append('\'').append(value).append('\'');
    } else {
      sb.append("null");
    }
    if (comma) {
      sb.append(',');
    }
  }

  // Attributes

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "See keyboardHandler tag."
   */
  public void setScope(String scope) {
    this.scope = evaluate("scope", scope, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "The path to the event handler's widget (by default it's the path of current widget in JSP context)."
   */
  public void setWidgetId(String widgetId) {
    this.event.setTarget(evaluate("widgetId", widgetId, String.class));
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "The event handler's name."
   */
  public void setEventId(String eventId) {
    this.event.setId(evaluate("eventId", eventId, String.class));
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "The event handler's parameter."
   */
  public void setEventParam(String eventParam) {
    this.event.setParam(evaluate("eventParam", eventParam, String.class));
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Enumerates the regions of markup to be updated in this widget scope. Please see <code>&lt;ui:updateRegion&gt;</code> for details."
   */
  public void setUpdateRegions(String updateRegions) {
    this.updateRegions = evaluate("updateRegions", updateRegions, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Enumerates the regions of markup to be updated globally. Please see <code>&lt;ui:updateRegion&gt;</code> for details."
   */
  public void setGlobalUpdateRegions(String globalUpdateRegions) {
    this.globalUpdateRegions = evaluate("globalUpdateRegions", globalUpdateRegions, String.class);
  }

  /**
   * Sets the precondition, default value is <code>null</code>
   * 
   * @see org.araneaframework.jsp.tag.uilib.form.element.BaseFormButtonTag#setOnClickPrecondition
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Sets an optional precondition (function name or JS boolean expression) that will be evaluate before event."
   */
  public void setPrecondition(String precondition) {
    this.event.setEventPrecondition(evaluate("precondition", precondition, String.class));
  }
}
