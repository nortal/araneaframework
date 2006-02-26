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
import org.araneaframework.servlet.core.StandardServletOutputData;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardServletOutputDataTests extends TestCase {
  private StandardServletOutputData out;
  private MockHttpServletRequest req;
  private MockHttpServletResponse resp;
  
  public void setUp() {
    req = new MockHttpServletRequest();
    resp = new MockHttpServletResponse();
    
    out = new StandardServletOutputData(req, resp);
  }
  
  public void testGetRequest() {
    assertEquals(req, out.getRequest());
  }
  
  public void testGetResponse() {
    assertEquals(resp, out.getResponse());
  }
  
  public void testSetGetAttribute() {
    out.pushAttribute("foo","bar");
    assertEquals("bar", out.getAttribute("foo"));
  }
  
  public void testExtendNarrow() {
    Map map = new HashMap();
    out.extend(Map.class, map);
    assertEquals(map, out.narrow(Map.class));
  }
  
  public void testSetMimeType() {
    String mime = "application/xhtml+xml";
    out.setMimeType(mime);
    assertEquals(mime, resp.getContentType());
  }
  
  public void testGetOutputStream() throws Exception {
    assertEquals(resp.getOutputStream(), out.getOutputStream());
  }
  
  public void testSetRequest() {
    req = new MockHttpServletRequest();
    out.setRequest(req);
    assertEquals(req, out.getRequest());
  }
  
  public void testSetResponse() {
    resp = new MockHttpServletResponse();
    out.setResponse(resp);
    assertEquals(resp, out.getResponse());
  }
  
  public void testGetAttributeKeys() {
    out.pushAttribute("foo","bar");
    assertEquals("foo", out.getAttributes().keySet().iterator().next());
  }
}
