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
import org.araneaframework.core.StandardPath;
import org.araneaframework.framework.core.BaseFilterService;

/**
 * A service that contains a service and routes actions to it. If <code>hasAction(InputData)</code> returns true, the
 * action is routed to the child, otherwise not.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class StandardContainerService extends BaseFilterService {

  private static final Log LOG = LogFactory.getLog(StandardContainerService.class);

  /**
   * The key of the action path parameter in the request.
   */
  public static final String ACTION_PATH_INPUT_DATA_PARAMETER = "serviceActionId";

  /**
   * Returns the path of action from the InputData. Uses the {@link #ACTION_PATH_INPUT_DATA_PARAMETER} constant to get
   * the path.
   * 
   * @param input The input data for this service.
   * @return The resolved action path, or <code>null</code> when it is not contained in the input.
   */
  protected Path getActionPath(InputData input) {
    String path = input.getGlobalData().get(ACTION_PATH_INPUT_DATA_PARAMETER);
    return path != null ? new StandardPath(path) : null;
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    Path actionPath = getActionPath(input);
    if (actionPath != null) {
      LOG.debug("Routing action to service '" + actionPath.toString() + "'");
      super.action(actionPath, input, output);
    }
  }
}
