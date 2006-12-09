package org.araneaframework.example.main.web.gwt;

import org.araneaframework.integration.gwt.GwtWidget;
import org.araneaframework.uilib.core.BaseUIWidget;

public class GwtI18nWidget extends BaseUIWidget {

  private static final long serialVersionUID = 1L;

  protected void init() throws Exception {
    setViewSelector("gwt/gwtI18n");
    addWidget("i18n", new GwtWidget("org.araneaframework.example.gwt.i18n.I18N"));
  }

  public void handleEventStart() throws Exception {
    getFlowCtx().start(new OtherWidget(), null, null);
  }

}
