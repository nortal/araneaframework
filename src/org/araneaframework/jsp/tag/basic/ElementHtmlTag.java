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

package org.araneaframework.jsp.tag.basic;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Element tag.
 * 
 * @author Oleg Mürk
 * @jsp.tag
 *  name = "element"
 *  body-content = "JSP"
 *  description = "Defines an HTML element."
 */
public class ElementHtmlTag extends BaseTag implements AttributedTagInterface {

  public static final String KEY = ElementHtmlTag.class.getName() + ".KEY";

  protected String name = null;

  protected Map<String, Object> attributes = new HashMap<String, Object>();

  protected boolean hasContent;

  protected boolean renderTag = true;

  protected boolean forceRenderBody;

  @Override
  public void setPageContext(PageContext pageContext) {
    this.attributes = new HashMap<String, Object>();
    this.hasContent = false;
    this.name = null;
    super.setPageContext(pageContext);
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    if (!this.renderTag && !this.forceRenderBody) {
      return SKIP_BODY;
    }

    addContextEntry(KEY, this);
    addContextEntry(ATTRIBUTED_TAG_KEY, this);

    if (this.renderTag) {
      JspUtil.writeOpenStartTag(out, this.name);
    }

    // Continue
    return EVAL_BODY_INCLUDE;
  }

  /**
   * After content tag.
   */
  @Override
  protected int doEndTag(Writer out) throws Exception {
    if (this.renderTag) {
      if (this.hasContent) {
        JspUtil.writeEndTag_SS(out, this.name);
      } else {
        JspUtil.writeAttributes(out, this.attributes);
        JspUtil.writeCloseStartEndTag_SS(out);
      }
    }
    return super.doEndTag(out);
  }

  protected void onContent(Writer out) throws Exception {
    if (this.renderTag) {
      this.hasContent = true;
      JspUtil.writeAttributes(out, this.attributes);
      JspUtil.writeCloseStartTag_SS(out);
    }
  }

  public void addAttribute(String name, String value) {
    value = evaluate("value", value, String.class);
    this.attributes.put(name, value);
  }

  // Tag attributes

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "true"
   *    description = "HTML element name."
   */
  public void setName(String name) throws JspException {
    this.name = evaluateNotNull("name", name, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "A Boolean that indicates whether the tag can be rendered or not. Default: true."
   */
  public void setRenderTag(boolean renderTag) {
    this.renderTag = renderTag;
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "A Boolean that indicates whether the body is rendered (even if the tag is not rendered) or not. Default: false."
   */
  public void setForceRenderBody(boolean forceRenderBody) {
    this.forceRenderBody = forceRenderBody;
  }
}
