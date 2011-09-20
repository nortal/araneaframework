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

package org.araneaframework.core.exception;

import org.araneaframework.core.util.Assert;

/**
 * Exception thrown when a problem occurs with event processing (delivering, handling, etc).
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class EventException extends AraneaRuntimeException {

  /**
   * Creates a new event delivery exception.
   * 
   * @param that The object, which is throwing the exception (required).
   * @param widgetId The ID of (event target) widget that failed.
   * @param eventId The ID of the registered event handler that was supposed to handle the event.
   * @param cause An exception that was caught (optional).
   */
  public EventException(Object that, String widgetId, String eventId, Exception cause) {
    super("Widget '" + widgetId + "' could not deliver event '" + eventId + "'." + Assert.thisToString(that), cause);
  }
}
