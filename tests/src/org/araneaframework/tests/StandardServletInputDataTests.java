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

package org.araneaframework.tests;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.araneaframework.Path;
import org.araneaframework.core.StandardPath;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.util.ServletUtil;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
 *
 */
public class StandardServletInputDataTests extends TestCase {
  private MockHttpServletRequest request;
  private StandardServletInputData input;
  
  @Override
  public void setUp() {
    request = new MockHttpServletRequest();
    request.addParameter("foo","bar");
    request.addParameter("a.foo","a bar");
    request.addParameter("a.extra.foo","a extra bar");
    request.addParameter("a.extra.foo2","a extra bar2");
    input = new StandardServletInputData(request);
  }
  
  public void testNormalGetScopedData() {
    assertEquals("a extra bar", input.getScopedData(new StandardPath("a.extra")).get("foo"));
  }
  
  public void testNormalGetScopedDataMultiple() {
    request = new MockHttpServletRequest();
    request.addParameter("foo","bar");
    request.addParameter("a.foo","a bar");
    request.addParameter("a.extra.foo","a extra bar");
    request.addParameter("a.extra.foo2","a extra bar2");

    assertEquals("a extra bar2", input.getScopedData(new StandardPath("a.extra")).get("foo2"));
  }
  
  //empty path has to return empty scoped data
  public void testNoScopeGetScopedData() {
    assertEquals(0,input.getScopedData(new StandardPath("")).size());
  }
  
  public void testWrongPathGetScopedData() {
    request = new MockHttpServletRequest();
    request.setAttribute("a","b");
    request.setAttribute("a.b","b");
    request.setAttribute("a.b.c","b");
    
    input = new StandardServletInputData(request);
    assertEquals(null ,input.getScopedData(new StandardPath("a.b.c.d")).get("c"));
  }
  
  public void testNonValidPath() {
    request = new MockHttpServletRequest();
    request.addParameter("a" + Path.SEPARATOR + Path.SEPARATOR + Path.SEPARATOR + "foo", "b");
    request.addParameter(Path.SEPARATOR, "c");
    request.addParameter(Path.SEPARATOR, "c");
    input = new StandardServletInputData(request);
    assertEquals(null, input.getScopedData(new StandardPath("")).get(Path.SEPARATOR));
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
      input.getScopedData(new StandardPath("a.extra")).put("a","b");
      fail("Was able to modify a unmodifiable map");
    }
    catch (UnsupportedOperationException e) {
      //success
    }
  }
  
  public void testChangeEmptyScopedData() {
    try {
      //invalid scope
      input.getScopedData(new StandardPath("nonexistent.scope.iam")).put("a","b");
      fail("Was able to modify a unmodifiable map");
    }
    catch (UnsupportedOperationException e) {
      //success
    }
  }
  
  public void testGetRequest() {
    assertEquals(ServletUtil.getRequest(input), request);
  }
  
  public void testExtendNarrow() {
    Map<Object, Object> map = new HashMap<Object, Object>();
    input.extend(Map.class, map);
    assertEquals(map, input.narrow(Map.class));
  }
}
