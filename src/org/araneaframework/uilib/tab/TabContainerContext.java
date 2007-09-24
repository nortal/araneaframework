package org.araneaframework.uilib.tab;

import java.io.Serializable;
import java.util.Map;

public interface TabContainerContext extends Serializable {
	TabWidget selectTab(String id);
	TabWidget getSelectedTab();
	
	boolean isTabSelected(String id);
	
	Map getTabs();
}
