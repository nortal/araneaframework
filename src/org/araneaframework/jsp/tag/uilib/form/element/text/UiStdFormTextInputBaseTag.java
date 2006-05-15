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

package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;


/**
 * Standard text input form element base tag.
 * 
 * @author Oleg Mürk
 */
public class UiStdFormTextInputBaseTag extends UiFormElementBaseTag {
  protected Long size;
  protected String onChangePrecondition = "return true;";

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Horizontal size, in characters." 
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
  public void setOnChangePrecondition(String onChangePrecondition) throws JspException {
    this.onChangePrecondition = (String) evaluate("onChangePrecondition", onChangePrecondition, String.class);
  }

  /* ***********************************************************************************
   * INPUT writing functions
   * ***********************************************************************************/

  protected void writeTextInput(Writer out, String inputType) throws Exception {
    writeTextInput(out, inputType, true, new HashMap());
  }

  protected void writeTextInput(Writer out, String inputType, boolean writeValue, Map customAttributes) throws Exception {
    String name = this.getScopedFullFieldId();    
    StringArrayRequestControl.ViewModel viewModel = ((StringArrayRequestControl.ViewModel)controlViewModel);

    // Write
    UiUtil.writeOpenStartTag(out, "input");
    UiUtil.writeAttribute(out, "id", getScopedFullFieldId());
    UiUtil.writeAttribute(out, "name", name);    
    UiUtil.writeAttribute(out, "class", getStyleClass());
    UiUtil.writeAttribute(out, "type", inputType);
    if (writeValue)
      UiUtil.writeAttribute(out, "value", viewModel.getSimpleValue());
    UiUtil.writeAttribute(out, "size", size);
    UiUtil.writeAttribute(out, "label", localizedLabel);
    UiUtil.writeAttribute(out, "tabindex", tabindex);

    for (Iterator i = customAttributes.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry attribute = (Map.Entry) i.next();
      UiUtil.writeAttribute(out, "" + attribute.getKey(), "" + attribute.getValue());
    }

    if (viewModel.isDisabled())
      UiUtil.writeAttribute(out, "disabled", "true");
    if (events && viewModel.isOnChangeEventRegistered())
      // We use "onblur" to simulate the textbox's "onchange" event
      // this is _not_ good, but there seems to be no other way
      this.writeEventAttributeForUiEvent(out, "onblur", derivedId, "onChanged", validateOnEvent, onChangePrecondition,
          updateRegionNames);
    UiUtil.writeAttributes(out, attributes);
    UiUtil.writeCloseStartEndTag_SS(out);
  }
}




