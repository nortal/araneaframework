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

package org.araneaframework.core;

import java.io.Serializable;
import org.araneaframework.InputData;

/**
 * A simple eventlistener which can be added to Composite Widgets.
 */
public interface EventListener extends Serializable {

  /**
   * Gets called when the event happens.
   * 
   * @param eventId The ID of the event.
   * @param input The request data of the event.
   * @throws Exception Any runtime exception that may occur.
   */
  public void processEvent(Object eventId, InputData input) throws Exception;

}
