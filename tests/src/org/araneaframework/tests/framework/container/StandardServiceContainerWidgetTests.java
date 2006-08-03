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
import org.araneaframework.framework.container.StandardServiceAdapterWidget;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulStandardService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardServiceContainerWidgetTests extends TestCase {
  private StandardServiceAdapterWidget parent;
  private MockEventfulStandardService child;
  
  private StandardServletInputData input;
  private StandardServletOutputData output;
  
  private MockHttpServletRequest req;
  private MockHttpServletResponse res;
  
  public void setUp() throws Exception {
    child = new MockEventfulStandardService();
    parent = new StandardServiceAdapterWidget();
    parent.setChildService(child);
    MockLifeCycle.begin(parent);
    
    req = new MockHttpServletRequest();
    res = new MockHttpServletResponse();
    
    input = new StandardServletInputData(req);
    output = new StandardServletOutputData(req, res);
  }
  
  public void testTranslatesRenderToAction() throws Exception {
    String pathStr = "i.am.a.path";
    req.addParameter(StandardServiceAdapterWidget.ACTION_PATH_INPUT_DATA_PARAMETER, pathStr);
    input = new StandardServletInputData(req);
    
    parent._getWidget().update(input);
    parent._getWidget().event(MockUtil.getPath(), input);
    parent._getWidget().render(output);
    assertTrue(child.getActionCalled());
  }
  
  //check how long the name is :)
  public void testDoesNotTranslateRenderToActionWithEmptyPath() throws Exception {
    req.addParameter(StandardServiceAdapterWidget.ACTION_PATH_INPUT_DATA_PARAMETER, "");
    input = new StandardServletInputData(req);
    
    parent._getWidget().update(input);
    parent._getWidget().event(MockUtil.getPath(), input);
    parent._getWidget().render(output);
    assertTrue(child.getActionCalled());
  }
  
  public void testDestroyGetsCalled() throws Exception {
    parent._getComponent().destroy();
    assertTrue(child.getDestroyCalled());
  }
}
