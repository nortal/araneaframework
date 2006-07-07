/*
 * Copyright 1999,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.araneaframework.jsp.engine;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import javax.servlet.jsp.tagext.BodyContent;
import org.apache.commons.el.ExpressionEvaluatorImpl;
import org.apache.commons.el.VariableResolverImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation of the PageContext class from the JSP spec. Also doubles as a VariableResolver for the EL.
 * 
 * @author Anil K. Vijendran
 * @author Larry Cable
 * @author Hans Bergsten
 * @author Pierre Delisle
 * @author Mark Roth
 * @author Jan Luehe
 */
public class PageContextImpl extends PageContext implements VariableResolver {

  // Logger
  private static Log log = LogFactory.getLog(PageContextImpl.class);

  // The expression evaluator, for evaluating EL expressions.
  private static ExpressionEvaluatorImpl elExprEval = new ExpressionEvaluatorImpl(false);

  // The variable resolver, for evaluating EL expressions.
  private VariableResolverImpl variableResolver;

  // per-servlet state
  private Servlet servlet;

  private ServletConfig config;

  private ServletContext context;

  // page-scope attributes
  private transient Hashtable attributes;

  // per-request state
  private transient ServletRequest request;

  private transient ServletResponse response;

  private transient HttpSession session;

  private boolean isIncluded;

  // initial output stream
  private transient JspWriter out;

  /*
   * Constructor.
   */
  PageContextImpl() {
    this.variableResolver = new VariableResolverImpl(this);
    this.attributes = new Hashtable(16);
  }

  public void initialize(Servlet servlet, ServletRequest request, ServletResponse response, String errorPageURL,
      boolean needsSession, int bufferSize, boolean autoFlush) throws IOException {


    // initialize state
    this.servlet = servlet;
    this.config = servlet.getServletConfig();
    this.context = config.getServletContext();
    this.request = request;
    this.response = response;

    // Setup session (if required)
    if (request instanceof HttpServletRequest && needsSession)
      this.session = ((HttpServletRequest) request).getSession();
    if (needsSession && session == null) throw new IllegalStateException("Page needs a session and none is available");

    this.out = new JspWriterImpl(response, bufferSize, autoFlush);

    // register names/values as per spec
    setAttribute(OUT, this.out);
    setAttribute(REQUEST, request);
    setAttribute(RESPONSE, response);

    if (session != null) setAttribute(SESSION, session);

    setAttribute(PAGE, servlet);
    setAttribute(CONFIG, config);
    setAttribute(PAGECONTEXT, this);
    setAttribute(APPLICATION, context);

    isIncluded = request.getAttribute("javax.servlet.include.servlet_path") != null;
  }

  public void release() {
    try {
      if (isIncluded) {
        ((JspWriterImpl) out).flushBuffer();
        // push it into the including jspWriter
      }
      else {
        // Old code:
        // out.flush();
        // Do not flush the buffer even if we're not included (i.e.
        // we are the main page. The servlet will flush it and close
        // the stream.
        ((JspWriterImpl) out).flushBuffer();
      }
    }
    catch (IOException ex) {
      log.warn("Internal error flushing the buffer in release()");
    }

    servlet = null;
    config = null;
    context = null;
    request = null;
    response = null;
    session = null;

    attributes.clear();
  }


  public Object getAttribute(final String name) {
    return attributes.get(name);

  }

  public Object getAttribute(final String name, final int scope) {
    switch (scope) {
    case PAGE_SCOPE:
      return attributes.get(name);

    case REQUEST_SCOPE:
      return request.getAttribute(name);

    case SESSION_SCOPE:
      if (session == null) {
        throw new IllegalStateException("jsp.error.page.noSession");
      }
      return session.getAttribute(name);

    case APPLICATION_SCOPE:
      return context.getAttribute(name);

    default:
      throw new IllegalArgumentException("Invalid scope");
    }
  }

  public void setAttribute(final String name, final Object attribute) {
    if (attribute != null) {
      attributes.put(name, attribute);
    }
    else {
      removeAttribute(name, PAGE_SCOPE);
    }
  }

  public void setAttribute(final String name, final Object o, final int scope) {
    if (o != null) {
      switch (scope) {
      case PAGE_SCOPE:
        attributes.put(name, o);
        break;

      case REQUEST_SCOPE:
        request.setAttribute(name, o);
        break;

      case SESSION_SCOPE:
        if (session == null) {
          throw new IllegalStateException("jsp.error.page.noSession");
        }
        session.setAttribute(name, o);
        break;

      case APPLICATION_SCOPE:
        context.setAttribute(name, o);
        break;

      default:
        throw new IllegalArgumentException("Invalid scope");
      }
    }
    else {
      removeAttribute(name, scope);
    }
  }

  public void removeAttribute(final String name, final int scope) {
    switch (scope) {
    case PAGE_SCOPE:
      attributes.remove(name);
      break;

    case REQUEST_SCOPE:
      request.removeAttribute(name);
      break;

    case SESSION_SCOPE:
      if (session == null) {
        throw new IllegalStateException("jsp.error.page.noSession");
      }
      session.removeAttribute(name);
      break;

    case APPLICATION_SCOPE:
      context.removeAttribute(name);
      break;

    default:
      throw new IllegalArgumentException("Invalid scope");
    }
  }

  public int getAttributesScope(final String name) {
    if (attributes.get(name) != null) return PAGE_SCOPE;

    if (request.getAttribute(name) != null) return REQUEST_SCOPE;

    if (session != null) {
      if (session.getAttribute(name) != null) return SESSION_SCOPE;
    }

    if (context.getAttribute(name) != null) return APPLICATION_SCOPE;

    return 0;
  }

  public Object findAttribute(final String name) {
    Object o = attributes.get(name);
    if (o != null) return o;

    o = request.getAttribute(name);
    if (o != null) return o;

    if (session != null) {
      o = session.getAttribute(name);
      if (o != null) return o;
    }

    return context.getAttribute(name);
  }

  public Enumeration getAttributeNamesInScope(final int scope) {
    switch (scope) {
    case PAGE_SCOPE:
      return attributes.keys();

    case REQUEST_SCOPE:
      return request.getAttributeNames();

    case SESSION_SCOPE:
      if (session == null) {
        throw new IllegalStateException("jsp.error.page.noSession");
      }
      return session.getAttributeNames();

    case APPLICATION_SCOPE:
      return context.getAttributeNames();

    default:
      throw new IllegalArgumentException("Invalid scope");
    }
  }

  public void removeAttribute(final String name) {
    try {
      removeAttribute(name, PAGE_SCOPE);
      removeAttribute(name, REQUEST_SCOPE);
      if (session != null) {
        removeAttribute(name, SESSION_SCOPE);
      }
      removeAttribute(name, APPLICATION_SCOPE);
    }
    catch (Exception ex) {
      // we remove as much as we can, and
      // simply ignore possible exceptions
    }
  }

  public JspWriter getOut() {
    return out;
  }

  public HttpSession getSession() {
    return session;
  }

  public Servlet getServlet() {
    return servlet;
  }

  public ServletConfig getServletConfig() {
    return config;
  }

  public ServletContext getServletContext() {
    return config.getServletContext();
  }

  public ServletRequest getRequest() {
    return request;
  }

  public ServletResponse getResponse() {
    return response;
  }

  /**
   * Returns the exception associated with this page context, if any. <p/> Added wrapping for Throwables to avoid
   * ClassCastException: see Bugzilla 31171 for details.
   * 
   * @return The Exception associated with this page context, if any.
   */
  public Exception getException() {
    return null;
  }

  public Object getPage() {
    return servlet;
  }

  private final String getAbsolutePathRelativeToContext(String relativeUrlPath) {
    String path = relativeUrlPath;

    if (!path.startsWith("/")) {
      String uri = (String) request.getAttribute("javax.servlet.include.servlet_path");
      if (uri == null) uri = ((HttpServletRequest) request).getServletPath();
      String baseURI = uri.substring(0, uri.lastIndexOf('/'));
      path = baseURI + '/' + path;
    }

    return path;
  }
  
  /**
   * Convert a possibly relative resource path into a context-relative
   * resource path that starts with a '/'.
   *
   * @param request The servlet request we are processing
   * @param relativePath The possibly relative resource path
   */
  public static String getContextRelativePath(ServletRequest request,
                                              String relativePath) {

      if (relativePath.startsWith("/"))
          return (relativePath);
      if (!(request instanceof HttpServletRequest))
          return (relativePath);
      HttpServletRequest hrequest = (HttpServletRequest) request;
      String uri = (String)
          request.getAttribute("javax.servlet.include.servlet_path");
      if (uri != null) {
          String pathInfo = (String)
              request.getAttribute("javax.servlet.include.path_info");
          if (pathInfo == null) {
              if (uri.lastIndexOf('/') >= 0) 
                  uri = uri.substring(0, uri.lastIndexOf('/'));
          }
      }
      else {
          uri = hrequest.getServletPath();
          if (uri.lastIndexOf('/') >= 0) 
              uri = uri.substring(0, uri.lastIndexOf('/'));
      }
      return uri + '/' + relativePath;

  }
  
  public void include(String relativeUrlPath) throws ServletException, IOException {
    out.flush();

    // FIXME - It is tempting to use request.getRequestDispatcher() to
    // resolve a relative path directly, but Catalina currently does not
    // take into account whether the caller is inside a RequestDispatcher
    // include or not. Whether Catalina *should* take that into account
    // is a spec issue currently under review. In the mean time,
    // replicate Jasper's previous behavior

    String resourcePath = getContextRelativePath(request, relativeUrlPath);
    RequestDispatcher rd = request.getRequestDispatcher(resourcePath);

    rd.include(request, response);
  }

  public void include(final String relativeUrlPath, final boolean flush) throws ServletException, IOException {
    include(relativeUrlPath);
  }

  public VariableResolver getVariableResolver() {
    return this;
  }

  public void forward(final String relativeUrlPath) throws ServletException, IOException {
    // JSP.4.5 If the buffer was flushed, throw IllegalStateException
    try {
      out.clear();
    }
    catch (IOException ex) {
      IllegalStateException ise = new IllegalStateException("jsp.error.attempt_to_clear_flushed_buffer");
      ise.initCause(ex);
      throw ise;
    }

    final String path = getAbsolutePathRelativeToContext(relativeUrlPath);
    String includeUri = (String) request.getAttribute(Constants.INC_SERVLET_PATH);

    if (includeUri != null) request.removeAttribute(Constants.INC_SERVLET_PATH);
    try {
      context.getRequestDispatcher(path).forward(request, response);
    }
    finally {
      if (includeUri != null) request.setAttribute(Constants.INC_SERVLET_PATH, includeUri);
      request.setAttribute(Constants.FORWARD_SEEN, "true");
    }
  }

  public BodyContent pushBody() {
    return (BodyContent) pushBody(null);
  }

  public JspWriter pushBody(Writer writer) {
    return out;
  }

  public JspWriter popBody() {
    return out;
  }

  /**
   * Provides programmatic access to the ExpressionEvaluator. The JSP Container must return a valid instance of an
   * ExpressionEvaluator that can parse EL expressions.
   */
  public ExpressionEvaluator getExpressionEvaluator() {
    return elExprEval;
  }

  public void handlePageException(Exception ex) throws IOException, ServletException {
    // Should never be called since handleException() called with a
    // Throwable in the generated servlet.
    handlePageException((Throwable) ex);
  }

  public void handlePageException(final Throwable t) throws IOException, ServletException {
  }

  /**
   * VariableResolver interface
   */
  public Object resolveVariable(String pName) throws ELException {
    return variableResolver.resolveVariable(pName);
  }
}
