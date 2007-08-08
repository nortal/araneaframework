/**
* Copyright 2006 Webmedia Group Ltd.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
**/
package org.araneaframework.uilib.tab;

import org.apache.commons.collections.map.LinkedMap;
import org.araneaframework.Widget;

/**
 * Interfaces representing some set of tabs, which are displayed to the user.
 * User can select one tab at time. Each tab is responsible for a creation of a widget, that should be 
 * displayed when that tab is selected.
 *  
 * @author Nikita Salnikov-Tarnovski (<a href="mailto:nikem@webmedia.ee">nikem@webmedia.ee</a>)
 *
 */
public interface TabContext {

  /**
   * Selects tab with given id. If <code>id</code> is <code>null</code>, then deselects all tabs.
   * @throws IllegalArgumentException if there is now tab with given id.
   * @return selected tab, or <code>null</code> if <code>id == null</code> was passed as the argument.
   */
  Tab selectTab(String id);
  
  /**
   * Returns the currently selected tab, or <code>null</code>
   */
  Tab getSelectedTab();
  
  /**
   * Returns the currently selected widget, or <code>null</code> 
   */
  Widget getSelectedWidget();
  
  Tab removeTab(String id);

  /**
   * Adds new tab with the given id to this tab container. If there were the tab
   * with the same id already, then <code>IllegalArgumentException</code> is thrown
   */
  void addTab(Tab tab);
  
  /**
   * Returns a copy of map containing all tabs in this container.
   */
  LinkedMap getTabs();
}
