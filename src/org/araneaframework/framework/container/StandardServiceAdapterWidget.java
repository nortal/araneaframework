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

package org.araneaframework.framework.container;

import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.BaseWidget;
import org.araneaframework.core.StandardPath;

/**
 * A widget that contains a child service. Calls the service's action only if it gets
 * an event.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardServiceAdapterWidget extends BaseWidget {
  public static final String ACTION_PATH_INPUT_DATA_PARAMETER = "widgetSubServiceActionId";
  
  private Service childService;
  private boolean eventReceived;
  private transient InputData input;

  /**
   * Set the child service.
   */
  public void setChildService(Service service) {
    childService = service;
  }
  
  protected void init() throws Exception {
    childService._getComponent().init(getEnvironment());
  }
  
  /**
   * Returns the path of action from the InputData. Uses the
   * {@link StandardServiceAdapterWidget#ACTION_PATH_INPUT_DATA_PARAMETER} to get the path.
   */
  protected Path getActionPath(InputData input) {
    return new StandardPath((String) input.getGlobalData().get(ACTION_PATH_INPUT_DATA_PARAMETER));
  }
  
  public void update(InputData input) {
    this.input = input;
    eventReceived = false;
  } 
  
  protected void propagate(Message message) throws Exception {
    message.send(null, childService);
  }
  
  public void event(Path path, InputData input) {
    if (!path.hasNext()) {
      eventReceived = true;
    }
  }  
  
  /**
   * TODO: why is it in render and not in event() ??
   * Calls child service's action only if an event was received. The action path
   * is constructed via <code>getActionPath(InputData)</code>. The InputData is
   * saved in the <code>update(InputData)</code> method.
   */
  public void render(OutputData output) throws Exception {
    if (eventReceived) {
      this.childService._getService().action(getActionPath(input), input, output);
    }
  }
  
  protected void destroy() throws Exception {
    childService._getComponent().destroy();
  }
}
