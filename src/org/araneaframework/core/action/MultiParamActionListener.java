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

import org.apache.commons.lang.StringUtils;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ProxiedHandlerUtil;

/**
 * An <code>ActionListener</code> that also allows multiple action parameters. Subclasses should implement
 * {@link #processAction(String, String[], InputData, OutputData)} to implement their own action handling.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.2.3
 */
public abstract class MultiParamActionListener extends StandardActionListener {

  private final String paramSeparator;

  /**
   * Creates a multi-parameter action listener with default separator.
   * 
   * @see ProxiedHandlerUtil.DEFAULT_PARAMETER_SEPARTOR
   */
  public MultiParamActionListener() {
    this(ProxiedHandlerUtil.DEFAULT_PARAMETER_SEPARTOR);
  }

  /**
   * Creates a multi-parameter action listener with a custom separator.
   * 
   * @param paramSeparator A custom parameter separator to use.
   */
  public MultiParamActionListener(String paramSeparator) {
    Assert.notNullParam(paramSeparator, "paramSeparator");
    this.paramSeparator = paramSeparator;
  }

  /**
   * {@inheritDoc}
   * <p>
   * This method is marked final. Subclasses should implement
   * {@link #processAction(String, String[], InputData, OutputData)}.
   */
  @Override
  protected final void processAction(String actionId, String actionParam, InputData input, OutputData output) {
    String[] parameter = actionParam == null ? new String[0] : StringUtils.split(actionParam, this.paramSeparator);
    processAction(actionId, parameter, input, output);
  }

  /**
   * Action processing method that includes an action parameter. Implementation should be able to handle different
   * <code>actionId</code>s.
   * 
   * @param actionId The ID of the incoming action.
   * @param actionParams The parameters for the event (never <code>null</code>).
   * @param input The input data.
   * @param output The output data.
   */
  protected abstract void processAction(String actionId, String[] actionParams, InputData input, OutputData output);
}
