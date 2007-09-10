package org.araneaframework.example.main.release.demos;

import org.araneaframework.Widget;
import org.araneaframework.example.main.web.demo.DemoMultiSelect;
import org.araneaframework.example.main.web.misc.RedirectingWidget;
import org.araneaframework.example.main.web.sample.SimpleFormWidget;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.tab.Tab;
import org.araneaframework.uilib.tab.TabContainerWidget;
import org.araneaframework.uilib.tab.WidgetTab;

public class DemoTabWidget extends BaseUIWidget {
  public void init() {
    setViewSelector("release/demos/tab");
    
    TabContainerWidget containerWidget = new TabContainerWidget();
    WidgetTab ajaxTab = new WidgetTab("simpleform", "Simple_Form", SimpleFormWidget.class);
    ajaxTab.setTooltip("Simple_Form");
    containerWidget.addTab(ajaxTab);
    containerWidget.addTab(new WidgetTab("simpleForm", "#Simple Form Widget", SimpleFormWidget.class));
    containerWidget.addTab(new WidgetTab("redirect", "#Redirecting widget", RedirectingWidget.class));
    containerWidget.addTab(new SimpleTab("multi", "#Multi Select demo"));

    addWidget("tabContainer", containerWidget);
  }
  
  public static class SimpleTab extends Tab {
    public SimpleTab(String id, String labelId) {
      super(id, labelId);
    }

    public Widget createWidget() {
      return new DemoMultiSelect();
    }
  }
}
