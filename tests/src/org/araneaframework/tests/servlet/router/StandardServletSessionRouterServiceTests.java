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

package org.araneaframework.tests.servlet.router;

import junit.framework.TestCase;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.ServiceFactory;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.core.MockEventfulBaseService;
import org.araneaframework.servlet.core.StandardServletInputData;
import org.araneaframework.servlet.core.StandardServletOutputData;
import org.araneaframework.servlet.router.StandardServletSessionRouterService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardServletSessionRouterServiceTests extends TestCase {
  private StandardServletSessionRouterService service;
  private MockEventfulBaseService child;
  
  private StandardServletInputData input;
  private StandardServletOutputData output;
  
  private MockHttpServletRequest req;
  private MockHttpServletResponse res;
 
  private Path path;
  
  public void setUp() throws Exception {
    service = new StandardServletSessionRouterService();
    child = new MockEventfulBaseService();
    ServiceFactory factory = new ServiceFactory() {
      public Service buildService() {
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
    assertTrue(null!=input.getRequest().getSession().getAttribute(StandardServletSessionRouterService.SESSION_SERVICE_KEY));
  } 
  
  public void testReusesOldSession() throws Exception {
    service._getService().action(path, input, output);
    Service sessService = (Service)input.getRequest().getSession().getAttribute(StandardServletSessionRouterService.SESSION_SERVICE_KEY);
    service._getService().action(path, input, output);
    assertEquals(sessService, input.getRequest().getSession().getAttribute(StandardServletSessionRouterService.SESSION_SERVICE_KEY));
  }
}
