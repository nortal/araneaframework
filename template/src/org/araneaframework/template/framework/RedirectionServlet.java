package org.araneaframework.template.framework;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Redirection servlet that should be mapped at "/".
 */
public class RedirectionServlet extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.sendRedirect(getServletConfig().getInitParameter("webapp-root"));
	}
}
