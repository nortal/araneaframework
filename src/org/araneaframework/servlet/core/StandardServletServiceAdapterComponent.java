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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.Service;
import org.araneaframework.core.BaseComponent;
import org.araneaframework.servlet.ServletInputData;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.ServletServiceAdapterComponent;

/**
 * Creates a StandardServletInputData and StandardServletOutputData from the
 * HttpServletRequest and HttpServletResponse respectively and routes the request to the
 * child services using a null Path.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */   
public class StandardServletServiceAdapterComponent extends BaseComponent implements ServletServiceAdapterComponent {
  /**
   * The key of the request attribute under which lies the ServletInputData.
   */
  public static final String INPUT_DATA_REQUEST_ATTRIBUTE = "org.araneaframework.servlet.ServletInputData";
  /**
   * The key of the request attribute under which lies the ServletOutputData.
   */
  public static final String OUTPUT_DATA_REQUEST_ATTRIBUTE = "org.araneaframework.servlet.ServletOutputData";
  
  private Service childService;
  
  protected void init() throws Exception {
    childService._getComponent().init(getEnvironment());
  }
  
  public void setChildService(Service service) {
    childService = service;
  }
  
  protected void destroy() throws Exception {
    childService._getComponent().destroy();
  }
  
  public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
    ServletInputData input = new StandardServletInputData(request);
    ServletOutputData output = new StandardServletOutputData(request, response);

    request.setAttribute(INPUT_DATA_REQUEST_ATTRIBUTE, input);
    request.setAttribute(OUTPUT_DATA_REQUEST_ATTRIBUTE, output);

    childService._getService().action(null, input, output);
  }
}
