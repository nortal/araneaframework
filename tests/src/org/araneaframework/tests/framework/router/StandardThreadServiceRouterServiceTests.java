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

package org.araneaframework.tests.framework.router;

import org.araneaframework.http.util.EnvironmentUtil;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.araneaframework.core.BaseService;
import org.araneaframework.core.NoSuchServiceException;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.router.StandardThreadServiceRouterService;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulStandardService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardThreadServiceRouterServiceTests extends TestCase {
  private StandardThreadServiceRouterService service;
  private MockEventfulStandardService child1;
  private MockEventfulStandardService child2;
  
  private StandardServletInputData input;
  private StandardServletOutputData output;
  
  private MockHttpServletRequest req;
  private MockHttpServletResponse res;
  
  private Map map;
  
  public void setUp() throws Exception {
    service = new StandardThreadServiceRouterService();
    map = new HashMap();
    
    child1 = new MockEventfulStandardService();
    child2 = new MockEventfulStandardService();
    
    req = new MockHttpServletRequest();
    res = new MockHttpServletResponse();
    
    input = new StandardServletInputData(req);
    output = new StandardServletOutputData(req, res);
    
    map.put("child1", child1);
    map.put("child2", child2);
    
    service.setServiceMap(map);
    service._getComponent().init(null, MockUtil.getEnv());
    
    service.setDefaultServiceId("child1");
  }
  
  public void testCloseRemoves() throws Exception, Throwable {
    service._getService().action(MockUtil.getPath(), input, output);
    ThreadContext sess = EnvironmentUtil.requireThreadContext(child1.getTheEnvironment());
    assertNotNull(sess.getService("child1"));
    sess.close("child1");
    assertTrue(child1.getDestroyCalled());
    assertNull(sess.getService("child1"));
  }

  public void testServiceExpiration() throws Exception {
    ThreadContext ctx = EnvironmentUtil.requireThreadContext(child1.getTheEnvironment());
    ctx.addService("newService", new BaseService(), new Long(1000));
    Thread.sleep(1200);
    assertNotNull("Action is not yet called.", ctx.getService("newService"));
    service._getService().action(MockUtil.getPath(), input, output);
    assertNull("Action is called when service should already be expired.", ctx.getService("newService"));
    
    // make sure that in addition to killing expired services, their lifetimes are updated in action()
    ctx.addService("nextService", new BaseService(), new Long(2000));
    MockHttpServletRequest req = new MockHttpServletRequest();
    req.addParameter(ThreadContext.THREAD_SERVICE_KEY, "nextService");
    input = new StandardServletInputData(req);
    
    Thread.sleep(1000);
    service._getService().action(MockUtil.getPath(), input, output);
    Thread.sleep(1500);
    service._getService().action(MockUtil.getPath(), input, output);

    assertNotNull("Should still be alive", ctx.getService("nextService"));
    
    Thread.sleep(2200);
    
    try {
      service._getService().action(MockUtil.getPath(), input, output);
      fail("Routing to 'nextService' should have failed.");
    } catch (NoSuchServiceException e) {
       //
    }

    assertNull("Should be dead now.", ctx.getService("nextService"));
  }
}
