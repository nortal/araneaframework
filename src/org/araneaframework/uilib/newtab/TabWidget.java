package org.araneaframework.uilib.newtab;

import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.http.util.EnvironmentUtil;

public class TabWidget extends BaseApplicationWidget implements TabContainerContext {
	protected String labelId;
	protected Widget labelWidget;
	protected Widget tabContentWidget;
	
	protected boolean isTabDisabled = false;
	protected boolean isActive = false;
	
	protected TabWidget(Widget tabContentWidget) {
		this.tabContentWidget = tabContentWidget;
	}
	
	public TabWidget(String labelId, Widget tabContentWidget) {
		this(tabContentWidget);
		this.labelId = labelId;
	}

	public TabWidget(Widget tab, Widget tabContentWidget) {
		this(tabContentWidget);
		this.labelWidget = tab;
	}
	
	/*888888888888 LIFECYCLE 8888888888888888 */
	protected void init() throws Exception {
		Assert.notNull(getEnvironment().getEntry(TabContainerContext.class));
	}
	
	/* ******* management methods for most fields ********/
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	/* ******* getters for most fields ********/
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
		return isTabDisabled;
	}

	public boolean isActive() {
		return isActive;
	}
}
