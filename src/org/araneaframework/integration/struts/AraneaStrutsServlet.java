package org.araneaframework.integration.struts;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionServlet;

public class AraneaStrutsServlet extends ActionServlet {
  public static final String ARANEA_INCLUDE = "araneaInclude";
  
  public static final String ARANEA_ENCODE_TAG = ":ara:";
  
  public static final String ORIGINAL_SERVLET_PATH = "araneaStrutsOriginalURI";
  public static final String ORIGINAL_PATH_INFO = "araneaStrutsOriginalPathInfo";
  

  protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    if (AraneaStrutsInfo.containsInfo(request)
        && request.getAttribute(ARANEA_INCLUDE) == null) {
      request.setAttribute(ORIGINAL_SERVLET_PATH, request.getServletPath());
      request.setAttribute(ORIGINAL_PATH_INFO, request.getPathInfo());
                 
      AraneaStrutsInfo araInfo = AraneaStrutsInfo.decode(request);      
      getServletContext().getRequestDispatcher(araInfo.toQuery()).include(request, response);
    }
    else {
      super.process(request, response);
    }
  }
}
