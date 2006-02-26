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

import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardPath;
import org.araneaframework.framework.core.BaseFilterWidget;

/**
 * A widget that contains a child widget. It routes an action to the child
 * widget if the request has an action specified. It routes an event to the
 * child if the request has an event specified.
 *
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardWidgetContainerWidget extends BaseFilterWidget {
  private static final Logger log = Logger.getLogger(StandardWidgetContainerWidget.class);
  
  /**
   * The key of the path of the event in the request.
   */
  public static final String EVENT_PATH_KEY = "widgetEventPath";
  
  /**
   * The key of the path of the action in the request.
   */
  public static final String ACTION_PATH_KEY = "widgetActionPath";
  
  protected void init() throws Exception {
    super.init();
    
    log.debug("Widget container widget initialized.");  
  }
  
  protected void destroy() throws Exception {
    super.destroy();
    
    log.debug("Widget container widget destroyed.");
  }
  
  /**
   * If <code>hasEvent(input)</code> returns true, event is called on the child.
   * The path to the child is constructed via <code>getEventPath(input)</code>.
   */
  protected void event(Path path, InputData input) throws Exception {
    if (hasEvent(input)) {
      log.debug("Routing request through the container to the child's event.");
      childWidget._getWidget().event(getEventPath(input), input);
    }
  }
    
  /**
   * If <code>hasAction(input)</code> returns true, action is called on the child.
   * The action path is constructed via <code>getActionPath(input)</code>.
   */
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (hasAction(input)) {
      log.debug("Routing request through the container to the child's action.");
      childWidget._getService().action(getActionPath(input), input, output);
    }
  }
    
  /**
   * Extracts the path from the input and returns it. This implementation uses
   * the EVENT_PATH_KEY parameter in the request and expects the event path to be
   * a dot-separated string. 
   */
  protected Path getEventPath(InputData input) {
    return new StandardPath((String) input.getGlobalData().get(EVENT_PATH_KEY));
  }
  
  /**
   * Returns true if the request contains an event.
   */
  protected boolean hasEvent(InputData input) {
    return input.getGlobalData().get(EVENT_PATH_KEY) != null;
  }
  
  /**
   * Extracts the path from the input and returns it. This implementation uses
   * the ACTION_PATH_KEY parameter in the request and expects the action path to be
   * a dot-separated string. 
   */
  protected Path getActionPath(InputData input) {
    return new StandardPath((String) input.getGlobalData().get(ACTION_PATH_KEY));
  }
  
  /**
   * Returns true if the request contains an action.
   */
  protected boolean hasAction(InputData input) {
    return input.getGlobalData().get(ACTION_PATH_KEY) != null;
  }
}
