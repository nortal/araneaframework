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

package org.araneaframework.jsp.tag.uilib.form.element.select;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.util.DisplayItemUtil;

/**
 * Standard radio select item form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *  name = "radioSelectItem"
 *  body-content = "JSP"
 *  description = "Form radio button, represents one item from UiLib 'SelectControl'."
 */
@SuppressWarnings("unchecked")
public class FormRadioSelectItemHtmlTag extends BaseFormElementHtmlTag {

  protected String value;

  protected String onChangePrecondition;

  protected String htmlId;

  protected boolean disabled;

  public FormRadioSelectItemHtmlTag() {
    this.baseStyleClass = "aranea-radio";
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    int result = super.doStartTag(out);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    return result;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    assertControlType("SelectControl");

    // Prepare
    String name = this.getFullFieldId();
    SelectControl.ViewModel viewModel = ((SelectControl.ViewModel) this.controlViewModel);

    // Write input tag
    if (this.value != null && !DisplayItemUtil.containsItem(viewModel.getSelectItems(), this.value)) {
      throw new AraneaJspException("Value '" + this.value + "' not found in values list.");
    }

    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "id", this.htmlId);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "type", "radio");
    JspUtil.writeAttribute(out, "value", this.value);
    JspUtil.writeAttribute(out, "tabindex", this.tabindex);

    if (this.disabled || viewModel.isDisabled()) {
      JspUtil.writeAttribute(out, "disabled", "disabled");
    }

    if (viewModel.isSelected(this.value)) {
      JspUtil.writeAttribute(out, "checked", "checked");
    }

    if (this.events && viewModel.isOnChangeEventRegistered()) {
      this.writeSubmitScriptForUiEvent(out, "onclick", this.derivedId, "onChanged", this.onChangePrecondition,
          this.updateRegionNames);
    }

    JspUtil.writeAttributes(out, this.attributes);
    JspUtil.writeCloseStartEndTag_SS(out);

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "The value of this radio button that will be submitted with form if this radio button is selected." 
   */
  public void setValue(String value) throws JspException  {
    this.value = evaluateNotNull("value", value, String.class);
  }
  
  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "The HTML id this radio button."
   * @since 1.1
   */
  public void setHtmlId(String htmlId) {
    this.htmlId = evaluate("htmlId", htmlId, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "Sets whether the radio item is disabled or not. By default, it's enabled."
   * @since 2.0
   */
  public void setDisabled(String disabled) {
    this.disabled = evaluate("disabled", disabled, Boolean.class);
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
}
