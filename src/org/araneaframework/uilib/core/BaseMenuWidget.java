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

import org.apache.commons.lang.StringUtils;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.event.StandardEventListener;
import org.araneaframework.core.exception.AraneaRuntimeException;
import org.araneaframework.core.message.RoutedMessage;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.MountContext;
import org.araneaframework.framework.container.ExceptionHandlingFlowContainerWidget;

/**
 * The base implementation of the menu context that handles a menu. All custom menu contexts should extend it, and
 * describe the menu structure in {@link #buildMenu()}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
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
   * An optional ID to be used with {@link MountContext}, so that requests coming to
   * [app_url]/[menuMountContextId]/[menuItemId] would be translated to menu invocations where menu item with ID of
   * [menuItemId] would be opened. When left to <code>null</code> then menu widget full path will be used instead for
   * registering mount-context listener.
   * 
   * @since 2.0
   */
  private String menuMountContextId;

  /**
   * Constructor that initializes the menu widget and sets the <code>topWidget</code> as its parent. Actual menu is
   * initialized in {@link #init()}.
   * 
   * @param topWidget The parent widget.
   * @throws Exception Any non-specific runtime exception that may occur.
   */
  public BaseMenuWidget(Widget topWidget) throws Exception {
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
    // Create menu.
    this.menu = buildMenu();
    addEventListener(MenuContext.MENU_SELECT_EVENT_KEY, new ItemSelectionListener());
    initMenuSelectorMountSupport();
    putViewData(MenuContext.MENU_VIEWDATA_KEY, this.menu);

    super.init();
    setFinishable(false);
  }

  @Override
  protected Environment getChildWidgetEnvironment() throws Exception {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), MenuContext.class, this);
  }

  /**
   * Initializes the menu selector that works with bookmarks.
   * 
   * @since 1.1.1
   */
  protected void initMenuSelectorMountSupport() {
    MountContext mc = getEnvironment().getEntry(MountContext.class);
    if (mc == null) {
      return;
    }

    if (this.menuMountContextId == null) {
      this.menuMountContextId = "/" + getScope() + "/";
    }

    mc.mount(getInputData(), this.menuMountContextId, new MountContext.MessageFactory() {

      public Message buildMessage(String url, final String suffix, InputData input, OutputData output) {
        return new RoutedMessage(getScope().toPath()) {

          @Override
          protected void execute(Component component) throws Exception {
            ((BaseMenuWidget) component).selectMenuItem(suffix);
          }
        };
      }
    });
  }

  public void selectMenuItem(String menuItemPath) throws Exception {
    this.selectionPath = null;
    final Widget newFlow = this.menu.selectMenuItem(menuItemPath);
    this.selectionPath = menuItemPath;

    reset(new EnvironmentAwareCallback() {

      public void call(Environment env) {
        if (newFlow != null) {
          start(newFlow);
        }
      }
    });
  }

  /**
   * Method that must be implemented to build the menu.
   * 
   * @return built menu.
   * @exception Exception Any non-specific runtime exception that may occur.
   */
  protected abstract MenuItem buildMenu() throws Exception;

  /**
   * Sub classes can implement this method to do some general work here after a menu item has been selected.
   * 
   * @since 2.0
   */
  protected void onMenuItemSelection() {
  }

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

  /**
   * Sets an optional ID to be used with {@link MountContext}, so that requests coming to
   * [app_url]/[menuMountContextId]/[menuItemId] would be translated to menu invocations where menu item with ID of
   * [menuItemId] would be opened. When left to <code>null</code> then menu widget full path will be used instead for
   * registering mount-context listener.
   * 
   * @param menuMountContextId The mount-context ID to be used for registering menu mount-context support.
   * @since 2.0
   * @see #initMenuSelectorMountSupport()
   */
  public void setMenuMountContextId(String menuMountContextId) {
    if (menuMountContextId != null && StringUtils.isBlank(menuMountContextId)) {
      throw new AraneaRuntimeException("The provided menu mount-context listener ID is blank!");
    }
    this.menuMountContextId = menuMountContextId;
  }

  /**
   * Provides the ID that will be or is already used with {@link MountContext}, so that requests coming to
   * [app_url]/[menuMountContextId]/[menuItemId] would be translated to menu invocations where menu item with ID of
   * [menuItemId] would be opened.
   * 
   * @return The mount-context ID used for registering menu mount-context support.
   * @since 2.0
   * @see #setMenuMountContextId(String)
   * @see #initMenuSelectorMountSupport()
   */
  public String getMenuMountContextId() {
    return this.menuMountContextId;
  }

  /**
   * Menu selection listener.
   */
  protected class ItemSelectionListener extends StandardEventListener {

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) {
      try {
        selectMenuItem(eventParam);
        onMenuItemSelection();
      } catch (Exception e) {
        ExceptionUtil.uncheckException(e);
      }
    }
  }

}
