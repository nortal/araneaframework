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

package org.araneaframework.http.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.StringEscapeUtils;
import org.araneaframework.core.Assert;

/**
 * A collection of name/value pairs in JSON data-interchange format.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */
public class JsonObject implements Serializable {

  protected StringBuffer buf = new StringBuffer();

  public JsonObject() {
    buf.append('{');
  }
  
  public <T,U> JsonObject(Map<T, U> map) {
    this();
    for (Iterator<Map.Entry<T, U>> i = map.entrySet().iterator(); i.hasNext();) {
      Map.Entry<T, U> entry = i.next();
      if (entry.getValue() instanceof String)
        setStringProperty(entry.getKey().toString(), (String)entry.getValue());
      else
        setProperty(entry.getKey().toString(), String.valueOf(entry.getValue()));
    }
  }

  /**
   * Set a name/value pair on this object. Duplicate names are not checked.
   * 
   * @param name
   *          name of the property
   * @param value
   *          value of the property. Can be a string in double quotes, or a
   *          number, or true or false or null, or an object or an array.
   */
  public void setProperty(String name, String value) {
    Assert.notNullParam(name, "name");
    if (buf.length() > 1)
      buf.append(',');
    buf.append('"');
    buf.append(StringEscapeUtils.escapeJavaScript(name));
    buf.append('"');
    buf.append(':');
    buf.append(value);
  }

  /**
   * Set a name/value pair on this object. Duplicate names are not checked.
   * 
   * @param name
   *          name of the property
   * @param value
   *          value of the property. It is automatically double-quoted to
   *          represent a string.
   */
  public void setStringProperty(String name, String value) {
    Assert.notNullParam(name, "name");
    if (buf.length() > 1)
      buf.append(',');
    buf.append('"');
    buf.append(StringEscapeUtils.escapeJavaScript(name));
    buf.append('"');
    buf.append(':');
    if (value != null) buf.append('"');
    buf.append(StringEscapeUtils.escapeJavaScript(String.valueOf(value)));
    if (value != null) buf.append('"');
  }

  /**
   * Get this object in JSON data-interchange format.
   */
  @Override
  public String toString() {
    buf.append('}');
    String string = buf.toString();
    buf.deleteCharAt(buf.length() - 1);
    return string;
  }
}
