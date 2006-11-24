package org.araneaframework.integration.struts;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AraneaJSPFilter implements Filter {

  public void destroy() {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException,
      ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    
    if (AraneaStrutsInfo.containsInfo(req) 
        && request.getAttribute(AraneaStrutsServlet.ARANEA_INCLUDE) == null) {
      
      request.setAttribute(AraneaStrutsServlet.ORIGINAL_SERVLET_PATH, req.getServletPath());
      request.setAttribute(AraneaStrutsServlet.ORIGINAL_PATH_INFO, req.getPathInfo());
                 
      AraneaStrutsInfo araInfo = AraneaStrutsInfo.decode(req);      
      request.getRequestDispatcher(araInfo.toQuery()).include(req, (HttpServletResponse) response);      
    }
    else 
      fc.doFilter(request, response);
  }

  public void init(FilterConfig arg0) throws ServletException {
  }
  
}
