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

package org.araneaframework.jsp.tag.presentation;  

import java.io.Writer;
import java.util.List;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.util.JspUpdateRegionUtil;

/**
 * Button base tag.
 * 
 * @author Oleg Mürk
 */
public class BaseEventButtonTag extends BaseSimpleButtonTag {
  protected String disabled;
  protected String updateRegions;
  protected String globalUpdateRegions;

  protected List<String> updateRegionNames;
  protected String eventTarget;

  protected UiUpdateEvent event;
  
  {
	  event = new UiUpdateEvent();
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    int result = super.doStartTag(out);

    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, id);
    
    if (contextWidgetId == null)
      throw new AraneaJspException("'eventButton' tag can only be used in a context widget!");

    updateRegionNames = JspUpdateRegionUtil.getUpdateRegionNames(pageContext, updateRegions, globalUpdateRegions);
    event.setUpdateRegionNames(updateRegionNames);
    event.setTarget(eventTarget == null ? contextWidgetId : eventTarget);
    event.setEventPrecondition(onClickPrecondition);

    return result;
  }
  
  /** @since 1.1 */
  protected boolean isDisabled() {
    return Boolean.valueOf(disabled).booleanValue();
  }
  
  /* ***********************************************************************************
   * Tag attributes
   * ********************************************************************************* */  

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Event id." 
   */
  public void setEventId(String eventId){
    event.setId(evaluate("eventId", eventId, String.class));
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Event parameter." 
   */
  public void setEventParam(String eventParam){
	event.setParam(evaluate("eventParam", eventParam, String.class));
  }
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Enumerates the regions of markup to be updated in this widget scope. Please see <code><ui:updateRegion></code> for details." 
   */
  public void setUpdateRegions(String updateRegions){
    this.updateRegions = evaluate("updateRegions", updateRegions, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "numerates the regions of markup to be updated globally. Please see <code><ui:updateRegion></code> for details." 
   */
  public void setGlobalUpdateRegions(String globalUpdateRegions){
    this.globalUpdateRegions = evaluate("globalUpdateRegions", globalUpdateRegions, String.class);
  }
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Widget where event should be routed, default is contextwidget." 
   */
  public void setEventTarget(String eventTarget){
    this.eventTarget = evaluate("eventTarget", eventTarget, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Disables button." 
   */
  public void setDisabled(String disabled){
    this.disabled = evaluate("disabled", disabled, String.class);
  }
  
  @Override
  public void doFinally() {
	super.doFinally();
	event.clear();
  }
}
