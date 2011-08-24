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
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.collections.Closure;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Scope;
import org.araneaframework.Widget;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.WidgetFactory;
import org.araneaframework.core.event.StandardEventListener;
import org.araneaframework.core.util.Assert;

/**
 * This class represents a UI widget that contains tabs ({@link TabWidget})s. Only one tab can be selected (active) at a
 * time, such tab is specified with {@link #selectTab(String)}. When on creation the selected tab is not specified, the
 * first tab is marked as selected. Tabs are added with {@link #addTab(String, String, Widget)}, removed with
 * {@link #removeTab(String)} and disabled (user cannot select them) with {@link #disableTab(String)}. By default tabs
 * preserve the addition order and are also presented in that order. When this {@link TabContainerWidget} has a
 * {@link Comparator} set, it will sort and present the tabs in an order specified by that {@link Comparator}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class TabContainerWidget extends BaseApplicationWidget implements TabContainerContext, TabRegistrationContext {

  private static final Log LOG = LogFactory.getLog(TabContainerWidget.class);

  public static final String TAB_SELECT_EVENT_ID = "activateTab";

  protected Map<String, TabWidget> tabs;

  protected TabWidget selected;

  protected String defaultSelectedTabId;

  protected TabSwitchListener tabSwitchListener = new DefaultTabSwitchListener();

  /**
   * This is just to make sure that we do not initialize ANY tabs after destroying process has already begun.
   */
  protected transient boolean dying = false;

  @Override
  protected Environment getChildWidgetEnvironment() throws Exception {
    Map<Class<?>, Object> entries = new LinkedHashMap<Class<?>, Object>(2);
    entries.put(TabContainerContext.class, this);
    entries.put(TabRegistrationContext.class, this);
    return new StandardEnvironment(super.getChildWidgetEnvironment(), entries);
  }

  protected void selectFirst() {
    if (!this.tabs.isEmpty()) {
      Map.Entry<String, TabWidget> first = this.tabs.entrySet().iterator().next();
      selectTab(first.getKey());
    }
  }

  // ===========================================================================
  // TabContainerContext IMPL
  // ===========================================================================
  public TabContainerWidget() {
    this.tabs = new LinkedHashMap<String, TabWidget>();
  }

  public TabContainerWidget(Comparator<String> comparator) {
    this.tabs = new TreeMap<String, TabWidget>(comparator);
  }

  public void addTab(String id, String labelId, Widget contentWidget) {
    addWidget(id, new TabWidget(labelId, contentWidget));
  }

  public void addTab(String id, String labelId, WidgetFactory contentWidgetFactory) {
    addWidget(id, new TabWidget(labelId, contentWidgetFactory));
  }

  public void addTab(String id, Widget labelWidget, Widget contentWidget) {
    addWidget(id, new TabWidget(labelWidget, contentWidget));
  }

  public void addTab(String id, Widget labelWidget, WidgetFactory contentWidgetFactory) {
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
    TabWidget target = StringUtils.isEmpty(id) ? null : this.tabs.get(id);
    Closure switchClosure = new TabSwitchClosure(target);

    if (target != null && this.tabSwitchListener.onSwitch(this.selected, target, switchClosure)) {
      switchClosure.execute(target);
    }

    return target != null;
  }

  public boolean isTabSelected(String id) {
    Assert.notNullParam(this, id, "id");
    return this.selected != null ? id.equals(this.selected.getScope().getId()) : false;
  }

  public TabContext getSelectedTab() {
    return this.selected;
  }

  public Map<String, TabWidget> getTabs() {
    return Collections.unmodifiableMap(this.tabs);
  }

  /**
   * Provides the current default ID of a tab that will be automatically selected once added. A <code>null</code> value
   * means that no default ID is used.
   * 
   * @return The ID of the tab that will be automatically selected.
   * @since 1.2.1
   */
  public String getDefaultSelectedTabId() {
    return this.defaultSelectedTabId;
  }

  /**
   * Sets a default ID of a tab that will be automatically selected once added. A <code>null</code> value means that no
   * default ID is used. Should be called before tabs are added.
   * 
   * @param defaultSelectedTabId
   * @see #selectTab(String)
   * @since 1.2.1
   */
  public void setDefaultSelectedTabId(String defaultSelectedTabId) {
    this.defaultSelectedTabId = defaultSelectedTabId;
  }

  public void setTabSwitchListener(TabSwitchListener tabSwitchListener) {
    Assert.notNullParam(this, tabSwitchListener, "tabSwitchListener");
    this.tabSwitchListener = tabSwitchListener;
  }

  public TabSwitchListener getTabSwitchListener() {
    return this.tabSwitchListener;
  }

  /*****************************************************************************
   * TabRegistrationContext IMPL
   */
  /**
   * @see org.araneaframework.uilib.tab.TabRegistrationContext#registerTab(org.araneaframework.uilib.tab.TabWidget)
   */
  public TabWidget registerTab(TabWidget tabWidget) {
    String id = tabWidget.getScope().getId().toString();
    TabWidget result = this.tabs.put(id, tabWidget);

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
    TabWidget result = this.tabs.remove(id);
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
   */
  protected class SelectionEventListener extends StandardEventListener {

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) {
      if (LOG.isTraceEnabled()) {
        LOG.trace(ClassUtils.getShortClassName(TabContainerWidget.class) + " received tab selection event for tab '"
            + eventParam + "'.");
      }
      selectTab(eventParam);
    }
  }

  /*****************************************************************************
   * Overrides for disableWidget()/enableWidget()
   */

  @Override
  public void disableWidget(String key) {
    if (!this.tabs.containsKey(key)) {
      super.disableWidget(key);
      return;
    }
    this.tabs.get(key).disableTab();
  }

  @Override
  public void enableWidget(String key) {
    if (!this.tabs.containsKey(key)) {
      super.enableWidget(key);
      return;
    }
    this.tabs.get(key).enableTab();
  }

  /* ****************** COMPONENT LIFECYCLE METHODS ************************** */
  @Override
  public Component.Interface _getComponent() {
    return new ComponentImpl();
  }

  protected class ComponentImpl extends BaseApplicationWidget.ComponentImpl {

    @Override
    public synchronized void init(Scope scope, Environment env) {
      super.init(scope, env);
      addEventListener(TAB_SELECT_EVENT_ID, new SelectionEventListener());
    }

    @Override
    public void destroy() {
      TabContainerWidget.this.dying = true;
      super.destroy();
    }
  }

  /**
   * This closure handles tab switching of this instance of {@link TabContainerWidget}. It can be executed only once
   * (per instance).
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   * @see TabContainerContext.TabSwitchListener
   * @since 1.2.2
   */
  public class TabSwitchClosure implements Closure, Serializable {

    private final TabWidget newSelectedTab;

    private boolean executed;

    public TabSwitchClosure(TabWidget newSelectedTab) {
      Assert.notNullParam(this, newSelectedTab, "newSelectedTab");
      this.newSelectedTab = newSelectedTab;
    }

    public void execute(Object obj) {
      if (!this.executed) {
        if (TabContainerWidget.this.selected != null) {
          TabContainerWidget.this.selected.deleselectTab();
        }

        TabContainerWidget.this.selected = this.newSelectedTab;
        TabContainerWidget.this.selected.enableTab();

        this.executed = true;
      }
    }
  }

}
