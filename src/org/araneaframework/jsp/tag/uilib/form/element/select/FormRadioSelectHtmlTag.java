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
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.SelectControl;

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
public class FormRadioSelectHtmlTag extends BaseFormCustomSelectHtmlTag {

  @Override
  protected void writeInput(Writer out, String htmlId, String value, boolean disabled) throws IOException {
    SelectControl.ViewModel viewModel = (SelectControl.ViewModel) this.controlViewModel;

    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "id", htmlId);
    JspUtil.writeAttribute(out, "name", getFullFieldId());
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "type", "radio");
    JspUtil.writeAttribute(out, "value", value);
    JspUtil.writeAttribute(out, "tabindex", this.tabindex);

    if (disabled) {
      JspUtil.writeAttribute(out, "disabled", "disabled");
    }

    if (viewModel.isSelected(value)) {
      JspUtil.writeAttribute(out, "checked", "checked");
    }

    if (this.events && viewModel.isOnChangeEventRegistered()) {
      this.writeSubmitScriptForUiEvent(out, "onclick", this.derivedId, "onChanged", "", this.updateRegionNames);
    }

    JspUtil.writeAttributes(out, this.attributes);
    JspUtil.writeCloseStartEndTag_SS(out);
  }
}
