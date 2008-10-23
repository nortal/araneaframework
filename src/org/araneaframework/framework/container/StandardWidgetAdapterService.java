/*
 * Copyright 2006-2008 Webmedia Group Ltd.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.ApplicationService;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.StandardPath;
import org.araneaframework.framework.core.BaseFilterWidget;

/**
 * Service that contains a widget.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardWidgetAdapterService extends BaseFilterWidget {

  private static final long serialVersionUID = 1L;

  private static final Log log = LogFactory.getLog(StandardWidgetAdapterService.class);

  /**
   * If <code>propagateAsAction(InputData)</code> returns true then the action
   * is propagated to the child. Otherwise if the request is the first one then:
   * <ul>
   * <li><code>update(input)</code></li>
   * <li><code>event(path, input)</code></li>
   * </ul>
   * are called on the child, if not then just <code>render(output).</code>
   */
  protected void action(Path path, InputData input, OutputData output) throws Exception {
   if (hasAction(input)) {
     Path actionPath = getActionPath(input);
     if (log.isDebugEnabled()) {
       log.debug("Routing action to widget '" + actionPath.toString() + "'");
     }
     childWidget._getService().action(actionPath, input, output);
   } else {
     if (log.isDebugEnabled()) {
       log.debug("Translating action() call to widget update()/event()/render() calls.");
     }

     childWidget._getWidget().update(input);
     if (hasEvent(input)) {
       Path eventPath = getEventPath(input);
       if (log.isDebugEnabled()) {
         log.debug("Routing event to widget '" + eventPath.toString() + "'");
       }
       childWidget._getWidget().event(eventPath, input);
     }
     childWidget._getWidget().render(output);
   }
 }

  /**
   * Extracts the path from the input and returns it. This implementation uses
   * the {@link ApplicationWidget#EVENT_PATH_KEY} parameter in the request and
   * expects the event path to be a dot-separated string.
   * 
   * @since 1.1
   */
  protected Path getEventPath(InputData input) {
    return new StandardPath((String) input.getGlobalData().get(
        ApplicationWidget.EVENT_PATH_KEY));
  }

  /**
   * Returns true if the request contains an event.
   * 
   * @since 1.1
   */
  protected boolean hasEvent(InputData input) {
    return input.getGlobalData().get(ApplicationWidget.EVENT_PATH_KEY) != null;
  }

  /**
   * Extracts the path from the input and returns it. This implementation uses
   * the {@link ApplicationService#ACTION_PATH_KEY} parameter in the request and
   * expects the action path to be a dot-separated string.
   * 
   * @since 1.1
   */
  protected Path getActionPath(InputData input) {
    return new StandardPath((String) input.getGlobalData().get(
        ApplicationService.ACTION_PATH_KEY));
  }

  /**
   * Returns true if the request contains an action.
   * 
   * @since 1.1
   */
  protected boolean hasAction(InputData input) {
    return input.getGlobalData().get(ApplicationService.ACTION_PATH_KEY) != null;
  }
}
