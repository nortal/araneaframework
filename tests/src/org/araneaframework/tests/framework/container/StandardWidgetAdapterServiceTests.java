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

package org.araneaframework.tests.framework.container;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.araneaframework.InputData;
import org.araneaframework.Path;
import org.araneaframework.core.ApplicationService;
import org.araneaframework.framework.container.StandardWidgetAdapterService;
import org.araneaframework.mock.MockInputData;
import org.araneaframework.mock.MockLifeCycle;
import org.araneaframework.mock.MockUtil;
import org.araneaframework.mock.core.MockEventfulStandardWidget;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardWidgetAdapterServiceTests extends TestCase {

  private StandardWidgetAdapterService adapter;

  private MockEventfulStandardWidget widget;

  @Override
  public void setUp() throws Exception {
    this.widget = new MockEventfulStandardWidget();

    this.adapter = new StandardWidgetAdapterService();
    this.adapter.setChildWidget(this.widget);
    MockLifeCycle.begin(this.adapter);
  }

  public void testActionUpdatesEventsRendersOnSecondRequest() throws Exception {
    this.adapter._getService().action(MockUtil.getPath(), MockUtil.getInput(), MockUtil.getOutput());
    this.adapter._getService().action(MockUtil.getPath(), MockUtil.getInput(), MockUtil.getOutput());

    assertTrue(this.widget.getUpdateCalled());
    assertTrue(!this.widget.getEventProcessed());
    assertTrue(this.widget.getRenderCalled());

    assertFalse(this.widget.getActionCalled());
  }

  public void testDoesNotActionUpdatesEventsRendersOnFirstRequest() throws Exception {
    Map<String, String> globalData = new HashMap<String, String>();
    globalData.put(ApplicationService.ACTION_PATH_KEY, "");
    MockInputData input = new MockInputData(globalData);
    this.adapter._getService().action(MockUtil.getPath(), input, MockUtil.getOutput());

    assertTrue(this.widget.getActionCalled());
    assertFalse(this.widget.getUpdateCalled());
    assertFalse(this.widget.getEventProcessed());
    assertFalse(this.widget.getRenderCalled());
  }

  public void testActionPropagates() throws Exception {
    this.adapter = new StandardWidgetAdapterService() {

      @Override
      protected boolean hasAction(InputData input) {
        return true;
      }

      @Override
      protected Path getActionPath(InputData input) {
        return null;
      }
    };
    this.widget = new MockEventfulStandardWidget();
    this.adapter.setChildWidget(this.widget);
    MockLifeCycle.begin(this.adapter);

    this.adapter._getService().action(MockUtil.getPath(), MockUtil.getInput(), MockUtil.getOutput());

    assertTrue(this.widget.getActionCalled());
  }

  public void testDestroyDestroysChild() throws Exception {
    this.adapter._getComponent().destroy();
    assertTrue(this.widget.getDestroyCalled());
  }
}
