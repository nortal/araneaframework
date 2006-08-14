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

package org.araneaframework.jsp;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class Event {
  private String id;
  private String target;
  private String param;

  public Event() {}
  
  public Event(String id, String target, String param) {
    this.id = id;
    this.target = target;
    this.param = param;
  }
  
  public String getId() {
    return id;
  }

  public void setId(String eventId) {
    this.id = eventId;
  }

  public String getParam() {
    return param;
  }

  public void setParam(String eventParam) {
    this.param = eventParam;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String targetWidgetId) {
    this.target = targetWidgetId;
  }
  
  /** returns the event attributes in form suitable for outputting to HTML */
  public StringBuffer getEventAttributes() {
    StringBuffer result = new StringBuffer();
    result.append(AraneaAttributes.EVENT_ID).append("=\"").append(getId()).append("\" ");

    if (getTarget() != null)
      result.append(AraneaAttributes.TARGET_WIDGET_ID).append("=\"").append(getTarget()).append("\" ");
    if (getParam() != null)
      result.append(AraneaAttributes.EVENT_PARAM).append("=\"").append(getParam()).append("\"");

    return result;
  }
  
  public void clear() {
    id = target = param = null;
  }
}
