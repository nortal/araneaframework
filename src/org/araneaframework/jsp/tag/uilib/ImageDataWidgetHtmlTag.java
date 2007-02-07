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

package org.araneaframework.jsp.tag.uilib;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.StyledTagInterface;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Displays an image from a {@link org.araneaframework.uilib.DataWidget}.
 *
 * @author Alar Kvell (alar@araneaframework.org)
 *
 * @jsp.tag
 *   name = "imageDataWidget"
 *   body-content = "JSP" 
 *   description = "Displays HTML <code>IMG</code> tag, which renders <code>DataWidget</code>'s data"
 */
public class ImageDataWidgetHtmlTag extends DataWidgetTag implements StyledTagInterface {

  protected String alt;
  protected String style;
  protected String styleClass;

  protected int doEndTag(Writer out) throws Exception {
    JspUtil.writeOpenStartTag(out, "img");
    JspUtil.writeAttribute(out, "id", fullId);
    JspUtil.writeAttribute(out, "src", getDataUrl());
    if (alt != null)
      JspUtil.writeAttribute(out, "alt", alt);
    if (styleClass != null)
      JspUtil.writeAttribute(out, "class", styleClass);
    if (style != null)
      JspUtil.writeAttribute(out, "style", style);
    JspUtil.writeCloseStartEndTag(out);
    return super.doEndTag(out);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false" 
   *   description = "Short description of the image."
   */
  public void setAlt(String alt) throws JspException {
    this.alt = (String) evaluate("alt", alt, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false" 
   *   description = "Inline style for HTML tag."
   */
  public void setStyle(String style) throws JspException {
    this.style = (String) evaluate("style", style, String.class);
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false" 
   *   description = "CSS class for tag"
   */
  public void setStyleClass(String styleClass) throws JspException {
    if (styleClass != null)
      this.styleClass = (String) evaluate("styleClass", styleClass, String.class);
  }

}
