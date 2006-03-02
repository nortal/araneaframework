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

package org.araneaframework.template.framework;

import java.io.Serializable;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public interface TemplateMenuContext extends Serializable {
	/**
	 * Selects (activates) the requested menu item.
	 * @param menuItemPath
	 */
	public void selectMenuItem(String menuItemPath) throws Exception;
}
