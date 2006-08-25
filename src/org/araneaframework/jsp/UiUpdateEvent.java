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
 * More advanced event that can be sent to widgets, includes event precondition 
 * (script that will be executed and only if returns true is event actually sent)
 * and updateregions that should be updated each time when event is processed.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class UiUpdateEvent extends UiEvent {
  private List updateRegionNames;
  private String eventPrecondition;

  public UiUpdateEvent() {}

  public UiUpdateEvent(String id, String target, String param) {
	super(id, target, param);
  }
  
  public UiUpdateEvent(String id, String target, String param, List updateRegionNames) {
	super(id, target, param);
	this.updateRegionNames = updateRegionNames;
  }

  public List getUpdateRegionNames() {
    return updateRegionNames;
  }

  public void setUpdateRegionNames(List updateRegionNames) {
    this.updateRegionNames = updateRegionNames;
  }
  
  public String getEventPrecondition() {
    return eventPrecondition;
  }

  public void setEventPrecondition(String eventPrecondition) {
    this.eventPrecondition = eventPrecondition;
  }

  public StringBuffer getEventAttributes() {
    StringBuffer result = super.getEventAttributes();
    if (eventPrecondition != null)
      result.append(' ').append(AraneaAttributes.EVENT_CONDITION).append("=\"").append(eventPrecondition).append('"');
    if (getUpdateRegionNames() != null && !getUpdateRegionNames().isEmpty())
      result.append(' ').append(AraneaAttributes.UPDATE_REGIONS).append("=\"").append(JspUpdateRegionUtil.formatUpdateRegionsJS(getUpdateRegionNames())).append("\"");

    return result;
  }

  public void clear() {
    super.clear();
    updateRegionNames = null;
    eventPrecondition = null;
  }
}
