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
import org.apache.commons.lang.StringUtils;
import org.araneaframework.Environment;
import org.araneaframework.Widget;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.core.StandardEnvironment;

/**
 * This widget encapsulates one or more tabs. When one tab is selected
 * then that tab returns a widget that should be displayed. That widget is
 * added using {@link #SELECTED_TAB_KEY} identifier.
 * <p/>
 * All tabs should be added to this container prior to this widget {@link #init()} method, which means
 * prior to executing {@link #addWidget(Object, Widget)} method with this widget.
 * <p/>
 * Custom components should include this widget, not extend it.
 * 
 * @see TabContext for public API
 * 
 * @author Nikita Salnikov-Tarnovski (<a href="mailto:nikem@webmedia.ee">nikem@webmedia.ee</a>)
 *
 */
public class TabContainerWidget extends BaseApplicationWidget implements TabContext{

  public final static String SELECTED_TAB_KEY = "selected";
  private LinkedMap tabs = new LinkedMap();
  private Tab selectedTab;
  
  public Tab getSelectedTab() {
    return selectedTab;
  }
  
  public Widget getSelectedWidget() {
    return getWidget(SELECTED_TAB_KEY);
  }
  
  public Tab selectTab(String id) {
    if(StringUtils.isEmpty(id)) {
      this.selectedTab = null;
      removeWidget(SELECTED_TAB_KEY);
      return null;
    }
    
    Tab tab = (Tab) tabs.get(id);
    if(tab == null) {
      throw new IllegalArgumentException("There is no tab with id " + id);
    }
    Widget widgetToSelect = tab.createWidget();
    addWidget(SELECTED_TAB_KEY, widgetToSelect);
    this.selectedTab = tab;
    return this.selectedTab;
  }
  
  public void addTab(Tab tab) {
    if(tab == null) {
      throw new IllegalArgumentException("Cannot add null tab!");
    }
    if(tabs.containsValue(tab)) {
      throw new IllegalArgumentException("Such tab already exists!");
    }
    tabs.put(tab.getId(), tab);
  }
  
  public Tab removeTab(String id) {
    Tab removedTab = (Tab) tabs.remove(id);
    if (selectedTab != null && id.equals(selectedTab.getId())) {
      selectTab(null);
    }
    return removedTab;
  }

  protected Environment getChildWidgetEnvironment() throws Exception {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), TabContext.class, this);
  }

  protected void init() throws Exception {
    if (this.tabs.size() == 0) {
      throw new IllegalStateException("No tabs defined!");
    }
    
    selectTab((String) this.tabs.get(0));
    
    setGlobalEventListener(new ProxyEventListener(this));
  }

  protected void destroy() throws Exception {
    selectTab(null);
    super.destroy();
  }
  
  public void handleEventSelect(String tabId) {
    selectTab(tabId);
  }

  public LinkedMap getTabs() {
    return new LinkedMap(tabs);
  }
}
