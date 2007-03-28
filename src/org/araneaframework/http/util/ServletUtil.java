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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.JspContext;
import org.araneaframework.jsp.tag.aranea.AraneaRootTag;
import org.araneaframework.uilib.UIWidget;

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
	
  private static final Logger log = Logger.getLogger(ServletUtil.class);
  
  /**
   * Includes the jsp specified by filePath using the the request and response streams
   * of the output. The pathname must begin with a "/" and is interpreted as relative to
   * the current context root. The context root in the env under the key ServletContext.class
   * is used.
   */
  public static void include(String filePath, Environment env, OutputData output) throws Exception {
    //FIXME
  }
  
  public static void include(String filePath, UIWidget widget, OutputData output) throws Exception {
    if (log.isDebugEnabled())
      log.debug("Including a resource from the absolute path '" + filePath + "'");
    
    Map attributeBackupMap = new HashMap();
    
    Environment env = widget.getEnvironment();
    HttpServletRequest req = getRequest(output.getInputData());
    setAttribute(req, attributeBackupMap, UIWidget.UIWIDGET_KEY, widget);
    setAttribute(req, attributeBackupMap, Environment.ENVIRONMENT_KEY, env);
    
    /* AraneaRootTag */
    JspContext config = (JspContext) env.requireEntry(JspContext.class);
    if (req.getAttribute(AraneaRootTag.LOCALIZATION_CONTEXT_KEY) == null) {
      setAttribute(req, attributeBackupMap,
        AraneaRootTag.LOCALIZATION_CONTEXT_KEY,
        AraneaRootTag.getLocalizationContext(config)
      );
    }
    
    ServletContext servletContext = (ServletContext) env.getEntry(ServletContext.class);
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
  
  public static Environment getEnvironment(ServletRequest req) {
    return (Environment) req.getAttribute(OutputData.OUTPUT_DATA_KEY);
  }
}
