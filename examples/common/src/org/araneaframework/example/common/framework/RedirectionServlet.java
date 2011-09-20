
package org.araneaframework.example.common.framework;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Redirection servlet that should be mapped at "/".
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class RedirectionServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (!"/".equals(request.getServletPath().trim())) {
      response.sendError(HttpServletResponse.SC_NOT_FOUND, "Accessing something besides '/' and mapped URLs!");
    } else {
      response.sendRedirect(getServletConfig().getInitParameter("webapp-root"));
    }
  }
}
