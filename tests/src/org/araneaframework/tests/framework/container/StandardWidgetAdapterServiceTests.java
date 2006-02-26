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
import org.araneaframework.InputData;
import org.araneaframework.framework.container.StandardWidgetAdapterService;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulStandardWidget;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardWidgetAdapterServiceTests extends TestCase {
  private StandardWidgetAdapterService adapter;
  private MockEventfulStandardWidget widget;
  
  public void setUp() throws Exception {
    widget = new MockEventfulStandardWidget();
    
    adapter = new StandardWidgetAdapterService();
    adapter.setChildWidget(widget);
    MockLifeCycle.begin(adapter);
  }
  
  public void testActionUpdatesEventsRendersOnSecondRequest() throws Exception {
    adapter._getService().action(MockUtil.getPath(), MockUtil.getInput(), MockUtil.getOutput());
    adapter._getService().action(MockUtil.getPath(), MockUtil.getInput(), MockUtil.getOutput());
    
    assertTrue(widget.getUpdateCalled());
    assertTrue(widget.getEventProcessed());
    assertFalse(widget.getActionCalled());
  }
  
  public void testDoesNotActionUpdatesEventsRendersOnFirstRequest() throws Exception {
    adapter._getService().action(MockUtil.getPath(), MockUtil.getInput(), MockUtil.getOutput());
    
    assertFalse(widget.getUpdateCalled());
    assertFalse(widget.getEventProcessed());
    assertFalse(widget.getActionCalled());
  }
  
  public void testActionPropagates() throws Exception {
    adapter = new StandardWidgetAdapterService() {
      public boolean propagateAsAction(InputData input) {
        return true;
      }
    };
    widget = new MockEventfulStandardWidget();
    adapter.setChildWidget(widget);
    MockLifeCycle.begin(adapter);
    
    adapter._getService().action(MockUtil.getPath(), MockUtil.getInput(), MockUtil.getOutput());
    
    assertTrue(widget.getActionCalled());
  }
  
  public void testDestroyDestroysChild() throws Exception {
    adapter._getComponent().destroy();
    assertTrue(widget.getDestroyCalled());
  }
}
