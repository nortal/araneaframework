package org.araneaframework.integration.struts;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.Environment;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.EventListener;
import org.araneaframework.http.util.ServletUtil;

public class AraneaUtil {
  private StrutsWidget parent;
  
  private AraneaUtil(StrutsWidget parent) {
    this.parent = parent;
  }

  public static AraneaUtil get(HttpServletRequest req) {
    OutputData output = ServletUtil.getOutputData(req);
    StrutsWidget parent = (StrutsWidget) output.getAttribute(StrutsWidget.STRUTS_WIDGET_KEY);
    return new AraneaUtil(parent);
  }

  public void addEventListener(Object eventId, EventListener listener) {
    parent.addEventListener(eventId, listener);
  }

  public void addWidget(Object key, Widget child) {
    parent.addWidget(key, child);
    child._getWidget().process();
  }

  public void clearEventlisteners(Object eventId) {
    parent.clearEventlisteners(eventId);
  }

  public void disableWidget(Object key) {
    parent.disableWidget(key);
  }

  public void enableWidget(Object key) {
    parent.enableWidget(key);
  }

  public Map getChildren() {
    return parent.getChildren();
  }

  public Environment getEnvironment() {
    return parent.getEnvironment();
  }

  public Widget getWidget(Object key) {
    return parent.getWidget(key);
  }
}
