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

package org.araneaframework.template.framework;

import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.InputData;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardEventListener;
import org.araneaframework.framework.container.ExceptionHandlingFlowContainerWidget;
import org.araneaframework.uilib.core.MenuItem;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public abstract class TemplateMenuWidget extends ExceptionHandlingFlowContainerWidget implements TemplateMenuContext {
	public static final String MENU_VIEWDATA_KEY = "menu";
	public static final String MENU_SELECT_EVENT_KEY = "menuSelect";
	
	protected MenuItem menu;  
	
	// CONSTRUCTOR 
	public TemplateMenuWidget(Widget topWidget) throws Exception {
		super(topWidget);
		
		menu = buildMenu();
		addEventListener(TemplateMenuWidget.MENU_SELECT_EVENT_KEY, new ItemSelectionHandler());
		putViewData(TemplateMenuWidget.MENU_VIEWDATA_KEY, menu);
	}
	
	// MENU SELECTION LISTENER
	private class ItemSelectionHandler extends StandardEventListener {
		public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
			TemplateMenuWidget.this.selectMenuItem(eventParam);
		}
	}

	public void selectMenuItem(String menuItemPath) throws Exception {
		final Widget newFlow = menu.selectMenuItem(menuItemPath);
    
    reset(new EnvironmentAwareCallback() {
      public void call(org.araneaframework.Environment env) throws Exception {
        if (newFlow != null)
          start(newFlow, null, null);
      }
    });
	}

	/**
	 * Method that must be implemented to build the menu.
	 * @return built menu.
	 */
	protected abstract MenuItem buildMenu() throws Exception;
}
