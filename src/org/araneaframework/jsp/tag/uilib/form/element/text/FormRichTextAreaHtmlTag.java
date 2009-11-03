/*
 * Copyright 2006-2007 Webmedia Group Ltd.
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

package org.araneaframework.jsp.tag.uilib.form.element.text;

import java.io.Writer;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Standard text input form element tag with rich text editor capabilities.
 * The tag uses a special style class denoted by EDITOR_SELECTOR which makes
 * it possible for the RTE library to locate the area and wrap it. 
 * 
 * @author Toomas Römer
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "richTextarea"
 *   body-content = "JSP"
 *   description = "Form text input field (textarea) wrapped with rich text editor capabilities."
 */
public class FormRichTextAreaHtmlTag extends FormTextareaHtmlTag {

  public static final String EDITOR_SELECTOR = "richTextEditor";

  @Override
  protected String getStyleClass() {
    return EDITOR_SELECTOR;
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    initializeRichEditor(out);

    return super.doStartTag(out);
  }

  protected void initializeRichEditor(Writer out) throws Exception {
    JspUtil.writeOpenStartTag(out, "script");
    JspUtil.writeAttribute(out, "type", "text/javascript");
    JspUtil.writeCloseStartTag_SS(out);
    out.write("AraneaTinyMCEInit();");
    JspUtil.writeEndTag(out, "script");
  }
}
