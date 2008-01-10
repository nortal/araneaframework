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
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public abstract class UilibEnvironmentUtil extends EnvironmentUtil {
	public static ConfigurationContext getConfigurationContext(Environment env) {
		return (ConfigurationContext) env.getEntry(ConfigurationContext.class);
	}

	public static FormContext getFormContext(Environment env) {
		return (FormContext) env.getEntry(FormContext.class);
	}
	
	public static TreeContext getTreeContext(Environment env) {
		return (TreeContext) env.getEntry(TreeContext.class);
	}
	
	public static TreeNodeContext getTreeNodeContext(Environment env) {
		return (TreeNodeContext) env.getEntry(TreeNodeContext.class);
	}
	
	public static MenuContext getMenuContext(Environment env) {
		return (MenuContext) env.getEntry(MenuContext.class);
	}
}
