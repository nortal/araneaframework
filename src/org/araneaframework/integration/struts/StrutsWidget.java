package org.araneaframework.integration.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.araneaframework.OutputData;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.util.ServletUtil;

public class StrutsWidget extends BaseApplicationWidget {
  private String strutsURI;
  
  public StrutsWidget(String strutsURI) {
    this.strutsURI = strutsURI;
  }

  protected void render(OutputData output) throws Exception {
    HttpServletResponse res = ServletUtil.getResponse(output);
    HttpServletRequest req = ServletUtil.getRequest(output.getInputData());
    
    res = new StrutsResponse(res);
    ServletUtil.setResponse(output, res);      
    
    req = new StrutsRequest(req);
    ServletUtil.setRequest(output.getInputData(), req); 
    
    req.setAttribute(AraneaStrutsServlet.ARANEA_INCLUDE, Boolean.TRUE);
   
    String strutsServletPath = 
      (String) req.getAttribute(AraneaStrutsServlet.ORIGINAL_SERVLET_PATH);
    if (strutsServletPath != null)
      strutsURI = strutsServletPath;
    
    req.getRequestDispatcher(strutsURI).include(req, res);
  }
  
  private class StrutsResponse extends  HttpServletResponseWrapper {
    public StrutsResponse(HttpServletResponse arg0) {
      super(arg0);
    }
    
    public String encodeURL(String arg0) {
      return super.encodeURL(encodeStrutsURL(arg0));
    }
   
    public String encodeRedirectURL(String arg0) {
      return super.encodeRedirectURL(encodeStrutsURL(arg0));
    }       
  }
  
  private class StrutsRequest extends HttpServletRequestWrapper {
    public StrutsRequest(HttpServletRequest request) {
      super(request);
    }
    
    public String getPathInfo() {
      return (String) getAttribute(AraneaStrutsServlet.ORIGINAL_PATH_INFO);
    }
  }
  
  private String encodeStrutsURL(String baseURL) {       
    StringBuffer url = new StringBuffer(baseURL);
    
    TopServiceContext topCtx = 
      (TopServiceContext) getEnvironment().requireEntry(TopServiceContext.class);
    ThreadContext threadCtx = 
      (ThreadContext) getEnvironment().requireEntry(ThreadContext.class);
    
    AraneaStrutsInfo asInfo = new AraneaStrutsInfo(
        (String) ((HttpInputData) getInputData()).getContainerPath(), 
        (String) topCtx.getCurrentId(), 
        (String) threadCtx.getCurrentId(), 
        null);
    
    url.append(baseURL.contains("?") ? "&" : "?");
    
    url.append(asInfo.encode());
    return url.toString();
  }
}
