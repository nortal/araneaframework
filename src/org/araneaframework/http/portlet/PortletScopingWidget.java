package org.araneaframework.http.portlet;

import javax.portlet.RenderResponse;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.http.util.PortletUtil;

public class PortletScopingWidget extends BaseApplicationWidget {
  private Widget scopedWidget = null;
  private String childId; 
  
  public void setScopedWidget(Widget w) {
   this.scopedWidget = w;
  }
  
  protected void init() throws Exception {
    super.init();
    RenderResponse rr = (RenderResponse)PortletUtil.getResponse(getInputData());

    childId = rr != null ? rr.getNamespace() : "xtr"; 
    addWidget(childId, scopedWidget);
  }

  protected void render(OutputData output) throws Exception {
    getWidget(childId)._getWidget().render(output);
  }
}
