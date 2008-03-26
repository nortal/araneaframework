package org.araneaframework.http.util;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import org.araneaframework.InputData;

public abstract class PortletUtil {
  private PortletUtil() {};
  
  public static PortletRequest getRequest(InputData input) {
    return (PortletRequest) input.narrow(PortletRequest.class);
  }
  
  public static PortletResponse getResponse(InputData input) {
    return (PortletResponse) input.narrow(PortletResponse.class);
  }
}
