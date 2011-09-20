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
 * @author Toomas Römer (toomas@webmedia.ee)
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
    this.service = new StandardHttpSessionRouterService();
    this.child = new MockEventfulBaseService();
    ServiceFactory factory = new ServiceFactory() {

      public Service buildService(Environment env) {
        return StandardServletSessionRouterServiceTests.this.child;
      }
    };

    this.service.setSessionServiceFactory(factory);
    MockLifeCycle.begin(this.service);

    this.req = new MockHttpServletRequest();
    this.res = new MockHttpServletResponse();

    this.input = new StandardServletInputData(this.req);
    this.output = new StandardServletOutputData(this.req, this.res);
  }

  public void testCreatesNewSession() throws Exception {
    this.service._getService().action(this.path, this.input, this.output);
    assertTrue(this.child.getInitCalled());
    assertTrue(null != ServletUtil.getRequest(this.input).getSession()
        .getAttribute(StandardHttpSessionRouterService.SESSION_SERVICE_KEY));
  }

  public void testReusesOldSession() throws Exception {
    this.service._getService().action(this.path, this.input, this.output);
    Service sessService = (Service) ServletUtil.getRequest(this.input).getSession()
        .getAttribute(StandardHttpSessionRouterService.SESSION_SERVICE_KEY);
    this.service._getService().action(this.path, this.input, this.output);

    Service service = (Service) ServletUtil.getRequest(this.input).getSession()
        .getAttribute(StandardHttpSessionRouterService.SESSION_SERVICE_KEY);

    assertEquals(sessService, service);
  }
}
