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

package org.araneaframework.http.util;

import java.io.Serializable;
import org.apache.commons.lang.StringEscapeUtils;
import org.araneaframework.core.Assert;

/**
 * An ordered list of values in JSON data-interchange format.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */
public class JsonArray implements Serializable {

  protected StringBuffer buf = new StringBuffer();

  public JsonArray() {
    buf.append('[');
  }

  /**
   * Append a value to this array.
   * 
   * @param element
   *          value to append to this array. Can be a string in double quotes,
   *          or a number, or true or false or null, or an object or an array.
   */
  public void append(String element) {
    Assert.notNullParam(element, "element");
    if (buf.length() > 1)
      buf.append(',');
    buf.append(element);
  }

  /**
   * Append a value to this array.
   * 
   * @param element
   *          value to append to this array. It is automatically double-quoted
   *          to represent a string.
   */
  public void appendString(String element) {
    Assert.notNullParam(element, "element");
    if (buf.length() > 1)
      buf.append(',');
    buf.append('"');
    buf.append(StringEscapeUtils.escapeJavaScript(element));
    buf.append('"');
  }

  /**
   * Get this array in JSON data-interchange format.
   */
  @Override
  public String toString() {
    buf.append(']');
    String string = buf.toString();
    buf.deleteCharAt(buf.length() - 1);
    return string;
  }

}
