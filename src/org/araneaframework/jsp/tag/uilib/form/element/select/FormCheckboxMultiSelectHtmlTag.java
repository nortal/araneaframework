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
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.control.BaseSelectControl.ViewModel;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * Standard select form element tag.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *  name = "checkboxMultiSelect"
 *  body-content = "JSP"
 *  description = "Form multi-select checkbox field, represents UiLib 'MultiSelectControl'."
 */
public class FormCheckboxMultiSelectHtmlTag extends BaseFormElementHtmlTag {

  /**
   * A boolean setting to override default configuration of {@link ConfigurationContext#LOCALIZE_FIXED_CONTROL_DATA}.
   * 
   * @since 1.2
   */
  protected String localizeDisplayItems;

  protected String type = "horizontal";

  protected boolean labelBefore;

  public FormCheckboxMultiSelectHtmlTag() {
    setHasElementContextSpan(false);
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    return EVAL_BODY_INCLUDE;
  }

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public int doEndTag(Writer out) throws Exception {
    assertControlType("MultiSelectControl");

    if (!"horizontal".equals(this.type) && !"vertical".equals(this.type)) {
      throw new AraneaJspException("Attribute 'type' can be only either 'horizontal' or 'vertical'!");
    }

    // Prepare
    ViewModel viewModel = (MultiSelectControl.ViewModel) this.controlViewModel;
    FormCheckboxMultiSelectItemLabelHtmlTag label = new FormCheckboxMultiSelectItemLabelHtmlTag();
    FormCheckboxMultiSelectItemHtmlTag item = new FormCheckboxMultiSelectItemHtmlTag();

    for (Object selectItem : viewModel.getSelectItems()) {
      DisplayItem displayItem = (DisplayItem) selectItem;

      // Set the corresponding HTML id for label and checkbox so that clicking on label sets the checkbox value too:
      String checkboxId = viewModel.getScope().toString() + displayItem.getValue();

      registerSubtag(item);
      item.setHtmlId(checkboxId);

      if (this.labelBefore) {
        writeLabel(label, this.derivedId, checkboxId, displayItem.getValue());
      }

      item.setId(this.derivedId);
      item.setValue(displayItem.getValue());
      item.setEvents(Boolean.toString(this.events));
      item.setValidateOnEvent(Boolean.toString(this.validateOnEvent));
      item.setStyleClass(getStyleClass());

      if (this.updateRegions != null) {
        item.setUpdateRegions(this.updateRegions);
      }

      if (this.globalUpdateRegions != null) {
        item.setGlobalUpdateRegions(this.globalUpdateRegions);
      }

      if (getStyle() != null) {
        item.setStyle(getStyle());
      }

      if (this.tabindex != null) {
        item.setTabindex(this.tabindex);
      }

      executeStartSubtag(item);
      executeEndTagAndUnregister(item);

      if (!this.labelBefore) {
        writeLabel(label, this.derivedId, checkboxId, displayItem.getValue());
      }

      if ("horizontal".equals(this.type)) {
        out.write("&nbsp;");
      } else if ("vertical".equals(this.type)) {
        JspUtil.writeStartEndTag(out, "br");
      }
    }

    return super.doEndTag(out);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "The way the checkboxes will be rendered - can be either 'vertical' or 'horizontal'. By default 'horizontal'."
   */
  public void setType(String type) {
    this.type = evaluate("type", type, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Boolean that controls whether label is before or after each checkbox. False by default."
   */
  public void setLabelBefore(String labelBefore) throws JspException {
    this.labelBefore = evaluateNotNull("labelBefore", labelBefore, Boolean.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Whether to localize display items. Provides a way to override ConfigurationContext.LOCALIZE_FIXED_CONTROL_DATA."
   * @since 1.2
   */
  public void setLocalizeDisplayItems(String localizeDisplayItems) throws JspException {
    this.localizeDisplayItems = evaluateNotNull("localizeDisplayItems", localizeDisplayItems, String.class);
  }

  protected void writeLabel(FormCheckboxMultiSelectItemLabelHtmlTag label, String id, String checkboxId, String value)
      throws JspException {
    registerSubtag(label);
    label.setId(id);
    label.setCheckboxId(checkboxId);
    label.setLocalizeDisplayItems(this.localizeDisplayItems);
    label.setValue(value);
    executeStartSubtag(label);
    executeEndTagAndUnregister(label);
  }
}
