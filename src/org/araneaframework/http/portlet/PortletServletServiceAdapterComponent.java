package org.araneaframework.http.portlet;

import java.io.IOException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Service;
import org.araneaframework.core.BaseComponent;
import org.araneaframework.core.BaseEnvironment;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.ServletServiceAdapterComponent;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.http.util.ServletUtil;

public class PortletServletServiceAdapterComponent extends BaseComponent implements ServletServiceAdapterComponent {
  private Service childService;

  private static final ThreadLocal localInput = new ThreadLocal();
  private static final ThreadLocal localOutput = new ThreadLocal();

  protected void init() throws Exception {
    childService._getComponent().init(getScope(), new BaseEnvironment() {

      public Object getEntry(Object key) {
        if (InputData.class.equals(key))
          return localInput.get();
        if (OutputData.class.equals(key))
          return localOutput.get();
        return getEnvironment().getEntry(key);
      }  
    });
  }

  public void setChildService(Service service) {
    childService = service;
  }

  protected void destroy() throws Exception {
    childService._getComponent().destroy();
  }

  public void service(HttpServletRequest request, HttpServletResponse response) {
    HttpInputData input = new PortletInputData(request);
    localInput.set(input);
    HttpOutputData output = new PortletOutputData(request,response);
    localOutput.set(output);

    try {
      request.setAttribute(InputData.INPUT_DATA_KEY, input);
      request.setAttribute(OutputData.OUTPUT_DATA_KEY, output);

      childService._getService().action(null, input, output);
      response.flushBuffer();
    } catch (IOException e) {
      ExceptionUtil.uncheckException(e);
    } finally {
      localInput.set(null);
      localOutput.set(null);
    }
  }
  
  private static class PortletInputData extends StandardServletInputData {
    public PortletInputData(HttpServletRequest request) {
      super(request);
      extend(PortletRequest.class, request.getAttribute("javax.portlet.request"));
      extend(PortletResponse.class, request.getAttribute("javax.portlet.response"));
    }

    public String getContainerURL() {
      RenderResponse response = (RenderResponse) ServletUtil.getRequest(this).getAttribute("javax.portlet.response");
      return response.createActionURL().toString();
    }
  }
  
  private static class  PortletOutputData extends StandardServletOutputData {
    public PortletOutputData(HttpServletRequest request, HttpServletResponse response) {
      super(request, response);
      extend(PortletRequest.class, request.getAttribute("javax.portlet.request"));
      extend(PortletResponse.class, request.getAttribute("javax.portlet.response"));
    }
  }
}
