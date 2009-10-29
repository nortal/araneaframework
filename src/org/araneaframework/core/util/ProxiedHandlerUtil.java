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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.ProxyActionListener;
import org.araneaframework.core.ProxyEventListener;

/**
 * Utility class for accessing methods that will be invocation targets of {@link ProxyEventListener} and
 * {@link ProxyActionListener}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.0.12
 */
public abstract class ProxiedHandlerUtil {

  private static final Log LOG = LogFactory.getLog(ProxiedHandlerUtil.class);

  private static final Class<?>[] EMTPY_CLASS_ARRAY = new Class[0];

  public static final String EVENT_HANDLER_PREFIX = "handleEvent";

  public static final String ACTION_HANDLER_PREFIX = "handleAction";

  /*
   * The general rule for constructing handler names: handlerPrefix + handlerId = handlerPrefixHandlerId.
   */
  public static String getHandlerName(String handlerPrefix, String handlerId) {
    return handlerPrefix + StringUtils.capitalize(handlerId);
  }

  /**
   * Provides whether the <code>target</code> has the given handler method that takes no parameters.
   * 
   * @param target The widget that is expected to have a handler.
   * @param methodName The name of the method that the target should have.
   * @return <code>true</code> when the target has the method with given name and the method takes no parameters.
   */
  public static boolean hasEmptyHandler(Object target, String methodName) {
    return hasHandler(target, methodName, EMTPY_CLASS_ARRAY);
  }

  /**
   * Provides whether the <code>target</code> has the given handler method that takes given parameters.
   * 
   * @param target The widget that is expected to have a handler.
   * @param methodName The name of the method that the target should have.
   * @param params The classes the parameters should have in the given order.
   * @return <code>true</code> when the target has the method with given name and the method takes no parameters.
   */
  public static boolean hasHandler(Object target, String methodName, Class<?>... params) {
    return MethodUtils.getAccessibleMethod(target.getClass(), methodName, params) != null;
  }

  /**
   * Invokes the method of <code>eventTarget</code> that can handle the event <code>eventId</code>. The method that will
   * be invoked, will also be logged at DEBUG level. In case when no event handler is found, it will log it at WARN
   * level.
   * <p>
   * This method tries to invoke even handlers in the given order (the method quits after a method has been found and
   * invoked):
   * <ol>
   * <li>eventTarget.handleEvent[EventId]()</li>
   * <li>eventTarget.handleEvent[EventId](String) (<code>eventParam</code>)</li>
   * <li>eventTarget.handleEvent[EventId](String[]) (<code>eventParam</code> split into an array at every ';' symbol)</li>
   * <li>eventTarget.handleEvent[EventId](List<String>) (<code>eventParam</code> split into a list at every ';' symbol)</li>
   * <li>eventTarget.handleEvent[EventId](Map<String, String>) (<code>eventParam</code> split into entries at every ';'
   * symbol and into key and value at the first '=' symbol in every entry)</li>
   * </ol>
   * 
   * @param eventId The event that should be called. It is used to look up the right method.
   * @param eventParam An optional parameter to be provided to the handler.
   * @param eventTarget The widget that is expected to handle this event by providing a corresponding event handler.
   * @throws Exception Any unexpected exception that may occur (e.g. while invoking the method).
   */
  public static void invokeEventHandler(String eventId, String eventParam, Widget eventTarget) throws Exception {
    invokeHandler(EVENT_HANDLER_PREFIX, eventId, eventParam, eventTarget);
  }

  /**
   * Invokes the method of <code>actionTarget</code> that can handle the action <code>actionId</code>. The method, that
   * will be invoked, will also be logged at DEBUG level. In case when no action handler is found, it will log it at
   * WARN level.
   * <p>
   * The following algorithm explains its work:
   * <ol>
   * <li>look for a method: actionTarget.handleAction[ActionId](); if found, invoke it, and quit this method.</li>
   * <li>look for a method: actionTarget.handleAction[ActionId](String); if found, invoke it with
   * <code>actionParam</code>, and quit this method.</li>
   * <li>look for a method: actionTarget.handleAction[ActionId](String[]); if found, split <code>actionParam</code> into
   * array at every ';' symbol, invoke it with created array as its parameter, and quit this method.</li>
   * </ol>
   * 
   * @param actionId The action that should be called. It is used to look up the right method.
   * @param actionParam An optional parameter to be provided to the handler.
   * @param actionTarget The widget that is expected to handle this action by providing a corresponding action handler.
   * @throws Exception Any unexpected exception that may occur (e.g. while invoking the method).
   */
  public static void invokeActionHandler(String actionId, String actionParam, Widget actionTarget) throws Exception {
    invokeHandler(ACTION_HANDLER_PREFIX, actionId, actionParam, actionTarget);
  }

  private static void invokeHandler(String handlerPrefix, String handlerId, String param, Widget eventTarget) throws Exception {
    String handlerName = getHandlerName(handlerPrefix, handlerId);
    String className = eventTarget.getClass().getName();

    // First, let's try to find a handle method with an empty argument:
    if (hasEmptyHandler(eventTarget, handlerName)) {
      log(handlerName, "", className);
      MethodUtils.invokeExactMethod(eventTarget, handlerName, null);

    } else if (hasHandler(eventTarget, handlerName, String.class)) {
      log(handlerName, "String", className);
      MethodUtils.invokeExactMethod(eventTarget, handlerName, param);

    } else if (hasHandler(eventTarget, handlerName, String[].class)) {
      log(handlerName, "String[]", className);
      MethodUtils.invokeExactMethod(eventTarget, handlerName, splitParam(param));

    } else if (hasHandler(eventTarget, handlerName, List.class)) {
      log(handlerName, "List<String>", className);
      MethodUtils.invokeExactMethod(eventTarget, handlerName, parseParamList(param));

    } else if (hasHandler(eventTarget, handlerName, Map.class)) {
      log(handlerName, "Map<String, String>", className);
      MethodUtils.invokeExactMethod(eventTarget, handlerName, parseParamMap(param));

    } else if (LOG.isWarnEnabled()) {
      logHandlerNotFound(handlerPrefix, handlerId, eventTarget);
    }
  }

  // Logs the calling of a handler method.
  public static void log(String handlerName, String param, String className) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Calling method '" + handlerName + "(" + param + ")' of class '" + className + "'.");
    }
  }

  // This method is null-safe.
  public static String[] splitParam(String param) {
    return StringUtils.split(param, ';');
  }

  // Parses the parameter to make a list out of it. The list items are the same as array items from #splitParam(String).
  public static List<String> parseParamList(String param) {
    return new ArrayList<String>(Arrays.asList(splitParam(param)));
  }

  // Parses the parameter to make a map out of it. Map rows are parsed as in #splitParam(String). For each row, the row
  // is splitted at first "=" symbol (the first part is key, the other is value), while empty Strings are replaced with
  // nulls.
  public static Map<String, String> parseParamMap(String param) {
    Map<String, String> handlerParam = new HashMap<String, String>();
    for (String entry : splitParam(param)) {
      if (!StringUtils.isEmpty(entry)) {
        String key = StringUtils.defaultIfEmpty(StringUtils.substringBefore(entry, "="), null);
        String value = StringUtils.defaultIfEmpty(StringUtils.substringAfter(entry, "="), null);
        handlerParam.put(key, value);
      }
    }
    return handlerParam;
  }

  public static void logHandlerNotFound(String prefix, String handlerId, Widget target) {
    String type = prefix.equals(EVENT_HANDLER_PREFIX) ? "event" : "action";
    StringBuffer msg = new StringBuffer("Widget '").append(target.getScope());
    msg.append("' cannot deliver ").append(type).append(" as no ").append(type);
    msg.append(" listeners were registered for the ").append(type).append("Id '").append(handlerId).append("'!");
    msg.append(Assert.thisToString(target));
    LOG.warn(msg.toString());
  }
}
