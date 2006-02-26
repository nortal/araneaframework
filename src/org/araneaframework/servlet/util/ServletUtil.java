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

package org.araneaframework.servlet.util;

import javax.servlet.ServletContext;
import org.araneaframework.Environment;
import org.araneaframework.servlet.ServletInputData;
import org.araneaframework.servlet.ServletOutputData;

/**
 * An util for dynamically including jsp pages.
 *  
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class ServletUtil {
  
  /**
   * Includes the jsp specified by filePath using the the request and response streams
   * of the output. The pathname must begin with a "/" and is interpreted as relative to
   * the current context root. The context root in the env under the key ServletContext.class
   * is used.
   */
  public static void include(String filePath, Environment env, ServletOutputData output) throws Exception {
    ServletContext servletContext = 
      (ServletContext) env.getEntry(ServletContext.class);
    servletContext.getRequestDispatcher(filePath).include(output.getRequest(), output.getResponse());
  }

  /**
  * Includes the jsp specified by file using the the request and response streams
  * of the output. The pathname specified may be relative, although it cannot extend
  * outside the current servlet context. If the path begins with a "/" it is interpreted
  * as relative to the current context root. 
  */
  public static void includeRelative(String filePath, Environment env, ServletOutputData output) throws Exception {
    output.getRequest().getRequestDispatcher(filePath).include(output.getRequest(), output.getResponse());
  }
  
  public static void publishModel(ServletInputData input, String name, Object model) {
    input.getRequest().setAttribute(name, model);
  }
}
