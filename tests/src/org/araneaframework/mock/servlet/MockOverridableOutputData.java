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

package org.araneaframework.mock.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.http.ServletOverridableOutputData;
import org.araneaframework.http.core.StandardServletOutputData;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class MockOverridableOutputData extends StandardServletOutputData implements ServletOverridableOutputData{
  private transient HttpServletRequest req;
  private transient HttpServletResponse res;
  
  public MockOverridableOutputData(HttpServletRequest req, HttpServletResponse res) {
    super(req, res);
    
    this.req = req;
    this.res = res;
  }

  public void setResponse(HttpServletResponse res) {
    this.res = res;
  }
  
  public void setRequest(HttpServletRequest req) {
    this.req = req;
  }

  public HttpServletRequest getRequest() {
    return req;
  }

  public HttpServletResponse getResponse() {
    return res;
  }  
}
