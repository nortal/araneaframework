package org.araneaframework.uilib.tab;

import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.Scope;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.http.util.EnvironmentUtil;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class TabWidget extends BaseApplicationWidget {
	private static final long serialVersionUID = 1L;

	/** Child key for tab's label widget. */
	public static final String LABEL_WIDGET_KEY = "tlw";
	/** Child key for tab's content widget. */
	public static final String CONTENT_WIDGET_KEY = "tcw";

	protected String labelId;
	protected Widget labelWidget;
	protected Widget tabContentWidget;
	protected boolean disabled = false;

	protected TabWidget(Widget tabContentWidget) {
		this.tabContentWidget = tabContentWidget;
	}
	
	public TabWidget(String labelId, Widget tabContentWidget) {
		this(tabContentWidget);
		this.labelId = labelId;
	}

	public TabWidget(Widget labelWidget, Widget tabContentWidget) {
		this(tabContentWidget);
		this.labelWidget = labelWidget;
	}
	
	/* LIFECYCLE methods */
	protected void init() throws Exception {

	}

	protected void enableTab() {
		disabled = false;
		if (_getDisabledChildren().containsKey(CONTENT_WIDGET_KEY)) {
			enableWidget(CONTENT_WIDGET_KEY);
		} else if (!_getChildren().containsKey(CONTENT_WIDGET_KEY)) {
			addWidget(CONTENT_WIDGET_KEY, tabContentWidget);
		}
	}

	protected void disableTab() {
		disabled = true;
		if (_getDisabledChildren().containsKey(CONTENT_WIDGET_KEY))
			disableWidget(CONTENT_WIDGET_KEY);
	}

	/* PUBLIC GETTERS*/
	public String getLabel() {
		return EnvironmentUtil.requireLocalizationContext(getEnvironment()).localize(labelId);
	}

	public Widget getLabelWidget() {
		return labelWidget;
	}

	public Widget getTabContentWidget() {
		return tabContentWidget;
	}

	public boolean isTabDisabled() {
		return disabled;
	}
	
	public boolean isSelected() {
		if (!isInitialized())
			return false;
		return getTabContainerContext().isTabSelected(getScope().getId().toString());
	}

	/* ****************** COMPONENT LIFECYCLE METHODS ************************** */
	public Component.Interface _getComponent() {
		return new ComponentImpl();
	}

	protected class ComponentImpl extends BaseApplicationWidget.ComponentImpl {
		public synchronized void init(Scope scope, Environment env) {
			super.init(scope, env);
			TabContainerContext tabContainer = getTabContainerContext();
			Assert.notNull(this, tabContainer, "TabWidget initialization failed due to TabContainerContext missing from Environment. Make sure that TabWidget is child of TabContainerWidget");

			TabRegistrationContext tabRegistrationContext = getTabRegistrationContext();
			Assert.notNull(this, tabRegistrationContext, "TabWidget initialization failed due to TabRegistrationContext missing from Environment. Make sure that TabWidget is child of TabRegistrationContext");
			tabRegistrationContext.registerTab(TabWidget.this);
			
			if (labelWidget != null)
				addWidget(LABEL_WIDGET_KEY, labelWidget);
		}
	}

	protected void destroy() throws Exception {
		TabRegistrationContext tabRegistrationContext = (TabRegistrationContext) getEnvironment().requireEntry(TabRegistrationContext.class);
		tabRegistrationContext.unregisterTab(TabWidget.this);

		super.destroy();
	}

	/* ****************** PROTECTED METHODS ************************** */
	protected TabContainerContext getTabContainerContext() {
		return (TabContainerContext) getEnvironment().getEntry(TabContainerContext.class);
	}
	
	protected TabRegistrationContext getTabRegistrationContext() {
		return (TabRegistrationContext) getEnvironment().getEntry(TabRegistrationContext.class);
	}
}
