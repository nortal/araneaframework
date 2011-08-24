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

package org.araneaframework.core.action;

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.ApplicationService;

/**
 * A base solution for action listeners. All action listeners should implement
 * {@link #processAction(String, String, InputData, OutputData)} to provide their custom solution.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.0.4
 */
public abstract class StandardActionListener implements ActionListener {

  /**
   * @{inheritDoc}
   * <p>
   * This method is marked final. Subclasses should implement
   * {@link #processAction(String, String, InputData, OutputData)}.
   */
  public final void processAction(String actionId, InputData input, OutputData output) {
    String actionParam = input.getGlobalData().get(ApplicationService.ACTION_PARAMETER_KEY);
    processAction(actionId, actionParam, input, output);
  }

  /**
   * Action processing method that includes an action parameter. Implementation should be able to handle different
   * <code>actionId</code>s.
   * 
   * @param actionId The ID of the incoming action (not empty string).
   * @param actionParam The parameter for the action (<code>null</code>, when it's absent).
   * @param input The input data (always present).
   * @param output The output data for the incoming data (always present).
   */
  protected abstract void processAction(String actionId, String actionParam, InputData input, OutputData output);

}
