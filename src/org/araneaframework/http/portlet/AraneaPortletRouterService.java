package org.araneaframework.http.portlet;

import javax.portlet.RenderResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.BaseService;
import org.araneaframework.core.ServiceFactory;
import org.araneaframework.http.util.PortletUtil;

public class AraneaPortletRouterService extends BaseService {
  private static final Log log = LogFactory.getLog(AraneaPortletRouterService.class);
  protected ServiceFactory serviceFactory;
  protected Service child;

  public void setServiceFactory(ServiceFactory serviceFactory) {
    this.serviceFactory = serviceFactory;
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {
    RenderResponse rr = (RenderResponse) PortletUtil.getResponse(input);
    if (rr != null) {
      if (!_getChildren().containsKey(rr.getNamespace())) {
        log.debug("No hierarchy for portlet NS '" + rr.getNamespace() + "'.");
        
        Service service = serviceFactory.buildService(getEnvironment());
        _getChildren().put(rr.getNamespace(), service);
        service._getComponent().init(null, getEnvironment());
      }
      
      if (log.isDebugEnabled())
        log.debug("Portlet router servicing namespace '" + rr.getNamespace() + "'.");
      
      Service service = (Service) _getChildren().get(rr.getNamespace());
      service._getService().action(path, input, output);
    } else {
      if (child == null) {
        child = serviceFactory.buildService(getEnvironment());
        child._getComponent().init(null, getEnvironment());
      }

      child._getService().action(path, input, output);
    }
  }
}
