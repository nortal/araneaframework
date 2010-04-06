/*
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
 */

package org.araneaframework.uilib.core;

import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.RoutedMessage;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardEventListener;
import org.araneaframework.framework.MountContext;
import org.araneaframework.framework.container.ExceptionHandlingFlowContainerWidget;

/**
 * The base implementation of the menu context that handles a menu. All custom menu contexts should extend it, and
 * describe the menu structure in {@link #buildMenu()}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class BaseMenuWidget extends ExceptionHandlingFlowContainerWidget implements MenuContext {

  /**
   * The container (a root) that holds the menu items.
   */
  protected MenuItem menu;

  /**
   * The path of the currently selected menu item.
   */
  private String selectionPath;

  /**
   * Constructor that initializes the menu widget and sets the <code>topWidget</code> as its parent
   * 
   * @param topWidget The parent widget.
   */
  public BaseMenuWidget(Widget topWidget) {
    super(topWidget);
  }

  /**
   * Initializes the menu. Also marks it as not finishable (it means that this widget does not invoke
   * <code>FlowContext.finish()</code> nor <code>FlowContext.cancel()</code>).
   * 
   * @exception Exception Any non-specific exception that may occur.
   */
  @Override
  protected void init() throws Exception {
    super.init();
    setFinishable(false);
    this.menu = buildMenu();
    addEventListener(MenuContext.MENU_SELECT_EVENT_KEY, new ItemSelectionListener());
    putViewData(MenuContext.MENU_VIEWDATA_KEY, menu);
  }

  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), MenuContext.class, this);
  }

  /**
   * Initializes the menu selector that works with bookmarks.
   * 
   * @since 1.1.1
   */
  protected void initMenuSelectorMountSupport() {
    MountContext mc = getEnvironment().getEntry(MountContext.class);
    if (mc == null)
      return;

    mc.mount(getInputData(), "/" + getScope() + "/", new MountContext.MessageFactory() {

      public Message buildMessage(String url, final String suffix, InputData input, OutputData output) {
        // TODO: Allow the bookmarks to work with login widget
        // int i = suffix.indexOf('/');
        // if (i == -1)
        // throw new IllegalArgumentException("URL '" + url +
        // "' should contain both menu widget identifier and menu item identifier!");
        //        
        // String menuWidgetId = suffix.substring(0, i);
        // final String menuItemId = suffix.substring(i + 1);

        return new RoutedMessage(getScope().toPath()) {

          @Override
          protected void execute(Component component) throws Exception {
            ((BaseMenuWidget) component).selectMenuItem(suffix);
          }
        };
      }
    });
  }

  /**
   * Menu selection listener.
   */
  protected class ItemSelectionListener extends StandardEventListener {

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) throws Exception {
      BaseMenuWidget.this.selectMenuItem(eventParam);
    }
  }

  public void selectMenuItem(String menuItemPath) throws Exception {
    this.selectionPath = null;
    final Widget newFlow = this.menu.selectMenuItem(menuItemPath);
    this.selectionPath = menuItemPath;

    reset(new EnvironmentAwareCallback() {

      public void call(Environment env) throws Exception {
        if (newFlow != null)
          start(newFlow);
      }
    });
  }

  /**
   * Method that must be implemented to build the menu.
   * 
   * @return built menu.
   */
  protected abstract MenuItem buildMenu();

  /**
   * Provides the {@link org.araneaframework.Path} of the currently selected menu item as a <code>String</code>.
   * 
   * @return the path of the currently selected menu item, or <code>null</code>
   * @since 1.1.1
   */
  public String getSelectionPath() {
    return this.selectionPath;
  }

  public MenuItem getMenu() {
    return this.menu;
  }

  public void setMenu(MenuItem menu) {
    this.menu = menu;
  }

  
}
