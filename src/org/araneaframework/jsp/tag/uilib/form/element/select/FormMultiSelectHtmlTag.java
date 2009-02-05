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
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.ConfigurationContextUtil;


/**
 * Standard multiselect form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "multiSelect"
 *   body-content = "JSP"
 *   description = "Form list input field, represents UiLib "MultiSelectControl"."
 */
public class FormMultiSelectHtmlTag extends BaseFormElementHtmlTag {

  protected Long size;

  /**
   * A boolean setting to override default configuration of
   * {@link ConfigurationContext#LOCALIZE_FIXED_CONTROL_DATA}.
   * 
   * @since 1.2
   */
  protected Boolean localizeDisplayItems;

  {
    baseStyleClass = "aranea-multi-select";
  }

  protected int doStartTag(Writer out) throws Exception {
    int r = super.doStartTag(out);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    return r;
  }

  protected int doEndTag(Writer out) throws Exception {
    assertControlType("MultiSelectControl");    
    
    // Prepare
    String name = this.getFullFieldId();     
    MultiSelectControl.ViewModel viewModel = ((MultiSelectControl.ViewModel)controlViewModel);
    
    // Write input tag
    JspUtil.writeOpenStartTag(out, "select");
    JspUtil.writeAttribute(out, "id", name);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "multiple", "multiple");
    JspUtil.writeAttribute(out, "size", size);
    JspUtil.writeAttribute(out, "tabindex", tabindex);

    if (viewModel.isOnChangeEventRegistered()) {
      this.writeSubmitScriptForUiEvent(out, "onchange", derivedId, "onChanged", "", updateRegionNames);
    }

    if (viewModel.isDisabled()) {
      JspUtil.writeAttribute(out, "disabled", "disabled");
    }

    JspUtil.writeAttributes(out, attributes);
    writeBackgroundValidationAttribute(out);
    JspUtil.writeCloseStartTag(out);      

    if (this.localizeDisplayItems == null) {
      this.localizeDisplayItems = ConfigurationContextUtil
          .isLocalizeControlData(getEnvironment());
    }

    for(Iterator i = viewModel.getSelectItems().iterator(); i.hasNext();) {
      DisplayItem item = (DisplayItem)i.next();
      String value = item.getValue();
      String label = item.getDisplayString();

      if (this.localizeDisplayItems.booleanValue()) {
        label = JspUtil.getResourceString(pageContext, label);
      }

      JspUtil.writeOpenStartTag(out, "option");      
      JspUtil.writeAttribute(out, "value", value != null ? value : "");

      if (viewModel.getValueSet().contains(value)) {
        JspUtil.writeAttribute(out, "selected", "selected");
      }

      JspUtil.writeCloseStartTag_SS(out);
      JspUtil.writeEscaped(out, label);
      JspUtil.writeEndTag(out, "option");
    }
    
    // Close tag
    JspUtil.writeEndTag_SS(out, "select");
    
    super.doEndTag(out);
    return EVAL_PAGE;  
  }
  
  //
  // Attributes
  //  
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Vertical size, number of options displayed." 
   */
  public void setSize(String size) throws JspException {
    this.size = (Long)evaluate("size", size, Long.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Whether to localize display items. Provides a way to override ConfigurationContext.LOCALIZE_FIXED_CONTROL_DATA."
   * 
   * @since 1.2
   */
  public void setLocalizeDisplayItems(String localizeDisplayItems) throws JspException {
    this.localizeDisplayItems = (Boolean) evaluateNotNull(
        "localizeDisplayItems", localizeDisplayItems, Boolean.class);
  }

}
