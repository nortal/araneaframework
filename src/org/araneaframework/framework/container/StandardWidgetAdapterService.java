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
 * Service that delegates requests to child widget. The incoming request is expected to be either action or event
 * depending on the input data parameters. If action data is not present, the request will be treated as event.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardWidgetAdapterService extends BaseFilterWidget {

  private static final Log LOG = LogFactory.getLog(StandardWidgetAdapterService.class);

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (hasAction(input)) {
      Path actionPath = getActionPath(input);

      if (LOG.isDebugEnabled()) {
        LOG.debug("Routing action to widget '" + actionPath + "'");
      }

      getChildWidget()._getService().action(actionPath, input, output);
    } else {

      if (LOG.isDebugEnabled()) {
        LOG.debug("Translating action() call to widget update()/event()/render() calls.");
      }

      getChildWidget()._getWidget().update(input);

      if (hasEvent(input)) {
        Path eventPath = getEventPath(input);

        if (LOG.isDebugEnabled()) {
          LOG.debug("Routing event to widget '" + eventPath + "'");
        }

        getChildWidget()._getWidget().event(eventPath, input);
      }

      getChildWidget()._getWidget().render(output);
    }
  }

  /**
   * Extracts the event target path from the input and returns it. Throws a runtime exception when the input data does
   * not contain event path data
   * 
   * @param input Input data for the service.
   * @return The event path form input data (never <code>null</code>).
   * @see ApplicationWidget#EVENT_PATH_KEY
   * @since 1.1
   */
  protected static Path getEventPath(InputData input) {
    return new StandardPath(input.getGlobalData().get(ApplicationWidget.EVENT_PATH_KEY));
  }

  /**
   * Checks whether the input data contains event data.
   * 
   * @param input Input data for the service.
   * @return A Boolean that is <code>true</code> when the input data contains an event data to a widget.
   * @since 1.1
   */
  protected static boolean hasEvent(InputData input) {
    return input.getGlobalData().get(ApplicationWidget.EVENT_PATH_KEY) != null;
  }

  /**
   * Extracts the action target path from the input and returns it. Throws a runtime exception when the input data does
   * not contain action path data
   * 
   * @param input Input data for the service.
   * @return The action path form input data (never <code>null</code>).
   * @see ApplicationService#ACTION_PATH_KEY
   * @since 1.1
   */
  protected static Path getActionPath(InputData input) {
    return new StandardPath(input.getGlobalData().get(ApplicationService.ACTION_PATH_KEY));
  }

  /**
   * Checks whether the input data contains action data.
   * 
   * @param input Input data for the service.
   * @return A Boolean that is <code>true</code> when the input data contains an event data to a service/widget.
   * @since 1.1
   */
  protected static boolean hasAction(InputData input) {
    return input.getGlobalData().get(ApplicationService.ACTION_PATH_KEY) != null;
  }
}
