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
import org.araneaframework.core.BaseService;
import org.araneaframework.core.exception.NoSuchServiceException;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.router.StandardThreadServiceRouterService;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulStandardService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardThreadServiceRouterServiceTests extends TestCase {

  private StandardThreadServiceRouterService service;

  private MockEventfulStandardService child1;

  private MockEventfulStandardService child2;

  private StandardServletInputData input;

  private StandardServletOutputData output;

  private MockHttpServletRequest req;

  private MockHttpServletResponse res;

  private Map<String, Service> map;

  @Override
  public void setUp() throws Exception {
    this.service = new StandardThreadServiceRouterService();
    this.map = new HashMap<String, Service>();

    this.child1 = new MockEventfulStandardService();
    this.child2 = new MockEventfulStandardService();

    this.req = new MockHttpServletRequest();
    this.res = new MockHttpServletResponse();

    this.input = new StandardServletInputData(this.req);
    this.output = new StandardServletOutputData(this.req, this.res);

    this.map.put("child1", this.child1);
    this.map.put("child2", this.child2);

    this.service.setServiceMap(this.map);
    this.service._getComponent().init(null, MockUtil.getEnv());

    this.service.setDefaultServiceId("child1");
  }

  public void testCloseRemoves() throws Exception, Throwable {
    this.service._getService().action(MockUtil.getPath(), this.input, this.output);
    ThreadContext sess = EnvironmentUtil.requireThreadContext(this.child1.getTheEnvironment());
    assertNotNull(sess.getService("child1"));
    sess.close("child1");
    assertTrue(this.child1.getDestroyCalled());
    assertNull(sess.getService("child1"));
  }

  public void testServiceExpiration() throws Exception {
    ThreadContext ctx = EnvironmentUtil.requireThreadContext(this.child1.getTheEnvironment());
    ctx.addService("newService", new BaseService(), new Long(1000));

    Thread.sleep(1200);

    assertNotNull("Action is not yet called, so service should still exist.", ctx.getService("newService"));
    this.service._getService().action(MockUtil.getPath(), this.input, this.output);
    assertNull("Action is called, so service should be expired now.", ctx.getService("newService"));

    // make sure that in addition to killing expired services, their lifetimes are updated in action()
    ctx.addService("nextService", new BaseService(), new Long(2000));
    MockHttpServletRequest req = new MockHttpServletRequest();
    req.addParameter(ThreadContext.THREAD_SERVICE_KEY, "nextService");
    this.input = new StandardServletInputData(req);

    Thread.sleep(1000);
    this.service._getService().action(MockUtil.getPath(), this.input, this.output);
    Thread.sleep(1500);
    this.service._getService().action(MockUtil.getPath(), this.input, this.output);

    assertNotNull("Should still be alive", ctx.getService("nextService"));

    Thread.sleep(2200);

    try {
      this.service._getService().action(MockUtil.getPath(), this.input, this.output);
      fail("Routing to 'nextService' should have failed.");
    } catch (NoSuchServiceException e) {
      //
    }

    assertNull("Should be dead now.", ctx.getService("nextService"));
  }
}
