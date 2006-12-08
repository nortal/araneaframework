package org.araneaframework.example.main.web.gwt;

import org.araneaframework.integration.gwt.GwtWidget;
import org.araneaframework.uilib.core.BaseUIWidget;

public class GwtKitchenSinkWidget extends BaseUIWidget {

  private static final long serialVersionUID = 1L;

  protected void init() throws Exception {
    setViewSelector("gwt/gwtKitchenSink");
    addWidget("kitchen1", new GwtWidget("org.araneaframework.example.gwt.kitchensink.KitchenSink"));
    addWidget("kitchen2", new GwtWidget("org.araneaframework.example.gwt.kitchensink.KitchenSink"));
    addWidget("kitchen3", new GwtWidget("org.araneaframework.example.gwt.kitchensink.KitchenSink"));
  }

}
