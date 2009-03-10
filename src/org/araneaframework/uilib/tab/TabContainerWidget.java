/**
 * Copyright 2006-2007 Webmedia Group Ltd. Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.araneaframework.uilib.tab;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Scope;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardEventListener;
import org.araneaframework.core.WidgetFactory;

/**
 * This class represents a UI widget that contains tabs ({@link TabWidget})s.
 * Only one tab can be selected (active) at a time, such tab is specified with
 * {@link #selectTab(String)}. When on creation the selected tab is not
 * specified, the first tab is marked as selected. Tabs are added with
 * {@link #addTab(String, String, Widget)}, removed with
 * {@link #removeTab(String)} and disabled (user cannot select them) with
 * {@link #disableTab(String)}. By default tabs preserve the addition order and
 * are also presented in that order. When this {@link TabContainerWidget} has a
 * {@link Comparator} set, it will sort and present the tabs in an order
 * specified by that {@link Comparator}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class TabContainerWidget extends BaseApplicationWidget
  implements TabContainerContext, TabRegistrationContext {

  private static final long serialVersionUID = 1L;

  private static final Log log = LogFactory.getLog(TabContainerWidget.class);

  public static final String TAB_SELECT_EVENT_ID = "activateTab";

  protected Map tabs;

  protected TabWidget selected;

  protected String defaultSelectedTabId;

  /**
   * This is just to make sure that we do not initialize ANY tabs after
   * destroying process has already begun.
   */
  protected transient boolean dying = false;

  protected Environment getChildWidgetEnvironment() throws Exception {
    Map entries = new LinkedMap(2);
    entries.put(TabContainerContext.class, this);
    entries.put(TabRegistrationContext.class, this);
    return new StandardEnvironment(super.getChildWidgetEnvironment(), entries);
  }

  protected void selectFirst() {
    if (!this.tabs.isEmpty()) {
      Map.Entry first = (Map.Entry) this.tabs.entrySet().iterator().next();
      selectTab(first.getKey().toString());
    }
  }

  // ===========================================================================
  // TabContainerContext IMPL
  // ===========================================================================
  public TabContainerWidget() {
    this.tabs = new LinkedMap();
  }

  public TabContainerWidget(Comparator comparator) {
    this.tabs = new TreeMap(comparator);
  }

  public void addTab(String id, String labelId, Widget contentWidget) {
    addWidget(id, new TabWidget(labelId, contentWidget));
  }

  public void addTab(String id, String labelId,
      WidgetFactory contentWidgetFactory) {
    addWidget(id, new TabWidget(labelId, contentWidgetFactory));
  }

  public void addTab(String id, Widget labelWidget, Widget contentWidget) {
    addWidget(id, new TabWidget(labelWidget, contentWidget));
  }

  public void addTab(String id, Widget labelWidget,
      WidgetFactory contentWidgetFactory) {
    addWidget(id, new TabWidget(labelWidget, contentWidgetFactory));
  }

  public boolean disableTab(String id) {
    disableWidget(id);
    return this.tabs.containsKey(id);
  }

  public boolean enableTab(String id) {
    enableWidget(id);
    return this.tabs.containsKey(id);
  }

  public boolean removeTab(String id) {
    boolean result = this.tabs.containsKey(id);
    removeWidget(id);
    return result;
  }

  public boolean selectTab(String id) {
    if (this.selected != null) {
      this.selected.deleselectTab();
    }
    if (!StringUtils.isEmpty(id)) {
      this.selected = (TabWidget) this.tabs.get(id);
      this.selected.enableTab();
    } else {
      this.selected = null;
    }
    return this.selected != null;
  }

  public boolean isTabSelected(String id) {
    Assert.notNullParam(this, id, "id");
    if (this.selected == null) {
      return false;
    }
    return id.equals(this.selected.getScope().getId());
  }

  public TabContext getSelectedTab() {
    return this.selected;
  }

  public Map getTabs() {
    return Collections.unmodifiableMap(this.tabs);
  }

  /**
   * Provides the current default ID of a tab that will be automatically
   * selected once added. A <code>null</code> value means that no default ID is
   * used.
   * 
   * @return The ID of the tab that will be auomatically selected.
   * @since 1.2.1
   */
  public String getDefaultSelectedTabId() {
    return this.defaultSelectedTabId;
  }

  /**
   * Sets a default ID of a tab that will be automatically selected once added.
   * A <code>null</code> value means that no default ID is used. Should be
   * called before tabs are added.
   * 
   * @param defaultSelectedTabId
   * @see #selectTab(String)
   * @since 1.2.1
   */
  public void setDefaultSelectedTabId(String defaultSelectedTabId) {
    this.defaultSelectedTabId = defaultSelectedTabId;
  }

  /*****************************************************************************
   * TabRegistrationContext IMPL
   ****************************************************************************/
  /**
   * @see org.araneaframework.uilib.tab.TabRegistrationContext#registerTab(org.araneaframework.uilib.tab.TabWidget)
   */
  public TabWidget registerTab(TabWidget tabWidget) {
    String id = tabWidget.getScope().getId().toString();
    TabWidget result = (TabWidget) this.tabs.put(id, tabWidget);

    if (!this.dying) {
      if (this.tabs.size() == 1) {
        selectFirst();
      } else if (this.defaultSelectedTabId != null && this.tabs.containsKey(this.defaultSelectedTabId)) {
        selectTab(this.defaultSelectedTabId);
      }
    }

    return result;
  }

  /**
   * @see org.araneaframework.uilib.tab.TabRegistrationContext#unregisterTab(org.araneaframework.uilib.tab.TabWidget)
   */
  public TabWidget unregisterTab(TabWidget tabWidget) {
    String id = tabWidget.getScope().getId().toString();
    TabWidget result = (TabWidget) this.tabs.remove(id);
    if (result == this.selected) {
      this.selected = null;
      if (!this.dying) {
        if (this.defaultSelectedTabId == null) {
          selectFirst();
        } else if (this.tabs.containsKey(this.defaultSelectedTabId)) {
          selectTab(this.defaultSelectedTabId);
        }
      }
    }
    return result;
  }

  /*****************************************************************************
   * Tab selection listener.
   ****************************************************************************/
  protected class SelectionEventListener extends StandardEventListener {

    private static final long serialVersionUID = 1L;

    public void processEvent(String eventId, String eventParam, InputData input)
        throws Exception {
      if (log.isTraceEnabled()) {
        log.trace(ClassUtils.getShortClassName(TabContainerWidget.class)
            + " received tab selection event for tab '" + eventParam + "'.");
      }
      selectTab(eventParam);
    }
  }

  /*****************************************************************************
   * Overrides for disableWidget()/enableWidget()
   ****************************************************************************/
  public void disableWidget(Object key) {
    if (!this.tabs.containsKey(key)) {
      super.disableWidget(key);
      return;
    }
    TabWidget tabWidget = (TabWidget) this.tabs.get(key);
    tabWidget.disableTab();
  }

  public void enableWidget(Object key) {
    if (!this.tabs.containsKey(key)) {
      super.enableWidget(key);
      return;
    }
    TabWidget tabWidget = (TabWidget) this.tabs.get(key);
    tabWidget.enableTab();
  }

  /* ****************** COMPONENT LIFECYCLE METHODS ************************** */
  public Component.Interface _getComponent() {
    return new ComponentImpl();
  }

  protected class ComponentImpl extends BaseApplicationWidget.ComponentImpl {

    private static final long serialVersionUID = 1L;

    public synchronized void init(Scope scope, Environment env) {
      super.init(scope, env);
      addEventListener(TAB_SELECT_EVENT_ID, new SelectionEventListener());
    }

    public void destroy() {
      TabContainerWidget.this.dying = true;
      super.destroy();
    }
  }
}
