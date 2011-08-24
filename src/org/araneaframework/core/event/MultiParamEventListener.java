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
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.util.ProxiedHandlerUtil;

/**
 * An <code>EventListener</code> that also allows multiple event parameters. Subclasses should implement
 * {@link #processEvent(String, String[], InputData)} to implement their own event handling.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.2.3
 */
public abstract class MultiParamEventListener implements EventListener {

  /**
   * This method is marked final. Subclasses should implement {@link #processEvent(String, String[], InputData)}.
   */
  public final void processEvent(String eventId, InputData input) {
    Object parameter = input.getGlobalData().get(ApplicationWidget.EVENT_PARAMETER_KEY);

    if (parameter == null) {
      parameter = new String[0];
    } else if (parameter instanceof String) {
      parameter = StringUtils.split((String) parameter, getParameterSeparator((String) parameter));
    }

    processEvent(eventId, (String[]) parameter, input);
  }

  /**
   * A method to override to use another separator for the given value.
   * 
   * @param value The value of the parameter to the event listener.
   * @return A not empty string used for splitting the parameter value into an array (defaults to semi-colon).
   */
  protected String getParameterSeparator(String value) {
    return ProxiedHandlerUtil.DEFAULT_PARAMETER_SEPARTOR;
  }

  /**
   * Event handling with a parameter array. The array is never <code>null</code>. All implementations should handle each
   * <code>eventId</code> separately.
   * 
   * @param eventId The ID of the event.
   * @param eventParams The parameters for the event.
   * @param input The request data of the event.
   */
  protected abstract void processEvent(String eventId, String[] eventParams, InputData input);
}
