package org.araneaframework.example.main.web.gwt;

import org.araneaframework.integration.gwt.GwtWidget;
import org.araneaframework.uilib.core.BaseUIWidget;

public class GwtHelloWidget extends BaseUIWidget {

  private static final long serialVersionUID = 1L;

  protected void init() throws Exception {
    setViewSelector("gwt/gwtHello");
    addWidget("hello1", new GwtWidget("org.araneaframework.example.gwt.hello.Hello"));
    addWidget("hello2", new GwtWidget("org.araneaframework.example.gwt.hello.Hello"));
    addWidget("hello3", new GwtWidget("org.araneaframework.example.gwt.hello.Hello"));
  }

}
