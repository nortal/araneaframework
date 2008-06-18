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

import java.util.List;
import org.araneaframework.jsp.util.JspUpdateRegionUtil;

/**
 * More advanced event that can be sent to widgets. Includes
 * <ul>
 * <li>event precondition - a client-side script that will be executed and,
 * only if returns <code>true</code>, is event actually sent,</li>
 * <li>update regions - regions that should be updated each time when event is
 * processed.</li>
 * </ul>
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class UiUpdateEvent extends UiEvent {

  private List<String> updateRegionNames;

  private String eventPrecondition;

  /**
   * A constructor without any field initialization.
   */
  public UiUpdateEvent() {}

  /**
   * A constructor that initializes all the common event fields.
   * 
   * @param id The event ID.
   * @param target The path of the target that should respond to this event.
   * @param param A <code>String</code> parameter for the event listener.
   */
  public UiUpdateEvent(String id, String target, String param) {
	super(id, target, param);
  }

  /**
   * A constructor that initializes all the fields, except
   * <code>eventPrecondition</code>.
   * 
   * @param id The event ID.
   * @param target The path of the target that should respond to this event.
   * @param param A <code>String</code> parameter for the event listener.
   * @param updateRegionNames comma-separated region names
   */
  public UiUpdateEvent(String id, String target, String param, List<String> updateRegionNames) {
	super(id, target, param);
	this.updateRegionNames = updateRegionNames;
  }

  /**
   * Provides the comma-separated region names that should be updated when the
   * response arrives to the client-side.
   * 
   * @return the comma-separated region names or <code>null</code>.
   */
  public List<String> getUpdateRegionNames() {
    return updateRegionNames;
  }

  /**
   * Specifies the comma-separated region names that should be updated when the
   * response arrives to the client-side.
   * 
   * @param updateRegionNames the comma-separated region names or <code>null</code>.
   */
  public void setUpdateRegionNames(List<String> updateRegionNames) {
    this.updateRegionNames = updateRegionNames;
  }

  /**
   * Provides the event precondition that returns whether the event should
   * occur.
   * 
   * @return the event precondition or <code>null</code>.
   */
  public String getEventPrecondition() {
    return eventPrecondition;
  }

  /**
   * Specifies the event precondition that returns whether the event should
   * occur.
   * 
   * @param eventPrecondition the event precondition or <code>null</code>.
   */
  public void setEventPrecondition(String eventPrecondition) {
    this.eventPrecondition = eventPrecondition;
  }

  @Override
  public StringBuffer getEventAttributes() {
    StringBuffer result = super.getEventAttributes();
    if (eventPrecondition != null) {
      result.append(" ").append(AraneaAttributes.Event.CONDITION);
      result.append("=\"");
      result.append(eventPrecondition).append('"');
    }

    List<String> updtRgns = getUpdateRegionNames();
    if (updtRgns != null && !updtRgns.isEmpty()) {
      result.append(" ");
      result.append(AraneaAttributes.Event.UPDATE_REGIONS);
      result.append("=\"");
      result.append(JspUpdateRegionUtil.formatUpdateRegionsJS(updtRgns));
      result.append("\"");
    }

    return result;
  }

  @Override
  public void clear() {
    super.clear();
    updateRegionNames = null;
    eventPrecondition = null;
  }
}
