package org.araneaframework.http.portlet;

import javax.portlet.RenderResponse;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.NoSuchNarrowableException;
import org.araneaframework.http.util.PortletUtil;

public class PortletScopingWidget extends BaseApplicationWidget {
  private Widget scopedWidget = null;
  private String childId; 
  
  public void setScopedWidget(Widget w) {
   this.scopedWidget = w;
  }
  
  protected void init() throws Exception {
    super.init();
    RenderResponse rr = null;
    try {
      rr = (RenderResponse)PortletUtil.getResponse(getInputData());
    } catch (NoSuchNarrowableException ex) {
      // ok, not running under portal
    }

    childId = rr != null ? rr.getNamespace() : "x"; 
    addWidget(childId, scopedWidget);
  }

  protected void render(OutputData output) throws Exception {
    getWidget(childId)._getWidget().render(output);
  }
}
