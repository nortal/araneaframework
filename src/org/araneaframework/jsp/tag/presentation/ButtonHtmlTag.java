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

package org.araneaframework.jsp.tag.presentation;      

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Standard button tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "basicButton"
 *   body-content = "JSP"
 *   description = "Represents an HTML form button."
 */
public class ButtonHtmlTag extends BaseButtonTag {
  private static final String RENDER_BUTTON = "button";
  private static final String RENDER_INPUT = "input";

  protected String renderMode;

  {
    baseStyleClass = "aranea-button";
  }

  public ButtonHtmlTag() {
    renderMode = ButtonHtmlTag.RENDER_BUTTON;
  }

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    
    addContextEntry(AttributedTagInterface.HTML_ELEMENT_KEY, id);
    
    JspUtil.writeOpenStartTag(out, renderMode.equals(ButtonHtmlTag.RENDER_BUTTON) ? ButtonHtmlTag.RENDER_BUTTON : ButtonHtmlTag.RENDER_INPUT);
    if (renderMode.equals(ButtonHtmlTag.RENDER_INPUT))
      JspUtil.writeAttribute(out, "type", "button");    
    JspUtil.writeAttribute(out, "id", id);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "onclick", onclick);
    if (labelId != null && renderMode.equals(ButtonHtmlTag.RENDER_INPUT)) {
      JspUtil.writeAttribute(out, "value", JspUtil.getResourceString(pageContext, labelId));      
    }
    if (renderMode.equals(ButtonHtmlTag.RENDER_BUTTON))
      JspUtil.writeCloseStartTag_SS(out);      
    if (renderMode.equals(ButtonHtmlTag.RENDER_INPUT))
      JspUtil.writeCloseStartEndTag(out);      

    // Continue
    return EVAL_BODY_INCLUDE;
  }

  protected int doEndTag(Writer out) throws Exception {  
    if (renderMode.equals(ButtonHtmlTag.RENDER_BUTTON)) {
      if (labelId != null)            
        JspUtil.writeEscaped(out, JspUtil.getResourceString(pageContext, labelId));
      JspUtil.writeEndTag(out, "button");       
    }

    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;      
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = 
   *     "Allowed values are (button | input) - the corresponding HTML tag will be used for rendering. Default is button." 
   */
  public void setRenderMode(String renderMode) throws JspException {
    if (!(renderMode.equals(ButtonHtmlTag.RENDER_BUTTON) || renderMode.equals(ButtonHtmlTag.RENDER_INPUT)))
      throw new UiException("<ui:basicButton> 'renderMode' attribute must be '" + ButtonHtmlTag.RENDER_BUTTON + "' or '"+ ButtonHtmlTag.RENDER_INPUT+"'");
    this.renderMode = (String) evaluate("renderMode", renderMode, String.class);
  }
}




