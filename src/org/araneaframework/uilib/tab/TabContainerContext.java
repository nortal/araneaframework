package org.araneaframework.uilib.tab;

import java.io.Serializable;

public interface TabContainerContext extends Serializable {
	TabWidget selectTab(String id);
	TabWidget getSelectedTab();
	
	boolean isTabSelected(String id);
}
