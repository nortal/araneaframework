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

package org.araneaframework.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.Environment;

/**
 * A simple {@link org.araneaframework.Environment} implementation.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardEnvironment extends BaseEnvironment {

  private static final long serialVersionUID = 1L;

  private static final String space = " ";

  private static final String lf = "\n";

  private Map entries;

  private Environment parentEnv;

  /**
   * Constructs an object with the env parent Environment and with the entries
   * data.
   * 
   * @param env the parent environment
   * @param entries a map of the entries in the Environment
   */
  public StandardEnvironment(Environment env, Map entries) {
    Assert.notNullParam(entries, "entries");
    this.entries = entries;
    parentEnv = env;
  }

  /**
   * Constructs an object with the env parent Environment and entries data
   * containing &lt;key, value&gt;.
   * 
   * @param env the parent environment
   * @param key a key of the value in the map of the Environment entries.
   * @param value a value corresponding to given key in the map of the
   *            Environment entries.
   */
  public StandardEnvironment(Environment env, Object key, Object value) {
    Assert.notNullParam(key, "key");
    entries = new HashMap(1);
    entries.put(key, value);
    parentEnv = env;
  }

  /**
   * Returns the map with the entries in this Environment. An entry is a key
   * value pair.
   * 
   * @return a map with the entries.
   */
  public Map getEntryMap() {
    return entries;
  }

  /**
   * Returns the corresponding value of this Envrionment's entries. If none is
   * found from the entries then the entry is returned from the parent
   * environment. If a value to the key does not exist,
   * AraneaNoSuchEnvironmentEntryException is thrown.
   * 
   * @param key the key of the entry
   * @return the Object under the key provided
   * @throws AraneaNoSuchEnvironmentEntryException
   */
  public Object getEntry(Object key) {
    if (entries.containsKey(key)) {
      return entries.get(key);
    }
    if (parentEnv == null) {
      return null;
    }
    return parentEnv.getEntry(key);
  }

  public String toString() {
    return toString(0);
  }

  private String toString(int pad) {
    String padding = StringUtils.leftPad("", pad, space);
    StringBuffer result = new StringBuffer();
    if (entries != null) {
      for (Iterator i = entries.entrySet().iterator(); i.hasNext();) {
        Map.Entry e = (Map.Entry) i.next();
        result.append(padding + e.getKey() + "="
            + ObjectUtils.identityToString(e.getValue()) + lf);
      }
    }
    if (parentEnv instanceof StandardEnvironment) {
      result.append(((StandardEnvironment) parentEnv).toString(pad
          + (space.length() * 2)));
    }
    result.append("\n");
    return result.toString();
  }
}
