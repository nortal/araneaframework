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
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DefaultEvent extends Event {
  private List updateRegionNames;

  public List getUpdateRegionNames() {
    return updateRegionNames;
  }

  public void setUpdateRegionNames(List updateRegionNames) {
    this.updateRegionNames = updateRegionNames;
  }
  
  public StringBuffer getEventAttributes() {
    StringBuffer result = new StringBuffer();
    result.append(AraneaAttributes.EVENT_ID).append("=\"").append(getId()).append("\" ");

    if (getTarget() != null)
      result.append(AraneaAttributes.TARGET_WIDGET_ID).append("=\"").append(getTarget()).append("\" ");
    if (getParam() != null)
      result.append(AraneaAttributes.EVENT_PARAM).append("=\"").append(getParam()).append("\" ");
    if (getUpdateRegionNames() != null && !getUpdateRegionNames().isEmpty())
      result.append(AraneaAttributes.UPDATE_REGIONS).append("=\"").append(JspUpdateRegionUtil.formatUpdateRegionsJS(getUpdateRegionNames())).append("\"");

    return result;
  }

  public void reset() {
    super.reset();
    updateRegionNames = null;
  }
}
