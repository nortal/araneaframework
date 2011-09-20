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
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.exception.AraneaRuntimeException;
import org.araneaframework.framework.ExceptionHandlerFactory;
import org.araneaframework.framework.filter.StandardCriticalExceptionHandlingFilterService;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulBaseService;
import org.araneaframework.mock.core.MockEventfulStandardService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardExceptionHandlingFilterServiceTests extends TestCase {

  private StandardCriticalExceptionHandlingFilterService service;

  private MockEventfulBaseService child;

  private ExceptionHandlerFactory factory;

  private MockEventfulStandardService factoryCreatedService;

  private StandardServletInputData input;

  private StandardServletOutputData output;

  private MockHttpServletRequest req;

  private MockHttpServletResponse res;

  private Throwable exception;

  @Override
  public void setUp() throws Exception {
    this.factoryCreatedService = new MockEventfulStandardService();
    this.factoryCreatedService._getComponent().init(null, MockUtil.getEnv());

    this.factory = new ExceptionHandlerFactory() {

      public Service buildExceptionHandler(Throwable e, Environment environment) {
        StandardExceptionHandlingFilterServiceTests.this.exception = e;
        StandardExceptionHandlingFilterServiceTests.this.factoryCreatedService = new MockEventfulStandardService();
        return StandardExceptionHandlingFilterServiceTests.this.factoryCreatedService;
      }
    };

    this.service = new StandardCriticalExceptionHandlingFilterService();
    this.child = new MockEventfulBaseService();

    this.service.setChildService(this.child);
    this.service.setExceptionHandlerFactory(this.factory);
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

  public void testActionNoException() throws Exception {
    this.service._getService().action(MockUtil.getPath(), this.input, this.output);
    assertTrue(this.child.getActionCalled());
  }

  public void testActionThrowsException() throws Exception {
    final Exception exception = new AraneaRuntimeException("Another one bites the dust");

    this.service = new StandardCriticalExceptionHandlingFilterService();
    this.child = new MockEventfulBaseService() {

      @Override
      public void action(Path path, InputData input, OutputData output) throws Exception {
        ((StandardServletOutputData) output).getOutputStream().write(new byte[] { 1 });
        throw exception;
      }
    };

    this.service.setChildService(this.child);
    this.service.setExceptionHandlerFactory(this.factory);
    MockLifeCycle.begin(this.service);

    this.service._getService().action(MockUtil.getPath(), this.input, this.output);

    // exception gets forwarded to render
    assertEquals(exception, this.exception);
    // render gets called
    assertTrue(this.factoryCreatedService.getActionCalled());
  }
}
