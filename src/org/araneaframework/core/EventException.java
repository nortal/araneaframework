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

/**
 * Exception thrown when event could not be delivered to receiver.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class EventException extends AraneaRuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Creates an exception, which means that the event could not be developed to
   * the addressee (widget, form control, etc) or listener.
   * 
   * @param that object which throws the exception
   * @param widgetId id of event target widget
   * @param eventId event id
   * @param cause
   */
  public EventException(Object that, String widgetId, String eventId,
      Exception cause) {
    super("Widget '" + widgetId + "' could not deliver event '" + eventId
        + "'." + Assert.thisToString(that), cause);
  }
}
