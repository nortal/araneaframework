package org.araneaframework.uilib.newtab;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Scope;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardEventListener;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class TabContainerWidget extends BaseApplicationWidget implements TabContainerContext, TabRegistrationContext {
	private static final Log log = LogFactory.getLog(TabContainerWidget.class);
	
	public static final String TAB_SELECT_EVENT_ID = "activateTab";

	protected Map tabs = new LinkedMap();
	protected TabWidget selected;
	
	protected Environment getChildWidgetEnvironment() throws Exception {
		Map entries = new HashMap(2);
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
	public TabWidget getSelectedTab() {
		return selected;
	}

	public TabWidget selectTab(String id) {
		if (selected != null)
			selected.disableTab();
			
		if (!StringUtils.isEmpty(id)) {
			selected = (TabWidget) tabs.get(id);
			selected.enableTab();
		} else
			selected = null;

		return selected;
	}

	public boolean isTabSelected(String id) {
		Assert.notNullParam(this, id, "id");
		if (selected == null)
			return false;

		return id.equals(selected.getScope().getId());
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
		if (first)
			selectFirst();

		return result;
	}
	
	/**
	 * @see org.araneaframework.uilib.newtab.TabRegistrationContext#unregisterTab(org.araneaframework.uilib.newtab.TabWidget)
	 */
	public TabWidget unregisterTab(TabWidget tabWidget) {
		TabWidget result = (TabWidget) tabs.remove(tabWidget.getScope().getId().toString());
		if (result == selected) {
			selected = null;
			selectFirst();
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
	}
}
