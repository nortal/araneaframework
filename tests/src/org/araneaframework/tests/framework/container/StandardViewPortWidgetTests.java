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

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.araneaframework.Environment;
import org.araneaframework.OutputData;
import org.araneaframework.core.BaseWidget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.ViewPortContext;
import org.araneaframework.framework.container.StandardViewPortWidget;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockBaseWidget;
import org.araneaframework.servlet.core.StandardServletOutputData;

/**
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardViewPortWidgetTests extends TestCase {
  private StandardViewPortWidget widget;
  private BaseWidget childWidget;
  private Object attribute;
  
  public void setUp() throws Exception {
    childWidget = new MockBaseWidget();
    attribute = null;
    
    widget = new StandardViewPortWidget() {
      protected void renderViewPort(OutputData output, Object currentViewPort) throws Exception {
        attribute = output.getAttribute(ViewPortContext.VIEW_PORT_WIDGET_KEY);
      }
            
      public Environment getEnvironment() {
        Map entries = new HashMap();
        entries.put("foo","barDefault");
        return new StandardEnvironment(super.getEnvironment(), entries);
      }
    };
    //XXX
    //widget.setChild(childWidget);
    widget._getComponent().init(MockUtil.getEnv());
  }
  
  public void testRenderAttributeGetsSet() throws Exception {
    StandardServletOutputData output = MockUtil.getOutput();
    widget._getWidget().render(output);
    assertEquals(widget, attribute);
  }
  
  public void testTest() throws Exception {
    /*TestMockStandardWidget childWidget1 = new TestMockStandardWidget();
    childWidget1.addEntry("foo", "childWidget1");
    childWidget1.addEntry("childWidget1", "true");
    TestMockStandardWidget childWidget2 = new TestMockStandardWidget();
    childWidget2.addEntry("foo", "childWidget2");
    childWidget2.addEntry("childWidget2", "true");*/
    
    //XXX
    /*widget.pushGlobalWidget("child1", childWidget1);
    widget.pushGlobalWidget("child2", childWidget2);
    
    widget.popGlobalWidget("child1");*/
    
    // parent's environment entries did not get overwritten
    //assertEquals("barDefault", widget.getChildEnvironment().getEntry("foo"));
    // child widget uses its own entries
    //assertEquals("childWidget2", childWidget2.getEnvironmentEntries().get("foo"));
    // child has access to parent's entries
    //XXX
    //assertNotNull(childWidget2.getEnvironment().getEntry(ViewPortContext.class));
      // popGlobalWidget cleaned the environment from the first widget's entries
    assertNull(widget.getChildEnvironment().getEntry("childWidget1"));
  }
  
  /*public static class TestMockStandardWidget extends StandardWidget implements EnvironmentProvider.WidgetInterface {
    private Map entries = new HashMap();
    
    public void addEntry(Object key, Object value) {
      entries.put(key, value);
    }
    
    public Map getEnvironmentEntries() {
      Map map = new HashMap(entries);
      //map.putAll(super.get);
      return map;
    }
  }*/
}
