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

package org.araneaframework.framework.container;

import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.BaseWidget;
import org.araneaframework.core.StandardPath;
import org.araneaframework.core.util.Assert;

/**
 * Widget that delegates incoming event (to this widget) as an action request to child service.
 * <p>
 * More exactly, the {@link #update(InputData)} method resets the flag for monitoring whether
 * {@link #event(Path, InputData)} is called. Then during {@link #render(OutputData)} method call, the flag is checked.
 * When event was invoked on this widget, the <code>render()</code> method invokes the action method on child service.
 * <p>
 * The <code>render()</code> method is here used as a place for invoking action call because the service action method
 * is a place for services generating the response. In widget life-cycle, it usually corresponds to the
 * <code>render()</code> method.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardServiceAdapterWidget extends BaseWidget {

  /**
   * The input data parameter that is used for child service action path construction, when present.
   */
  public static final String ACTION_PATH_INPUT_DATA_PARAMETER = "widgetSubServiceActionId";

  private Service childService;

  private boolean eventReceived;

  private transient InputData input;

  /**
   * Sets the child service where this filter can forward requests. The child service cannot be changed once a
   * child-service is initialized.
   * 
   * @param service The child service where this filter can forward requests.
   */
  public final void setChildService(Service service) {
    Assert.isTrue(this.childService == null || !isInitialized(),
        "Cannot specify a child service more than once or after adapter is initialized.");
    this.childService = service;
  }

  /**
   * Resolves the value for action path to be forwarded to the child service. When the child-service action path is
   * provided in the input data, its value will be used. Otherwise defaults to <code>null</code>.
   * 
   * @param input The input data for the widget.
   * @return The action path object or <code>null</code>.
   * @see #ACTION_PATH_INPUT_DATA_PARAMETER
   */
  protected static Path getActionPath(InputData input) {
    String actionPath = input.getGlobalData().get(ACTION_PATH_INPUT_DATA_PARAMETER);
    return actionPath == null ? null : new StandardPath(actionPath);
  }

  @Override
  protected void propagate(Message message) {
    message.send(null, this.childService);
  }

  @Override
  protected void init() {
    Assert.notNull(this, this.childService, "Child service is missing!");
    this.childService._getComponent().init(getScope(), getEnvironment());
  }

  @Override
  public void update(InputData input) {
    this.input = input;
    this.eventReceived = false;
  }

  @Override
  public void event(Path path, InputData input) {
    if (!path.hasNext()) {
      this.eventReceived = true;
    }
  }

  @Override
  public void render(OutputData output) {
    if (this.eventReceived) {
      this.childService._getService().action(getActionPath(this.input), this.input, output);
    }
  }

  @Override
  protected void destroy() {
    this.childService._getComponent().destroy();
  }
}
