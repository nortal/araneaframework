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

import java.io.Serializable;
import org.araneaframework.InputData;

/**
 * An event listener for composite widgets. Widgets can have several event listeners (each for a different event).
 * <p>
 * Just to remind, action listener depends on input (global) data, see
 * {@link org.araneaframework.core.ApplicationWidget} for more information about the parameters.
 * 
 * @see org.araneaframework.core.ApplicationWidget
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface EventListener extends Serializable {

  /**
   * Event processing method. Implementation can be able to handle different <code>eventId</code>s.
   * 
   * @param eventId The ID of the incoming event.
   * @param input The input data.
   */
  void processEvent(String eventId, InputData input);
}
