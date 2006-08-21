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

package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.io.Writer;
import java.util.StringTokenizer;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementDisplayTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.StringArrayRequestControl;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "textareaDisplay"
 *   body-content = "JSP"
 *   description = "Form textarea display field, represents UiLib "TextareaControl"."
 */
public class FormTextareaDisplayHtmlTag extends BaseFormElementDisplayTag {
  {
    baseStyleClass = "aranea-textarea-display";
  }

  protected int doEndTag(Writer out) throws Exception {        
    StringArrayRequestControl.ViewModel viewModel = ((StringArrayRequestControl.ViewModel) controlViewModel);

    JspUtil.writeOpenStartTag(out, "span");
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttributes(out, attributes);
    JspUtil.writeCloseStartTag(out);

    if (viewModel.getSimpleValue() != null)
      for (StringTokenizer lines = new StringTokenizer(viewModel.getSimpleValue(), "\n"); lines.hasMoreTokens(); ) {
        String line = lines.nextToken();
        boolean isPreviousNbsp = false;
        for (int i = 0; i < line.length(); i++) {
          if (line.charAt(i) == ' ') {
            if (isPreviousNbsp) {
              out.write(" ");
              isPreviousNbsp = false;
            } else {              
              out.write("&nbsp;");
              isPreviousNbsp = true;
            }                          
          } 
          else {
            isPreviousNbsp = false;
            JspUtil.writeEscaped(out, line.substring(i, i+1));
          }
        }

        if (lines.hasMoreTokens())
          JspUtil.writeStartEndTag(out, "br");
      }

    JspUtil.writeEndTag(out, "span");

    return super.doEndTag(out);  
  }
}
