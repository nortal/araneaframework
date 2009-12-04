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

package org.araneaframework.jsp.tag.uilib.list;  

import java.io.Writer;
import java.util.List;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.presentation.BaseSimpleButtonTag;
import org.araneaframework.jsp.util.JspUpdateRegionUtil;

/**
 * List row button base tag.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class BaseListRowButtonTag extends BaseSimpleButtonTag {

  protected String updateRegions;

  protected String globalUpdateRegions;

  protected List<String> updateRegionNames;

  protected UiUpdateEvent event;

  
  public BaseListRowButtonTag() {
    this.event = new UiUpdateEvent();
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    int result = super.doStartTag(out);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, id);

    if (this.contextWidgetId == null) {
      throw new AraneaJspException("'listRow(Link)Button' tags can only be used in a context widget!");
    }

    this.event.setParam((String) requireContextEntry(BaseListRowsTag.ROW_REQUEST_ID_KEY));
    this.event.setTarget(this.contextWidgetId);
    this.event.setUpdateRegionNames(JspUpdateRegionUtil.getUpdateRegionNames(this.pageContext, this.updateRegions,
        this.globalUpdateRegions));
    this.event.setEventPrecondition(this.onClickPrecondition);

    return result;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Event id."
   */
  public void setEventId(String eventId) {
    this.event.setId(evaluate("eventId", eventId, String.class));
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Enumerates the regions of markup to be updated in this widget scope. Please see <code><ui:updateRegion></code> for details."
   */
  public void setUpdateRegions(String updateRegions) {
    this.updateRegions = evaluate("updateRegions", updateRegions, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Enumerates the regions of markup to be updated globally. Please see <code><ui:updateRegion></code> for details."
   */
  public void setGlobalUpdateRegions(String globalUpdateRegions) {
    this.globalUpdateRegions = evaluate("globalUpdateRegions", globalUpdateRegions, String.class);
  }

  @Override
  public void doFinally() {
    super.doFinally();
    this.event.clear();
  }
}
