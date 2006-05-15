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

package org.araneaframework.jsp.tag.uilib.list;  

import java.io.Writer;
import java.util.List;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.tag.presentation.UiSimpleButtonBaseTag;
import org.araneaframework.jsp.util.UiUpdateRegionUtil;

/**
 * List row button base tag.
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class UiListRowButtonBaseTag extends UiSimpleButtonBaseTag {

  protected String eventId;
  protected String eventParam;
  protected String updateRegions;
  protected String globalUpdateRegions;  

  protected List updateRegionNames;

  protected int doStartTag(Writer out) throws Exception {
    int result = super.doStartTag(out);

    if (contextWidgetId == null)
      throw new UiException("'listRow(Link)Button' tags can only be used in a context widget!");

    eventParam = (String) requireContextEntry(UiListRowsTag.ROW_REQUEST_ID_KEY);

    updateRegionNames = UiUpdateRegionUtil.getUpdateRegionNames(pageContext, updateRegions, globalUpdateRegions);

    return result;
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Event id." 
   */
  public void setEventId(String eventId) throws JspException {
    this.eventId = (String)evaluate("eventId", eventId, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Enumerates the regions of markup to be updated in this widget scope. Please see <code><ui:updateRegion></code> for details." 
   */
  public void setUpdateRegions(String updateRegions) throws JspException {
    this.updateRegions = (String) evaluate("updateRegions", updateRegions, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Enumerates the regions of markup to be updated globally. Please see <code><ui:updateRegion></code> for details." 
   */
  public void setGlobalUpdateRegions(String globalUpdateRegions) throws JspException {
    this.globalUpdateRegions = (String) evaluate("globalUpdateRegions", globalUpdateRegions, String.class);
  }  
}




