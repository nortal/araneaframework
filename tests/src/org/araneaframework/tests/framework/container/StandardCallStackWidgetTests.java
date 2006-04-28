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
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.container.StandardFlowContainerWidget;
import org.araneaframework.mock.widget.MockCallableWidget;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardCallStackWidgetTests extends TestCase {
  private StandardFlowContainerWidget stackWidget;
  private MockCallableWidget topWidget;
  private MockCallableWidget childWidget;
  private MockCallableWidget childWidget2;
  private StandardEnvironment env;
  
  public void setUp() throws Exception {    
    topWidget = new MockCallableWidget();
    childWidget = new MockCallableWidget();
    childWidget2 = new MockCallableWidget();
    
    env = new StandardEnvironment(null, null);
    
    stackWidget = new StandardFlowContainerWidget(topWidget);
    stackWidget._getComponent().init(env);
  }
  
  public void testCallingContract() throws Exception {
    stackWidget.start(childWidget, childWidget.getConfigurator(), childWidget.getHandler());
    
    assertTrue(topWidget.getDeactivateCalled());
    assertTrue(childWidget.getConfigureCalled());
    assertEquals(childWidget, stackWidget.getChildren().get(StandardFlowContainerWidget.FLOW_KEY));
  }
  
  public void testCancelCallContract() throws Exception {
    stackWidget.start(childWidget, childWidget.getConfigurator(), childWidget.getHandler());
    
    stackWidget.cancel();
    assertTrue(childWidget.getOnCallCancelled());
    assertTrue(topWidget.getActivateCalled());
    assertEquals(topWidget, stackWidget.getChildren().get(StandardFlowContainerWidget.FLOW_KEY));
  }
    
  public void testReturnCallContract() throws Exception {
    stackWidget.start(childWidget, childWidget.getConfigurator(), childWidget.getHandler());
    
    stackWidget.finish("returnCall");
    assertTrue("returnCall".equals(childWidget.getReturnCallReturned()));
    assertTrue(topWidget.getActivateCalled());
    assertEquals(topWidget, stackWidget.getChildren().get(StandardFlowContainerWidget.FLOW_KEY));
  }
  
  public void testDestroyDestroysChildrenOnStack() throws Exception {
    stackWidget.start(childWidget, childWidget.getConfigurator(), childWidget.getHandler());
    stackWidget.start(childWidget2, childWidget.getConfigurator(), childWidget.getHandler());
    
    stackWidget._getComponent().destroy();
    
    assertEquals(true, childWidget.getDestroyCalled());
    assertEquals(true, childWidget2.getDestroyCalled());
  }
}
