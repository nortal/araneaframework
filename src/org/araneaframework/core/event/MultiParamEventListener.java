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

package org.araneaframework.core.event;

import org.apache.commons.lang.StringUtils;
import org.araneaframework.InputData;
import org.araneaframework.core.util.ProxiedHandlerUtil;

/**
 * An <code>EventListener</code> that also allows multiple event parameters. Subclasses should implement
 * {@link #processEvent(String, String[], InputData)} to implement their own event handling.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.2.3
 */
public abstract class MultiParamEventListener extends StandardEventListener {

  private final String paramSeparator;

  /**
   * Creates a multi-parameter event listener with default separator.
   * 
   * @see ProxiedHandlerUtil.DEFAULT_PARAMETER_SEPARTOR
   */
  public MultiParamEventListener() {
    this(ProxiedHandlerUtil.DEFAULT_PARAMETER_SEPARTOR);
  }

  /**
   * Creates a multi-parameter event listener with a custom separator.
   * 
   * @param paramSeparator A custom parameter separator to use.
   */
  public MultiParamEventListener(String paramSeparator) {
    this.paramSeparator = paramSeparator;
  }

  /**
   * {@inheritDoc}
   * <p>
   * This method is marked final. Subclasses should implement {@link #processEvent(String, String[], InputData)}.
   */
  @Override
  protected void processEvent(String eventId, String eventParam, InputData input) {
    String[] parameter = eventParam == null ? new String[0] : StringUtils.split(eventParam, this.paramSeparator);
    processEvent(eventId, parameter, input);
  }

  /**
   * Event handling with a parameter array. Implementations can handle multiple <code>eventId</code>s.
   * 
   * @param eventId The ID of the event.
   * @param eventParams The parameters for the event (never <code>null</code>).
   * @param input The input data for the listener.
   */
  protected abstract void processEvent(String eventId, String[] eventParams, InputData input);
}
