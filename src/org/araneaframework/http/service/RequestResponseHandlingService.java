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

package org.araneaframework.http.service;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseService;
import org.araneaframework.http.util.ServletUtil;

/**
 * The service that intercepts incoming request and can provide request response handling globally. Override this class
 * to set headers, process parameters etc. By default, this class does not manipulate anything.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public class RequestResponseHandlingService extends BaseService {

  /**
   * The headers that can be set through a setter (e.g. in Spring configuration file).
   */
  protected Map<String, String> defaultheaders = new HashMap<String, String>();

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpServletRequest request = ServletUtil.getRequest(input);
    HttpServletResponse response = ServletUtil.getResponse(output);

    beforeRequestProcessing(request, response);
    super.action(path, input, output);
    afterRequestProcessing(request, response);
  }

  /**
   * This method should be overridden to modify the request/response before request is processed. Override it to set
   * headers, process parameters etc.
   * 
   * @param request The incoming HTTP request.
   * @param response The outgoing HTTP response.
   */
  protected void beforeRequestProcessing(HttpServletRequest request, HttpServletResponse response) {
  }

  /**
   * This method should be overridden to modify the request/response after request is processed. Override it to set
   * headers, process parameters etc.
   * 
   * @param request The incoming HTTP request.
   * @param response The outgoing HTTP response.
   */
  protected void afterRequestProcessing(HttpServletRequest request, HttpServletResponse response) {
  }

  /**
   * Sets a map of default headers. Useful for not having to extend or access this class, but just setting the default
   * headers in Spring configuration file.
   * 
   * @param defaultheaders A map of header names and values.
   */
  public void setDefaultheaders(Map<String, String> defaultheaders) {
    this.defaultheaders = defaultheaders;
  }
}
