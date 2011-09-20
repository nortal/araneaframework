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

package org.araneaframework.tests.framework.router;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.araneaframework.Service;
import org.araneaframework.framework.SessionServiceContext;
import org.araneaframework.framework.router.StandardSessionServiceRouterService;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulStandardService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardSessionServiceRouterServiceTests extends TestCase {

  private StandardSessionServiceRouterService service;

  private MockEventfulStandardService child1;

  private MockEventfulStandardService child2;

  private StandardServletInputData input;

  private StandardServletOutputData output;

  private MockHttpServletRequest req;

  private MockHttpServletResponse res;

  private Map<String, Service> map;

  @Override
  public void setUp() throws Exception {
    this.service = new StandardSessionServiceRouterService();
    this.map = new HashMap<String, Service>();

    this.child1 = new MockEventfulStandardService();
    this.child2 = new MockEventfulStandardService();

    this.req = new MockHttpServletRequest();
    this.res = new MockHttpServletResponse();

    this.input = new StandardServletInputData(this.req);
    this.output = new StandardServletOutputData(this.req, this.res);

    this.map.put("child1", this.child1);
    this.map.put("child2", this.child2);

    this.service.setServiceMap(this.map);
    this.service._getComponent().init(null, MockUtil.getEnv());

    this.service.setDefaultServiceId("child1");
  }

  public void testCloseRemoves() throws Exception {
    this.service._getService().action(MockUtil.getPath(), this.input, this.output);
    SessionServiceContext sess = this.child1.getTheEnvironment().getEntry(SessionServiceContext.class);
    sess.close("child1");
    assertTrue(this.child1.getDestroyCalled());
  }
}
