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

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardContinuationFilterServiceTests extends TestCase {
  //JEV continuations are gone
  /*private StandardContinuationFilterService service;
  private MockEventfulStandardService child;
  
  private StandardEnvironment env;
  private MockHttpServletRequest req;
  private MockHttpServletResponse res;
  private StandardServletInputData input;
  private StandardServletOutputData output;
  private StandardPath path;
  private MockRenderableStandardService renderable;
  
  public void setUp() throws Exception {
    service = new StandardContinuationFilterService();
    
    renderable = new MockRenderableStandardService();
    
    child = new MockEventfulStandardService() {
      public void action(Path path, InputData input, OutputData output) throws Exception {
        throw new AraneaContinuation(renderable);
      }
    };
    service.setChildService(child);
    
    req = new MockHttpServletRequest();
    input = new StandardServletInputData(req);
    res = new MockHttpServletResponse();
    output = new StandardServletOutputData(req, res);
    output.extend(ServletAtomicResponseExtension.class, new MockServletAtomicResponseExtension());
    
    path = new StandardPath("");
    
    Map map = new HashMap();
    map.put(ThreadContext.class, null);
    map.put(SynchronizingContext.class, null);
    
    env = new StandardEnvironment(null, map);
    
    service._getComponent().init(env);
  }
  
  public void testContinuationActionGetsCalledOnError() throws Exception {
    service._getService().action(path, input, output);
    assertTrue(renderable.getActionCalled());
  }
  
  public void testDestroyDestroysChild() throws Exception {
    service._getComponent().destroy();
    assertTrue(child.getDestroyCalled());
  }*/
  
  /* JEV: sorry, muutsin kontrakti natuke, tuleks testid ka natuke muuda :(
  public void testContinuationRequestCallsAction() throws Exception {
    service._getService().action(path, input, output);
    req.addParameter(StandardContinuationFilterService.ROUTE_TO_CONTINUATION_REQUEST_PARAMETER, "true");
    input = new StandardServletInputData(req);
    service._getService().action(path, input, output);
    assertTrue(renderable.getActionCalled());
  }
  
  public void testContinuationRequestWithEmptyContinuation() throws Exception {
    req.addParameter(StandardContinuationFilterService.ROUTE_TO_CONTINUATION_REQUEST_PARAMETER, "true");
    input = new StandardServletInputData(req);
    try {
      service._getService().action(path, input, output);
      fail("Was able to call continuation request with empty continuation");
    }
    catch (AraneaNoSuchContinuationException e) {
      //success
    }
  }
  */
}
