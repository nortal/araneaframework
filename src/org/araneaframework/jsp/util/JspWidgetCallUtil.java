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

package org.araneaframework.jsp.util;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import org.araneaframework.jsp.UiEvent;
import org.araneaframework.jsp.UiUpdateEvent;

/**
 * Standard util for producing calls to UiLib widgets in various
 * container frameworks. 
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class JspWidgetCallUtil {
  public static final String SIMPLE_SUBMIT_FUNCTION = "return _ap.event(this);";

  /**
   * Write out form submit script for specified attribute of HTML element. Aranea custom HTML 
   * tag attributes (See {@link org.araneaframework.jsp.AraneaAttributes}) are expected to be
   * present for submit logic to work.
   * 
   * @param out 
   * @param attributeName HTML attribute name, ('onclick', 'onchange', ...)
   */
  public static void writeSubmitScriptForEvent(Writer out, String attributeName) throws IOException {
    JspUtil.writeOpenAttribute(out, attributeName);
    out.write(JspWidgetCallUtil.getSubmitScriptForEvent());
    JspUtil.writeCloseAttribute(out);
  }

  /**
   * Write out form submit script for specified attribute of HTML element, along with Aranea 
   * custom HTML tag attributes (See {@link org.araneaframework.jsp.AraneaAttributes}) that
   * are determined by <code>event</code> parameter.
   * 
   * @param out 
   * @param attributeName HTML attribute name, ('onclick', 'onchange', ...)
   * @param event event that should be activated when HTML element 
   */  
  public static void writeSubmitScriptForEvent(Writer out, String attributeName, UiEvent event) throws IOException {
    JspUtil.writeEventAttributes(out, event);
	JspWidgetCallUtil.writeSubmitScriptForEvent(out, attributeName);
  }

  /** 
   * Returns simple submit script for HTML element. This should be used whenever HTML 
   * element has just one event handling attribute that causes form submit, but can also 
   * be used when submit event should always take the same attributes, regardless of 
   * the DOM event that activates the submit function.
   *  
   * @return {@link #SIMPLE_SUBMIT_FUNCTION} */
  public static String getSubmitScriptForEvent() {
    return SIMPLE_SUBMIT_FUNCTION;
  }
  
  /** @since 1.0.2 */
  public static String getSubmitScriptForEvent(UiUpdateEvent event) {
    StringBuffer sb = new StringBuffer();
    sb.append("araneaPage().getSystemForm(),");
    String eventId = event.getId() != null ? "'" + event.getId() + "'" : "null";
    String eventTarget = event.getTarget() != null ? "'" + event.getTarget() + "'" : "null";
    String eventParam = event.getParam() != null ? "'" + event.getParam() + "'" : "null";
    String eventPrecondition = event.getEventPrecondition() != null ? "'" + event.getEventPrecondition() + "'" : "null";
    List updateRegionNames = event.getUpdateRegionNames() != null ? event.getUpdateRegionNames() : Collections.EMPTY_LIST;
    
    sb.append(eventId).append(",");
    sb.append(eventTarget).append(",");
    sb.append(eventParam).append(",");
    sb.append(eventPrecondition).append(",");
    sb.append("\"").append(JspUpdateRegionUtil.formatUpdateRegionsJS(updateRegionNames)).append("\"");
    sb.append(");");
    return sb.toString();
  }
}
