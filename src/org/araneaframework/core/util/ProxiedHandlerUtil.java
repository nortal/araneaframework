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

package org.araneaframework.core.util;

import java.lang.reflect.Method;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.ProxyActionListener;
import org.araneaframework.core.ProxyEventListener;

/**
 * Utility class for accessing methods that will be invocation targets of
 * {@link ProxyEventListener} and {@link ProxyActionListener}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.0.12
 */
public abstract class ProxiedHandlerUtil {

  private static final Log log = LogFactory.getLog(ProxiedHandlerUtil.class);

  private static final Class[] EMTPY_CLASS_ARRAY = new Class[0];

  public static final String EVENT_HANDLER_PREFIX = "handleEvent";

  public static final String ACTION_HANDLER_PREFIX = "handleAction";

  /**
   * Provides a method of <code>eventTarget</code> that can handle the event.
   * If there is no such method ("handleEvent[EventId]()") then
   * <code>null</code> will be returned.
   * 
   * @param eventId The event that was called. It is used to lookup the right
   *            method.
   * @param eventTarget The widget that is expected to handle this event by
   *            providing a corresponding event handler.
   * @return An existing method ("handleEvent[EventId]()") that handles the
   *         event, or <code>null</code> if such is not found.
   */
  public static Method getEventHandler(String eventId, Object eventTarget) {
    return getEventHandler(eventId, eventTarget, EMTPY_CLASS_ARRAY);
  }

  /**
   * Provides a method of <code>eventTarget</code> that can handle the event.
   * If there is no such method ("handleEvent[EventId]([eventParams])") then
   * <code>null</code> will be returned.
   * 
   * @param eventId The event that was called. It is used to lookup the right
   *            method.
   * @param eventTarget The widget that is expected to handle this event by
   *            providing a corresponding event handler.
   * @param params An array of classes corresponding to parameter types of the
   *            method in right order.
   * @return An existing method ("handleEvent[EventId]([eventParams])") that
   *         handles the event, or <code>null</code> if such is not found.
   */
  public static Method getEventHandler(String eventId, Object eventTarget, Class[] params) {
    return getHandler(EVENT_HANDLER_PREFIX, eventId, eventTarget, params);
  }

  /**
   * Provides a method of <code>actionTarget</code> that can handle the
   * action. If there is no such method ("handleAction[ActionId]()") then
   * <code>null</code> will be returned.
   * 
   * @param actionId The action that was called. It is used to lookup the right
   *            method.
   * @param actionTarget The widget that is expected to handle this action by
   *            providing a corresponding action handler.
   * @return An existing method ("handleAction[ActionId]()") that handles the
   *         action, or <code>null</code> if such is not found.
   */
  public static Method getActionHandler(String actionId, Object actionTarget) {
    return getActionHandler(actionId, actionTarget, EMTPY_CLASS_ARRAY);
  }

  /**
   * Provides a method of <code>actionTarget</code> that can handle the event.
   * If there is no such method ("actionTarget[ActionId]([actionParams])") then
   * <code>null</code> will be returned.
   * 
   * @param actionId The action that was called. It is used to lookup the right
   *            method.
   * @param actionTarget The widget that is expected to handle this action by
   *            providing a corresponding action handler.
   * @param params An array of classes corresponding to parameter types of the
   *            method in right order.
   * @return An existing method ("handleAction[ActionId]([actionParams])") that
   *         handles the action, or <code>null</code> if such is not found.
   */
  public static Method getActionHandler(String actionId, Object actionTarget, Class[] params) {
    return getHandler(ACTION_HANDLER_PREFIX, actionId, actionTarget, params);
  }

  private static Method getHandler(String handlerPrefix, String eventId,
      Object eventTarget, Class[] params) {

    String eventHandlerName = handlerPrefix + eventId.substring(0, 1).toUpperCase() + eventId.substring(1);
    Method result = null;

    try {
      result = eventTarget.getClass().getDeclaredMethod(eventHandlerName, params);
    } catch (NoSuchMethodException e) {
      try {
        result = eventTarget.getClass().getMethod(eventHandlerName, params);
      } catch (NoSuchMethodException nsme) {
        /* OK */
      }
    }

    if (result != null && !result.isAccessible()) {
      result.setAccessible(true);
    }

    return result;
  }

  /**
   * Invokes the method of <code>eventTarget</code> that can handle the event
   * <code>eventId</code>. The method that will be invoked, will also be
   * logged at DEBUG level. In case when no event handler is found, it will log
   * it at WARN level.
   * <p>
   * The following algorithm explains its work:
   * <ol>
   * <li>look for a method: eventTarget.handleEvent[EventId](); if found,
   * invoke it, and quit this method.</li>
   * <li>look for a method: eventTarget.handleEvent[EventId](String); if found,
   * invoke it with <code>eventParam</code>, and quit this method.</li>
   * <li>look for a method: eventTarget.handleEvent[EventId](String[]); if
   * found, split <code>eventParam</code> into array at every ';' symbol,
   * invoke it with created array as its parameter, and quit this method.</li>
   * </ol>
   * 
   * @param eventId The event that should be called. It is used to look up the
   *            right method.
   * @param eventParam An optional parameter to be provided to the handler.
   * @param eventTarget The widget that is expected to handle this event by
   *            providing a corresponding event handler.
   * @throws Exception Any unexpected exception that may occur (e.g. while
   *             invoking the method).
   */
  public static void invokeEventHandler(String eventId, String eventParam, Widget eventTarget)
      throws Exception {

    // lets try to find a handle method with an empty argument
    Method eventHandler = getEventHandler(eventId, eventTarget);
    if (eventHandler != null) {
      if (log.isDebugEnabled()) {
        log.debug("Calling method '" + eventHandler.getName()
            + "()' of class '" + eventTarget.getClass().getName() + "'.");
      }
      eventHandler.invoke(eventTarget, new Object[] {});
      return;
    }

    // lets try to find a method with a String type argument
    eventHandler = getEventHandler(eventId, eventTarget, new Class[] { String.class });
    if (eventHandler != null) {
      if (log.isDebugEnabled()) {
        log.debug("Calling method '" + eventHandler.getName()
            + "(String)' of class '" + eventTarget.getClass().getName() + "'.");
      }
      eventHandler.invoke(eventTarget, new Object[] { eventParam });
      return;
    }

    // lets try to find a method with a String[] type argument
    eventHandler = getEventHandler(eventId, eventTarget, new Class[] { String[].class });
    if (eventHandler != null) {
      if (log.isDebugEnabled()) {
        log.debug("Calling method '" + eventHandler.getName()
            + "(String[])' of class '" + eventTarget.getClass().getName() + "'.");
      }
      // split() is null-safe method:
      String[] params = StringUtils.split(eventParam, ';');
      eventHandler.invoke(eventTarget, new Object[] { params });
      return;
    }

    if (log.isWarnEnabled()) {
      log.warn("Widget '" + eventTarget.getScope() + "' cannot deliver "
          + "event as no event listeners were registered for the event id '"
          + eventId + "'!" + Assert.thisToString(eventTarget));
    }
  }

  /**
   * Invokes the method of <code>actionTarget</code> that can handle the
   * action <code>actionId</code>. The method, that will be invoked, will
   * also be logged at DEBUG level. In case when no action handler is found, it
   * will log it at WARN level.
   * <p>
   * The following algorithm explains its work:
   * <ol>
   * <li>look for a method: actionTarget.handleAction[ActionId](); if found,
   * invoke it, and quit this method.</li>
   * <li>look for a method: actionTarget.handleAction[ActionId](String); if
   * found, invoke it with <code>actionParam</code>, and quit this method.</li>
   * <li>look for a method: actionTarget.handleAction[ActionId](String[]); if
   * found, split <code>actionParam</code> into array at every ';' symbol,
   * invoke it with created array as its parameter, and quit this method.</li>
   * </ol>
   * 
   * @param actionId The action that should be called. It is used to look up the
   *            right method.
   * @param actionParam An optional parameter to be provided to the handler.
   * @param actionTarget The widget that is expected to handle this action by
   *            providing a corresponding action handler.
   * @throws Exception Any unexpected exception that may occur (e.g. while
   *             invoking the method).
   */
  public static void invokeActionHandler(String actionId,
      String actionParam, Widget actionTarget) throws Exception {

    // lets try to find a handle method with an empty argument
    Method actionHandler = getActionHandler(actionId, actionTarget);
    if (actionHandler != null) {
      if (log.isDebugEnabled()) {
        log.debug("Calling method '" + actionHandler.getName()
            + "()' of class '" + actionTarget.getClass().getName() + "'.");
      }
      actionHandler.invoke(actionTarget, new Object[] {});
      return;
    }

    // lets try to find a method with a String type argument
    actionHandler = getActionHandler(actionId, actionTarget, new Class[] { String.class });  
    if (actionHandler != null) {
      if (log.isDebugEnabled()) {
        log.debug("Calling method '" + actionHandler.getName()
            + "(String)' of class '" + actionTarget.getClass().getName() + "'.");
      }
      actionHandler.invoke(actionTarget, new Object[] { actionParam });
      return;
    }

    // lets try to find a method with a String[] type argument
    actionHandler = getActionHandler(actionId, actionTarget, new Class[] { String.class });  
    if (actionHandler != null) {
      if (log.isDebugEnabled()) {
        log.debug("Calling method '" + actionHandler.getName()
            + "(String[])' of class '" + actionTarget.getClass().getName() + "'.");
      }
      // split() is null-safe method:
      String[] params = StringUtils.split(actionParam, ';');
      actionHandler.invoke(actionTarget, new Object[] { params });
      return;
    }

    if (log.isWarnEnabled()) {
      StringBuffer logMessage = new StringBuffer("ProxyActionListener");
      if (actionTarget != null) {
        logMessage.append(" '" + actionTarget.getScope() + "'");
      }
      logMessage.append(" cannot deliver action as no action listeners were "
          + "registered for the action id '");
      logMessage.append(actionId);
      logMessage.append("'!");
      logMessage.append(Assert.thisToString(actionTarget));
      log.warn(logMessage);
    }
  }

}
