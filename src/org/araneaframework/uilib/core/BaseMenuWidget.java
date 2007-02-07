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

import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.InputData;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardEventListener;
import org.araneaframework.framework.container.ExceptionHandlingFlowContainerWidget;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class BaseMenuWidget extends ExceptionHandlingFlowContainerWidget implements MenuContext {
  protected MenuItem menu;

  // CONSTRUCTOR 
  public BaseMenuWidget(Widget topWidget) throws Exception {
    super(topWidget);
    
    menu = buildMenu();
    addEventListener(MenuContext.MENU_SELECT_EVENT_KEY, new ItemSelectionListener());
  }
  
  protected void init() throws Exception {
	super.init();
  }

  protected Environment getChildWidgetEnvironment() throws Exception {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), MenuContext.class, this);
  }
  
  // MENU SELECTION LISTENER
  protected class ItemSelectionListener extends StandardEventListener {
    public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
    	BaseMenuWidget.this.selectMenuItem(eventParam);
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
  
  public MenuItem getMenu() throws Exception {
    return menu;
  }

  public void setMenu(MenuItem menu) throws Exception {
    this.menu = menu;
  }
}
