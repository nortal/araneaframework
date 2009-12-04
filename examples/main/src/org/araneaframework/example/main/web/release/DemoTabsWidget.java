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

package org.araneaframework.example.main.web.release;

import org.araneaframework.example.main.web.MenuWidget;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.araneaframework.Widget;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.core.MenuItem;
import org.araneaframework.uilib.tab.TabContainerWidget;
import org.araneaframework.uilib.util.UilibEnvironmentUtil;

/**
 * Demonstrates usage of tabs &mdash; {@link TabContainerWidget}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoTabsWidget extends TemplateBaseWidget {

  @Override
  protected void init() throws Exception {
    setViewSelector("release/tab");
    addTabContainer();
  }

  // Whole method is a hack to determine menu content and show it in different tabs.
  private TabContainerWidget addTabContainer() throws IllegalAccessException,
      InstantiationException {
    TabContainerWidget containerWidget = new TabContainerWidget();

    // The tab container must be initialized before tabs will be defined:
    addWidget("tabContainer", containerWidget);

    MenuWidget menu = (MenuWidget) UilibEnvironmentUtil.getMenuContext(getEnvironment());
    Map<String, MenuItem> araneaDemos = menu.getAraneaMenu().getSubMenu();

    // Let's add tabs that will be the menu elements of this menu branch:
    for (Map.Entry<String, MenuItem> entry : araneaDemos.entrySet()) {
      MenuItem menuItem = entry.getValue();

      Field classfield = (Field) CollectionUtils.find(Arrays.asList(menuItem.getClass().getDeclaredFields()),
          new BeanPropertyValueEqualsPredicate("name", "flowClass"));

      if (classfield == null) {
        continue;
      }

      classfield.setAccessible(true);

      Class<?> clazz = (Class<?>) classfield.get(menuItem);
      containerWidget.addTab(entry.getKey(), menuItem.getLabel(), (Widget) clazz.newInstance());

      // show tab for current widget too, if it was found from menu, but disable it
      if (this.getClass().equals(clazz)) {
        containerWidget.disableTab(entry.getKey());
      }
    }

    return containerWidget;
  }
}
