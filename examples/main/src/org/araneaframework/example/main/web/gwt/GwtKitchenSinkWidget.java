package org.araneaframework.example.main.web.gwt;

import org.araneaframework.integration.gwt.GwtWidget;
import org.araneaframework.uilib.core.BaseUIWidget;

public class GwtKitchenSinkWidget extends BaseUIWidget {

  private static final long serialVersionUID = 1L;

  protected void init() throws Exception {
    setViewSelector("gwt/gwtKitchenSink");
    addWidget("kitchen", new GwtWidget("org.araneaframework.example.gwt.kitchensink.KitchenSink"));
  }

  public void handleEventStart() throws Exception {
    getFlowCtx().start(new OtherWidget(), null, null);
  }

}
