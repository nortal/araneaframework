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

package org.araneaframework.uilib.core;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface MenuContext {
  public static final String MENU_VIEWDATA_KEY = "menu";
  public static final String MENU_SELECT_EVENT_KEY = "menuSelect";

  /**
   * Selects (activates) the requested menu item.
   * @param menuItemPath
   */
  public void selectMenuItem(String menuItemPath) throws Exception;

  public MenuItem getMenu() throws Exception;
  public void setMenu(MenuItem menu) throws Exception;
}
