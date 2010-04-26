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

package org.araneaframework.uilib.tab;

import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.Scope;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.WidgetFactory;
import org.araneaframework.http.util.EnvironmentUtil;

/**
 * Represents a tab managed by {@link TabContainerContext} implementation {@link TabContainerWidget}. Tab consists of
 * <i>label</i> and <i>content</i>, represented either by {@link Widget}s (for stateful tabs) or by
 * {@link WidgetFactory}ies (for stateless tabs). Difference between stateful and stateless tabs is that stateless tabs
 * forget the state when they become inactive (de-selected).
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class TabWidget extends BaseApplicationWidget implements TabContext {

  /**
   * Child key for tab's label widget.
   */
  public static final String LABEL_WIDGET_KEY = "tlw";

  /**
   * Child key for tab's content widget.
   */
  public static final String CONTENT_WIDGET_KEY = "tcw";

  protected String labelId;

  protected Widget labelWidget;

  protected Widget tabContentWidget;

  protected WidgetFactory tabContentWidgetFactory;

  protected boolean disabled;

  // CONSTRUCTORS of all kinds

  protected TabWidget(Widget tabContentWidget) {
    this.tabContentWidget = tabContentWidget;
  }

  protected TabWidget(WidgetFactory tabContentWidgetFactory) {
    this.tabContentWidgetFactory = tabContentWidgetFactory;
  }

  public TabWidget(String labelId, Widget tabContentWidget) {
    this(tabContentWidget);
    this.labelId = labelId;
  }

  public TabWidget(Widget labelWidget, Widget tabContentWidget) {
    this(tabContentWidget);
    this.labelWidget = labelWidget;
  }

  public TabWidget(String labelId, WidgetFactory tabContentWidgetFactory) {
    this(tabContentWidgetFactory);
    this.labelId = labelId;
  }

  public TabWidget(Widget labelWidget, WidgetFactory tabContentWidgetFactory) {
    this(tabContentWidgetFactory);
    this.labelWidget = labelWidget;
  }

  // Enabling/disabling/de-selecting tabs

  public void enableTab() {
    this.disabled = false;

    if (_getDisabledChildren().containsKey(CONTENT_WIDGET_KEY)) {
      enableWidget(CONTENT_WIDGET_KEY);
    } else if (!_getChildren().containsKey(CONTENT_WIDGET_KEY)) {
      if (isStateless()) {
        addWidget(CONTENT_WIDGET_KEY, this.tabContentWidgetFactory.buildWidget(getEnvironment()));
      } else {
        addWidget(CONTENT_WIDGET_KEY, this.tabContentWidget);
      }
    }
  }

  public void disableTab() {
    this.disabled = true;
    if (_getDisabledChildren().containsKey(CONTENT_WIDGET_KEY)) {
      disableWidget(CONTENT_WIDGET_KEY);
    }
  }

  public void deleselectTab() {
    if (isStateless()) {
      removeWidget(CONTENT_WIDGET_KEY);
    }
  }

  /* PUBLIC GETTERS */
  public String getLabel() {
    if (this.labelId == null) {
      return null;
    }
    return EnvironmentUtil.requireLocalizationContext(getEnvironment()).localize(this.labelId);
  }

  public Widget getLabelWidget() {
    return this.labelWidget;
  }

  public Widget getTabContentWidget() {
    return isStateless() ? getWidget(CONTENT_WIDGET_KEY) : this.tabContentWidget;
  }

  public boolean isTabDisabled() {
    return this.disabled;
  }

  public boolean isSelected() {
    return isInitialized() ? getTabContainerContext().isTabSelected(getScope().getId().toString()) : false;
  }

  public boolean isStateless() {
    return this.tabContentWidgetFactory != null;
  }

  // ****************** COMPONENT LIFECYCLE METHODS ************************** //

  @Override
  public Component.Interface _getComponent() {
    return new ComponentImpl();
  }

  protected class ComponentImpl extends BaseApplicationWidget.ComponentImpl {

    @Override
    public synchronized void init(Scope scope, Environment env) {
      super.init(scope, env);
      TabContainerContext tabContainer = getTabContainerContext();
      Assert.notNull(this, tabContainer, "TabWidget initialization failed due to TabContainerContext missing from "
          + "Environment. Make sure that TabWidget is child of TabContainerWidget");

      TabRegistrationContext tabRegistrationContext = getTabRegistrationContext();

      Assert.notNull(this, tabRegistrationContext, "TabWidget initialization failed due to TabRegistrationContext "
          + "missing from Environment. Make sure that TabWidget is child of TabRegistrationContext");

      tabRegistrationContext.registerTab(TabWidget.this);

      if (TabWidget.this.labelWidget != null) {
        addWidget(LABEL_WIDGET_KEY, TabWidget.this.labelWidget);
      }
    }
  }

  @Override
  protected void destroy() throws Exception {
    TabRegistrationContext tabRegistrationContext = getEnvironment().requireEntry(TabRegistrationContext.class);
    tabRegistrationContext.unregisterTab(TabWidget.this);
    super.destroy();
  }

  // ****************** PROTECTED METHODS ************************** //

  protected TabContainerContext getTabContainerContext() {
    return getEnvironment().getEntry(TabContainerContext.class);
  }

  protected TabRegistrationContext getTabRegistrationContext() {
    return getEnvironment().getEntry(TabRegistrationContext.class);
  }
}
