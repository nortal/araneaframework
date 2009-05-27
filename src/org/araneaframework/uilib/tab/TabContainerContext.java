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

package org.araneaframework.uilib.tab;

import org.araneaframework.framework.ConfirmationContext;
import org.apache.commons.collections.Closure;
import java.io.Serializable;
import java.util.Map;
import org.araneaframework.Widget;
import org.araneaframework.core.WidgetFactory;

/**
 * This context represents interface of tab management component.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public interface TabContainerContext extends Serializable {

  /**
   * Adds the stateful tab with specified identifier.
   * 
   * @param id tab identifier
   * @param labelId key to resource text to be used as tab label
   * @param contentWidget tab's content widget
   */
  void addTab(String id, String labelId, Widget contentWidget);

  /**
   * Adds the stateful tab with specified identifier.
   * 
   * @param id tab identifier
   * @param labelWidget tab's label widget
   * @param contentWidget tab's content widget
   */
  void addTab(String id, Widget labelWidget, Widget contentWidget);

  /**
   * Adds the stateless (content widget is destroyed when tab is deselected) tab
   * with specified identifier.
   * 
   * @param id tab identifier
   * @param labelId key to resource text to be used as tab label
   * @param contentWidgetFactory {@link WidgetFactory} that produces tab's
   *            contents.
   */
  void addTab(String id, String labelId, WidgetFactory contentWidgetFactory);

  /**
   * Adds the stateless (content widget is destroyed when tab is deselected) tab
   * with specified identifier.
   * 
   * @param id tab identifier
   * @param labelWidget tab's label widget
   * @param contentWidgetFactory
   */
  void addTab(String id, Widget labelWidget, WidgetFactory contentWidgetFactory);

  /**
   * Removes tab with given id.
   * @return whether the tab existed and was really removed
   */
  boolean removeTab(String id);

  /**
   * Disables the tab with given <i>id</i> &mdash; the label be shown but tab
   * cannot be selected before it is enabled again.
   * 
   * @return whether tab with given <i>id</i> existed
   */
  boolean disableTab(String id);

  /**
   * Enables the tab with the <i>id</i> that previously was disabled.
   * 
   * @return whether the tab with given <i>id</i> existed.
   */
  boolean enableTab(String id);

  /**
   * Switches selected tab to one identified by <i>id</id>.
   * 
   * @return whether tab with given <i>id</i> existed.
   */
  boolean selectTab(String id);

  /**
   * Specifies whether the specified tab is currently active (selected).
   * 
   * @return whether the specified tab is currently active (selected).
   */
  boolean isTabSelected(String id);

  /**
   * Returns the currently selected tab widget, which is the container for the
   * widget that was added using the <code>addTab(..)</code> method.
   * 
   * @return The currently selected <code>TabContext</code>.
   */
  TabContext getSelectedTab();

  /**
   * Returns all the tabs present in this {@link TabContainerContext}. Keys in
   * the map are tab identifiers, values are implementation dependent structures
   * that hold tab information. Returned <code>Map</code> must be
   * unmodifiable.
   * 
   * @return A map of tabs.
   */
  Map getTabs();

  /**
   * Sets the listener for tab switch events. There can be only one listener per
   * tab container. The <code>tabSwitchListener</code> parameter cannot be
   * <code>null</code>. Tab switch occurs when the currently
   * selected tab changes.
   * 
   * @param tabSwitchListener A listener for listening tab switch events.
   * @since 1.2.2
   */
  void setTabSwitchListener(TabSwitchListener tabSwitchListener);

  /**
   * Returns the current listener for tab switch events.
   * 
   * @return the current listener for tab switch events.
   * @since 1.2.2
   */
  TabSwitchListener getTabSwitchListener();

  /**
   * An interface for tab switch listeners. Tab switch occurs when the currently
   * selected tab changes.
   * 
   * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
   * @since 1.2.2
   */
  interface TabSwitchListener extends Serializable {

    /**
     * A listener for tab switching. Before the selected tab will be replaced
     * with a new one, this method is called to check whether the switch is
     * allowed. Note that the <code>selectedTab</code> parameter may be
     * <code>null</code> if no tab is currently selected.
     * <p>
     * The last parameter is a tab switch closure that is executed only when the
     * listener returns <code>true</code> or when the listener executes it
     * itself. Therefore, this closure can also be used with
     * {@link ConfirmationContext#confirm(Closure, String)}.
     * 
     * @param selectedTab The currently selected tab. May be <code>null</code>.
     * @param newTab The tab that will replace the current one.
     * @param switchClosure A closure that handles tab switch.
     * @return <code>true</code>, if the switch is allowed.
     */
    boolean onSwitch(TabWidget selectedTab, TabWidget newTab, Closure switchClosure);

  }

}
