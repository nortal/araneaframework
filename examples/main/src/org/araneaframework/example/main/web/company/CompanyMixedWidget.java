package org.araneaframework.example.main.web.company;

import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.Widget;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.core.WidgetFactory;
import org.araneaframework.framework.container.StandardFlowContainerWidget;
import org.araneaframework.uilib.core.BaseUIWidget;

public class CompanyMixedWidget extends BaseUIWidget {
  protected void init() throws Exception {
    super.init();
    
    addWidget("companyAranea", 
        new StandardFlowContainerWidget(
            new CompanyListWidget()));
    
    addWidget("companyStruts", 
        new StandardFlowContainerWidget(
            new CompanyListWidget(false, new WidgetFactory() {
      public Widget buildWidget(Environment env) {
        return new StrutsCompanyAddWidget();
      }
    })));
    
    addWidget("companyJsf", 
        new StandardFlowContainerWidget(
            new CompanyListWidget(false, new WidgetFactory() {
      public Widget buildWidget(Environment env) {
        return new JsfCompanyAddWidget();
      }
    })));
    
    setViewSelector("company/companyMixed");
  }
  
    
  protected void process() throws Exception {    
    propagate(new BroadcastMessage() {
      protected void execute(Component component) throws Exception {
        if (component instanceof CompanyListWidget) {
          ((CompanyListWidget) component).refreshList();        
        }
      }
    });
    
    super.process();  }
}
