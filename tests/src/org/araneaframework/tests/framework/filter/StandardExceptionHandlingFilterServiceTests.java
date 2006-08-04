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

package org.araneaframework.tests.framework.filter;

import junit.framework.TestCase;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.framework.ExceptionHandlerFactory;
import org.araneaframework.framework.filter.StandardCriticalExceptionHandlingFilterService;
import org.araneaframework.http.ServletOutputData;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.MockRenderableStandardService;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulBaseService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardExceptionHandlingFilterServiceTests extends TestCase {
  private StandardCriticalExceptionHandlingFilterService service;
  private MockEventfulBaseService child;
  private ExceptionHandlerFactory factory;
  private MockRenderableStandardService factoryCreatedService;
  
  private StandardServletInputData input;
  private StandardServletOutputData output;
  
  private MockHttpServletRequest req;
  private MockHttpServletResponse res;
  
  private Throwable exception;
  
  public void setUp() throws Exception {
    factoryCreatedService = new MockRenderableStandardService();
    factoryCreatedService._getComponent().init(MockUtil.getEnv());
    
    factory = new ExceptionHandlerFactory() {
      public Service buildExceptionHandler(Throwable e, Environment environment) {
        StandardExceptionHandlingFilterServiceTests.this.exception = e;
        factoryCreatedService = new MockRenderableStandardService();
        return factoryCreatedService;
      }
    };
    
    service = new StandardCriticalExceptionHandlingFilterService();
    child = new MockEventfulBaseService();
    
    service.setChildService(child);
    service.setExceptionHandlerFactory(factory);
    MockLifeCycle.begin(service);
    
    req = new MockHttpServletRequest();
    res = new MockHttpServletResponse();
    
    input = new StandardServletInputData(req);
    output = new StandardServletOutputData(req, res);
  }
    
  public void testDestroyDestroysChild() throws Exception {
    service._getComponent().destroy();
    assertTrue(child.getDestroyCalled());
  }
  
  public void testActionNoException() throws Exception {
    service._getService().action(MockUtil.getPath(), input, output);
    assertTrue(child.getActionCalled());
  }
  
  public void testActionThrowsException() throws Exception {
    final Exception exception = new AraneaRuntimeException("Another one bites the dust");
    
    service = new StandardCriticalExceptionHandlingFilterService();
    child = new MockEventfulBaseService() {
      public void action(Path path, InputData input, OutputData output) throws Exception {
        ((ServletOutputData)output).getResponse().getOutputStream().write(new byte[] {1});
        throw exception;
      }
    };
    
    service.setChildService(child);
    service.setExceptionHandlerFactory(factory);
    MockLifeCycle.begin(service);
    
    service._getService().action(MockUtil.getPath(), input, output);
    
    // exception gets forwarded to render
    assertEquals(exception, this.exception);
    // render gets called
    assertTrue(factoryCreatedService.getActionCalled());
  }
}
