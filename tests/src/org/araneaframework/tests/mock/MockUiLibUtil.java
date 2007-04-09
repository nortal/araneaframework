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

package org.araneaframework.tests.mock;

import javax.servlet.http.HttpServletRequest;
import org.araneaframework.Widget;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.StandardPath;
import org.araneaframework.http.core.StandardServletInputData;
import org.springframework.mock.web.MockHttpServletRequest;

public class MockUiLibUtil {
  public static void emulateHandleEvent(Widget widget, String eventId, String eventParameter) throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    
    String eventPath = "";
    String eventListenerId = eventId;
    
    int lastDot = eventId.lastIndexOf(".");
    
    if (lastDot != -1) {
      eventPath = eventId.substring(0, lastDot);
      eventListenerId = eventId.substring(lastDot + 1);
    }
    
    request.addParameter(ApplicationWidget.EVENT_PARAMETER_KEY, eventParameter);
    request.addParameter(ApplicationWidget.EVENT_HANDLER_ID_KEY, eventListenerId);
    StandardServletInputData input = new StandardServletInputData(request);
    
    widget._getWidget().event(new StandardPath(eventPath), input);
  }
  
  public static void emulateHandleEventForControl(Widget widget, String eventId, String eventParameter) throws Exception {
    MockHttpServletRequest request = new MockHttpServletRequest();
    
    request.addParameter(ApplicationWidget.EVENT_PARAMETER_KEY, eventParameter);
   StandardServletInputData input = new StandardServletInputData(request);
    
    widget._getWidget().event(new StandardPath(eventId), input);
  }
  
  public static void emulateHandleRequest(Widget widget, String widgetId, HttpServletRequest request) throws Exception {
    StandardServletInputData input = new StandardServletInputData(request);
    
    widget._getWidget().update(input);
  }  
}
