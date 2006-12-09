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
import org.apache.log4j.Logger;

public class AraneaStrutsFilter implements Filter {
  private static final Logger log = Logger.getLogger(AraneaStrutsFilter.class);
  
  public static final String ARANEA_INCLUDE = "araneaInclude";
  
  public static final String ORIGINAL_SERVLET_PATH = "araneaStrutsOriginalURI";
  public static final String ORIGINAL_PATH_INFO = "araneaStrutsOriginalPathInfo";

  public void destroy() {
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException,
      ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    
    if (AraneaStrutsInfo.containsInfo(req) 
        && request.getAttribute(ARANEA_INCLUDE) == null) {
      
      request.setAttribute(ORIGINAL_SERVLET_PATH, req.getServletPath());
      request.setAttribute(ORIGINAL_PATH_INFO, req.getPathInfo());
                 
      AraneaStrutsInfo araInfo = AraneaStrutsInfo.decode(req);      
      
      log.debug("Rerouting inclusion request from '" + req.getRequestURI() + "' to '" + araInfo.toQuery() +"'");
      
      request.getRequestDispatcher(araInfo.toQuery()).include(req, (HttpServletResponse) response);      
    }
    else {
      log.debug("Routing request to: " + req.getRequestURI());
      
      fc.doFilter(request, response);
    }
  }

  public void init(FilterConfig arg0) throws ServletException {
    log.info("Aranea inclusion filter started"); 
  }
  
}
