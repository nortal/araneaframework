package org.araneaframework.example.main.web.gwt;

import org.araneaframework.integration.gwt.GwtWidget;
import org.araneaframework.uilib.core.BaseUIWidget;

public class GwtJsonWidget extends BaseUIWidget {

  private static final long serialVersionUID = 1L;

  protected void init() throws Exception {
    setViewSelector("gwt/gwtJson");
    addWidget("json", new GwtWidget("org.araneaframework.example.gwt.json.JSON"));
  }

  public void handleEventStart() throws Exception {
    getFlowCtx().start(new OtherWidget(), null, null);
  }

}
