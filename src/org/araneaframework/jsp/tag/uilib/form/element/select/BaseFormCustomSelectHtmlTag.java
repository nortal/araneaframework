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
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.BaseSelectControl;
import org.araneaframework.uilib.form.control.BaseSelectControl.ViewModel;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.support.DisplayItemGroup;
import org.araneaframework.uilib.util.ConfigurationUtil;

/**
 * Base tag for rendering custom select form elements.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 */
@SuppressWarnings("unchecked")
public abstract class BaseFormCustomSelectHtmlTag extends BaseFormElementHtmlTag {

  protected String type = "horizontal";

  protected boolean labelBefore;

  protected Boolean localizeDisplayItems;

  public BaseFormCustomSelectHtmlTag() {
    setHasElementContextSpan(false);
  }

  @Override
  @SuppressWarnings("rawtypes")
  public int doEndTag(Writer out) throws Exception {
    assertControlTypes("SelectControl", "MultiSelectControl");

    if (!"horizontal".equals(this.type) && !"vertical".equals(this.type)) {
      throw new AraneaJspException("Attribute 'type' can be only either 'horizontal' or 'vertical'!");
    }

    // Prepare
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);

    ViewModel viewModel = (BaseSelectControl.ViewModel) this.controlViewModel;
    renderItems(out, viewModel.getGroups(), viewModel.getScope().toString(), viewModel.isDisabled());

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  protected void renderItems(Writer out, List<DisplayItemGroup> groups, String scopePrefix, boolean disabled) throws Exception {
    for (DisplayItemGroup group : groups) {
      if (group.isDisabled() || group.isEmpty()) {
        continue;
      }

      for (DisplayItem displayItem : group.getEnabledOptions()) {
        // Set the same HTML id for label ("for") and input ("id") so that clicking on label triggers input focus:
        String htmlId = scopePrefix + displayItem.getValue();

        if (this.labelBefore) {
          writeLabel(out, htmlId, displayItem.getLabel());
        }

        writeInput(out, htmlId, displayItem.getValue(), disabled);

        if (!this.labelBefore) {
          writeLabel(out, htmlId, displayItem.getLabel());
        }

        if ("horizontal".equals(this.type)) {
          out.write("&nbsp;");
        } else if ("vertical".equals(this.type)) {
          JspUtil.writeStartEndTag(out, "br");
        }
      }
    }
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "The way the inputs will be rendered - can be either vertical or horizontal. By default horizontal."
   */
  public void setType(String type) {
    this.type = evaluate("type", type, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Boolean that controls whether label is before or after each input. False by default."
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

  protected void writeLabel(Writer out, String htmlId, String labelId) throws IOException {
    JspUtil.writeOpenStartTag(out, "span");
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeCloseStartTag_SS(out);

    JspUtil.writeOpenStartTag(out, "label");
    JspUtil.writeAttribute(out, "for", htmlId);
    JspUtil.writeCloseStartTag_SS(out);
    JspUtil.writeEscaped(out, evaluateLabel(labelId));
    JspUtil.writeEndTag_SS(out, "label");

    JspUtil.writeEndTag(out, "span");
  }

  protected String evaluateLabel(String value) {
    this.localizeDisplayItems = ConfigurationUtil.isLocalizeControlData(getEnvironment(), this.localizeDisplayItems);
    return this.localizeDisplayItems ? JspUtil.getResourceString(this.pageContext, value) : value;
  }

  protected abstract void writeInput(Writer out, String htmlId, String value, boolean disabled) throws IOException;
}
