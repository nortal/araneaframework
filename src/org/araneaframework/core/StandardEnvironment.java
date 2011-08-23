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
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.Environment;
import org.araneaframework.core.util.Assert;

/**
 * A simple <tt>Environment</tt> implementation that should cover the most common needs.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardEnvironment extends BaseEnvironment {

  private final Map<Class<?>, Object> entries;

  private final Environment parentEnv;

  /**
   * Constructs an object with the parent environment and with the entries data. When environment items are searched,
   * custom entries will be searched first, and the parent environment will be used as a fall-back.
   * 
   * @param env The parent environment (optional).
   * @param entries A map of entries to add to this environment (required).
   */
  public StandardEnvironment(Environment env, Map<Class<?>, Object> entries) {
    Assert.notNullParam(entries, "entries");
    this.entries = entries;
    this.parentEnv = env;
  }

  /**
   * Constructs an object with the parent Environment and adds an entry containing &lt;key, value&gt;. When environment
   * items are searched, custom entries will be searched first, and the parent environment will be used as a fall-back.
   * 
   * @param env The parent environment (optional).
   * @param key A key for the value to be added to this environment (required).
   * @param value A value corresponding to given key (may be <code>null</code>).
   */
  public <T> StandardEnvironment(Environment env, Class<T> key, T value) {
    Assert.notNullParam(key, "key");
    this.entries = new HashMap<Class<?>, Object>(1);
    this.entries.put(key, value);
    this.parentEnv = env;
  }

  /**
   * Returns the map with the custom entries in this environment (and not in the parent environment).
   * 
   * @return A map with this environment entries.
   */
  public Map<Class<?>, Object> getEntryMap() {
    return this.entries;
  }

  @SuppressWarnings("unchecked")
  public <T> T getEntry(Class<T> key) {
    if (this.entries.containsKey(key)) {
      return (T) this.entries.get(key);
    }
    return this.parentEnv == null ? null : this.parentEnv.getEntry(key);
  }

  /**
   * Prints out the environment entries in as a tree.
   */
  @Override
  public String toString() {
    return toString(0);
  }

  /**
   * Recursive method for printing out environment entries tree.
   * 
   * @param pad A number of spaces for padding current environment entries. Will be increased by 2 with each recursion.
   * @return Environment entries as a tree.
   */
  private String toString(int pad) {
    String padding = StringUtils.leftPad("", pad, " ");
    StringBuffer result = new StringBuffer();
    if (this.entries != null) {
      for (Entry<Class<?>, Object> entry : this.entries.entrySet()) {
        result.append(padding + entry.getKey() + "=" + ObjectUtils.identityToString(entry.getValue()) + "\n");
      }
    }
    if (this.parentEnv instanceof StandardEnvironment) {
      result.append(((StandardEnvironment) this.parentEnv).toString(pad + 2));
    }
    result.append("\n");
    return result.toString();
  }
}
