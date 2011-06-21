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

import junit.framework.TestCase;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.http.util.AtomicResponseHelper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * Note: at least in Aranea 2.0, the {@link AtomicResponseHelper} only handles commit and roll-back of content. Headers,
 * cookies etc are not handled. If necessary, it can be implemented in later versions.
 * 
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

  private int getResponseLength() {
    return this.res.getContentAsByteArray().length;
  }

  public void testCommitWriter() throws Exception {
    this.output.getWriter().write("Hello, World!");
    assertEquals("The response must be empty, because the data is not committed.", getResponseLength(), 0);
    this.atomic.commit();
    assertTrue("The response must NOT be empty, because the data is committed.", getResponseLength() > 0);
  }

  /*
   * MockHttpServletResponse.getContentAsByteArray() commits the PrintWriter and OutputStream. The reset method does not
   * reset the PrintWriter nor OutputStream, just the internal buffer. But after resetting and then flushing, the old
   * contents of the PrintWriter or OutputStream is copied to the internal buffer. It doesn't seem correct but don't
   * have time to start making changes to the mock of Spring. Anyways this test will stay empty, until I find a
   * solution.
   */
  public void testRollbackWriter() throws Exception {
    this.output.getWriter().write("Hello, World!");
    assertEquals("The response must be empty, because the data is not committed.", getResponseLength(), 0);
    this.atomic.rollback();
    assertEquals("The response must be empty, because the data was rolled back.", getResponseLength(), 0);
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
