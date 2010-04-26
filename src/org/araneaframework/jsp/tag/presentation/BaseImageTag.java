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
import org.apache.commons.lang.StringUtils;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * UI image tag.
 * 
 * @author Oleg Mürk
 */
public abstract class BaseImageTag extends PresentationTag {

  /**
   * Usual HTML &lt;img&gt; tag attribute.
   */
  protected String code, src, width, height, alt, title;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    if (this.code != null) {
      Info info = getImageInfo(this.code);
      if (info == null) {
        throw new AraneaJspException("Missing image description with code '" + this.code + "'");
      }
      this.src = info.src;
      this.width = info.width;
      this.height = info.height;
    }

    if (this.alt == null ^ this.title == null) {
      this.alt = StringUtils.defaultString(this.alt, this.title);
      this.title = StringUtils.defaultString(this.alt, this.title);
    }

    return EVAL_BODY_INCLUDE;
  }

  protected abstract Info getImageInfo(String code);

  /**
   * Image info class.
   */
  protected static class Info {

    public String src;

    public String width;

    public String height;

    public String alt;

    public Info(String src, String width, String height) {
      this.src = src;
      this.width = width;
      this.height = height;
      this.alt = null;
    }

    public Info(String src, String width, String height, String alt) {
      this.src = src;
      this.width = width;
      this.height = height;
      this.alt = alt;
    }
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Image code."
   */
  public void setCode(String code) {
    this.code = evaluate("code", code, String.class);
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Image src."
   */
  public void setSrc(String src) {
    this.src = evaluate("src", src, String.class);
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Image width"
   */

  public void setWidth(String width) {
    this.width = evaluate("width", width, String.class);
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Image height."
   */
  public void setHeight(String height) {
    this.height = evaluate("height", height, String.class);
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Image alternate text."
   */
  public void setAlt(String alt) {
    this.alt = evaluate("alt", alt, String.class);
    if (this.alt != null) {
      this.alt = JspUtil.getResourceStringOrNull(this.pageContext, this.alt);
    }
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Image title"
   */
  public void setTitle(String title) {
    this.title = evaluate("title", title, String.class);
    if (this.title != null) {
      this.title = JspUtil.getResourceStringOrNull(this.pageContext, this.title);
    }
  }
}
