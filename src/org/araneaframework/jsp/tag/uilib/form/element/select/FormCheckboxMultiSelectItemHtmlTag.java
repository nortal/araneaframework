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

import org.apache.commons.lang.ArrayUtils;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.MultiSelectControl;


/**
 * Form multiselect rendering tag, represents one item from {@link org.araneaframework.uilib.form.control.MultiSelectControl}. 
 * Default <code>styleClass</code> is &quot;aranea-multi-checkbox&quot;. 
 * It will be rendered with HTML &lt;input type=&quot;checkbox&quot;&gt; tag.
 * 
 * @author Oleg Mürk
 * @author Jevgeni Kabanov
 * <br>
 * @jsp.tag
 *   name = "checkboxMultiSelectItem"
 *   body-content = "JSP"
 *   description = "Form checkbox, represents one item from UiLib 'MultiSelectControl' rendered with checkboxes." 
 */
@SuppressWarnings("unchecked")
public class FormCheckboxMultiSelectItemHtmlTag extends BaseFormElementHtmlTag {

  protected String value;

  /**
   * The custom specified ID for the tag. The value should be used when setting the ID of the rendered HTML tag.
   * @since 1.1
   */
  protected String htmlId;

  
  public FormCheckboxMultiSelectItemHtmlTag() {
    this.baseStyleClass = "aranea-multi-checkbox";
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    int r = super.doStartTag(out);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    return r;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    // Type check
    assertControlType("MultiSelectControl");

    // Prepare
    String name = getFullFieldId();
    MultiSelectControl.ViewModel viewModel = ((MultiSelectControl.ViewModel) controlViewModel);

    if (viewModel.getSelectedItem() == null) {
      throw new AraneaJspException("Value '" + value + "' not found in values list.");
    }

    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "id", htmlId);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "type", "checkbox");
    JspUtil.writeAttribute(out, "value", this.value);
    JspUtil.writeAttribute(out, "tabindex", this.tabindex);

    if (viewModel.isDisabled() || viewModel.getSelectedItem().isDisabled()) {
      JspUtil.writeAttribute(out, "disabled", "disabled");
    }

    if (ArrayUtils.contains(viewModel.getValues(), this.value)) {
      JspUtil.writeAttribute(out, "checked", "disabled");
    }

    JspUtil.writeAttributes(out, this.attributes);

    if (this.events && viewModel.isOnChangeEventRegistered()) {
      this.writeSubmitScriptForUiEvent(out, "onclick", this.derivedId, "onChanged", "", this.updateRegionNames);
    }

    writeBackgroundValidationAttribute(out);

    JspUtil.writeCloseStartEndTag_SS(out);

    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "The value of this checkbox that will be submitted with form if this checkbox is selected."
   */
  public void setValue(String value) throws JspException {
    this.value = evaluateNotNull("value", value, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Sets the HTML id of this checkbox button."
   * @since 1.1
   */
  public void setHtmlId(String htmlId) {
    this.htmlId = evaluate("htmlId", htmlId, String.class);
  }
}
