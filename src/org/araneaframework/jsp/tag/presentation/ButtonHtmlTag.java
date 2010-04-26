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

package org.araneaframework.jsp.tag.presentation;

import java.io.Writer;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Standard button tag.
 * 
 * @author Oleg Mürk
 * @jsp.tag name = "basicButton" body-content = "JSP" description = "Represents an HTML form button."
 */
public class ButtonHtmlTag extends BaseButtonTag {

  public ButtonHtmlTag() {
    this.baseStyleClass = "aranea-button";
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, this.id);

    writeButtonStartTag(out);

    JspUtil.writeAttribute(out, "id", this.id);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "onclick", this.onclick);
    JspUtil.writeAttribute(out, "tabindex", this.tabindex);

    writeButtonCloseTag(out, false);

    return EVAL_BODY_INCLUDE;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    writeButtonCloseTag(out, true);
    return super.doEndTag(out);
  }

}
