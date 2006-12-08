package org.araneaframework.example.main.web.gwt;

import org.araneaframework.uilib.core.BaseUIWidget;

public class OtherWidget extends BaseUIWidget {

  private static final long serialVersionUID = 1L;

  protected void init() throws Exception {
    super.init();
    setViewSelector("gwt/other");
  }

  public void handleEventFinish() throws Exception {
    getFlowCtx().finish(null);
  }

}
