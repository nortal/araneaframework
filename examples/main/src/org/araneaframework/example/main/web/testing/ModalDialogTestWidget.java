package org.araneaframework.example.main.web.testing;

import org.araneaframework.uilib.util.UilibEnvironmentUtil;
import org.araneaframework.http.util.EnvironmentUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.ClassUtils;
import org.araneaframework.Widget;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.OverlayRootWidget;
import org.araneaframework.example.main.web.menu.MenuWidget;
import org.araneaframework.uilib.core.MenuContext;
import org.araneaframework.uilib.core.MenuItem;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.DisplayItemUtil;

public class ModalDialogTestWidget extends TemplateBaseWidget {

  private static final long serialVersionUID = 1L;

  private FormWidget form;
  private List menuitems = new ArrayList();
  
  protected void init() throws Exception {
    setViewSelector("testing/modalDialogTest");
    MenuWidget menuWidget = (MenuWidget) UilibEnvironmentUtil.getMenuContext(getEnvironment());
    new MenuItemCollector().visit(menuWidget.getMenu(), menuitems);
    
    form = new FormWidget();
    SelectControl select = new SelectControl();
    select.addItem(new DisplayItem(null, t("select.choose")));
    select.addOnChangeEventListener(new OnChangeEventListener() {

      private static final long serialVersionUID = 1L;

      public void onChange() throws Exception {
        if (form.getElement("classSelect").convertAndValidate()) {
          String className = (String) form.getValueByFullName("classSelect");
          
          Widget newInstance = (Widget) org.springframework.util.ClassUtils.forName(className).newInstance();
          Widget testWidget = new OverlayRootWidget(new WrapperWidget(newInstance));

          getOverlayCtx().start(testWidget);
        }
      }
    });
    
    DisplayItemUtil.addItemsFromBeanCollection(select, menuitems, new ValueTransformer(), new DSTransformer());

    form.addElement("classSelect", "#Select your poison", select, new StringData(), false);
    addWidget("form", form);
  }

  public void handleEventReturn() {
    getFlowCtx().cancel();
  }

  private static class DSTransformer implements Transformer {
    public Object transform(Object o) {
      return ClassUtils.getShortClassName(((Class)o).getName());
    }
  }

  private static class ValueTransformer implements Transformer {
    public Object transform(Object o) {
      return ((Class)o).getName();
    }
  }
  
  private static class WrapperWidget extends TemplateBaseWidget {

    private static final long serialVersionUID = 1L;

    private Widget wrapped;
    
    public WrapperWidget(Widget wrapped) {
      this.wrapped= wrapped;
    }

    protected void init() throws Exception {
      setViewSelector("testing/renderChild");
      addWidget("wrapped", wrapped);
    }

    public void handleEventGobacknow() {
      getFlowCtx().cancel();
    }
  }

  private class MenuItemCollector {
    public void visit(MenuItem menu, List itemList) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
      Field classfld = menu.getClass().getDeclaredField("flowClass");
      
      if (classfld != null) {
        classfld.setAccessible(true);
        Class clazz = (Class) classfld.get(menu);
        if (clazz != null)
          itemList.add(clazz);
      }

      Map subMenu = menu.getSubMenu();
      
      if (subMenu == null) return;
      
      for (Iterator i = subMenu.entrySet().iterator(); i.hasNext(); ) {
        Map.Entry entry = (Map.Entry) i.next();
        MenuItem menuItem = ((MenuItem)entry.getValue());
        new MenuItemCollector().visit(menuItem, itemList);
      }
    }
  }
}
