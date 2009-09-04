
package org.araneaframework.example.main.release.demos;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.araneaframework.Widget;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.menu.MenuWidget;
import org.araneaframework.uilib.core.MenuItem;
import org.araneaframework.uilib.tab.TabContainerWidget;
import org.araneaframework.uilib.util.UilibEnvironmentUtil;

/**
 * Demonstrates usage of tabs &mdash; {@link TabContainerWidget}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoNewTabWidget extends TemplateBaseWidget {

  private static final long serialVersionUID = 1L;

  protected void init() throws Exception {
    setViewSelector("release/demos/tab");
    addTabContainer();
  }

  // Whole method is a hack to determine menu content and show it in different tabs.
  private TabContainerWidget addTabContainer() throws IllegalAccessException,
      InstantiationException {
    TabContainerWidget containerWidget = new TabContainerWidget();

    // The tab container must be initialized before tabs will be defined:
    addWidget("tabContainer", containerWidget);

    MenuWidget menu = (MenuWidget) UilibEnvironmentUtil.getMenuContext(getEnvironment());
    Map araneaDemos = menu.getAraneaMenu().getSubMenu();

    // Let's add tabs that will be the menu elements of this menu branch:
    for (Iterator i = araneaDemos.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      MenuItem menuItem = ((MenuItem) entry.getValue());

      Field classfield = (Field) CollectionUtils.find(
          Arrays.asList(menuItem.getClass().getDeclaredFields()),
          new BeanPropertyValueEqualsPredicate("name", "flowClass"));

      if (classfield == null) {
        continue;
      }

      classfield.setAccessible(true);

      Class clazz = (Class) classfield.get(menuItem);
      containerWidget.addTab((String) entry.getKey(), menuItem.getLabel(),
          (Widget) clazz.newInstance());

      // show tab for current widget too, if it was found from menu, but disable it
      if (this.getClass().equals(clazz)) {
        containerWidget.disableTab((String) entry.getKey());
      }
    }

    return containerWidget;
  }
}
