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

package org.araneaframework.mock.core;

import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.EventListener;

/**
 * @author toomas
 *
 */
public class MockStandardWidget extends BaseApplicationWidget {
  private boolean eventProcessed = false;
  
  protected Object getEventId(InputData input) {
    return input.getGlobalData().get(ApplicationWidget.EVENT_HANDLER_ID_KEY);
  }
  
  public boolean getEventProcessed() {
    return eventProcessed;
  }
  
  public class MockEventListener implements EventListener {
    public void processEvent(Object eventId, InputData input) throws Exception {
      MockStandardWidget.this.eventProcessed = true;
    }    
  }
  
  public Environment getTheEnvironment() {
    return getEnvironment();
  }
}
