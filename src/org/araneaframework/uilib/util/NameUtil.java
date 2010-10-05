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

import org.apache.commons.lang.StringUtils;
import org.araneaframework.backend.util.BeanUtil;

/**
 * This class is a general helper, which is used throughout UiLib to parse <code>String</code>s like events and
 * hierarchical names. This helper mainly deals with path-like names, which may contain dots to separate names. More
 * specifically, a full name is in form like "<code>[zero.or.more.prefix.names.]name</code>". A full name may have a
 * prefix, but always has a name. Therefore, when the full name has no prefix, methods parsing the prefix return
 * <code>null</code>, and methods parsing the name suffix will return the original parameter.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class NameUtil {

  private static final String ACTION_DELIM = "$";

  /**
   * This method is here to support tags which cannot use the fancier method {@link #getFullName(String...)} due to
   * being legacy code.
   * 
   * @see #getFullName(String...)
   */
  public static String getFullName(String prefix, String suffix) {
    return getFullName(new String[] { prefix, suffix });
  }

  /**
   * Constructs a full name from the given <code>names</code>. Each name will be concatenated by a dot while omitting
   * empty or <code>null</code> name values. This method is <code>null</code>-safe and when <code>names</code> array is
   * empty, the result will be <code>null</code>.
   * 
   * @param names An array of names to concatenate in the order they are given.
   * @return The constructed full name or <code>null</code>.
   */
  public static String getFullName(String... names) {
    StringBuffer fullName = new StringBuffer();

    if (names != null) {
      for (String name : names) {
        if (StringUtils.isNotEmpty(name)) {
          if (fullName.length() > 0) {
            fullName.append(BeanUtil.NESTED_DELIM);
          }
          fullName.append(name);
        }
      }
    }

    return fullName.length() > 0 ? fullName.toString() : null;
  }

  protected static boolean containsDot(String fullName) {
    return StringUtils.contains(fullName, BeanUtil.NESTED_DELIM);
  }

  /**
   * Returns the part of <code>fullName</code> that follows the last dot. When the parameter contains no dots, the
   * result will be equal to the original parameter value.
   * 
   * @param fullName The full name, which may or may not contain dots.
   * @return The part that comes after the last dot, or the original input string.
   */
  public static String getShortestSuffix(String fullName) {
    return containsDot(fullName) ? StringUtils.substringAfterLast(fullName, BeanUtil.NESTED_DELIM) : fullName;
  }

  /**
   * Returns the part of <code>fullName</code> that follows the first dot. When the parameter contains no dots, the
   * result will be equal to the original parameter value.
   * 
   * @param fullName The full name, which may or may not contain dots.
   * @return The part that comes after the last dot, or the original input string.
   */
  public static String getLongestSuffix(String fullName) {
    return containsDot(fullName) ? StringUtils.substringAfter(fullName, BeanUtil.NESTED_DELIM) : fullName;
  }

  /**
   * Returns the part of <code>fullName</code> that precedes the first dot. When the parameter contains no dots, the
   * result will be <code>null</code>.
   * 
   * @param fullName The full name, which may or may not contain dots.
   * @return The part that comes before the first dot, or <code>null</code>.
   */
  public static String getShortestPrefix(String fullName) {
    return containsDot(fullName) ? StringUtils.substringBefore(fullName, BeanUtil.NESTED_DELIM) : null;
  }

  /**
   * Returns the part of <code>fullName</code> that precedes the last dot. When the parameter contains no dots, the
   * result will be <code>null</code>.
   * 
   * @param fullName The full name, which may or may not contain dots.
   * @return The part that comes before the last dot, or <code>null</code>.
   * @since 1.0.9
   */
  public static String getLongestPrefix(String fullName) {
    return containsDot(fullName) ? StringUtils.substringBeforeLast(fullName, BeanUtil.NESTED_DELIM) : null;
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
