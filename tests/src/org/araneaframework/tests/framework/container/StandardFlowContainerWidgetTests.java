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

import java.lang.reflect.Field;
import java.util.HashMap;
import junit.framework.TestCase;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.container.StandardFlowContainerWidget;
import org.araneaframework.mock.core.MockEventfulBaseWidget;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardFlowContainerWidgetTests extends TestCase {

  private StandardFlowContainerWidget stackWidget;

  private MockEventfulBaseWidget topWidget;

  private MockEventfulBaseWidget childWidget;

  private MockEventfulBaseWidget childWidget2;

  private StandardEnvironment env;

  private static String BASE_FLOW_KEY;

  private static String TOP_FLOW_KEY;

  static {
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

  @Override
  public void setUp() throws Exception {
    this.topWidget = new MockEventfulBaseWidget();
    this.childWidget = new MockEventfulBaseWidget();
    this.childWidget2 = new MockEventfulBaseWidget();

    this.env = new StandardEnvironment(null, new HashMap<Class<?>, Object>());

    this.stackWidget = new StandardFlowContainerWidget(this.topWidget);
    this.stackWidget._getComponent().init(null, this.env);
  }

  public void testCallingContract() throws Exception {
    MemoizingFlowContextConfigurator configurator = new MemoizingFlowContextConfigurator();
    MemoizingFlowContextHandler handler = new MemoizingFlowContextHandler();
    this.stackWidget.start(this.childWidget, configurator, handler);

    assertTrue(this.topWidget.isDisableCalled());
    assertTrue(configurator.isConfigured());
    assertEquals(this.childWidget, this.stackWidget.getChildren().get(BASE_FLOW_KEY + "1"));
  }

  public void testCancelCallContract() throws Exception {
    MemoizingFlowContextConfigurator configurator = new MemoizingFlowContextConfigurator();
    MemoizingFlowContextHandler handler = new MemoizingFlowContextHandler();
    this.stackWidget.start(this.childWidget, configurator, handler);

    this.stackWidget.cancel();
    assertTrue(handler.isCancelled());
    assertTrue(this.topWidget.isEnableCalled());
    assertEquals(this.topWidget, this.stackWidget.getChildren().get(TOP_FLOW_KEY));
  }

  public void testReturnCallContract() throws Exception {
    MemoizingFlowContextConfigurator configurator = new MemoizingFlowContextConfigurator();
    MemoizingFlowContextHandler handler = new MemoizingFlowContextHandler();
    this.stackWidget.start(this.childWidget, configurator, handler);

    this.stackWidget.finish("returnCall");
    assertTrue("returnCall".equals(handler.getReturnValue()));
    assertTrue(this.topWidget.isEnableCalled());
    assertEquals(this.topWidget, this.stackWidget.getChildren().get(TOP_FLOW_KEY));
  }

  public void testDestroyDestroysChildrenOnStack() throws Exception {
    this.stackWidget.start(this.childWidget, new MemoizingFlowContextConfigurator(), new MemoizingFlowContextHandler());
    this.stackWidget
        .start(this.childWidget2, new MemoizingFlowContextConfigurator(), new MemoizingFlowContextHandler());

    this.stackWidget._getComponent().destroy();

    assertEquals(true, this.childWidget.getDestroyCalled());
    assertEquals(true, this.childWidget2.getDestroyCalled());
  }

  protected static class MemoizingFlowContextHandler implements FlowContext.Handler<Object> {

    private Object returnValue = null;

    private boolean finished;

    private boolean cancelled;

    public void onCancel() {
      this.cancelled = true;
    }

    public void onFinish(Object returnValue) {
      this.finished = true;
      this.returnValue = returnValue;
    }

    public Object getReturnValue() {
      return this.returnValue;
    }

    public boolean isFinished() {
      return this.finished;
    }

    public boolean isCancelled() {
      return this.cancelled;
    }
  }

  protected static class MemoizingFlowContextConfigurator implements FlowContext.Configurator {

    private boolean configured;

    private Widget flow;

    public void configure(Widget flow) {
      this.configured = true;
      this.flow = flow;
    }

    public boolean isConfigured() {
      return this.configured;
    }

    public Widget getFlow() {
      return this.flow;
    }
  }
}
