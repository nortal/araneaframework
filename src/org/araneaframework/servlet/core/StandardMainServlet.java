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

package org.araneaframework.servlet.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.servlet.ServletServiceAdapterComponent;

/**
 * Aranea's main servlet. Routes GET & POST methods to the root component's
 * <code>service(HttpServletRequest, HttpServletResponse)</code> method.
 * <p>
 * Enriches the environment with objects of ServletContext and ServletConfig. They
 * can be accessed later from the environment under the keys ServletContext.class 
 * and ServletConfig.class respectively.
 * </p>
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public abstract class StandardMainServlet extends HttpServlet {
  private static final Logger log = Logger.getLogger(StandardMainServlet.class);
  private ServletServiceAdapterComponent serviceAdapter;
  
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    
    serviceAdapter = buildRootComponent();    

    Map entries = new HashMap();
    entries.put(ServletContext.class, config.getServletContext());
    entries.put(ServletConfig.class, config);
    entries.putAll(getEnvironmentEntries());
    Environment env = new StandardEnvironment(null, entries);
    
    try {
      serviceAdapter._getComponent().init(env);
    } 
    catch (Exception e) {
      log.error("Unable to start Aranea.", e);
      throw new ServletException(e);
    }
    
    log.info("Aranea started");
  }
  
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      serviceAdapter.service(req, resp);
    }
    catch (Exception e) {
      throw new ServletException(e);
    }
  }
  
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    try {
      serviceAdapter.service(req, resp);
    }
    catch (Exception e) {
      throw new ServletException(e);
    }
  } 

  /**
   * Builds the root component of this servlet. All the requests (GET & POST) will be routed
   * through the root component.
   */
  protected abstract ServletServiceAdapterComponent buildRootComponent();  
  
  protected Map getEnvironmentEntries() {
	  return new HashMap();
  }
}
