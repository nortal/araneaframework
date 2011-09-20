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
 */
public class StandardServletInputDataTests extends TestCase {

  private MockHttpServletRequest request;

  private StandardServletInputData input;

  @Override
  public void setUp() {
    this.request = new MockHttpServletRequest();
    this.request.addParameter("foo", "bar");
    this.request.addParameter("a.foo", "a bar");
    this.request.addParameter("a.extra.foo", "a extra bar");
    this.request.addParameter("a.extra.foo2", "a extra bar2");
    this.input = new StandardServletInputData(this.request);
  }

  public void testNormalGetScopedData() {
    assertEquals("a extra bar", this.input.getScopedData(new StandardPath("a.extra")).get("foo"));
  }

  public void testNormalGetScopedDataMultiple() {
    this.request = new MockHttpServletRequest();
    this.request.addParameter("foo", "bar");
    this.request.addParameter("a.foo", "a bar");
    this.request.addParameter("a.extra.foo", "a extra bar");
    this.request.addParameter("a.extra.foo2", "a extra bar2");

    assertEquals("a extra bar2", this.input.getScopedData(new StandardPath("a.extra")).get("foo2"));
  }

  // empty path has to return empty scoped data
  public void testNoScopeGetScopedData() {
    assertEquals(0, this.input.getScopedData(new StandardPath("")).size());
  }

  public void testWrongPathGetScopedData() {
    this.request = new MockHttpServletRequest();
    this.request.setAttribute("a", "b");
    this.request.setAttribute("a.b", "b");
    this.request.setAttribute("a.b.c", "b");

    this.input = new StandardServletInputData(this.request);
    assertEquals(null, this.input.getScopedData(new StandardPath("a.b.c.d")).get("c"));
  }

  public void testNonValidPath() {
    this.request = new MockHttpServletRequest();
    this.request.addParameter("a" + Path.SEPARATOR + Path.SEPARATOR + Path.SEPARATOR + "foo", "b");
    this.request.addParameter(Path.SEPARATOR, "c");
    this.request.addParameter(Path.SEPARATOR, "c");
    this.input = new StandardServletInputData(this.request);
    assertEquals(null, this.input.getScopedData(new StandardPath("")).get(Path.SEPARATOR));
  }

  public void testChangeGlobalData() {
    try {
      this.input.getGlobalData().put("a", "b");
      fail("Was able to modify a unmodifiable map");
    } catch (UnsupportedOperationException e) {
      // success
    }
  }

  public void testChangeScopedData() {
    try {
      // valid scope
      this.input.getScopedData(new StandardPath("a.extra")).put("a", "b");
      fail("Was able to modify a unmodifiable map");
    } catch (UnsupportedOperationException e) {
      // success
    }
  }

  public void testChangeEmptyScopedData() {
    try {
      // invalid scope
      this.input.getScopedData(new StandardPath("nonexistent.scope.iam")).put("a", "b");
      fail("Was able to modify a unmodifiable map");
    } catch (UnsupportedOperationException e) {
      // success
    }
  }

  public void testGetRequest() {
    assertEquals(ServletUtil.getRequest(this.input), this.request);
  }

  public void testExtendNarrow() {
    Map<Object, Object> map = new HashMap<Object, Object>();
    this.input.extend(Map.class, map);
    assertEquals(map, this.input.narrow(Map.class));
  }
}
