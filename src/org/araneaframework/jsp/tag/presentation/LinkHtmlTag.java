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
import org.araneaframework.jsp.util.JspUtil;

/**
 * Standard link tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "link"
 *   body-content = "JSP"
 *   description = "Usual HTML link, acts as a <i>&lt;a&gt;</i> HTML tag."
 */
public class LinkHtmlTag extends BaseLinkTag {

  private String disabledStyleClass;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    JspUtil.writeOpenStartTag(out, "a");
    JspUtil.writeAttribute(out, "id", this.id);
    JspUtil.writeAttribute(out, "class", !this.disabled ? getStyleClass() : getDisabledStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());

    if (!this.disabled) {
      JspUtil.writeAttribute(out, "href", this.href);
      JspUtil.writeAttribute(out, "target", this.target);
    } else {
      JspUtil.writeAttribute(out, "href", "javascript:");
    }

    JspUtil.writeCloseStartTag_SS(out);

    return EVAL_BODY_INCLUDE;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    JspUtil.writeEndTag_SS(out, "a");
    return super.doEndTag(out);
  }

  public String getDisabledStyleClass() {
    return this.disabledStyleClass;
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "CSS class for disabled link" 
   */
  public void setDisabledStyleClass(String disabledStyleClass){
    this.disabledStyleClass = this.evaluate("disabledStyleClass", disabledStyleClass, String.class);
  }
}
