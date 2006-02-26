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

package org.araneaframework.tests;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.araneaframework.core.EmptyPathStackException;
import org.araneaframework.servlet.core.StandardServletInputData;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardServletInputDataTests extends TestCase {
  private MockHttpServletRequest request;
  private StandardServletInputData input;
  
  public void setUp() {
    request = new MockHttpServletRequest();
    request.addParameter("foo","bar");
    request.addParameter("a.foo","a bar");
    request.addParameter("a.extra.foo","a extra bar");
    request.addParameter("a.extra.foo2","a extra bar2");
    input = new StandardServletInputData(request);
  }
  
  public void testNormalGetScopedData() {
    input.pushScope("a");
    input.pushScope("extra");
    assertEquals("a extra bar", input.getScopedData().get("foo"));
  }
  
  public void testNormalGetScopedDataMultiple() {
    request = new MockHttpServletRequest();
    request.addParameter("foo","bar");
    request.addParameter("a.foo","a bar");
    request.addParameter("a.extra.foo","a extra bar");
    request.addParameter("a.extra.foo2","a extra bar2");

    input.pushScope("a");
    input.pushScope("extra");
    assertEquals("a extra bar2", input.getScopedData().get("foo2"));
  }
  
  //empty path has to return empty scoped data
  public void testNoScopeGetScopedData() {
    assertEquals(0,input.getScopedData().size());
  }
  
  public void testWrongPathGetScopedData() {
    request = new MockHttpServletRequest();
    request.setAttribute("a","b");
    request.setAttribute("a.b","b");
    request.setAttribute("a.b.c","b");
    
    input = new StandardServletInputData(request);
    input.pushScope("a");input.pushScope("b");input.pushScope("c");input.pushScope("d");
    assertEquals(null ,input.getScopedData().get("c"));
  }
  
  public void testNonValidPath() {
    request = new MockHttpServletRequest();
    request.addParameter("a...foo","b");
    request.addParameter(".","c");
    request.addParameter(".","c");
    input = new StandardServletInputData(request);
    assertEquals(null, input.getScopedData().get("."));
  }
  
  public void testChangeGlobalData() {
    try {
      input.getGlobalData().put("a","b");
      fail("Was able to modify a unmodifiable map");
    }
    catch (UnsupportedOperationException e) {
      //success
    }
  }
  
  public void testChangeScopedData() {
    try {
      //valid scope
      input.pushScope("a");
      input.pushScope("extra");
      
      input.getScopedData().put("a","b");
      fail("Was able to modify a unmodifiable map");
    }
    catch (UnsupportedOperationException e) {
      //success
    }
  }
  
  public void testChangeEmptyScopedData() {
    try {
      //invalid scope
      input.pushScope("nonexistent");
      input.pushScope("scope");
      input.pushScope("iam");
      
      input.getScopedData().put("a","b");
      fail("Was able to modify a unmodifiable map");
    }
    catch (UnsupportedOperationException e) {
      //success
    }
  }
  
  public void testGetRequest() {
    assertEquals(input.getRequest(), request);
  }
  
  public void testExtendNarrow() {
    Map map = new HashMap();
    input.extend(Map.class, map);
    assertEquals(map, input.narrow(Map.class));
  }
  
  public void testPopScopeFromEmptyScope() {
    input = new StandardServletInputData(request);
    try {
      input.popScope();
      fail();
    }
    catch(EmptyPathStackException e) {
      //success
    }
  }
  
  public void testPopScope() {
    input = new StandardServletInputData(request);
    input.pushScope("a");
    input.pushScope("b");
    input.popScope();
    assertEquals("a", input.getScope().toString());
  }
}
