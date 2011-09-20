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

package org.araneaframework.tests.framework.router;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.araneaframework.Service;
import org.araneaframework.core.exception.NoSuchServiceException;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulStandardService;
import org.araneaframework.mock.servlet.filter.MockBaseServiceRouterService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class BaseServiceRouterServiceTests extends TestCase {

  private MockBaseServiceRouterService service;

  private MockEventfulStandardService child1;

  private MockEventfulStandardService child2;

  private StandardServletInputData input;

  private StandardServletOutputData output;

  private MockHttpServletRequest req;

  private MockHttpServletResponse res;

  private Map<String, Service> map;

  @Override
  public void setUp() throws Exception {
    this.service = new MockBaseServiceRouterService();
    this.map = new HashMap<String, Service>();

    this.child1 = new MockEventfulStandardService();
    this.child2 = new MockEventfulStandardService();

    this.req = new MockHttpServletRequest();
    this.res = new MockHttpServletResponse();

    this.input = new StandardServletInputData(this.req);
    this.output = new StandardServletOutputData(this.req, this.res);

    this.map.put("first", this.child1);
    this.map.put("second", this.child2);

    this.service.setServiceMap(this.map);
    this.service._getComponent().init(null, MockUtil.getEnv());
  }

  public void testActionGetsCalled() throws Exception {
    this.req.addParameter("serviceId", "first");
    this.input = new StandardServletInputData(this.req);

    this.service._getService().action(MockUtil.getPath(), this.input, this.output);

    this.req = new MockHttpServletRequest();
    this.req.addParameter("serviceId", "second");
    this.input = new StandardServletInputData(this.req);

    this.service._getService().action(MockUtil.getPath(), this.input, this.output);

    assertTrue(this.child1.getActionCalled());
    assertTrue(this.child2.getActionCalled());
  }

  public void testNonExistentServiceThrowsException() throws Exception {
    this.req.addParameter("serviceId", "nonExistentService");
    this.input = new StandardServletInputData(this.req);
    try {
      this.service._getService().action(MockUtil.getPath(), this.input, this.output);
      fail("Was able to call a non existent service");
    } catch (NoSuchServiceException e) {
      // success
    }
  }
}
