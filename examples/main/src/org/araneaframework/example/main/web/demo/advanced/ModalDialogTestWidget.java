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

package org.araneaframework.example.main.web.demo.advanced;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ClassUtils;
import org.araneaframework.Widget;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.MenuWidget;
import org.araneaframework.uilib.core.MenuItem;
import org.araneaframework.uilib.core.OverlayRootWidget;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.DefaultSelectControl;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.util.DisplayItemUtil;
import org.araneaframework.uilib.util.UilibEnvironmentUtil;

public class ModalDialogTestWidget extends TemplateBaseWidget {

  private FormWidget form;

  private final List<Class<?>> menuItems = new ArrayList<Class<?>>();

  @Override
  protected void init() throws Exception {
    setViewSelector("test/modalDialog/main");

    MenuWidget menuWidget = (MenuWidget) UilibEnvironmentUtil.getMenuContext(getEnvironment());
    new MenuItemCollector().visit(menuWidget.getMenu(), this.menuItems);

    this.form = new FormWidget();

    DefaultSelectControl select = new DefaultSelectControl();
    select.addItem(t("select.choose"), null);
    select.addOnChangeEventListener(new OnChangeEventListener() {

      public void onChange() {
        if (ModalDialogTestWidget.this.form.getElement("classSelect").convertAndValidate()) {
          try {
            String className = (String) ModalDialogTestWidget.this.form.getValueByFullName("classSelect");
            Widget newInstance = (Widget) org.springframework.util.ClassUtils.forName(className).newInstance();
            Widget testWidget = new OverlayRootWidget(new WrapperWidget(newInstance));
            getOverlayCtx().start(testWidget);
          } catch (InstantiationException e) {
            ExceptionUtil.uncheckException(e);
          } catch (IllegalAccessException e) {
            ExceptionUtil.uncheckException(e);
          } catch (ClassNotFoundException e) {
            ExceptionUtil.uncheckException(e);
          }
        }
      }
    });

    DisplayItemUtil.addItemsFromBeanCollection(select, this.menuItems, new DSTransformer(), new ValueTransformer());

    this.form.addElement("classSelect", "modalDialogTest.desc", select, new StringData(), false);
    addWidget("form", this.form);
  }

  public void handleEventReturn() {
    getFlowCtx().cancel();
  }

  private static class DSTransformer implements Transformer {

    @SuppressWarnings("unchecked")
    public Object transform(Object o) {
      return ClassUtils.getShortClassName(((Class) o).getName());
    }
  }

  private static class ValueTransformer implements Transformer {

    @SuppressWarnings("unchecked")
    public Object transform(Object o) {
      return ((Class) o).getName();
    }
  }

  private static class WrapperWidget extends TemplateBaseWidget {

    private final Widget wrapped;

    public WrapperWidget(Widget wrapped) {
      this.wrapped = wrapped;
    }

    @Override
    protected void init() throws Exception {
      setViewSelector("test/modalDialog/renderChild");
      addWidget("wrapped", this.wrapped);
    }

    @SuppressWarnings("unused")
    public void handleEventGobacknow() {
      getFlowCtx().cancel();
    }
  }

  private class MenuItemCollector {

    public void visit(MenuItem menu, List<Class<?>> itemList) throws Exception {
      Field classfld = menu.getClass().getDeclaredField("flowClass");

      if (classfld != null) {
        classfld.setAccessible(true);
        Class<?> clazz = (Class<?>) classfld.get(menu);
        if (clazz != null && !itemList.contains(clazz)) {
          itemList.add(clazz);
        }
      }

      Map<String, MenuItem> subMenu = menu.getSubMenu();

      if (subMenu != null) {
        for (MenuItem menuItem : subMenu.values()) {
          new MenuItemCollector().visit(menuItem, itemList);
        }
      }
    }
  }
}
