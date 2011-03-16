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
import java.util.List;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.control.BaseSelectControl.ViewModel;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.support.DisplayItemGroup;
import org.araneaframework.uilib.util.ConfigurationUtil;

/**
 * Standard multi-select form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *  name = "multiSelect"
 *  body-content = "JSP"
 *  description = "Form list input field, represents UiLib 'MultiSelectControl'."
 */
public class FormMultiSelectHtmlTag extends BaseFormElementHtmlTag {

  protected Long size;

  /**
   * A boolean setting to override default configuration of {@link ConfigurationContext#LOCALIZE_FIXED_CONTROL_DATA}.
   * 
   * @since 2.0
   */
  protected Boolean localizeDisplayItems;

  public FormMultiSelectHtmlTag() {
    this.baseStyleClass = "aranea-multi-select";
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    int r = super.doStartTag(out);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    return r;
  }

  @Override
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected int doEndTag(Writer out) throws Exception {
    assertControlType("MultiSelectControl");

    // Prepare
    String name = this.getFullFieldId();
    ViewModel viewModel = ((MultiSelectControl.ViewModel) this.controlViewModel);

    // Write input tag
    JspUtil.writeOpenStartTag(out, "select");
    JspUtil.writeAttribute(out, "id", name);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "multiple", "multiple");
    JspUtil.writeAttribute(out, "size", this.size);
    JspUtil.writeAttribute(out, "tabindex", this.tabindex);

    if (viewModel.isOnChangeEventRegistered()) {
      this.writeSubmitScriptForUiEvent(out, "onchange", this.derivedId, "onChanged", "", this.updateRegionNames);
    }

    if (viewModel.isDisabled()) {
      JspUtil.writeAttribute(out, "disabled", "disabled");
    }

    JspUtil.writeAttributes(out, this.attributes);
    JspUtil.writeCloseStartTag(out);

    this.localizeDisplayItems = ConfigurationUtil.isLocalizeControlData(getEnvironment(), this.localizeDisplayItems);

    writeOptions(out, viewModel.getGroups(), viewModel.getValues());

    // Close tag
    JspUtil.writeEndTag_SS(out, "select");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  // Write items
  protected void writeOptions(Writer out, List<DisplayItemGroup> groups, String[] selectedValues) throws IOException {
    for (DisplayItemGroup group : groups) {
      if (group.isDisabled() || group.isEnabledEmpty()) {
        continue;
      } else if (!group.isNoGroup()) {
        String label = group.getLabel();

        if (this.localizeDisplayItems) {
          label = JspUtil.getResourceString(this.pageContext, label);
        }

        JspUtil.writeOpenStartTag(out, "optgroup");
        JspUtil.writeAttribute(out, "label", label);
        JspUtil.writeCloseStartTag(out);
      }

      for (DisplayItem item : group.getEnabledOptions()) {
        if (!item.isDisabled()) {
          String value = StringUtils.defaultString(item.getValue());
          String label = item.getLabel();

          if (this.localizeDisplayItems) {
            label = JspUtil.getResourceString(this.pageContext, label);
          }

          JspUtil.writeOpenStartTag(out, "option");
          JspUtil.writeAttributeForced(out, "value", value);

          if (ArrayUtils.contains(selectedValues, value)) {
            JspUtil.writeAttribute(out, "selected", "selected");
          }

          JspUtil.writeCloseStartTag_SS(out);
          JspUtil.writeEscaped(out, label);
          JspUtil.writeEndTag(out, "option");
        }

        if (!group.isNoGroup()) {
          JspUtil.writeEndTag(out, "optgroup");
        }
      }
    }
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Vertical size, number of options displayed."
   */
  public void setSize(String size) {
    this.size = evaluate("size", size, Long.class);
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
