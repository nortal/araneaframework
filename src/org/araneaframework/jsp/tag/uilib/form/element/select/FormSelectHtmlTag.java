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

package org.araneaframework.jsp.tag.uilib.form.element.select;

import java.io.Writer;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.support.DisplayItem;


/**
 * Standard select form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "select"
 *   body-content = "JSP"
 *   description = "Form dropdown list input field, represents UiLib "SelectControl"."
 */
public class FormSelectHtmlTag extends BaseFormElementHtmlTag {
  protected Long size = null;
  protected String onChangePrecondition;

  {
    baseStyleClass = "aranea-select";
  }
  
  protected int doStartTag(Writer out) throws Exception {
    int r = super.doStartTag(out);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    return r;
  }

  public int doEndTag(Writer out) throws Exception {
    // Type check
    assertControlType("SelectControl");    

    // Prepare
    String name = this.getFullFieldId();     
    SelectControl.ViewModel viewModel = ((SelectControl.ViewModel)controlViewModel);

    // Write input tag
    JspUtil.writeOpenStartTag(out, "select");
    JspUtil.writeAttribute(out, "id", name);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "tabindex", tabindex);
    JspUtil.writeAttribute(out, "size", size);

    if (viewModel.isDisabled())
      JspUtil.writeAttribute(out, "disabled", "disabled");
    if (events && viewModel.isOnChangeEventRegistered()) {
      UiUpdateEvent event = new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT, formFullId + "." + derivedId, null, updateRegionNames);
      event.setEventPrecondition(onChangePrecondition);
      JspUtil.writeEventAttributes(out, event);
      JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onchange");
    }
    JspUtil.writeAttributes(out, attributes);
    writeBackgroundValidationAttribute(out);
    JspUtil.writeCloseStartTag(out);

    // Write items
    String selectedValue = viewModel.getSimpleValue();

    for(Iterator i = viewModel.getSelectItems().iterator(); i.hasNext();) {
      DisplayItem item = (DisplayItem)i.next();
      if (!item.isDisabled()) {
        String value = item.getValue();
        String label = item.getDisplayString();

        JspUtil.writeOpenStartTag(out, "option");      
        JspUtil.writeAttribute(out, "value", value != null ? value : "");
        if ((value == null && selectedValue == null) ||              
            (value != null && value.equals(selectedValue)))
          JspUtil.writeAttribute(out, "selected", "selected");
        JspUtil.writeCloseStartTag_SS(out);
        JspUtil.writeEscaped(out, label);
        JspUtil.writeEndTag(out, "option");
      }
    }

    // Close tag
    JspUtil.writeEndTag_SS(out, "select");

    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;  
  }

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Number of select elements visible at once." 
   */
  public void setSize(String size) throws JspException {
    this.size = (Long)evaluate("size", size, Long.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Precondition for deciding whether go to server side or not." 
   */
  public void setOnChangePrecondition(String onChangePrecondition)throws JspException {
    this.onChangePrecondition = (String) evaluate("onChangePrecondition", onChangePrecondition, String.class);
  }
}
