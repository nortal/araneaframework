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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import junit.framework.TestCase;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.filter.StandardSerializingAuditFilterService;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulBaseService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardSerializingAuditFilterServiceTests extends TestCase {
  private StandardSerializingAuditFilterService service;
  private MockEventfulBaseService child;
  private MockHttpSession httpSession;
  
  private StandardServletInputData input;
  private StandardServletOutputData output;
  
  private MockHttpServletRequest req;
  private MockHttpServletResponse res;
  
  @Override
  public void setUp() throws Exception {
    service = new StandardSerializingAuditFilterService();
    child = new MockEventfulBaseService();
    service.setChildService(child);
    
    httpSession = new MockHttpSession();
    
    Map map = new HashMap();
    map.put(HttpSession.class, httpSession);
    
    MockLifeCycle.begin(service, new StandardEnvironment(null, map));
    
    req = new MockHttpServletRequest();
    res = new MockHttpServletResponse();
    
    input = new StandardServletInputData(req);
    output = new StandardServletOutputData(req, res);
  }
    
  public void testDestroyDestroysChild() throws Exception {
    service._getComponent().destroy();
    assertTrue(child.getDestroyCalled());
  }
  
  public void testAction() throws Exception {
    service._getService().action(MockUtil.getPath(), input, output);
    assertTrue(child.getActionCalled());
  }
  
  public void testXmlOutput() throws Exception {
    String dir = "build";
    service.setTestXmlSessionPath(dir);
    
    service._getService().action(MockUtil.getPath(), input, output);
    
    File file = new File(dir+"/"+httpSession.getId()+".xml");
    assertTrue(file.length()>0);
    // delete the file
    file.delete();
    assertFalse(file.exists());
  }
}
