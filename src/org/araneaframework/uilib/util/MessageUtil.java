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

package org.araneaframework.uilib.util;

import java.text.MessageFormat;
import org.araneaframework.Environment;
import org.araneaframework.Widget;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.jsp.UiEvent;
import org.araneaframework.jsp.util.JspWidgetCallUtil;

/**
 * Contains various useful methods to localize, format and create link messages.
 */
public class MessageUtil {

  /**
   * Localizes a message that does not require any parameter.
   * 
   * @param messageCode The code to retrieve the message.
   * @param env The environment that is expected to contain the localization
   *            context.
   * @return The localized message.
   */
  public static String localize(String messageCode, Environment env) {
    LocalizationContext locCtx = 
      (LocalizationContext) env.getEntry(LocalizationContext.class);
    return locCtx.getResourceBundle().getString(messageCode);    
  }

  /**
   * Formats the localized message by inserting given <code>parameter</code> . 
   * @param message
   * @param parameter
   * @return
   */
  public static String format(String message, Object parameter) {
    return format(message, new Object[] {parameter});
  }  
  
  public static String format(String message, Object parameter1, Object parameter2) {
    return format(message, new Object[] {parameter1, parameter2});
  }    
  
  public static String format(String message, Object[] parameters) {
    return MessageFormat.format(message, parameters);
  }
  
  public static String localizeAndFormat(String message, Object parameter, Environment env) {
    return localizeAndFormat(message, new Object[] {parameter}, env);
  }
  
  public static String localizeAndFormat(String message, Object parameter1, Object parameter2, Environment env) {
    return localizeAndFormat(message, new Object[] {parameter1, parameter2}, env);
  }
  
  public static String localizeAndFormat(String message, Object[] parameters, Environment env) {
    return format(localize(message, env), parameters);
  }

  public static String createEventMessage(String message, Object[] params,
      String eventId, String eventParam, String widgetFullId, Environment env) {
    return createEventMessage(localizeAndFormat(message, params, env), eventId,
        eventParam, widgetFullId);
  }

  public static String createEventMessage(String message, Object[] params,
      String eventId, String eventParam, Widget targetWidget, Environment env) {
    return createEventMessage(localizeAndFormat(message, params, env), eventId,
        eventParam, targetWidget.getScope().toString());
  }

  public static String createEventMessage(String localizedMessage,
      String eventId, String eventParam, Widget targetWidget) {
    return createEventMessage(localizedMessage, eventId, eventParam,
        targetWidget.getScope().toString());
  }

  public static String createEventMessage(String localizedMessage,
      String eventId, String eventParam, String widgetFullId) {
    UiEvent event = new UiEvent();
    event.setId(eventId);
    event.setParam(eventParam);
    event.setTarget(widgetFullId);
    return createEventMessage(localizedMessage, event);
  }

  public static String createEventMessage(String localizedMessage, UiEvent event) {
    StringBuffer msg = new StringBuffer("<a href=\"#\" onclick=\"");
    msg.append(JspWidgetCallUtil.getSubmitScriptForEvent());
    msg.append("\" ");
    msg.append(event.getEventAttributes());
    msg.append(">");
    msg.append(localizedMessage);
    msg.append("</a>");
    return msg.toString();
  }
}
