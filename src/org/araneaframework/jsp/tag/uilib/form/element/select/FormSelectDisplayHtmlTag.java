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
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementDisplayTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.util.ConfigurationUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @jsp.tag
 *  name = "selectDisplay"
 *  body-content = "JSP"
 *  description = "Form select display field, represents UiLib "SelectControl"."
 */
@SuppressWarnings("unchecked")
public class FormSelectDisplayHtmlTag extends BaseFormElementDisplayTag {

  /**
   * A boolean setting to override default configuration of {@link ConfigurationContext#LOCALIZE_FIXED_CONTROL_DATA}.
   * 
   * @since 1.2
   */
  protected Boolean localizeDisplayItems;

  public FormSelectDisplayHtmlTag() {
    this.baseStyleClass = "aranea-select-display";
  }

  protected int doEndTag(Writer out) throws Exception {
    SelectControl.ViewModel viewModel = ((SelectControl.ViewModel) this.controlViewModel);

    JspUtil.writeOpenStartTag(out, "span");
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttributes(out, this.attributes);
    JspUtil.writeCloseStartTag(out);

    this.localizeDisplayItems = ConfigurationUtil.isLocalizeControlData(getEnvironment(), this.localizeDisplayItems);

    String label = viewModel.getSelectedItem().getLabel();

    if (this.localizeDisplayItems.booleanValue()) {
      label = JspUtil.getResourceString(this.pageContext, label);
    }

    JspUtil.writeEscaped(out, label);
    JspUtil.writeEndTag_SS(out, "span");
    return super.doEndTag(out);
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
