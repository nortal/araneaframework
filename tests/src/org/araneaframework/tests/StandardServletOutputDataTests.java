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
import org.araneaframework.InputData;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.http.util.ServletUtil;
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
    StandardServletInputData inputData = new StandardServletInputData(req);
    req.setAttribute(InputData.INPUT_DATA_KEY, inputData);
    resp = new MockHttpServletResponse();
    
    out = new StandardServletOutputData(req, resp);
  }
  
  public void testGetRequest() {
    assertEquals(req, ServletUtil.getRequest(out.getInputData()));
  }
  
  public void testGetResponse() {
    assertEquals(resp, ServletUtil.getResponse(out));
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
    out.setContentType(mime);
    assertEquals(mime, resp.getContentType());
  }
  
  public void testGetOutputStream() throws Exception {
    assertEquals(resp.getOutputStream(), out.getOutputStream());
  }
  
  public void testGetAttributeKeys() {
    out.pushAttribute("foo","bar");
    assertEquals("foo", out.getAttributes().keySet().iterator().next());
  }
}
