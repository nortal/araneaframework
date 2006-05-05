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
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.uilib.form.UiFormElementBaseTag;
import org.araneaframework.jsp.util.UiUtil;
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
public class UiStdFormFileUploadTag extends UiFormElementBaseTag {
  protected Long size = null;

  public UiStdFormFileUploadTag() {
    baseStyleClass = "aranea-file-upload";
  }

  protected int doEndTag(Writer out) throws Exception {
    assertControlType("FileUploadControl");

    // Prepare
    String name = this.getScopedFullFieldId();    
    FileUploadControl.ViewModel viewModel = ((FileUploadControl.ViewModel)controlViewModel);

    // Build accepted mime-types list
    String accept = null;
    if (viewModel.getPermittedMimeFileTypes() != null) {
      StringBuffer acceptBuffer = new StringBuffer();
      for(Iterator i = viewModel.getPermittedMimeFileTypes().iterator(); i.hasNext();) {
        String mimeType = (String)i.next();
        acceptBuffer.append(mimeType);
        if (i.hasNext())
          acceptBuffer.append(",");
      }
      accept = acceptBuffer.toString();
    }

    // Write
    UiUtil.writeOpenStartTag(out, "input");
    UiUtil.writeAttribute(out, "id", name);
    UiUtil.writeAttribute(out, "name", name);
    UiUtil.writeAttribute(out, "class", getStyleClass());
    UiUtil.writeAttribute(out, "type", "file");
    UiUtil.writeAttribute(out, "accept", accept);
    UiUtil.writeAttribute(out, "size", size);
    UiUtil.writeAttribute(out, "label", localizedLabel);
    UiUtil.writeAttribute(out, "tabindex", tabindex);
    UiUtil.writeAttributes(out, attributes);
    UiUtil.writeCloseStartEndTag_SS(out);
    
    UiUtil.writeStartTag(out, "script");
    out.write("document.getElementById('");
    out.write(systemFormId);
    out.write("').enctype='multipart/form-data';");
    UiUtil.writeEndTag_SS(out, "script");

    super.doEndTag(out);
    return EVAL_PAGE;
  }

  public void setSize(String size) throws JspException {
    this.size = (Long)evaluate("size", size, Long.class);
  }
}
