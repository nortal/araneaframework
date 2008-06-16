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
   * Formats the localized message by inserting given <code>parameter</code>
   * to the placeholder in the message.
   * 
   * @param message The message containing a placeholder for the parameter.
   * @param parameter The parameter that will be used in the message.
   * @return The formatted message.
   */
  public static String format(String message, Object parameter) {
    return format(message, new Object[] {parameter});
  }  
  
  /**
   * Formats the localized message by inserting given <code>parameter</code>
   * to the placeholder in the message.
   * 
   * @param message The message containing a placeholder for the parameter.
   * @param parameter The parameter that will be used in the message.
   * @return The formatted message.
   */
  public static String format(String message, Object parameter1, Object parameter2) {
    return format(message, new Object[] {parameter1, parameter2});
  }    

  /**
   * Formats the localized message by inserting given <code>parameters</code>
   * to the placeholders in the message.
   * 
   * @param message The message containing placeholders for the parameters.
   * @param parameters The parameters that will be used in the message.
   * @return The formatted message.
   */
  public static String format(String message, Object[] parameters) {
    return MessageFormat.format(message, parameters);
  }
  
  /**
   * Localizes and then formats the localized message by inserting given
   * <code>parameter</code> to the placeholder.
   * 
   * @param message The key to retrieve the message.
   * @param parameter The parameter that will be used in the localized message.
   * @return The localized and formatted message.
   */
  public static String localizeAndFormat(String message, Object parameter, Environment env) {
    return localizeAndFormat(message, new Object[] {parameter}, env);
  }
  
  /**
   * Localizes and then formats the localized message by inserting given
   * parametersto the placeholders.
   * 
   * @param message The key to retrieve the message.
   * @param parameter1 The first parameter that will be used in the localized
   *            message.
   * @param parameter2 The second parameter that will be used in the localized
   *            message.
   * @param env The environment that is expected to contain the localization
   *            context.
   * @return The localized and formatted message.
   */
  public static String localizeAndFormat(String message, Object parameter1, Object parameter2, Environment env) {
    return localizeAndFormat(message, new Object[] {parameter1, parameter2}, env);
  }
  
  /**
   * Localizes and then formats the localized message by inserting given
   * <code>parameters</code> to the placeholders.
   * 
   * @param message The key to retrieve the message.
   * @param parameter1 The first parameter that will be used in the localized
   *            message.
   * @param parameter2 The second parameter that will be used in the localized
   *            message.
   * @param env The environment that is expected to contain the localization
   *            context.
   * @return The localized and formatted message.
   */
  public static String localizeAndFormat(String message, Object[] parameters, Environment env) {
    return format(localize(message, env), parameters);
  }

  /**
   * Returns HTML code for wrapping the message in a link to invoke an event.
   * The event is described by event ID (that refers to the event handler),
   * optional event parameter, and the runtime full widget ID (to deliver the
   * event to the right widget).
   * <br>
   * Note that the message should be rendered as-is (not escaped).
   * 
   * @param message The key to retrieve and localize the message.
   * @param params The parameters for the localized message.
   * @param eventId The ID of the event to be invoked.
   * @param eventParam The parameter for the event.
   * @param widgetFullId The full ID of the event widget.
   * @param env The environment that is expected to contain the localization
   *            context.
   * @return The HTML code that contains a link.
   * 
   * @since 1.1.4
   */
  public static String createEventMessage(String message, Object[] params,
      String eventId, String eventParam, String widgetFullId, Environment env) {
    return createEventMessage(localizeAndFormat(message, params, env), eventId,
        eventParam, widgetFullId);
  }

  /**
   * Returns HTML code for wrapping the message in a link to invoke an event.
   * The event is described by event ID (that refers to the event handler),
   * optional event parameter, and the runtime full widget ID (to deliver the
   * event to the right widget). <br>
   * Note that the message should be rendered as-is (not escaped).
   * 
   * @param message The key to retrieve and localize the message.
   * @param params The parameters for the localized message.
   * @param eventId The ID of the event to be invoked.
   * @param eventParam The parameter for the event.
   * @param targetWidget The widget that can handle the event (the widget must
   *            have its scope).
   * @param env The environment that is expected to contain the localization
   *            context.
   * @return The HTML code that contains a link.
   * 
   * @since 1.1.4
   */
  public static String createEventMessage(String message, Object[] params,
      String eventId, String eventParam, Widget targetWidget, Environment env) {
    return createEventMessage(localizeAndFormat(message, params, env), eventId,
        eventParam, targetWidget.getScope().toString());
  }

  /**
   * Returns HTML code for wrapping the message in a link to invoke an event.
   * The event is described by event ID (that refers to the event handler),
   * optional event parameter, and the runtime full widget ID (to deliver the
   * event to the right widget).
   * <br>
   * Note that the message should be rendered as-is (not escaped).
   * 
   * @param localizedMessage The localized message.
   * @param eventId The ID of the event to be invoked.
   * @param eventParam The parameter for the event.
   * @param targetWidget The widget that can handle the event (the widget must
   *            have its scope).
   * @return The HTML code that contains a link.
   * 
   * @since 1.1.4
   */
  public static String createEventMessage(String localizedMessage,
      String eventId, String eventParam, Widget targetWidget) {
    return createEventMessage(localizedMessage, eventId, eventParam,
        targetWidget.getScope().toString());
  }

  /**
   * Returns HTML code for wrapping the message in a link to invoke an event.
   * The event is described by event ID (that refers to the event handler),
   * optional event parameter, and the runtime full widget ID (to deliver the
   * event to the right widget).
   * <br>
   * Note that the message should be rendered as-is (not escaped).
   * 
   * @param localizedMessage The localized message.
   * @param eventId The ID of the event to be invoked.
   * @param eventParam The parameter for the event.
   * @param widgetFullId The full ID of the event widget.
   * @return The HTML code that contains a link.
   * @since 1.1.4
   */
  public static String createEventMessage(String localizedMessage,
      String eventId, String eventParam, String widgetFullId) {
    UiEvent event = new UiEvent();
    event.setId(eventId);
    event.setParam(eventParam);
    event.setTarget(widgetFullId);
    return createEventMessage(localizedMessage, event);
  }

  /**
   * Returns HTML code for wrapping the message in a link to invoke an event.
   * The event is described by event ID (that refers to the event handler),
   * optional event parameter, and the runtime full widget ID (to deliver the
   * event to the right widget).
   * <br>
   * Note that the message should be rendered as-is (not escaped).
   * 
   * @param localizedMessage The localized message.
   * @param event The event information.
   * @return The HTML code that contains a link.
   * @since 1.1.4
   */
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
