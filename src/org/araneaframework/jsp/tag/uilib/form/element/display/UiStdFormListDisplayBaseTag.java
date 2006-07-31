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

package org.araneaframework.jsp.tag.uilib.form.element.display;

import java.io.IOException;
import java.io.Writer;
import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseDisplayTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.form.control.DisplayControl;

/**
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 */
public abstract class UiStdFormListDisplayBaseTag extends UiFormElementBaseDisplayTag {
  protected static final String NEWLINE_SEPARATOR_CODE ="\n";

  protected String separator = UiStdFormListDisplayBaseTag.NEWLINE_SEPARATOR_CODE;
  protected DisplayControl.ViewModel displayControlViewModel;

  protected int doStartTag(Writer out) throws Exception {    
    super.doStartTag(out);

    assertControlType("DisplayControl");           

    displayControlViewModel = (DisplayControl.ViewModel) controlViewModel;           

    if (getStyleClass() != null) {
      UiUtil.writeOpenStartTag(out, "span");
      UiUtil.writeAttribute(out, "class", getStyleClass());
      UiUtil.writeAttribute(out, "style", getStyle());
      UiUtil.writeAttributes(out, attributes);
      UiUtil.writeCloseStartTag(out);
    }    

    return SKIP_BODY;
  }

  protected int doEndTag(Writer out) throws Exception {    
    if (getStyleClass() != null)
      UiUtil.writeEndTag(out, "span");    

    super.doEndTag(out);

    return EVAL_PAGE;
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "The separator between list items, can be any string and "\n", meaning a newline (by default "\n")." 
   */
  public void setSeparator(String separator) {
    this.separator = separator;
  }  

  protected void writeSeparator(Writer out) throws IOException, UiException {
    if (NEWLINE_SEPARATOR_CODE.equals(separator))      
      UiUtil.writeStartEndTag(out, "br");
    else 
      UiUtil.writeEscaped(out, separator);      
  }      
}
