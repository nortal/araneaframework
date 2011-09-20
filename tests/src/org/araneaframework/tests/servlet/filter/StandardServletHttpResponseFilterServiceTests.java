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

package org.araneaframework.tests.servlet.filter;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.araneaframework.core.exception.AraneaRuntimeException;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.http.filter.StandardHttpResponseFilterService;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulStandardService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardServletHttpResponseFilterServiceTests extends TestCase {

  private StandardHttpResponseFilterService parent;

  private MockEventfulStandardService child;

  private StandardServletOutputData output;

  private MockHttpServletRequest req;

  private MockHttpServletResponse res;

  @Override
  public void setUp() throws Exception {
    this.req = new MockHttpServletRequest();
    this.res = new MockHttpServletResponse();

    this.output = new StandardServletOutputData(this.req, this.res);

    this.parent = new StandardHttpResponseFilterService();
    this.child = new MockEventfulStandardService();
    this.parent.setChildService(this.child);
    MockLifeCycle.begin(this.parent);
  }

  public void testNullContentType() throws Exception {
    this.parent.setContentType(null);
    try {
      this.parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), MockUtil.getOutput());
      fail("Was able to call action, with Content-Type being null");
    } catch (AraneaRuntimeException e) {
      // success
    }
  }

  public void testContentType() throws Exception {
    this.parent.setContentType("text/css");
    this.parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), this.output);
    assertTrue("text/css".equals(ServletUtil.getResponse(this.output).getContentType()));
  }

  public void testCacheable() throws Exception {
    this.parent.setContentType("text/css");
    this.parent.setCacheable(false);
    this.parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), this.output);
    assertTrue("no-cache".equals(this.res.getHeader("Pragma").toString().toLowerCase()));
  }

  public void testCacheableTrue() throws Exception {
    this.parent.setContentType("text/css");
    this.parent.setCacheable(true);
    this.parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), this.output);
    assertTrue(this.res.getHeader("Cache-Control") != null);
  }

  public void testAddCookies() throws Exception {
    Map<String, String> map = new HashMap<String, String>();
    map.put("theDaily", "Wtf");
    map.put("Paula", "Brillant");

    this.parent.setCookies(map);
    this.parent.setContentType("text/css");

    this.parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), this.output);

    assertTrue(this.res.getCookies().length == 2);

    assertTrue("Wtf".equals(this.res.getCookie("theDaily").getValue()));
    assertTrue("Brillant".equals(this.res.getCookie("Paula").getValue()));
  }

  public void testAddHeaders() throws Exception {
    Map<String, String> map = new HashMap<String, String>();

    map.put("Transfer-Encoding", "chunked");
    map.put("Content-Encoding", "gzip");

    this.parent.setHeaders(map);
    this.parent.setContentType("text/css");

    this.parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), this.output);

    assertTrue("chunked".equals(this.res.getHeader("Transfer-Encoding")));
    assertTrue("gzip".equals(this.res.getHeader("Content-Encoding")));
  }

  public void testCacheHoldingTime() throws Exception {
    this.parent.setContentType("text/css");
    this.parent.setCacheable(true);
    this.parent.setCacheHoldingTime(1000);

    this.parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), this.output);
    assertTrue(this.res.getHeaders("Cache-Control").size() > 0);
    assertTrue(this.res.getHeaders("Cache-Control").get(0) instanceof String);
    String header = (String) this.res.getHeaders("Cache-Control").get(0);
    assertTrue(header.indexOf("max-age=1") != -1);
  }

  public void testActionGetsCalled() throws Exception {
    this.parent.setContentType("text/css");
    this.parent._getService().action(MockUtil.getPath(), MockUtil.getInput(), this.output);
    assertTrue(this.child.getActionCalled());
  }

  public void testDestroyDestroysChild() throws Exception {
    this.parent._getComponent().destroy();
    assertTrue(this.child.getDestroyCalled());
  }
}
