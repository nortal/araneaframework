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

package org.araneaframework.tests.framework.container;

import junit.framework.TestCase;
import org.araneaframework.Path;
import org.araneaframework.framework.container.StandardContainerWidget;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulStandardWidget;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardContainerWidgetTests extends TestCase {
  private StandardContainerWidget parent;
  private MockEventfulStandardWidget child;
  
  private StandardServletInputData input;
  private StandardServletOutputData output;
  
  private MockHttpServletRequest req;
  private MockHttpServletResponse res;
  
  private Path path;
  
  public void setUp() throws Exception {
    child = new MockEventfulStandardWidget();
    parent = new StandardContainerWidget();
    parent.setRoot(child);
    MockLifeCycle.begin(parent);
    
    req = new MockHttpServletRequest();
    res = new MockHttpServletResponse();
    
    input = new StandardServletInputData(req);
    output = new StandardServletOutputData(req, res);
    
    path = MockUtil.getPath();
  }
  
  public void testRenderGetsCalled() throws Exception {
    parent._getWidget().render(output);
    assertTrue(child.getRenderCalled());
  }
  
  public void testActionGetsCalled() throws Exception {
    String pathStr = "i.am.a.path.who.are.you";
    req.addParameter(StandardContainerWidget.ACTION_PATH_KEY, pathStr);
    input = new StandardServletInputData(req);
    
    parent._getService().action(path, input, output);
    assertTrue(child.getActionCalled());
    assertTrue(pathStr.equals(child.getPath().toString()));
  }
  
  public void testActionDoesNotGetCalled() throws Exception {
    String pathStr = null;
    req.addParameter(StandardContainerWidget.ACTION_PATH_KEY, pathStr);
    input = new StandardServletInputData(req);
    
    parent._getService().action(path, input, output);
    assertFalse(child.getActionCalled());
  }
  
  public void testEventGetsCalled() throws Exception {
    String pathStr = "i.am.a.path.who.are.you";
    req.addParameter(StandardContainerWidget.EVENT_PATH_KEY, pathStr);
    input = new StandardServletInputData(req);
    
    parent._getWidget().event(path, input);
    assertTrue(child.getEventProcessed());
    assertTrue(pathStr.equals(child.getPath().toString()));
  }
  
  public void testEventDoesNotGetCalled() throws Exception {
    String pathStr = null;
    req.addParameter(StandardContainerWidget.EVENT_PATH_KEY, pathStr);
    input = new StandardServletInputData(req);
    
    parent._getWidget().event(path, input);
    assertFalse(child.getEventProcessed());
  }
  
  public void testUpdateGetsCalled() throws Exception {
    parent._getWidget().update(input);
    assertTrue(child.getUpdateCalled());
  }
  
  public void testDestroyGetsCalled() throws Exception {
    parent._getComponent().destroy();
    assertTrue(child.getDestroyCalled());
  }
}
