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

package org.araneaframework.framework.filter;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.core.BaseFilterWidget;

/**
 * Filter widget that enriches children's environment with specified context entries that are provided through
 * {@link #setContexts(Map)}. Note that environment can contain entries where keys are classes and values are objects
 * that are (sub-)objects of the key class.
 * 
 * @see #setContexts(Map)
 * @see Environment
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StandardContextMapFilterWidget extends BaseFilterWidget {

  private static final Log LOG = LogFactory.getLog(StandardContextMapFilterWidget.class);

  protected Map<Class<?>, Object> contexts = new HashMap<Class<?>, Object>();

  @Override
  protected void init() throws Exception {
    getChildWidget()._getComponent().init(getScope(), new StandardEnvironment(getEnvironment(), this.contexts));

    if (LOG.isDebugEnabled()) {
      LOG.debug("Following contexts were added to environment: " + this.contexts);
    }
  }

  /**
   * Sets the environment context entries that will be made accessible to child components. The keys are String objects
   * that must resolve to class names. The keys may have suffix ".class" (but don't have to). The values are objects
   * that must be either the same type as the class resolved by the key, or a sub-type of it.
   * <p>
   * Examples of valid map entries:
   * <ul>
   * <li>&lt;entry key="java.lang.Object" value="Plain text" /&gt;
   * <li>&lt;entry key="java.lang.String.class" value="Another plain text string" /&gt;
   * <li>&lt;entry key="java.lang.Runnable" value-ref="myRunnableBean" /&gt;
   * </ul>
   * 
   * @param contexts A map of entries to be passed to the child components as environment entries. Keys are strings of
   *          full class names and corresponding value objects must be assignable to classes resolved from key.
   * @see Environment
   */
  public void setContexts(Map<String, Object> contexts) {
    if (contexts != null) {
      for (Map.Entry<String, Object> entry : contexts.entrySet()) {
        String key = entry.getKey();

        if (key.endsWith(".class")) {
          key = StringUtils.substringBeforeLast(key, ".");
        }

        Class<?> resolvedClass = null;
        try {
          resolvedClass = Class.forName(key);
        } catch (ClassNotFoundException e) {
          ExceptionUtil.uncheckException("Could not find the matching class for key '" + key + "'.", e);
        }

        Assert.isInstanceOf(resolvedClass, entry.getValue(), "The value, where entry key is '" + key
            + "', must be a sub-type of the class specified by key.");

        this.contexts.put(resolvedClass, entry.getValue());
      }
    }
  }
}
