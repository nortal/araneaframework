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

package org.araneaframework.uilib.util;

import org.araneaframework.backend.util.BeanUtil;

import org.apache.commons.lang.StringUtils;

/**
 * This class is a general helper, which is used throughout UiLib to parse <code>String</code>s like events and
 * hierarchical names.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class NameUtil {

  private static final String ACTION_DELIM = "$";

  /**
   * Returns the full name as "<code>prefix</code>.<code>name</code>". If prefix isn't empty, a dot is added after it.
   * 
   * @param prefix the full name prefix.
   * @param name the current name.
   * @return The full name.
   */
  public static String getFullName(String prefix, String name) {
    return StringUtils.isEmpty(prefix) ? name : prefix + BeanUtil.NESTED_DELIM + name;
  }

  /**
   * Returns the last name of the <code>fullName</code> that doesn't contain any dots.
   * 
   * @param fullName The current name.
   * @return The part that comes after final dot, or the same as input string.
   */
  public static String getLastName(String fullName) {
    boolean dot = StringUtils.contains(fullName, BeanUtil.NESTED_DELIM);
    return dot ? StringUtils.substringAfterLast(fullName, BeanUtil.NESTED_DELIM) : fullName;
  }

  /**
   * Returns the prefix of the full name (prefix is part before the first dot, or the whole full name if it contains no
   * dots).
   * 
   * @param fullName the full name.
   * @return prefix of the full name.
   */
  public static String getNamePrefix(String fullName) {
    return StringUtils.substringBefore(fullName, BeanUtil.NESTED_DELIM);
  }

  /**
   * Returns the full prefix of given full name. That is, the part before the last dot, or NULL when fullName is not
   * nested).
   * 
   * @param fullName the full name.
   * @return full prefix of the full name.
   * @since 1.0.9
   */
  public static String getLongestPrefix(String fullName) {
    boolean dot = StringUtils.contains(fullName, BeanUtil.NESTED_DELIM);
    return dot ? StringUtils.substringBeforeLast(fullName, BeanUtil.NESTED_DELIM) : null;
  }

  /**
   * Returns the shortest suffix of given full name. That is, the part after the last dot, or fullName when fullName is
   * not nested).
   * 
   * @param fullName the full name.
   * @return full prefix of the full name.
   * @since 1.0.9
   */
  public static String getShortestSuffix(String fullName) {
    return StringUtils.substringBefore(fullName, BeanUtil.NESTED_DELIM);
  }

  /**
   * Returns suffix of the full name (suffix is part after the first dot, or a <code>null</code> if full name contains
   * no dots).
   * 
   * @param fullName full name.
   * @return suffix of the full name or <code>null</code>.
   */
  public static String getNameSuffix(String fullName) {
    boolean dot = StringUtils.contains(fullName, BeanUtil.NESTED_DELIM);
    return dot ? StringUtils.substringAfterLast(fullName, BeanUtil.NESTED_DELIM) : null;
  }

  /**
   * Returns the event name extracted from the action parameter of the <code>"Event"</code> action. It is the part in
   * the action parameter before the first <code>"$"</code>.
   * 
   * @param eventActionParam the action parameter of the <code>"Event"</code> action
   * @return the event name.
   */
  public static String getEventName(String eventActionParam) {
    return StringUtils.substringBefore(eventActionParam, ACTION_DELIM);
  }

  /**
   * Returns the event parameter extracted from the action parameter of the <code>"Event"</code> action. It is the part
   * in the action parameter after the first <code>"$"</code>.
   * 
   * @param eventActionParam the action parameter of the <code>"Event"</code> action
   * @return the event parameter.
   */
  public static String getEventParam(String eventActionParam) {
    return StringUtils.substringAfter(eventActionParam, ACTION_DELIM);
  }
}
