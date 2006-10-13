package example;

import org.araneaframework.framework.container.StandardFlowContainerWidget;
import org.araneaframework.uilib.core.BaseUIWidget;

public class RootWidget extends BaseUIWidget {
  protected void init() throws Exception {        
    setViewSelector("root");
    addWidget("flowContainer1", new StandardFlowContainerWidget(new NameWidget()));
    addWidget("flowContainer2", new StandardFlowContainerWidget(new NameWidget()));
    addWidget("flowContainer3", new StandardFlowContainerWidget(new NameWidget()));
  }
}