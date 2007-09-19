package org.araneaframework.uilib.newtab;

import org.araneaframework.InputData;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardEventListener;

public class TabContainerWidget extends BaseApplicationWidget {
	public static final String TAB_SELECT_EVENT_ID = "activateTab";
	
	protected void init() throws Exception {
		addEventListener(TAB_SELECT_EVENT_ID, new SelectionEventListener());
	}
	
	protected class SelectionEventListener extends StandardEventListener {
		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
			
		}		
	}
}
