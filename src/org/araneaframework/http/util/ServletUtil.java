/**
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
**/

package org.araneaframework.http.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
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
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.jsp.tag.context.WidgetContextTag;
import org.araneaframework.jsp.tag.uilib.WidgetTag;

/**
 * Utility methods for Aranea framework running inside a servlet container. Includes
 * functions for rendering JSP pages and direct access to <code>HttpServletResponse</code>
 * and <code>HttpServletRequest</code>.
 *  
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * @author Alar Kvell (alar@araneaframework.org)
 */
public abstract class ServletUtil {
	
  private static final Log log = LogFactory.getLog(ServletUtil.class);

  /** @since 1.1 */
  public static final String UIWIDGET_KEY = "org.araneaframework.http.util.ServletUtil.UIWIDGET";
  /** @since 1.1 */
  public static final String LOCALIZATION_CONTEXT_KEY = Config.FMT_LOCALIZATION_CONTEXT + ".request";

  /**
   * Includes the jsp specified by filePath using the the request and response streams
   * of the output. The pathname must begin with a "/" and is interpreted as relative to
   * the current context root. The context root in the env under the key ServletContext.class
   * is used.
   */
  public static void include(String filePath, Environment env, OutputData output) throws Exception {
    include(filePath, env, output, null);
  }
  
  /**
   * Includes the jsp specified by filePath using the the request and response streams
   * of the output. The pathname must begin with a "/" and is interpreted as relative to
   * the current context root. The context root in the env under the key ServletContext.class
   * is used.
   * 
   * Widget is made available to JSP, so contextWidget tag can be used.
   * 
   * @since 1.1
   */
  public static void include(String filePath, ApplicationWidget widget, OutputData output) throws Exception {
    include(filePath, widget.getEnvironment(), output, widget);
  }

  private static void include(String filePath, Environment env, OutputData output, ApplicationWidget widget) throws Exception {
    if (log.isDebugEnabled())
      log.debug("Including a resource from the absolute path '" + filePath + "'");

    Map attributeBackupMap = new HashMap();
    HttpServletRequest req = getRequest(output.getInputData());
    if (widget != null) {
      setAttribute(req, attributeBackupMap, UIWIDGET_KEY, widget);
      setAttribute(req, attributeBackupMap, WidgetContextTag.CONTEXT_WIDGET_KEY, widget);
      String fullId = widget.getScope().toString();
      ApplicationWidget.WidgetViewModel viewModel = (ApplicationWidget.WidgetViewModel) widget._getViewable().getViewModel();
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

    ServletContext servletContext = (ServletContext) env.requireEntry(ServletContext.class);
    servletContext.getRequestDispatcher(filePath).include(getRequest(output.getInputData()), getResponse(output));

    restoreAttributes(req, attributeBackupMap);
  }

  private static void setAttribute(HttpServletRequest req, Map attributeBackupMap, String name, Object value) {
    attributeBackupMap.put(name, req.getAttribute(name));
    if (value != null) {
      req.setAttribute(name, value);
    } else {
      req.removeAttribute(name);
    }
  }
  
  private static void restoreAttributes(HttpServletRequest req, Map attributeBackupMap) {
    for (Iterator i = attributeBackupMap.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      if (entry.getValue() != null) {
        req.setAttribute((String) entry.getKey(), entry.getValue());
      } else {
        req.removeAttribute((String) entry.getKey());
      }
    }
  }
  
  /**
   * Includes the jsp specified by file using the the request and response streams
   * of the output. The pathname specified may be relative, although it cannot extend
   * outside the current servlet context. If the path begins with a "/" it is interpreted
   * as relative to the current context root. 
   */
  public static void includeRelative(String filePath, Environment env, OutputData output) throws Exception {
    log.debug("Including a resource from the relative path '" + filePath + "'");
    
    getRequest(output.getInputData()).getRequestDispatcher(filePath).include(
        getRequest(output.getInputData()), 
        getResponse(output));
  }
  
  public static void publishModel(InputData input, String name, Object model) {
    getRequest(input).setAttribute(name, model);
  }
  
  public static HttpServletRequest getRequest(InputData input) {
    return (HttpServletRequest) input.narrow(HttpServletRequest.class);
  }
  
  public static void setRequest(InputData input, HttpServletRequest req) {
    input.extend(HttpServletRequest.class, req);
  }
  
  public static HttpServletResponse getResponse(OutputData output) {
    return (HttpServletResponse) output.narrow(HttpServletResponse.class);
  }
  
  public static void setResponse(OutputData output, HttpServletResponse res) {
    output.extend(HttpServletResponse.class, res);
  }
  
  public static HttpInputData getInputData(ServletRequest req) {
    return (HttpInputData) req.getAttribute(InputData.INPUT_DATA_KEY);
  }
  
  public static HttpOutputData getOutputData(ServletRequest req) {
    return (HttpOutputData) req.getAttribute(OutputData.OUTPUT_DATA_KEY);
  }
  
  /** @since 1.1 */
  public static Environment getEnvironment(ServletRequest req) {
    return (Environment) req.getAttribute(Environment.ENVIRONMENT_KEY);
  }

  /** @since 1.1 */
  public static javax.servlet.jsp.jstl.fmt.LocalizationContext buildLocalizationContext(Environment env) {
    LocalizationContext localizationContext = (LocalizationContext) env.getEntry(LocalizationContext.class);
    if (localizationContext == null)
      return null;
    return new javax.servlet.jsp.jstl.fmt.LocalizationContext(
      new StringAdapterResourceBundle(localizationContext.getResourceBundle()),
      localizationContext.getLocale()
    );
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
    
    protected Object handleGetObject(String key) {
      Object object = bundle.getObject(key);
      return (object != null) ? object.toString() : null;
    } 
    
    public Enumeration getKeys() {
      return bundle.getKeys();
    }
  }

}
