package org.araneaframework.http.util;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderResponse;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;

public abstract class PortletUtil {
  private PortletUtil() {};
  
  public static PortletRequest getRequest(InputData input) {
    return (PortletRequest) input.narrow(PortletRequest.class);
  }
  
  public static PortletResponse getResponse(InputData input) {
    return (PortletResponse) input.narrow(PortletResponse.class);
  }
  
  public static PortletResponse getResponse(OutputData output) {
    return (PortletResponse) output.narrow(PortletResponse.class);
  }

  public static String getPortletNS(InputData input) {
    RenderResponse response = (RenderResponse)getResponse(input);
    if (response == null)
      return null;

    return response.getNamespace();
  }
  
  public static String getPortletNS(OutputData output) {
    RenderResponse response = (RenderResponse)getResponse(output);
    if (response == null)
      return null;

    return response.getNamespace();
  }
}
