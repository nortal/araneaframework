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

package org.araneaframework.tests.framework.filter;

import junit.framework.TestCase;
import org.araneaframework.framework.filter.StandardStatisticFilterService;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulBaseService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardStatisticFilterServiceTests extends TestCase {

  private StandardStatisticFilterService service;

  private MockEventfulBaseService child;

  private StandardServletInputData input;

  private StandardServletOutputData output;

  private MockHttpServletRequest req;

  private MockHttpServletResponse res;

  @Override
  public void setUp() throws Exception {
    this.service = new StandardStatisticFilterService();
    this.child = new MockEventfulBaseService();
    this.service.setChildService(this.child);
    MockLifeCycle.begin(this.service);

    this.req = new MockHttpServletRequest();
    this.res = new MockHttpServletResponse();

    this.input = new StandardServletInputData(this.req);
    this.output = new StandardServletOutputData(this.req, this.res);
  }

  public void testDestroyDestroysChild() throws Exception {
    this.service._getComponent().destroy();
    assertTrue(this.child.getDestroyCalled());
  }

  public void testActionGetsRouted() throws Exception {
    this.service._getService().action(MockUtil.getPath(), this.input, this.output);
    assertTrue(this.child.getActionCalled());
  }
}
