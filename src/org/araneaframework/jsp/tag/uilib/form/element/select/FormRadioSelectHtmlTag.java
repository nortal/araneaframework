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
import java.util.List;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.BaseSelectControl.ViewModel;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.ConfigurationUtil;

/**
 * Standard select form element tag.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *  name = "radioSelect"
 *  body-content = "JSP"
 *  description = "Form radio-select buttons field, represents UiLib 'SelectControl'."
 */
@SuppressWarnings("unchecked")
public class FormRadioSelectHtmlTag extends BaseFormElementHtmlTag {

  protected String type = "horizontal";

  protected boolean labelBefore = false;

  protected Boolean localizeDisplayItems;

  public FormRadioSelectHtmlTag() {
    setHasElementContextSpan(false);
  }

  @Override
  public int doEndTag(Writer out) throws Exception {
    assertControlType("SelectControl");

    if (!"horizontal".equals(this.type) && !"vertical".equals(this.type)) {
      throw new AraneaJspException("Attribute 'type' can be only either 'horizontal' or 'vertical'!");
    }

    // Prepare
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);

    SelectControl<Object>.ViewModel viewModel = (ViewModel) controlViewModel;
    renderItems(out, viewModel.getSelectItems(), viewModel.getScope().toString());

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  protected void renderItems(Writer out, List<DisplayItem> items, String scopePrefix) throws Exception {
    FormRadioSelectItemLabelHtmlTag label = new FormRadioSelectItemLabelHtmlTag();

    for (DisplayItem displayItem : items) {
      if (displayItem.isGroup()) {
        renderItems(out, displayItem.getChildOptions(), scopePrefix);
        continue;
      }

      // Set the same HTML id for label ("for") and radio-button ("id") so that clicking on label affects radio-button
      // selection:
      String radioId = scopePrefix + "-" + displayItem.getValue();

      if (this.labelBefore && displayItem.getLabel() != null) {
        writeLabel(label, this.derivedId, radioId, displayItem.getLabel());
      }

      FormRadioSelectItemHtmlTag item = registerSubtag(new FormRadioSelectItemHtmlTag());
      item.setHtmlId(radioId);
      item.setId(this.derivedId);
      item.setValue(displayItem.getValue());
      item.setDisabled(Boolean.toString(displayItem.isDisabled()));
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

      executeSubtag(item);
      unregisterSubtag(item);

      if (!this.labelBefore && displayItem.getLabel() != null) {
        writeLabel(label, this.derivedId, radioId, displayItem.getLabel());
      }

      if ("horizontal".equals(this.type)) {
        out.write("&nbsp;");
      } else if ("vertical".equals(this.type)) {
        JspUtil.writeStartEndTag(out, "br");
      }
    }
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "The way the radio buttons will be rendered - can be either vertical or horizontal. By default horizontal."
   */
  public void setType(String type) {
    this.type = evaluate("type", type, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Boolean that controls whether label is before or after each radio button. False by default."
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
    this.localizeDisplayItems = evaluateNotNull("localizeDisplayItems", localizeDisplayItems, Boolean.class);
  }

  protected void writeLabel(FormRadioSelectItemLabelHtmlTag label, String id, String radioId, String labelId)
      throws JspException {
    registerSubtag(label);
    label.setId(id);
    label.setRadioId(radioId);
    label.setValue(evaluateLabel(labelId));
    label.setLocalizeLabel(Boolean.toString(false));
    executeStartSubtag(label);
    executeEndTagAndUnregister(label);
  }

  protected String evaluateLabel(String value) {
    this.localizeDisplayItems = ConfigurationUtil.isLocalizeControlData(getEnvironment(), this.localizeDisplayItems);
    return this.localizeDisplayItems ? JspUtil.getResourceString(this.pageContext, value) : value;
  }
}
