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

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.araneaframework.core.NoSuchServiceException;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulStandardService;
import org.araneaframework.mock.servlet.filter.MockBaseServiceRouterService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class BaseServiceRouterServiceTests extends TestCase {
  private MockBaseServiceRouterService service;
  private MockEventfulStandardService child1;
  private MockEventfulStandardService child2;
  
  private StandardServletInputData input;
  private StandardServletOutputData output;
  
  private MockHttpServletRequest req;
  private MockHttpServletResponse res;
  
  private Map map;
  
  public void setUp() throws Exception {
    service = new MockBaseServiceRouterService();
    map = new HashMap();
    
    child1 = new MockEventfulStandardService();
    child2 = new MockEventfulStandardService();
    
    req = new MockHttpServletRequest();
    res = new MockHttpServletResponse();
    
    input = new StandardServletInputData(req);
    output = new StandardServletOutputData(req, res);
    
    map.put("first", child1);
    map.put("second", child2);
    
    service.setServiceMap(map);
    service._getComponent().init(MockUtil.getEnv());
  }
  
  public void testActionGetsCalled() throws Exception {
    req.addParameter("serviceId", "first");
    input = new StandardServletInputData(req);
    
    service._getService().action(MockUtil.getPath(), input, output);
    
    req = new MockHttpServletRequest();
    req.addParameter("serviceId", "second");
    input = new StandardServletInputData(req);
    
    service._getService().action(MockUtil.getPath(), input, output);
    
    assertTrue(child1.getActionCalled());
    assertTrue(child2.getActionCalled());
  }
  
  public void testNonExistentServiceThrowsException() throws Exception {
    req.addParameter("serviceId", "nonExistentService");
    input = new StandardServletInputData(req);
    try {
      service._getService().action(MockUtil.getPath(), input, output);
      fail("Was able to call a non existent service");
    }
    catch (NoSuchServiceException e) {
      //success
    }
  }
}
