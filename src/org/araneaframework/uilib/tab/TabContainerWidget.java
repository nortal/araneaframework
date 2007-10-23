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
 * Only one tab can be selected (active) at a time, such tab is specified with {@link #selectTab(String)}.
 * When on creation the selected tab is not specified, the first tab is marked as selected.
 *
 * Tabs are added with {@link #addWidget(Object, org.araneaframework.Widget)}, removed
 * with {@link #removeWidget(Object)} and disabled (user cannot select them) with {@link #disableWidget(Object)}.
 * Note that only added {@link TabWidget}s are actually treated (and rendered by Aranea JSP tags) as tabs, 
 * other children are accepted but the developer needs to handle their rendering.
 * 
 * By default tabs preserve the addition order and are also presented in that order. When this {@link TabContainerWidget}
 * has a {@link Comparator} set, it will sort and present the tabs in an order specified by that {@link Comparator}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class TabContainerWidget extends BaseApplicationWidget implements TabContainerContext, TabRegistrationContext {
	private static final Log log = LogFactory.getLog(TabContainerWidget.class);
	
	public static final String TAB_SELECT_EVENT_ID = "activateTab";

	protected Map tabs;
	protected TabWidget selected;

	/** This is just to make sure that we do not initialize ANY tabs after destroying process has already begun. */
	protected transient boolean dying = false;

	protected Environment getChildWidgetEnvironment() throws Exception {
		Map entries = new LinkedMap(2);
		entries.put(TabContainerContext.class, this);
		entries.put(TabRegistrationContext.class, this);
		return new StandardEnvironment(super.getChildWidgetEnvironment(), entries);
	}

	protected void selectFirst() {
		if (!tabs.isEmpty()) {
			Map.Entry first = (Map.Entry) tabs.entrySet().iterator().next();
			selectTab(first.getKey().toString());
		}
	}

	/* ********************************************
	 * TabContainerContext IMPL
	 **********************************************/
	public TabContainerWidget() {
		tabs = new LinkedMap();
	}
	
	public TabContainerWidget(Comparator comparator) {
		tabs = new TreeMap(comparator);
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
		return tabs.containsKey(id);
	}

	public boolean enableTab(String id) {
		enableWidget(id);
		return tabs.containsKey(id);
	}

	public boolean removeTab(String id) {
		boolean result = tabs.containsKey(id);
		removeWidget(id);
		return result;
	}

	public boolean selectTab(String id) {
		if (selected != null) {
			selected.deleselectTab();
		}
		
		if (!StringUtils.isEmpty(id)) {
			selected = (TabWidget) tabs.get(id);
			selected.enableTab();
		} else
			selected = null;

		return selected != null;
	}

	public boolean isTabSelected(String id) {
		Assert.notNullParam(this, id, "id");
		if (selected == null)
			return false;

		return id.equals(selected.getScope().getId());
	}

	public Widget getSelectedTab() {
		return selected;
	}

	public Map getTabs() {
		return Collections.unmodifiableMap(tabs);
	}
	/* ********************************************
	 * TabRegistrationContext IMPL
	 **********************************************/
	/**
	 * @see org.araneaframework.uilib.newtab.TabRegistrationContext#registerTab(org.araneaframework.uilib.newtab.TabWidget)
	 */
	public TabWidget registerTab(TabWidget tabWidget) {
		boolean first = tabs.isEmpty();
		TabWidget result = (TabWidget) tabs.put(tabWidget.getScope().getId().toString(), tabWidget);
		if (first && !dying) selectFirst();

		return result;
	}
	
	/**
	 * @see org.araneaframework.uilib.newtab.TabRegistrationContext#unregisterTab(org.araneaframework.uilib.newtab.TabWidget)
	 */
	public TabWidget unregisterTab(TabWidget tabWidget) {
		TabWidget result = (TabWidget) tabs.remove(tabWidget.getScope().getId().toString());
		if (result == selected) {
			selected = null;
			if (!dying) selectFirst();
		}

		return result;
	}

	/****************************************************
	 * Tab selection listener. 
	 ****************************************************/
	protected class SelectionEventListener extends StandardEventListener {
		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
			if (log.isTraceEnabled()) {
				log.trace(ClassUtils.getShortClassName(TabContainerWidget.class) + " received tab selection event for tab '" + eventParam + "'.");
			}
			selectTab(eventParam);
		}
	}

	/* ********************************************
	 * Overrides for disableWidget()/enableWidget()
	 **********************************************/
	public void disableWidget(Object key) {
		if (!tabs.containsKey(key)) {
			super.disableWidget(key);
			return;
		}
		TabWidget tabWidget = (TabWidget) tabs.get(key);
		tabWidget.disableTab();
	}

	public void enableWidget(Object key) {
		if (!tabs.containsKey(key)) {
			super.enableWidget(key);
			return;
		}

		TabWidget tabWidget = (TabWidget) tabs.get(key);
		tabWidget.enableTab();
	}
	/* ****************** COMPONENT LIFECYCLE METHODS ************************** */
	public Component.Interface _getComponent() {
		return new ComponentImpl();
	}

	protected class ComponentImpl extends BaseApplicationWidget.ComponentImpl {
		public synchronized void init(Scope scope, Environment env) {
			super.init(scope, env);
			addEventListener(TAB_SELECT_EVENT_ID, new SelectionEventListener());
		}

		public void destroy() {
			dying = true;
			super.destroy();
		}
	}
}
