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
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardPath;
import org.araneaframework.framework.core.BaseFilterService;

/**
 * A service that contains a service and routes actions to it. If
 * <code>hasAction(InputData)</code> returns true, the action is routed to the child, otherwise
 * not.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardContainerService extends BaseFilterService {
  /**
   * The key of the action path parameter in the request.
   */
  public static final String ACTION_PATH_INPUT_DATA_PARAMETER = "serviceActionId";

  /**
   * Returns the path of action from the InputData. Uses the ACTION_PATH_INPUT_DATA_PARAMETER
   * to get the path.
   */
  protected Path getActionPath(InputData input) {
    return new StandardPath((String) input.getGlobalData().get(ACTION_PATH_INPUT_DATA_PARAMETER));
  }
  
  /**
   * Determines if the request contains an action. Checks if the ACTION_PATH_INPUT_DATA_PARAMETER
   * is set in the request.
   */
  protected boolean hasAction(InputData input) {
    return input.getGlobalData().get(ACTION_PATH_INPUT_DATA_PARAMETER) != null;
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (hasAction(input)) {
      childService._getService().action(getActionPath(input), input, output);
    }
  }
}
