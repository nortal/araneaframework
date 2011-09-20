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

package org.araneaframework.jsp;

/**
 * Basic event that can be sent to widgets. Holds all the event properties necessary to deliver the event.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class UiEvent {

  private String id;

  private String target;

  private String param;

  /**
   * Default constructor without specifying any values.
   */
  public UiEvent() {
  }

  /**
   * Constructor with values.
   * 
   * @param id The event ID.
   * @param target The path of the target that should respond to this event.
   * @param param A <code>String</code> parameter for the event listener.
   */
  public UiEvent(String id, String target, String param) {
    this.id = id;
    this.target = target;
    this.param = param;
  }

  /**
   * Provides the event ID.
   * 
   * @return the event ID (or <code>null</code>).
   */
  public String getId() {
    return this.id;
  }

  /**
   * Specifies the event ID.
   * 
   * @param eventId The new event ID.
   */
  public void setId(String eventId) {
    this.id = eventId;
  }

  /**
   * Provides the parameter for the event handler.
   * 
   * @return the parameter for the event handler (or <code>null</code>).
   */
  public String getParam() {
    return this.param;
  }

  /**
   * Specifies the parameter for the event handler.
   * 
   * @param eventParam the new parameter for the event handler.
   */
  public void setParam(String eventParam) {
    this.param = eventParam;
  }

  /**
   * Provides the path of the target.
   * 
   * @return the path of the target (or <code>null</code>).
   */
  public String getTarget() {
    return this.target;
  }

  /**
   * Specifies the path of the target.
   * 
   * @param targetWidgetId the new path of the target.
   */
  public void setTarget(String targetWidgetId) {
    this.target = targetWidgetId;
  }

  /**
   * Provides the event attributes in a form suitable for outputting to HTML element as attributes.
   * 
   * @return the event attributes.
   */
  public StringBuffer getEventAttributes() {
    StringBuffer result = new StringBuffer();
    result.append(AraneaAttributes.Event.ID).append("=\"").append(getId()).append("\" ");

    if (getTarget() != null) {
      result.append(AraneaAttributes.Event.TARGET_WIDGET_ID).append("=\"").append(getTarget()).append("\" ");
    }
    if (getParam() != null) {
      result.append(AraneaAttributes.Event.PARAM).append("=\"").append(getParam()).append("\"");
    }

    return result;
  }

  /**
   * Resets the event properties to <code>null</code>.
   */
  public void clear() {
    this.id = this.target = this.param = null;
  }

}
