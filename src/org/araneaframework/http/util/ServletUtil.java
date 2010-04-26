/*
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.araneaframework.http.util;

import org.araneaframework.core.Assert;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.ApplicationService;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.OverlayContext;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.UpdateRegionContext;
import org.araneaframework.jsp.tag.context.WidgetContextTag;
import org.araneaframework.jsp.tag.uilib.WidgetTag;

/**
 * Utility methods for Aranea framework running inside a servlet container. Includes functions for rendering JSP pages
 * and direct access to <code>HttpServletResponse</code> and <code>HttpServletRequest</code>.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 */
public abstract class ServletUtil {

  private static final Log LOG = LogFactory.getLog(ServletUtil.class);

  /**
   * The key that is used for UI widget storing in session.
   * 
   * @since 1.1
   */
  public static final String UIWIDGET_KEY = "org.araneaframework.http.util.ServletUtil.UIWIDGET";

  /**
   * The key that is used for localization context storing and lookup.
   * 
   * @since 1.1
   */
  public static final String LOCALIZATION_CONTEXT_KEY = Config.FMT_LOCALIZATION_CONTEXT + ".request";

  /**
   * Includes the JSP specified by <code>filePath</code> using the the request and response streams of the
   * <code>output</code>. The path name must begin with a "/" (the root directory of deployment package). The context
   * root in the <code>env</code> under the key <code>ServletContext.class</code> is used.
   * 
   * @param filePath The file to include. Must begin with a forward slash (the root directory of deployment package).
   * @param env The Environment of the calling component. (Used for {@link ServletContext} lookup.)
   * @param output The component's output data. (It is used also for retrieving component's input data.)
   */
  public static void include(String filePath, Environment env, OutputData output) throws Exception {
    include(filePath, env, output, null);
  }

  /**
   * Includes the JSP specified by <code>filePath</code> using the the request and response streams of the
   * <code>output</code>. The path name must begin with a "/" (the root directory of deployment package). The context
   * root in the <code>env</code> under the key <code>ServletContext.class</code> is used.
   * <p>
   * Widget is made available to JSP, so &lt;ui:contextWidget/&gt; tag can be used.
   * 
   * @param filePath The file to include. Must begin with a forward slash (the root directory of deployment package).
   * @param widget The widget that will be made available to the included page. Its environment will be used for
   *          {@link ServletContext} lookup. Therefore must not be <code>null</code>.
   * @param output The component's output data. (It is used also for retrieving component's input data.)
   * @since 1.1
   */
  public static void include(String filePath, ApplicationWidget widget, OutputData output) throws Exception {
    Assert.notNullParam(widget, "widget");
    include(filePath, widget.getChildEnvironment(), output, widget);
  }

  /**
   * Includes the JSP specified by <code>filePath</code> using the the request and response streams of the
   * <code>output</code>. The path name must begin with a "/" (the root directory of deployment package). The context
   * root in the <code>env</code> under the key <code>ServletContext.class</code> is used.
   * <p>
   * <code>widget</code> is made available to JSP, so &lt;ui:contextWidget/&gt; tag can be used.
   * 
   * @param filePath The file to include. Must begin with a forward slash (the root directory of deployment package).
   * @param widget The widget that will be made available to the included page. Its environment will be used for
   *          {@link ServletContext} lookup. Therefore must not be <code>null</code>.
   * @param req The HTTP servlet request (usually retrieved from {@link HttpInputData}).
   * @param res The HTTP servlet response (usually retrieved from {@link HttpOutputData}).
   */
  public static void include(String filePath, ApplicationWidget widget, HttpServletRequest req, HttpServletResponse res)
      throws Exception {
    Assert.notNullParam(widget, "widget");
    include(filePath, widget, widget.getChildEnvironment(), req, res);
  }

  /**
   * Includes the JSP specified by <code>filePath</code> using the the request and response streams of the
   * <code>output</code>. The path name must begin with a "/" (the root directory of deployment package). The context
   * root in the <code>env</code> under the key <code>ServletContext.class</code> is used.
   * <p>
   * <code>widget</code> is made available to JSP, so &lt;ui:contextWidget/&gt; tag can be used.
   * 
   * @param filePath The file to include. Must begin with a forward slash (the root directory of deployment package).
   * @param env The Environment of the calling component. (Used for {@link ServletContext} lookup.)
   * @param req The HTTP servlet request (usually retrieved from {@link HttpInputData}).
   * @param res The HTTP servlet response (usually retrieved from {@link HttpOutputData}).
   */
  public static void include(String filePath, Environment env, HttpServletRequest req, HttpServletResponse res)
      throws Exception {
    include(filePath, null, env, req, res);
  }

  /**
   * Includes the JSP specified by <code>filePath</code> using the the request and response streams of the
   * <code>output</code>. The path name must begin with a "/" (the root directory of deployment package). The context
   * root in the <code>env</code> under the key <code>ServletContext.class</code> is used.
   * <p>
   * <code>widget</code> is made available to JSP, so &lt;ui:contextWidget/&gt; tag can be used.
   * 
   * @param filePath The file to include. Must begin with a forward slash (the root directory of deployment package).
   * @param env The Environment of the calling component. (Used for {@link ServletContext} lookup.)
   * @param widget The widget that will be made available to the included page. Its environment will be used for
   *          {@link ServletContext} lookup. Therefore must not be <code>null</code>.
   * @param output The component's output data. (It is used also for retrieving component's input data.)
   */
  private static void include(String filePath, Environment env, OutputData output, ApplicationWidget widget)
      throws Exception {
    include(filePath, widget, env, getRequest(output.getInputData()), getResponse(output));
  }

  private static void include(String filePath, ApplicationWidget widget, Environment env, HttpServletRequest req,
      HttpServletResponse res) throws Exception {

    if (LOG.isDebugEnabled()) {
      LOG.debug("Including a resource from the absolute path '" + filePath + "'");
    }

    Map<String, Object> attributeBackupMap = new HashMap<String, Object>();

    if (widget != null) {
      setAttribute(req, attributeBackupMap, UIWIDGET_KEY, widget);
      setAttribute(req, attributeBackupMap, WidgetContextTag.CONTEXT_WIDGET_KEY, widget);
      String fullId = widget.getScope() != null ? widget.getScope().toString() : null;
      ApplicationWidget.WidgetViewModel viewModel = (ApplicationWidget.WidgetViewModel) widget._getViewable()
          .getViewModel();
      setAttribute(req, attributeBackupMap, WidgetTag.WIDGET_KEY, widget);
      setAttribute(req, attributeBackupMap, WidgetTag.WIDGET_ID_KEY, fullId);
      setAttribute(req, attributeBackupMap, WidgetTag.WIDGET_VIEW_MODEL_KEY, viewModel);
      setAttribute(req, attributeBackupMap, WidgetTag.WIDGET_VIEW_DATA_KEY, viewModel.getData());
    } else {
      setAttribute(req, attributeBackupMap, UIWIDGET_KEY, null);
      setAttribute(req, attributeBackupMap, WidgetContextTag.CONTEXT_WIDGET_KEY, null);
      setAttribute(req, attributeBackupMap, WidgetTag.WIDGET_KEY, null);
      setAttribute(req, attributeBackupMap, WidgetTag.WIDGET_ID_KEY, null);
      setAttribute(req, attributeBackupMap, WidgetTag.WIDGET_VIEW_MODEL_KEY, null);
      setAttribute(req, attributeBackupMap, WidgetTag.WIDGET_VIEW_DATA_KEY, null);
    }

    setAttribute(req, attributeBackupMap, Environment.ENVIRONMENT_KEY, env);
    setAttribute(req, attributeBackupMap, LOCALIZATION_CONTEXT_KEY, buildLocalizationContext(env));

    simpleInclude(filePath, env, req, res);

    restoreAttributes(req, attributeBackupMap);
  }

  private static void setAttribute(HttpServletRequest req, Map<String, Object> attributeBackupMap, String name, Object value) {
    Assert.notNullParam(req, "req");
    Assert.notNullParam(attributeBackupMap, "attributeBackupMap");

    attributeBackupMap.put(name, req.getAttribute(name));
    if (value != null) {
      req.setAttribute(name, value);
    } else {
      req.removeAttribute(name);
    }
  }

  private static void restoreAttributes(HttpServletRequest req, Map<String, Object> attributeBackupMap) {
    Assert.notNullParam(req, "req");
    Assert.notNullParam(attributeBackupMap, "attributeBackupMap");

    for (Map.Entry<String, Object> entry : attributeBackupMap.entrySet()) {
      if (entry.getValue() != null) {
        req.setAttribute(entry.getKey(), entry.getValue());
      } else {
        req.removeAttribute(entry.getKey());
      }
    }
  }

  /**
   * Includes the JSP specified by file using the the request and response streams of the output. The pathname specified
   * may be relative, although it cannot extend outside the current servlet context. If the path begins with a "/" it is
   * interpreted as relative to the current context root.
   */
  public static void includeRelative(String filePath, OutputData output) throws Exception {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Including a resource from the relative path '" + filePath + "'");
    }

    getRequest(output.getInputData()).getRequestDispatcher(filePath).include(getRequest(output.getInputData()),
        getResponse(output));
  }

  /**
   * Includes the JSP specified by <code>filePath</code> using the the request and response streams of the
   * <code>output</code>. The path name must begin with a "/" (the root directory of deployment package). The context
   * root in the <code>env</code> under the key <code>ServletContext.class</code> is used.
   * <p>
   * Unlike <code>include(*)</code> methods, this method just includes the given file without setting any attributes.
   * 
   * @param filePath The file to include. Must begin with a forward slash (the root directory of deployment package).
   * @param env The Environment of the calling component. (Used for {@link ServletContext} lookup.)
   * @param input The component's input data.
   * @param output The component's output data.
   * @throws Exception
   */
  public static void simpleInclude(String filePath, Environment env, InputData input, OutputData output) throws Exception {
    simpleInclude(filePath, env, getRequest(input), getResponse(output));
  }

  public static void simpleInclude(String filePath, Environment env, HttpServletRequest req, HttpServletResponse resp) throws Exception {
    Assert.notNullParam(filePath, "filePath");
    Assert.notNullParam(env, "env");
    Assert.notNullParam(req, "req");
    Assert.notNullParam(resp, "resp");

    ServletContext servletContext = env.getEntry(ServletContext.class);
    servletContext.getRequestDispatcher(filePath).include(req, resp);
  }

  public static void publishModel(InputData input, String name, Object model) {
    getRequest(input).setAttribute(name, model);
  }

  public static HttpServletRequest getRequest(InputData input) {
    Assert.notNullParam(input, "input");
    return input.narrow(HttpServletRequest.class);
  }

  public static void setRequest(InputData input, HttpServletRequest request) {
    Assert.notNullParam(input, "input");
    Assert.notNullParam(request, "request");
    input.extend(HttpServletRequest.class, request);
  }

  public static HttpServletResponse getResponse(OutputData output) {
    Assert.notNullParam(output, "output");
    return output.narrow(HttpServletResponse.class);
  }

  public static void setResponse(OutputData output, HttpServletResponse response) {
    Assert.notNullParam(output, "output");
    Assert.notNullParam(response, "response");
    output.extend(HttpServletResponse.class, response);
  }

  public static HttpInputData getInputData(ServletRequest request) {
    Assert.notNullParam(request, "request");
    return (HttpInputData) request.getAttribute(InputData.INPUT_DATA_KEY);
  }

  public static HttpOutputData getOutputData(ServletRequest request) {
    Assert.notNullParam(request, "request");
    return (HttpOutputData) request.getAttribute(OutputData.OUTPUT_DATA_KEY);
  }

  /** @since 1.1 */
  public static Environment getEnvironment(ServletRequest request) {
    Assert.notNullParam(request, "request");
    return (Environment) request.getAttribute(Environment.ENVIRONMENT_KEY);
  }

  /** @since 2.0 */
  public static Environment getEnvironment(InputData input) {
    return getEnvironment(getRequest(input));
  }

  /** @since 1.1 */
  public static javax.servlet.jsp.jstl.fmt.LocalizationContext buildLocalizationContext(Environment env) {
    LocalizationContext localizationContext = EnvironmentUtil.getLocalizationContext(env);
    if (localizationContext == null) {
      return null;
    }
    return new javax.servlet.jsp.jstl.fmt.LocalizationContext(new StringAdapterResourceBundle(localizationContext
        .getResourceBundle()), localizationContext.getLocale());
  }

  /**
   * Provides a way to check whether the request is one of Aranea AJAX requests. Note that it does not work with
   * unparsed MultiPart request.
   * 
   * @param req The incoming request.
   * @return A Boolean indicating whether the request is one of Aranea AJAX requests.
   * @since 1.2.2
   */
  @SuppressWarnings("unchecked")
  public static boolean isAraneaAjaxRequest(ServletRequest req) {
    Assert.notNullParam(req, "req");

    Map<String, Object> params = req.getParameterMap() != null ? req.getParameterMap() : new HashMap<String, Object>();
    return params.containsKey(ApplicationService.ACTION_HANDLER_ID_KEY)
        || params.containsKey(UpdateRegionContext.UPDATE_REGIONS_KEY)
        || params.containsKey(OverlayContext.OVERLAY_REQUEST_KEY);
  }

  public static String getEncodedContainerUrl(InputData input) {
    Assert.notNullParam(input, "input");
    return ((HttpOutputData) input.getOutputData()).encodeURL(((HttpInputData) input).getContainerURL());
  }

  /**
   * Adapter resource bundle that converts all objects to string.
   * 
   * @since 1.1
   */
  public static class StringAdapterResourceBundle extends ResourceBundle {

    protected ResourceBundle bundle;

    public StringAdapterResourceBundle(ResourceBundle bundle) {
      this.bundle = bundle;
    }

    @Override
    protected Object handleGetObject(String key) {
      Object object = this.bundle.getObject(key);
      return object != null ? object.toString() : null;
    }

    @Override
    public Enumeration<String> getKeys() {
      return this.bundle.getKeys();
    }
  }

}
