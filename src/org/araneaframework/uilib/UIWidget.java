package org.araneaframework.uilib;

import javax.servlet.jsp.PageContext;
import org.araneaframework.core.ApplicationWidget;

public interface UIWidget extends ApplicationWidget {

  public static final String UIWIDGET_KEY = "org.araneaframework.uilib.UIWidget";

  void addContextEntry(String key, Object value);
  void removeContextEntry(String key);
  void hideContextEntries(PageContext pageContext);
  void restoreContextEntries(PageContext pageContext);

}
