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

/**
 * A simple {@link org.araneaframework.Environment} implementation.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardEnvironment extends BaseEnvironment {

  private Map<Class<?>, Object> entries;

  private Environment parentEnv;

  /**
   * Constructs an object with the env parent Environment and with the entries data.
   * 
   * @param env the parent environment
   * @param entries a map of the entries in the Environment
   */
  public StandardEnvironment(Environment env, Map<Class<?>, Object> entries) {
    Assert.notNullParam(entries, "entries");
    this.entries = entries;
    this.parentEnv = env;
  }

  /**
   * Constructs an object with the <code>env</code> parent Environment and entries data containing &lt;key, value&gt;.
   * 
   * @param env the parent environment
   * @param key a key of the value in the map of the Environment entries.
   * @param value a value corresponding to given key in the map of the Environment entries.
   */
  public <T> StandardEnvironment(Environment env, Class<T> key, T value) {
    Assert.notNullParam(key, "key");
    this.entries = new HashMap<Class<?>, Object>(1);
    this.entries.put(key, value);
    this.parentEnv = env;
  }

  /**
   * Returns the map with the entries in this Environment. An entry is a key value pair.
   * 
   * @return a map with the entries.
   */
  public Map<Class<?>, Object> getEntryMap() {
    return this.entries;
  }

  /**
   * Returns the corresponding value of this Envrionment's entries. If none is found from the entries then the entry is
   * returned from the parent environment. If a value to the key does not exist, AraneaNoSuchEnvironmentEntryException
   * is thrown.
   * 
   * @param key the key of the entry
   * @return the Object under the key provided
   * @throws AraneaNoSuchEnvironmentEntryException
   */
  @SuppressWarnings("unchecked")
  public <T> T getEntry(Class<T> key) {
    if (this.entries.containsKey(key)) {
      return (T) this.entries.get(key);
    }
    return this.parentEnv == null ? null : this.parentEnv.getEntry(key);
  }

  @Override
  public String toString() {
    return toString(0);
  }

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
