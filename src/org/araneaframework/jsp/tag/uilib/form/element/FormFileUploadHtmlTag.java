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
import java.util.Iterator;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.form.control.FileUploadControl;


/**
 * Standard text input form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "fileUpload"
 *   body-content = "JSP"
 *   description = "Form file upload field, represents UiLib 'FileUploadControl'."
 */
public class FormFileUploadHtmlTag extends BaseFormElementHtmlTag {
  protected Long size = null;

  {
    baseStyleClass = "aranea-file-upload";
  }
  
  @Override
  protected int doStartTag(Writer out) throws Exception {
    int r = super.doStartTag(out);
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, null);
    return r;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    assertControlType("FileUploadControl");

    // Prepare
    String name = this.getFullFieldId();    
    FileUploadControl.ViewModel viewModel = ((FileUploadControl.ViewModel)controlViewModel);

    // Build accepted mime-types list
    String accept = null;
    if (viewModel.getPermittedMimeFileTypes() != null) {
      StringBuffer acceptBuffer = new StringBuffer();
      for(Iterator<String> i = viewModel.getPermittedMimeFileTypes().iterator(); i.hasNext();) {
        String mimeType = i.next();
        acceptBuffer.append(mimeType);
        if (i.hasNext())
          acceptBuffer.append(",");
      }
      accept = acceptBuffer.toString();
    }

    // Write
    JspUtil.writeOpenStartTag(out, "input");
    JspUtil.writeAttribute(out, "id", name);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "type", "file");
    JspUtil.writeAttribute(out, "accept", accept);
    JspUtil.writeAttribute(out, "size", size);
    JspUtil.writeAttribute(out, "tabindex", tabindex);
    JspUtil.writeAttributes(out, attributes);
    JspUtil.writeCloseStartEndTag_SS(out);
    
    JspUtil.writeStartTag_SS(out, "script");
    out.write("_ap.setSystemFormEncoding('multipart/form-data');");
    JspUtil.writeEndTag_SS(out, "script");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  public void setSize(String size){
    this.size = evaluate("size", size, Long.class);
  }
}
