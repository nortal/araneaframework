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

import org.araneaframework.InputData;
import org.araneaframework.core.ApplicationWidget;

/**
 * An <code>EventListener</code> that also allows event parameters. Subclasses should implement
 * {@link #processEvent(String, String, InputData)} to provide their own event handling.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class StandardEventListener implements EventListener {

  /**
   * This method is marked final. Subclasses should implement {@link #processEvent(String, String, InputData)}.
   */
  public final void processEvent(String eventId, InputData input) {
    String eventParameter = input.getGlobalData().get(ApplicationWidget.EVENT_PARAMETER_KEY);
    processEvent(eventId, eventParameter, input);
  }

  /**
   * Event processing method that includes an event parameter. Implementation should be able to handle different
   * <code>eventId</code>s.
   * 
   * @param eventId The ID of the incoming event (not empty string).
   * @param eventParam The parameter for the event (<code>null</code>, when it's absent).
   * @param input The input data (always present).
   * @param output The output data for the incoming data (always present).
   */
  protected abstract void processEvent(String eventId, String eventParam, InputData input);
}
