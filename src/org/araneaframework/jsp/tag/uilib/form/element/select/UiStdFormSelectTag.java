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

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseTag;
import org.araneaframework.jsp.util.UiUtil;
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
public class UiStdFormSelectTag extends UiFormElementBaseTag {
  protected Long size = null;
  protected String onChangePrecondition = "return true;";

  public UiStdFormSelectTag() {
    styleClass = "aranea-select";
  }

  public int doEndTag(Writer out) throws Exception {
    // Type check
    assertControlType("SelectControl");    

    // Prepare
    String name = this.getScopedFullFieldId();     
    SelectControl.ViewModel viewModel = ((SelectControl.ViewModel)controlViewModel);

    // Write input tag
    UiUtil.writeOpenStartTag(out, "select");
    UiUtil.writeAttribute(out, "id", name);    
    UiUtil.writeAttribute(out, "name", name);
    UiUtil.writeAttribute(out, "class", getStyleClass());
    UiUtil.writeAttribute(out, "label", localizedLabel);
    UiUtil.writeAttribute(out, "tabindex", tabindex);
    UiUtil.writeAttribute(out, "size", size);

    if (viewModel.isDisabled())
      UiUtil.writeAttribute(out, "disabled", "true");
    if (events && viewModel.isOnChangeEventRegistered())
      this.writeEventAttributeForUiEvent(out, "onchange", derivedId, "onChanged", validateOnEvent, onChangePrecondition,
          updateRegionNames);
    UiUtil.writeAttributes(out, attributes);
    UiUtil.writeCloseStartTag(out);

    // Write items
    String selectedValue = viewModel.getSimpleValue();

    for(Iterator i = viewModel.getSelectItems().iterator(); i.hasNext();) {
      DisplayItem item = (DisplayItem)i.next();
      if (!item.isDisabled()) {
        String value = item.getValue();
        String label = item.getDisplayString();

        UiUtil.writeOpenStartTag(out, "option");      
        UiUtil.writeAttribute(out, "value", value != null ? value : "");
        if ((value == null && selectedValue == null) ||              
            (value != null && value.equals(selectedValue)))
          UiUtil.writeAttribute(out, "selected", "true");
        UiUtil.writeCloseStartTag_SS(out);
        UiUtil.writeEscaped(out, label);
        UiUtil.writeEndTag(out, "option");
      }
    }

    // Close tag
    UiUtil.writeEndTag_SS(out, "select");

    if (validate) writeValidationScript(out, viewModel);

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

  /**
   * Write validation javascript
   * @author Konstantin Tretyakov
   */
  protected void writeValidationScript(Writer out, SelectControl.ViewModel viewModel) throws IOException {
    UiUtil.writeStartTag(out, "script");
    out.write("uiAddSelectValidator(");
    UiUtil.writeScriptString(out, getScopedFullFieldId());
    out.write(", ");
    UiUtil.writeScriptString(out, localizedLabel);
    out.write(", ");
    out.write(viewModel.isMandatory() ? "true" : "false");
    out.write(");\n");
    UiUtil.writeEndTag_SS(out, "script");
  }      
}
