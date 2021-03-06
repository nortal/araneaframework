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

import org.araneaframework.Service;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.araneaframework.framework.SessionServiceContext;
import org.araneaframework.framework.router.StandardSessionServiceRouterService;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulStandardService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardSessionServiceRouterServiceTests extends TestCase {
  private StandardSessionServiceRouterService service;
  private MockEventfulStandardService child1;
  private MockEventfulStandardService child2;
  
  private StandardServletInputData input;
  private StandardServletOutputData output;
  
  private MockHttpServletRequest req;
  private MockHttpServletResponse res;
  
  private Map<String, Service> map;
  
  @Override
  public void setUp() throws Exception {
    service = new StandardSessionServiceRouterService();
    map = new HashMap<String, Service>();
    
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
  
  public void testCloseRemoves() throws Exception {
    service._getService().action(MockUtil.getPath(), input, output);
    SessionServiceContext sess = 
      child1.getTheEnvironment().getEntry(SessionServiceContext.class);
    sess.close("child1");
    assertTrue(child1.getDestroyCalled());
  }
}
