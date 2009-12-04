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
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementDisplayTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.ConfigurationUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag
 *  name = "multiSelectDisplay"
 *  body-content = "JSP"
 *  description = "Form multiselect display field, represents UiLib 'MultiSelectControl'."
 */
@SuppressWarnings("unchecked")
public class FormMultiSelectDisplayHtmlTag extends BaseFormElementDisplayTag {

  protected static final String NEWLINE_SEPARATOR_CODE = "\\n";

  protected String separator = ",&nbsp;";

  /**
   * A boolean setting to override default configuration of {@link ConfigurationContext#LOCALIZE_FIXED_CONTROL_DATA}.
   * 
   * @since 1.2
   */
  protected Boolean localizeDisplayItems;

  
  public FormMultiSelectDisplayHtmlTag() {
    this.baseStyleClass = "aranea-multi-select-display";
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    MultiSelectControl.ViewModel viewModel = ((MultiSelectControl.ViewModel) controlViewModel);

    this.localizeDisplayItems = ConfigurationUtil.isLocalizeControlData(getEnvironment(), this.localizeDisplayItems);

    JspUtil.writeOpenStartTag(out, "span");
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttributes(out, this.attributes);
    JspUtil.writeCloseStartTag(out);

    for (Iterator<DisplayItem> i = viewModel.getSelectedItems().iterator(); i.hasNext();) {
      String label = i.next().getLabel();

      if (this.localizeDisplayItems) {
        label = JspUtil.getResourceString(this.pageContext, label);
      }

      JspUtil.writeEscaped(out, label);

      if (i.hasNext()) {
        writeSeparator(out);
      }
    }

    return super.doEndTag(out);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "The separator between list items, can be any string and '\n', meaning a newline (by default ', ')."
   */
  public void setSeparator(String separator) {
    this.separator = separator;
  }

  protected void writeSeparator(Writer out) throws IOException {
    if (NEWLINE_SEPARATOR_CODE.equals(this.separator)) {
      JspUtil.writeStartEndTag(out, "br");
    } else {
      out.write(this.separator);
    }
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
