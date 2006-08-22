/**
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
 **/

package org.araneaframework.jsp.tag.uilib.form.element;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementDisplayTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 * @jsp.tag
 *   name = "checkboxDisplay"
 *   body-content = "JSP" 
 *   description = "Form checkbox display field, represents UiLib "CheckboxControl"."
 */
public class FormCheckboxDisplayHtmlTag extends BaseFormElementDisplayTag {
  protected String imageCode;

  {
    baseStyleClass = "aranea-checkbox-display";
  }
  
  protected int doEndTag(Writer out) throws Exception {        
    StringArrayRequestControl.ViewModel viewModel = ((StringArrayRequestControl.ViewModel) controlViewModel);

    JspUtil.writeOpenStartTag(out, "span");
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttributes(out, attributes);
    JspUtil.writeCloseStartTag(out);

    if ("true".equals(viewModel.getSimpleValue())) //TODO: image?
      JspUtil.writeEscaped(out, "x");

    return super.doEndTag(out);  
  }

  //XXX: why is it here, without attributes
  protected void setImageCode(String imageCode) throws JspException {
    this.imageCode = (String) evaluateNotNull("imageCode", imageCode, String.class);
  }
}
