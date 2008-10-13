package org.araneaframework.example.main;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SSContextServlet extends HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      resp.setContentType("application/json");
      resp.getWriter().write("{'attributes': { 'viewerId': '");
      resp.getWriter().write(req.getSession().getAttribute("username").toString());
     resp.getWriter().write("' }}");
    }
    catch (Exception e) {
      throw new ServletException(e.getMessage(), e);
    }
  }
}
