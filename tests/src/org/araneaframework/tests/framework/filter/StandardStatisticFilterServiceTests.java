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

package org.araneaframework.tests.framework.filter;

import junit.framework.TestCase;
import org.araneaframework.framework.filter.StandardStatisticFilterService;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulBaseService;
import org.araneaframework.servlet.core.StandardServletInputData;
import org.araneaframework.servlet.core.StandardServletOutputData;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardStatisticFilterServiceTests extends TestCase {
  private StandardStatisticFilterService service;
  private MockEventfulBaseService child;
  
  private StandardServletInputData input;
  private StandardServletOutputData output;
  
  private MockHttpServletRequest req;
  private MockHttpServletResponse res;
  
  public void setUp() throws Exception {
    service = new StandardStatisticFilterService();
    child = new MockEventfulBaseService();
    service.setChildService(child);
    MockLifeCycle.begin(service);
    
    req = new MockHttpServletRequest();
    res = new MockHttpServletResponse();
    
    input = new StandardServletInputData(req);
    output = new StandardServletOutputData(req, res);
  }
    
  public void testDestroyDestroysChild() throws Exception {
    service._getComponent().destroy();
    assertTrue(child.getDestroyCalled());
  }
  
  public void testActionGetsRouted() throws Exception {
    service._getService().action(MockUtil.getPath(), input, output);
    assertTrue(child.getActionCalled());
  }
  
  public void testGetSetNamespace() throws Exception {
    String nameSpace = "unittests";
    service.setNamespace(nameSpace);
    assertEquals(nameSpace, service.getNamespace());
  }
  
  public void testGetRequestTime() throws Exception {
    //XXX
    //service._getService().action(MockUtil.getPath(), input, output);
    //assertTrue(0<=service.getRequestTime()); 
  }
}
