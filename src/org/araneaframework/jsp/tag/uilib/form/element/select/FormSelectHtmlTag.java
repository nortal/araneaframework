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

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.Path;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.control.BaseSelectControl.ViewModel;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.ConfigurationUtil;

/**
 * Standard select form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *  name = "select"
 *  body-content = "JSP"
 *  description = "Form drop-down list input field, represents UiLib 'SelectControl'."
 */
@SuppressWarnings("unchecked")
public class FormSelectHtmlTag extends BaseFormElementHtmlTag {

  protected Long size = null;

  protected String onChangePrecondition;

  /**
   * A boolean setting to override default configuration of {@link ConfigurationContext#LOCALIZE_FIXED_CONTROL_DATA}.
   * 
   * @since 1.2
   */
  protected Boolean localizeDisplayItems;

  
  public FormSelectHtmlTag() {
    this.baseStyleClass = "aranea-select";
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    int r = super.doStartTag(out);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    return r;
  }

  @Override
  public int doEndTag(Writer out) throws Exception {
    assertControlType("SelectControl");

    // Prepare
    String name = this.getFullFieldId();
    SelectControl<Object>.ViewModel viewModel = (ViewModel) controlViewModel;

    // Write input tag
    JspUtil.writeOpenStartTag(out, "select");
    JspUtil.writeAttribute(out, "id", name);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "tabindex", tabindex);
    JspUtil.writeAttribute(out, "size", size);

    if (viewModel.isDisabled()) {
      JspUtil.writeAttribute(out, "disabled", "disabled");
    }

    if (this.events && viewModel.isOnChangeEventRegistered()) {
      UiUpdateEvent event =
          new UiUpdateEvent(OnChangeEventListener.ON_CHANGE_EVENT,
                            this.formFullId + Path.SEPARATOR + this.derivedId,
                            null,
                            this.updateRegionNames);
      event.setEventPrecondition(this.onChangePrecondition);
      JspUtil.writeEventAttributes(out, event);
      JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onchange");
    }

    JspUtil.writeAttributes(out, this.attributes);
    JspUtil.writeCloseStartTag(out);

    this.localizeDisplayItems = ConfigurationUtil.isLocalizeControlData(getEnvironment(), this.localizeDisplayItems);

    // Write items
    writeItems(out, viewModel);

    // Close tag
    JspUtil.writeEndTag_SS(out, "select");

    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;
  }

  private void writeItems(Writer out, SelectControl<Object>.ViewModel viewModel) throws IOException {
    for (DisplayItem item : viewModel.getSelectItems()) {
      if (item.isDisabled()) {
        continue;
      }
      if (item.isGroup()) {
        writeOptionGroup(out, viewModel, item);
      } else {
        writeOption(out, viewModel, item);
      }
    }
  }

  private void writeOptionGroup(Writer out, SelectControl<Object>.ViewModel viewModel, DisplayItem group)
      throws IOException {
    String label = group.getLabel();

    if (this.localizeDisplayItems) {
      label = JspUtil.getResourceString(this.pageContext, label);
    }

    JspUtil.writeOpenStartTag(out, "optgroup");
    JspUtil.writeAttribute(out, "label", label != null ? label : "");
    JspUtil.writeCloseStartTag_SS(out);

    for (DisplayItem child : group.getChildOptions()) {
      if (!child.isDisabled()) {
        writeOption(out, viewModel, child);
      }
    }

    JspUtil.writeEndTag(out, "optgroup");
  }

  private void writeOption(Writer out, SelectControl<Object>.ViewModel viewModel, DisplayItem item) throws IOException {
    String value = item.getValue();
    String label = item.getLabel();

    if (this.localizeDisplayItems) {
      label = JspUtil.getResourceString(this.pageContext, label);
    }

    JspUtil.writeOpenStartTag(out, "option");
    JspUtil.writeAttribute(out, "value", value != null ? value : "");

    if (viewModel.isSelected(value)) {
      JspUtil.writeAttribute(out, "selected", "selected");
    }

    JspUtil.writeCloseStartTag_SS(out);
    JspUtil.writeEscaped(out, label);
    JspUtil.writeEndTag(out, "option");
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Number of select elements visible at once."
   */
  public void setSize(String size) {
    this.size = evaluate("size", size, Long.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Precondition for deciding whether go to server side or not."
   */
  public void setOnChangePrecondition(String onChangePrecondition) {
    this.onChangePrecondition = evaluate("onChangePrecondition", onChangePrecondition, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Whether to localize display items. Provides a way to override ConfigurationContext.LOCALIZE_FIXED_CONTROL_DATA."
   * @since 1.2
   */
  public void setLocalizeDisplayItems(String localizeDisplayItems) throws JspException {
    this.localizeDisplayItems = evaluateNotNull("localizeDisplayItems", localizeDisplayItems, Boolean.class);
  }

}
