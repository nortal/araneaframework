/**
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
**/

package org.araneaframework.uilib.core;

import org.araneaframework.Component;
import org.araneaframework.InputData;
import org.araneaframework.Widget;
import org.araneaframework.core.EventListener;
import org.araneaframework.core.StandardEventListener;
import org.araneaframework.framework.container.StandardFlowContainerWidget;
import org.araneaframework.uilib.MenuContext;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public abstract class StandardMenuWidget extends StandardFlowContainerWidget implements MenuContext {
	public static final String MENU_VIEWDATA_KEY = "menu";
	public static final String MENU_SELECT_EVENT_KEY = "menuSelect";
	
	protected MenuItem menu;
	protected Class topWidgetClass;
	
	// CONSTRUCTOR 
	public StandardMenuWidget(Widget topWidget) throws Exception {
		super(topWidget);
		
		topWidgetClass = topWidget.getClass();
		
		menu = buildMenu();
		addEventListener(StandardMenuWidget.MENU_SELECT_EVENT_KEY, new ItemSelectionHandler());
		putViewData(StandardMenuWidget.MENU_VIEWDATA_KEY, menu);
	}
	
	// MENU SELECTION LISTENER
	private class ItemSelectionHandler extends StandardEventListener {
		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
			StandardMenuWidget.this.selectMenuItem(eventParam);
		}
	}

	public void selectMenuItem(String menuItemPath) throws Exception {
		Component newFlow = menu.selectMenuItem(menuItemPath);
		if (newFlow == null)
			replace((Component) topWidgetClass.newInstance(), null);
		else {
			replace((Component) topWidgetClass.newInstance(), null);
			start(newFlow, null, null);
		}
	}

	/**
	 * Method that must be implemented to build the menu.
	 * @return built menu.
	 */
	protected abstract MenuItem buildMenu() throws Exception;
}
