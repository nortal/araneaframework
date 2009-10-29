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

package org.araneaframework.tests.servlet.router;

import junit.framework.TestCase;
import org.araneaframework.Environment;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.ServiceFactory;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.http.router.StandardHttpSessionRouterService;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.core.MockEventfulBaseService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardServletSessionRouterServiceTests extends TestCase {
  private StandardHttpSessionRouterService service;
  private MockEventfulBaseService child;
  
  private StandardServletInputData input;
  private StandardServletOutputData output;
  
  private MockHttpServletRequest req;
  private MockHttpServletResponse res;
 
  private Path path;
  
  @Override
  public void setUp() throws Exception {
    service = new StandardHttpSessionRouterService();
    child = new MockEventfulBaseService();
    ServiceFactory factory = new ServiceFactory() {    	
      public Service buildService(Environment env) {
        return child;
      }
    };
    
    service.setSessionServiceFactory(factory);
    MockLifeCycle.begin(service);
    
    req = new MockHttpServletRequest();
    res = new MockHttpServletResponse();
    
    input = new StandardServletInputData(req);
    output = new StandardServletOutputData(req, res);
  }
  
  public void testCreatesNewSession() throws Exception {
    service._getService().action(path, input, output);
    assertTrue(child.getInitCalled());
    assertTrue(null != ServletUtil.getRequest(input).getSession().getAttribute(StandardHttpSessionRouterService.SESSION_SERVICE_KEY));
  } 
  
  public void testReusesOldSession() throws Exception {
    service._getService().action(path, input, output);
    Service sessService = (Service)ServletUtil.getRequest(input).getSession().getAttribute(StandardHttpSessionRouterService.SESSION_SERVICE_KEY);
    service._getService().action(path, input, output);

    Service service = (Service)ServletUtil.getRequest(input).getSession().getAttribute(StandardHttpSessionRouterService.SESSION_SERVICE_KEY);

    assertEquals(sessService, service);
  }
}
