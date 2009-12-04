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

package org.araneaframework.tests.servlet.util;

import javax.servlet.http.Cookie;
import junit.framework.TestCase;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.http.util.AtomicResponseHelper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class AtomicResponseHelperTests extends TestCase {

  private AtomicResponseHelper atomic;

  private StandardServletOutputData output;

  private MockHttpServletResponse res;

  @Override
  public void setUp() throws Exception {
    this.res = new MockHttpServletResponse();
    this.res.setContentType("text/html; charset=UTF-8");
    this.output = new StandardServletOutputData(new MockHttpServletRequest(), this.res);
    this.atomic = new AtomicResponseHelper(this.output);
  }

  public void testCommitWriter() throws Exception {
    this.res.getWriter().write("Hello, World!");
    this.atomic.commit();
    assertTrue(this.res.getContentAsByteArray().length > 0);
  }

  /*
   * MockHttpServletResponse.getContentAsByteArray() commits the PrintWriter and OutputStream. The reset method does not
   * reset the PrintWriter nor OutputStream, just the internal buffer. But after resetting and then flushing, the old
   * contents of the PrintWriter or OutputStream is copied to the internal buffer. It doesn't seem correct but don't
   * have time to start making changes to the mock of Spring. Anyways this test will stay empty, until I find a
   * solution.
   */
  public void testRollbackWriter() throws Exception {
    this.res.getWriter().write("Hello, World!");
    this.res.setCommitted(false);
    this.atomic.rollback();
    System.out.println(this.res.getContentAsByteArray().length + " " + this.res.getContentAsString());
    this.res.getContentAsByteArray();
    assertTrue(this.res.getContentAsByteArray().length == 0);
  }

  public void testRollBackHeaders() throws Exception {
    this.res.setHeader("key", "value");
    assertEquals("value", this.res.getHeader("key"));

    Cookie cookie = new Cookie("key", "value");
    this.res.addCookie(cookie);
    assertEquals(cookie, this.res.getCookie("key"));

    this.atomic.rollback();
    assertEquals(null, this.res.getHeader("key"));
    assertEquals(null, this.res.getCookie("key"));
  }

  public void testDoubleCommit() throws Exception {
    try {
      this.atomic.commit();
      this.atomic.commit();
      fail();
    } catch (IllegalStateException e) {
      // success
    }
  }
}
