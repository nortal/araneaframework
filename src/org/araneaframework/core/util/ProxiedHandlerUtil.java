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
 * {@link ProxyActionListener}. Note that this class is used centrally so that rules described here are global to
 * Aranea.
 * <p>
 * The general rule is that for every incoming action ("actionId"), the target method will be resolved by name
 * handleAction[ActionId](...). For every incoming event, the target method will be resolved exactly the same way,
 * except the method prefix has to be "handleEvent". The target method may accept (in the following order)
 * <ul>
 * <li>no parameters at all;
 * <li>a single string parameter (which is <code>null</code>, when no parameter is provided);
 * <li>a single string array parameter (which is empty, when no parameter is provided);
 * <li>a single {@link List} of strings parameter (which is empty, when no parameter is provided);
 * <li>a single {@link Map} of strings parameter (which is empty, when no parameter is provided);
 * </ul>
 * The first resolved handler will be used, others will be ignored. 
 * <p>
 * For array and list parameters, the incoming request parameter will be splitted by a separator (the one provided by
 * widget's <code>getParameterSeparator()</code> method, if exists, or by semicolon, which is default). For the map
 * parameter, the array/list values will be splitted by the equals (=) symbol so that the left side would become key
 * and the right side would become value. Unlike the list values separator, the equals symbol is not customizable.
 * <p>
 * The support for action/event handler array, list, and map parameters came in Aranea 2.0. 
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.0.12
 */
public abstract class ProxiedHandlerUtil {

  private static final Log LOG = LogFactory.getLog(ProxiedHandlerUtil.class);

  @SuppressWarnings("unchecked")
  private static final Class[] EMTPY_CLASS_ARRAY = new Class[0];

  /**
   * The event handler method prefix (used for event handler lookup).
   */
  public static final String EVENT_HANDLER_PREFIX = "handleEvent";

  /**
   * The action handler method prefix (used for action handler lookup).
   */
  public static final String ACTION_HANDLER_PREFIX = "handleAction";

  /**
   * The name of an optional method the target widget may have. The method is supposed to take no parameters and return
   * a string (a separator) that will be used for splitting action/event parameter into an array, list, or map. When the
   * method is not provided, {@value #DEFAULT_PARAMETER_SEPARTOR} will be used as the separator.
   * 
   * @since 2.0
   */
  public static final String PARAMETER_SEPARTOR_RESOLVER = "getParameterSeparator";

  /**
   * The default separator used for splitting action/event parameter into an array, list, or map.
   * 
   * @see #PARAMETER_SEPARTOR_RESOLVER
   * @since 2.0
   */
  public static final String DEFAULT_PARAMETER_SEPARTOR = ";";

  /**
   * The general rule for constructing handler names: handlerPrefix + handlerId = handlerPrefixHandlerId. Using
   * <code>null</code> as a parameter value won't cause any exceptions, so do it at your own risk!
   * 
   * @param handlerPrefix The prefix of the returned string.
   * @param handlerId The suffix of the returned string. The first letter will be capitalized.
   * @return The concatenated strings where the second parameter has its first letter capitalized.
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
      MethodUtils.invokeExactMethod(eventTarget, handlerName, splitParam(eventTarget, param));

    } else if (hasHandler(eventTarget, handlerName, List.class)) {
      log(handlerName, "List<String>", className);
      MethodUtils.invokeExactMethod(eventTarget, handlerName, parseParamList(eventTarget, param));

    } else if (hasHandler(eventTarget, handlerName, Map.class)) {
      log(handlerName, "Map<String, String>", className);
      MethodUtils.invokeExactMethod(eventTarget, handlerName, parseParamMap(eventTarget, param));

    } else if (LOG.isWarnEnabled()) {
      logHandlerNotFound(handlerPrefix, handlerId, eventTarget);
    }
  }

  /**
   * Logs the calling of a handler method.
   * 
   * @param handlerName The complete method name that is to be called.
   * @param param An optional parameters signature. Use empty string for no parameters.
   * @param className The class of the instance object to be called.
   */
  public static void log(String handlerName, String param, String className) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Calling method '" + handlerName + "(" + param + ")' of class '" + className + "'.");
    }
  }

  /**
   * This method contains central logic for splitting action/event parameter value into multiple strings (no nulls nor
   * empty strings!).
   * 
   * @param target The target widget of the incoming action/event. Used for resolving parameter separator.
   * @param param The parameter to be splitted. May be <code>null</code>.
   * @return An array of strings containing the splitted parameter. In case of <code>null</code>, an empty array.
   * @since 2.0
   */
  public static String[] splitParam(Widget target, String param) {
    String[] params = StringUtils.split(param, getParameterSeparator(target, param));
    return params == null ? new String[0] : params;
  }

  /**
   * Parses the parameter to make a list out of it. The list items are the same as array items from #splitParam(String).
   * 
   * @param target The target widget of the incoming action/event. Used for resolving parameter separator.
   * @param param The parameter to be splitted. May be <code>null</code>.
   * @return A list of strings containing the splitted parameter. In case of <code>null</code>, an empty list.
   * @since 2.0
   */
  public static List<String> parseParamList(Widget target, String param) {
    return new ArrayList<String>(Arrays.asList(splitParam(target, param)));
  }

  // This method is null-safe.
  private static String getParameterSeparator(Widget target, String param) {
    String result = DEFAULT_PARAMETER_SEPARTOR;

    if (target != null && param != null) {
      try {
        MethodUtils.invokeExactMethod(target.getClass(), PARAMETER_SEPARTOR_RESOLVER, param);
      } catch (NoSuchMethodException e) {
        LOG.debug("The action/event target does not have the '" + PARAMETER_SEPARTOR_RESOLVER + "' method.");
      } catch (Exception e) {
        LOG.warn("Unexpected exception while resolving parameter separator.", e);
      }
    }

    return result;
  }

  /**
   * Parses the parameter to make a map out of it. Map rows are parsed as in #splitParam(String). For each row, the row
   * is splitted at first "=" symbol (the first part is key, the other is value), while empty Strings are replaced with
   * nulls.
   * 
   * @param target The target widget of the incoming action/event. Used for resolving parameter separator.
   * @param param The parameter to be splitted. May be <code>null</code>.
   * @return A map of strings (keys, values) containing the splitted parameter. In case of <code>null</code>, an empty
   *         map.
   * @since 2.0
   */
  public static Map<String, String> parseParamMap(Widget target, String param) {
    Map<String, String> handlerParam = new HashMap<String, String>();
    for (String entry : splitParam(target, param)) {
      if (!StringUtils.isEmpty(entry)) {
        String key = StringUtils.defaultIfEmpty(StringUtils.substringBefore(entry, "="), null);
        String value = StringUtils.defaultIfEmpty(StringUtils.substringAfter(entry, "="), null);
        if (!StringUtils.isEmpty(key) || !StringUtils.isEmpty(value)) {
          handlerParam.put(key, value);
        }
      }
    }
    return handlerParam;
  }

  /**
   * Logs a warning message that a target handler, i.e. a method named [prefix][HandlerId](...), was not found.
   * 
   * @param prefix It should match either {@value #ACTION_HANDLER_PREFIX} or {@value #EVENT_HANDLER_PREFIX}.
   * @param handlerId The sent action or event ID.
   * @param target The target widget that should respond to the action or event.
   */
  public static void logHandlerNotFound(String prefix, String handlerId, Widget target) {
    if (LOG.isWarnEnabled()) {
      String type = prefix.equals(EVENT_HANDLER_PREFIX) ? "event" : "action";
      StringBuffer msg = new StringBuffer("Widget '").append(target.getScope());
      msg.append("' cannot deliver ").append(type).append(" as no ").append(type);
      msg.append(" listeners were registered for the ").append(type).append("Id '").append(handlerId).append("'!");
      msg.append(Assert.thisToString(target));
      LOG.warn(msg.toString());
    }
  }
}
