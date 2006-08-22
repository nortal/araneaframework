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

package org.araneaframework.tests.servlet.filter;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.http.filter.StandardHttpResponseFilterService;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.MockOutputData;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulStandardService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardServletHttpResponseFilterServiceTests extends TestCase {
  //private StandardServletHttpFilterService parent;
	
  private StandardHttpResponseFilterService parent;
  private MockEventfulStandardService child;
  
  private StandardServletOutputData output;
  
  private MockHttpServletRequest req;
  private MockHttpServletResponse res;
  
  public void setUp() throws Exception {
    req = new MockHttpServletRequest();
    res = new MockHttpServletResponse();
    
    output = new StandardServletOutputData(req, res);
    
    parent = new StandardHttpResponseFilterService();
    child = new MockEventfulStandardService();
    parent.setChildService(child);
    MockLifeCycle.begin(parent);
  }
  
  public void testNullContentType() throws Exception {
   parent.setContentType(null);
   try {
     parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), MockUtil.getOutput());
     fail("Was able to call action, with Content-Type being null");
   }
   catch(AraneaRuntimeException e) {
     //success
   }
  }
  
  public void testContentType() throws Exception {
    parent.setContentType("text/css");
    parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), output);
    //assertTrue("text/css".equals(output.getResponse().getContentType()));
  }
  
  public void testCacheable() throws Exception {
    parent.setContentType("text/css");
    parent.setCacheable(false);
    parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), output);
    assertTrue("no-cache".equals(res.getHeader("Pragma").toString().toLowerCase()));
  }
  
  public void testCacheableTrue() throws Exception {
    parent.setContentType("text/css");
    parent.setCacheable(true);
    parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), output);
    assertTrue(res.getHeader("Cache-Control")!=null);
  }
  
  public void testAddCookies() throws Exception {
    Map map = new HashMap();
    
    map.put("theDaily", "Wtf");
    map.put("Paula", "Brillant");
    
    parent.setCookies(map);
    parent.setContentType("text/css");
    
    parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), output);
    
    assertTrue(res.getCookies().length==2);
    
    assertTrue("Wtf".equals(res.getCookie("theDaily").getValue()));
    assertTrue("Brillant".equals(res.getCookie("Paula").getValue()));
  }
  
  public void testAddHeaders() throws Exception {
    Map map = new HashMap();
    
    map.put("Transfer-Encoding", "chunked");
    map.put("Content-Encoding", "gzip");
    
    parent.setHeaders(map);
    parent.setContentType("text/css");
    
    parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), output);
    
    assertTrue("chunked".equals(res.getHeader("Transfer-Encoding")));
    assertTrue("gzip".equals(res.getHeader("Content-Encoding")));
  }
  
  public void testCacheHoldingTime() throws Exception {
    parent.setContentType("text/css");
    parent.setCacheable(true);
    parent.setCacheHoldingTime(1000);
    
    parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), output);
    
    assertTrue(res.getHeaders("Cache-Control").indexOf("max-age=1")!=-1);
  }
  
  public void testActionGetsCalled() throws Exception {
    parent.setContentType("text/css");
    parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), output);
    assertTrue(child.getActionCalled());
  }
  
  public void testDestroyDestroysChild() throws Exception {
    parent._getComponent().destroy();
    assertTrue(child.getDestroyCalled());
  }
}
