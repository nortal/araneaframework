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

package org.araneaframework.uilib.core;

/**
 * This interface declares the required methods for menu context that handles a menu. Custom menu contexts should extend
 * the <code>BaseMenuWidget</code>.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @see BaseMenuWidget
 */
public interface MenuContext {

  /**
   * The <code>viewData</code> key that is used for the menu.
   */
  String MENU_VIEWDATA_KEY = "menu";

  /**
   * The event name for the menu selection listener.
   */
  String MENU_SELECT_EVENT_KEY = "menuSelect";

  /**
   * Selects (activates) the requested menu item.
   * 
   * @param menuItemPath The full path of the menu item.
   * @exception Exception Any non-specific exception that may occur.
   */
  void selectMenuItem(String menuItemPath) throws Exception;

  /**
   * Provides access to the (full) menu.
   * 
   * @return the menu.
   * @throws Exception Any non-specific runtime exception.
   */
  MenuItem getMenu() throws Exception;

  /**
   * Specifies the menu to use and show.
   * 
   * @param menu the new menu
   * @throws Exception Any non-specific runtime exception.
   */
  void setMenu(MenuItem menu) throws Exception;
}
