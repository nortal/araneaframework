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

package org.araneaframework.jsp.tag.uilib.form.element;

import java.io.Writer;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;

/**
 * Renders concealed {@link org.araneaframework.uilib.form.control.HiddenControl} in an HTML page.
 * 
 * @author Konstantin Tretyakov
 * 
 * @jsp.tag
 *  name = "hiddenInput"
 *  body-content = "JSP"
 *  description = "Represents a 'hidden' HTML input element mapped to an UiLib HiddenControl."
 */
public class FormHiddenInputHtmlTag extends BaseFormElementHtmlTag {

  @Override
  protected int doStartTag(Writer out) throws Exception {
    int r = super.doStartTag(out);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    return r;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected int doEndTag(Writer out) throws Exception {
    // Type check
    assertControlType("HiddenControl");

    String name = this.getFullFieldId();
    StringArrayRequestControl<?>.ViewModel viewModel = (StringArrayRequestControl.ViewModel) this.controlViewModel;

    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "id", name);
    JspUtil.writeAttribute(out, "type", "hidden");
    JspUtil.writeAttribute(out, "value", viewModel.getSimpleValue());
    JspUtil.writeCloseStartEndTag_SS(out);

    return super.doEndTag(out);
  }
}
