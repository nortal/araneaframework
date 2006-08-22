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

package org.araneaframework.tests.servlet.util;

import javax.servlet.http.Cookie;
import junit.framework.TestCase;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.http.util.AtomicResponseHelper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class AtomicResponseHelperTests extends TestCase {
  private AtomicResponseHelper atomic;
  private StandardServletOutputData output;
  private MockHttpServletResponse res;
  
  public void setUp() throws Exception {
    res = new MockHttpServletResponse();
    res.setContentType("text/html; charset=UTF-8");
    
    output = new StandardServletOutputData(new MockHttpServletRequest(), res);
    
    atomic = new AtomicResponseHelper(output);
  }
  
  public void testCommitWriter() throws Exception {
    res.getWriter().write("Hello, World!");
    atomic.commit();
    assertTrue(res.getContentAsByteArray().length > 0);
  }
  
  /*
   * MockHttpServletResponse.getContentAsByteArray() commits the
   * PrintWriter and OutputStream. The reset method does not reset
   * the PrintWriter nor OutputStream, just the internal buffer. But after
   * resetting and then flushing, the old contents of the PrintWriter or OutputStream
   * is copied to the internal buffer. It doesn't seem correct but don't have time
   * to start making changes to the mock of Spring. Anyways this test will stay empty, until
   * i find a solution.
   */
  public void testRollbackWriter() throws Exception {
    fail();
    /*
    res.getWriter().write("Hello, World!");
    atomic.rollback();
    System.out.println(res.getContentAsByteArray().length+" "+res.getContentAsString());
    assertTrue(res.getContentAsByteArray().length==0);*/
  }
  
  public void testRollBackHeaders() throws Exception {
    res.setHeader("key", "value");
    assertEquals("value", res.getHeader("key"));
    
    Cookie cookie = new Cookie("key", "value");
    res.addCookie(cookie);
    assertEquals(cookie, res.getCookie("key"));
        
    atomic.rollback();
    assertEquals(null, res.getHeader("key"));
    assertEquals(null, res.getCookie("key"));
  }
  
  public void testDoubleCommit() throws Exception {
    try {
      atomic.commit();
      atomic.commit();
      fail();
    }
    catch(IllegalStateException e) {
      //success
    }
  }
}
