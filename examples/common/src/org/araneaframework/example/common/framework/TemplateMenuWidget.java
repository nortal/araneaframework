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

package org.araneaframework.example.common.framework;

import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.InputData;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardEventListener;
import org.araneaframework.framework.container.ExceptionHandlingFlowContainerWidget;
import org.araneaframework.uilib.core.MenuItem;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class TemplateMenuWidget extends ExceptionHandlingFlowContainerWidget implements TemplateMenuContext {
  protected MenuItem menu;

  // CONSTRUCTOR 
  public TemplateMenuWidget(Widget topWidget) throws Exception {
    super(topWidget);
    
    menu = buildMenu();
    addEventListener(TemplateMenuContext.MENU_SELECT_EVENT_KEY, new ItemSelectionHandler());
    putViewData(TemplateMenuContext.MENU_VIEWDATA_KEY, menu);
  }
  
  protected Environment getChildWidgetEnvironment() throws Exception {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), TemplateMenuContext.class, this);
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
  
  // returns the name of currently running flow class, 
  // so that its source could be located and shown to user
  public String getFlowClassName() {
    String result = null;

    try {
      result = ((CallFrame) callStack.getFirst()).getWidget().getClass().getName();
    } catch (Exception e) {}

    return result;
  }

  // returns the name of currently running flow's view selector, 
  // so that its source could be located and shown to user
  public String getFlowViewSelector() {
    String result = null;

    try {
      result = ((ViewSelectorAware) ((CallFrame) callStack.getFirst()).getWidget()).getViewSelector();
    } catch (Exception e) {}

    return result;
  }
  
  /**
   * Method that must be implemented to build the menu.
   * @return built menu.
   */
  protected abstract MenuItem buildMenu() throws Exception;
}
