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

package org.araneaframework.core;

import org.araneaframework.core.util.ProxiedHandlerUtil;

import org.apache.commons.lang.StringUtils;

import org.araneaframework.InputData;
import org.araneaframework.OutputData;

/**
 * An <code>ActionListener</code> that also allows multiple action parameters. Subclasses should
 * implement {@link #processAction(String, String[], InputData, OutputData)} to implement their own
 * action handling.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.2.3
 */
public abstract class MultiParamActionListener implements ActionListener {

  /**
   * This method is marked final. Subclasses should implement
   * {@link #processAction(Object, String, InputData, OutputData)}.
   */
  public final void processAction(String actionId, InputData input,
      OutputData output) throws Exception {
    Object parameter = input.getGlobalData().get(ApplicationWidget.EVENT_PARAMETER_KEY);

    if (parameter == null) {
      parameter = new String[0];
    } else if (parameter instanceof String) {
      parameter = StringUtils.split((String) parameter, getParameterSeparator((String) parameter));
    }

    processAction(actionId, (String[]) parameter, input, output);
  }

  /**
   * A method to override to use another separator for the given value.
   * 
   * @param value The value of the parameter to the action listener.
   */
  public String getParameterSeparator(String value) {
    return ProxiedHandlerUtil.DEFAULT_PARAMETER_SEPARTOR;
  }

  /**
   * Action processing method that includes an action parameter. Implementation
   * should be able to handle different <code>actionId</code>s.
   * 
   * @param actionId The ID of the incoming action.
   * @param actionParam The parameter for the action (from request under name
   *            {@link ApplicationService#ACTION_PARAMETER_KEY})
   * @param input The request data.
   * @param output The response data.
   * @throws Exception Any runtime exception that might occur.
   */
  public abstract void processAction(String actionId, String[] actionParams,
      InputData input, OutputData output) throws Exception;

}
