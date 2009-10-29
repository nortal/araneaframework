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

package org.araneaframework.example.main.message;

import org.araneaframework.Component;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.example.main.web.menu.MenuWidget;

/**
 * A message that searches the {@link MenuWidget} to select the given menu item.
 */
public class MenuSelectMessage extends BroadcastMessage {

  private String menuPath;

  /**
   * Constructs a new message to select menu item named <code>menuPath</code>.
   * 
   * @param menuPath The name of the menu item.
   */
  public MenuSelectMessage(String menuPath) {
    this.menuPath = menuPath;
  }

  /**
   * Searches the {@link MenuWidget} to select the given menu item.
   */
  protected void execute(Component component) throws Exception {
    if (component instanceof MenuWidget) {
      MenuWidget w = (MenuWidget) component;
      w.selectMenuItem(this.menuPath);
    }
  }

}
