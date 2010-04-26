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
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.tag.uilib.WidgetTag;
import org.araneaframework.jsp.util.JspUpdateRegionUtil;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;

/**
 * Base class for list row form elements.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.1.4
 */
public abstract class BaseListRowControlTag extends PresentationTag {

  /**
   * A custom label for the control.
   */
  protected String labelId;

  /**
   * Specifies whether the control should be rendered as disabled. Default is active state.
   */
  protected boolean disabled;

  /**
   * Specifies custom <code>onclick</code> event. Default is none. Note that this is used when the event information is
   * missing. Otherwise, use 'eventPrecondition' attribute.
   */
  protected String onclick;

  /**
   * Specifies custom <code>acceskey</code> (defined by HTML). Default is none.
   */
  protected String accesskey;

  /**
   * HTML tabindex for the form element. This value must be a number between 0 and 32767.
   */
  protected String tabindex;

  /**
   * Specifies the initial state of the form control. Default is unchecked.
   */
  protected boolean checked;

  /**
   * The name of the event handler (in the widget that contains the list) that
   * will be invoked when the selection changes.
   */
  protected String onClickEventId;

  /**
   * The script that will be called before the submit.
   */
  protected String eventPrecondition;

  /**
   * Update regions that must be updated.
   */
  protected String updateRegions;

  /**
   * Global update regions that must be updated.
   */
  protected String globalUpdateRegions;


  protected void writeOnClickEvent(Writer out) throws Exception {
    if (this.onClickEventId != null) {
      UiUpdateEvent event = new UiUpdateEvent();
      event.setId(this.onClickEventId);
      event.setParam((String) requireContextEntry(BaseListRowsTag.ROW_REQUEST_ID_KEY));
      event.setTarget((String) requireContextEntry(WidgetTag.WIDGET_ID_KEY));
      event.setEventPrecondition(getOnclickScript());
      event.setUpdateRegionNames(JspUpdateRegionUtil.getUpdateRegionNames(this.pageContext, this.updateRegions,
          this.globalUpdateRegions));
      JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick", event);
    } else {
      JspUtil.writeAttribute(out, "onclick", getOnclickScript());
    }
  }

  /**
   * Provides "onclick" event script for the control. If none specified then <code>null</code> should be returned. Note
   * that the script is used also for Aranea events.
   * 
   * @return The script or <code>null</code>.
   */
  protected abstract String getOnclickScript();

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Specifies a custom label for the control."
   */
  public void setLabelId(String labelId) throws JspException {
    this.labelId = evaluateNotNull("labelId", labelId, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Specifies whether the control should be rendered as disabled. Default is active state."
   */
  public void setDisabled(String disabled) throws JspException {
    Boolean tempResult = evaluateNotNull("disabled", disabled, Boolean.class);
    this.disabled = tempResult.booleanValue();
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Specifies custom <code>onclick</code> event. Default is none."
   */
  public void setOnclick(String onclick) throws JspException {
    this.onclick = evaluateNotNull("onclick", onclick, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Specifies custom <code>acceskey</code> (defined by HTML). Default is none."
   */
  public void setAccessKey(String accessKey) throws JspException {
    this.accesskey = evaluateNotNull("accessKey", accessKey, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Specifies the initial state of the control. Default is unchecked."
   */
  public void setChecked(String checked) throws JspException {
    this.checked = evaluateNotNull("checked", checked, Boolean.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "HTML tabindex for the form element. This value must be a number between 0 and 32767."
   */
  public void setTabindex(String tabindex) throws JspException {
    this.tabindex = evaluateNotNull("tabindex", tabindex, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "The name of the event handler (in the widget that contains the list) that will be invoked when the selection changes."
   */   
  public void setOnChangeEventId(String onChangeEventId) throws JspException {
    this.onClickEventId = evaluateNotNull("onChangeEventId", onChangeEventId, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "The script that will be called before the submit."
   */
  public void setEventPrecondition(String eventPrecondition) throws JspException {
    this.eventPrecondition = evaluateNotNull("eventPrecondition", eventPrecondition, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Update regions that must be updated."
   */
  public void setUpdateRegions(String updateRegions) throws JspException {
    this.updateRegions = evaluateNotNull("updateRegions", updateRegions, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Global update regions that must be updated."
   */
  public void setGlobalUpdateRegions(String globalUpdateRegions) throws JspException {
    this.globalUpdateRegions = evaluateNotNull("globalUpdateRegions", globalUpdateRegions, String.class);
  }

}
