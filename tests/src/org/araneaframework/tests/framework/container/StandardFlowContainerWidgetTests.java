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

import java.lang.reflect.Field;
import java.util.HashMap;
import junit.framework.TestCase;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.container.StandardFlowContainerWidget;
import org.araneaframework.mock.widget.MockCallableWidget;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardFlowContainerWidgetTests extends TestCase {
  private StandardFlowContainerWidget stackWidget;
  private MockCallableWidget topWidget;
  private MockCallableWidget childWidget;
  private MockCallableWidget childWidget2;
  private StandardEnvironment env;
  
  private static String BASE_FLOW_KEY; 
  private static String TOP_FLOW_KEY;
  
  static  {
	  try {
		  Field f = StandardFlowContainerWidget.class.getDeclaredField("BASE_FLOW_KEY");
		  f.setAccessible(true);
		  BASE_FLOW_KEY = (String) f.get(new StandardFlowContainerWidget());
		  
		  Field g = StandardFlowContainerWidget.class.getDeclaredField("TOP_FLOW_KEY");
		  g.setAccessible(true);
		  TOP_FLOW_KEY = (String) g.get(new StandardFlowContainerWidget());
	  } catch (Exception e) {
		ExceptionUtil.uncheckException(e);
	}
  }
  
  public void setUp() throws Exception {    
    topWidget = new MockCallableWidget();
    childWidget = new MockCallableWidget();
    childWidget2 = new MockCallableWidget();
    
    env = new StandardEnvironment(null, new HashMap());
    
    stackWidget = new StandardFlowContainerWidget(topWidget);
    stackWidget._getComponent().init(null, env);
  }
  
  public void testCallingContract() throws Exception {
    stackWidget.start(childWidget, childWidget.getConfigurator(), childWidget.getHandler());

    assertTrue(topWidget.getDeactivateCalled());
    assertTrue(childWidget.getConfigureCalled());
    assertEquals(childWidget, stackWidget.getChildren().get(TOP_FLOW_KEY+"1"));
  }
  
  public void testCancelCallContract() throws Exception {
    stackWidget.start(childWidget, childWidget.getConfigurator(), childWidget.getHandler());
    
    stackWidget.cancel();
    assertTrue(childWidget.getOnCallCancelled());
    assertTrue(topWidget.getActivateCalled());
    assertEquals(topWidget, stackWidget.getChildren().get(TOP_FLOW_KEY));
  }
    
  public void testReturnCallContract() throws Exception {
    stackWidget.start(childWidget, childWidget.getConfigurator(), childWidget.getHandler());
    
    stackWidget.finish("returnCall");
    assertTrue("returnCall".equals(childWidget.getReturnCallReturned()));
    assertTrue(topWidget.getActivateCalled());
    assertEquals(topWidget, stackWidget.getChildren().get(TOP_FLOW_KEY));
  }
  
  public void testDestroyDestroysChildrenOnStack() throws Exception {
    stackWidget.start(childWidget, childWidget.getConfigurator(), childWidget.getHandler());
    stackWidget.start(childWidget2, childWidget.getConfigurator(), childWidget.getHandler());
    
    stackWidget._getComponent().destroy();
    
    assertEquals(true, childWidget.getDestroyCalled());
    assertEquals(true, childWidget2.getDestroyCalled());
  }
}
