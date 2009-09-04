/**
 * Copyright 2006-2007 Webmedia Group Ltd.
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

package org.araneaframework.uilib.util;

import org.araneaframework.Environment;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.core.MenuContext;
import org.araneaframework.uilib.form.FormContext;
import org.araneaframework.uilib.tree.TreeContext;
import org.araneaframework.uilib.tree.TreeNodeContext;

/**
 * Utility methods to retrieve contexts from the <code>Environment</code>.
 *
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public abstract class UilibEnvironmentUtil extends EnvironmentUtil {

  /**
   * Provides the <code>ConfigurationContext</code> from the
   * <code>Environment</code>.
   *
   * @param env The environment to use for lookup (required).
   * @return The <code>ConfigurationContext</code> or <code>null</code>.
   */
  public static ConfigurationContext getConfiguration(Environment env) {
    return env.getEntry(ConfigurationContext.class);
  }

  /**
   * Provides the <code>FormContext</code> from the
   * <code>Environment</code>.
   *
   * @param env The environment to use for lookup (required).
   * @return The <code>FormContext</code> or <code>null</code>.
   */
  public static FormContext getFormContext(Environment env) {
    return env.getEntry(FormContext.class);
  }

  /**
   * Provides the <code>TreeContext</code> from the
   * <code>Environment</code>.
   *
   * @param env The environment to use for lookup (required).
   * @return The <code>TreeContext</code> or <code>null</code>.
   */
  public static TreeContext getTreeContext(Environment env) {
    return env.getEntry(TreeContext.class);
  }

  /**
   * Provides the <code>TreeNodeContext</code> from the
   * <code>Environment</code>.
   *
   * @param env The environment to use for lookup (required).
   * @return The <code>TreeNodeContext</code> or <code>null</code>.
   */
  public static TreeNodeContext getTreeNodeContext(Environment env) {
    return env.getEntry(TreeNodeContext.class);
  }

  /**
   * Provides the <code>MenuContext</code> from the
   * <code>Environment</code>.
   *
   * @param env The environment to use for lookup (required).
   * @return The <code>MenuContext</code> or <code>null</code>.
   */
  public static MenuContext getMenuContext(Environment env) {
    return env.getEntry(MenuContext.class);
  }

}
