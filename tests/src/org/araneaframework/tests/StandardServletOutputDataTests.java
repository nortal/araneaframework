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
import org.araneaframework.InputData;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.http.util.ServletUtil;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardServletOutputDataTests extends TestCase {

  private StandardServletOutputData out;

  private MockHttpServletRequest req;

  private MockHttpServletResponse resp;

  @Override
  public void setUp() {
    this.req = new MockHttpServletRequest();
    StandardServletInputData inputData = new StandardServletInputData(this.req);
    this.req.setAttribute(InputData.INPUT_DATA_KEY, inputData);
    this.resp = new MockHttpServletResponse();

    this.out = new StandardServletOutputData(this.req, this.resp);
  }

  public void testGetRequest() {
    assertEquals(this.req, ServletUtil.getRequest(this.out.getInputData()));
  }

  public void testGetResponse() {
    assertEquals(this.resp, ServletUtil.getResponse(this.out));
  }

  public void testExtendNarrow() {
    Map<Object, Object> map = new HashMap<Object, Object>();
    this.out.extend(Map.class, map);
    assertEquals(map, this.out.narrow(Map.class));
  }

  public void testSetMimeType() {
    String mime = "application/xhtml+xml";
    this.out.setContentType(mime);
    assertEquals(mime, this.resp.getContentType());
  }

  public void testGetOutputStream() throws Exception {
    assertEquals(this.resp.getOutputStream(), this.out.getOutputStream());
  }
}
