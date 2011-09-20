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

package org.araneaframework.uilib.tab;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.collections.Closure;
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
   * Adds the stateless (content widget is destroyed when tab becomes inactive) tab with specified identifier.
   * 
   * @param id The new tab identifier.
   * @param labelId key to resource text to be used as tab label
   * @param contentWidgetFactory A widget factory for creating stateless tab content when tab becomes active.
   */
  void addTab(String id, String labelId, WidgetFactory contentWidgetFactory);

  /**
   * Adds the stateless (content widget is destroyed when tab becomes inactive) tab with specified identifier.
   * 
   * @param id The new tab identifier.
   * @param labelWidget A label widget for the tab.
   * @param contentWidgetFactory A widget factory for creating stateless tab content when tab becomes active.
   */
  void addTab(String id, Widget labelWidget, WidgetFactory contentWidgetFactory);

  /**
   * Removes tab with given identifier.
   * 
   * @param id The identifier for tab to be removed.
   * @return A Boolean that is <code>true</code> when the tab really existed and was removed.
   */
  boolean removeTab(String id);

  /**
   * Disables the tab with given ID &mdash; the label will still be shown but tab cannot be selected before the tab is
   * enabled again. All tabs are enabled, by default, when added.
   * 
   * @param id The identifier for tab to be disabled.
   * @return A Boolean that is <code>true</code> when the tab with given ID existed and was disabled.
   */
  boolean disableTab(String id);

  /**
   * Enables the tab with the ID that previously was disabled. All tabs are enabled, by default, when added.
   * 
   * @param id The identifier for tab to be enabled.
   * @return A Boolean that is <code>true</code> when the tab with given ID existed and was enabled.
   */
  boolean enableTab(String id);

  /**
   * Switches from currently selected tab to the one identified by given ID.
   * 
   * @param id The identifier for tab to be selected.
   * @return A Boolean that is <code>true</code> when the tab with given ID existed and was selected.
   */
  boolean selectTab(String id);

  /**
   * Provides whether the tab with given ID is currently active (selected).
   * 
   * @param id The identifier for tab to be checked.
   * @return A Boolean that is <code>true</code> when the tab existed and is currently active (selected).
   */
  boolean isTabSelected(String id);

  /**
   * Returns the currently selected tab widget, which is the container for the widget that was added using the
   * <code>addTab(..)</code> method.
   * 
   * @return The currently selected <code>TabContext</code>.
   */
  TabContext getSelectedTab();

  /**
   * Returns all the tabs present in this {@link TabContainerContext}. Keys in the map are tab identifiers, values are
   * implementation dependent structures that hold tab information. Returned <code>Map</code> must be unmodifiable.
   * 
   * @return A map of tabs.
   */
  Map<String, TabWidget> getTabs();

  /**
   * Sets the listener for tab switch events. There can be only one listener per tab container. The
   * <code>tabSwitchListener</code> parameter cannot be <code>null</code>. Tab switch occurs when the currently selected
   * tab changes.
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
   * An interface for tab switch listeners. Tab switch occurs when the currently selected tab changes.
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   * @since 1.2.2
   */
  interface TabSwitchListener extends Serializable {

    /**
     * A listener for tab switching. Before the selected tab will be replaced with a new one, this method is called to
     * check whether the switch is allowed. Note that the <code>selectedTab</code> parameter may be <code>null</code> if
     * no tab is currently selected.
     * <p>
     * The last parameter is a tab switch closure that is executed only when the listener returns <code>true</code> or
     * when the listener executes it itself. Therefore, this closure can also be used with
     * {@link org.araneaframework.framework.ConfirmationContext#confirm(Closure, String)}.
     * 
     * @param selectedTab The currently selected tab. May be <code>null</code>.
     * @param newTab The tab that will replace the current one.
     * @param switchClosure A closure that handles tab switch.
     * @return A Boolean that is <code>true</code>, if the switch is allowed.
     */
    boolean onSwitch(TabWidget selectedTab, TabWidget newTab, Closure switchClosure);
  }

}
