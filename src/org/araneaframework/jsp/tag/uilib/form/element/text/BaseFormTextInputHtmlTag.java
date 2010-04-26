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

package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;

/**
 * Standard text input form element base tag.
 * 
 * @author Oleg Mürk
 */
public class BaseFormTextInputHtmlTag extends BaseFormElementHtmlTag {

  protected Long size;

  protected String onChangePrecondition;

  protected String disabledRenderMode = RENDER_DISABLED_DISABLED;

  public BaseFormTextInputHtmlTag() {
    this.baseStyleClass = "aranea-text";
  }
  
  @Override
  protected int doStartTag(Writer out) throws Exception {
    int result = super.doStartTag(out);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    return result;
  }  

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Horizontal size, in characters." 
   */
  public void setSize(String size){
    this.size = evaluate("size", size, Long.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Precondition for deciding whether go to server side or not." 
   */
  public void setOnChangePrecondition(String onChangePrecondition){
    this.onChangePrecondition = evaluate("onChangePrecondition", onChangePrecondition, String.class);
  }

  /**
   * @jsp.attribute type = "java.lang.String"
   *                required = "false"
   *                description = "Specifies how to render a disabled input. Valid options are <code>'disabled'</code> and <code>'readonly'</code>. Default is <code>'disabled'</code>."
   * @since 1.1.3
   */
  public void setDisabledRenderMode(String disabledRenderMode) throws JspException {
    this.disabledRenderMode = evaluateDisabledRenderMode(disabledRenderMode);
  }

  // INPUT writing functions:

  protected void writeTextInput(Writer out, String inputType) throws Exception {
    writeTextInput(out, inputType, true, new HashMap<String, String>());
  }

  @SuppressWarnings("unchecked")
  protected void writeTextInput(Writer out, String inputType, boolean writeValue, Map<String, String> customAttributes)
      throws Exception {

    String name = this.getFullFieldId();
    StringArrayRequestControl<?>.ViewModel viewModel = (StringArrayRequestControl.ViewModel) this.controlViewModel;

    // Write
    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "id", name);
    JspUtil.writeAttribute(out, "name", name);    
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "type", inputType);
    if (writeValue)
      JspUtil.writeAttribute(out, "value", viewModel.getSimpleValue());
    JspUtil.writeAttribute(out, "size", this.size);
    JspUtil.writeAttribute(out, "tabindex", this.tabindex);
    
    for (Map.Entry<String, String> attribute : customAttributes.entrySet()) {
      JspUtil.writeAttribute(out, "" + attribute.getKey(), attribute.getValue());
    }

    if (viewModel.isDisabled()) {
      JspUtil.writeAttribute(out, this.disabledRenderMode, this.disabledRenderMode);
    }

    if (this.events && viewModel.isOnChangeEventRegistered()) {
      // We use "onblur" to simulate the textbox's "onchange" event
      // this is _not_ good, but there seems to be no other way
      JspUtil.writeAttribute(out, "onfocus", "Aranea.UI.saveValue(this)");

      if (this.onChangePrecondition == null) {
        this.onChangePrecondition = "return Aranea.UI.isChanged('" + name + "');";
      }

      this.writeSubmitScriptForUiEvent(out, "onblur", this.derivedId, "onChanged", this.onChangePrecondition,
          this.updateRegionNames);
    }
    JspUtil.writeAttributes(out, this.attributes);
    JspUtil.writeCloseStartEndTag_SS(out);
  }

  @Override
  public void doFinally() {
    super.doFinally();
    this.onChangePrecondition = null;
  }
}
