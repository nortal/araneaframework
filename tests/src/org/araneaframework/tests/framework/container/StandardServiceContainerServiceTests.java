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

package org.araneaframework.tests.framework.container;

import junit.framework.TestCase;
import org.araneaframework.Path;
import org.araneaframework.framework.container.StandardContainerService;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulStandardService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardServiceContainerServiceTests extends TestCase {

  private StandardContainerService parent;

  private MockEventfulStandardService child;

  private StandardServletInputData input;

  private StandardServletOutputData output;

  private MockHttpServletRequest req;

  private MockHttpServletResponse res;

  private Path path;

  @Override
  public void setUp() throws Exception {
    this.child = new MockEventfulStandardService();
    this.parent = new StandardContainerService();
    this.parent.setChildService(this.child);
    MockLifeCycle.begin(this.parent);

    this.req = new MockHttpServletRequest();
    this.res = new MockHttpServletResponse();

    this.input = new StandardServletInputData(this.req);
    this.output = new StandardServletOutputData(this.req, this.res);

    this.path = MockUtil.getPath();
  }

  public void testActionGetsCalled() throws Exception {
    String pathStr = "i.am.a.path.who.are.you";
    this.req.addParameter(StandardContainerService.ACTION_PATH_INPUT_DATA_PARAMETER, pathStr);
    this.input = new StandardServletInputData(this.req);

    this.parent._getService().action(this.path, this.input, this.output);
    assertTrue(this.child.getActionCalled());
    assertTrue(pathStr.equals(this.child.getPath().toString()));
  }

  public void testActionDoesNotGetCalled() throws Exception {
    this.input = new StandardServletInputData(this.req);

    this.parent._getService().action(this.path, this.input, this.output);
    assertFalse(this.child.getActionCalled());
  }

  public void testDestroyGetsCalled() throws Exception {
    this.parent._getComponent().destroy();
    assertTrue(this.child.getDestroyCalled());
  }
}
