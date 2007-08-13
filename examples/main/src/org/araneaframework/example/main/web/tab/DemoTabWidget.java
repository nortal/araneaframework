package org.araneaframework.example.main.web.tab;

import org.araneaframework.Widget;
import org.araneaframework.example.main.web.demo.DemoMultiSelect;
import org.araneaframework.example.main.web.misc.AjaxRequestErrorWidget;
import org.araneaframework.example.main.web.misc.EventErrorWidget;
import org.araneaframework.example.main.web.misc.RedirectingWidget;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.tab.Tab;
import org.araneaframework.uilib.tab.TabContainerWidget;
import org.araneaframework.uilib.tab.WidgetTab;

public class DemoTabWidget extends BaseUIWidget {

  public void init() {
    setViewSelector("tab/tab");
    
    TabContainerWidget containerWidget = new TabContainerWidget();
    WidgetTab ajaxTab = new WidgetTab("ajax", "#Ajax error widget", AjaxRequestErrorWidget.class);
    ajaxTab.setTooltip("#Ajaxt tab Tooltip");
    containerWidget.addTab(ajaxTab);
    containerWidget.addTab(new WidgetTab("event", "#Event error widget", EventErrorWidget.class));
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
