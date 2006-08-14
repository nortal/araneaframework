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
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "eventButton"
 *   body-content = "JSP"
 *   description = "Represents an HTML form button."
 */
public class EventButtonHtmlTag extends BaseEventButtonTag {
  private static final String RENDER_BUTTON = "button";
  private static final String RENDER_INPUT = "input";

  {
     baseStyleClass = "aranea-button";
  }

  protected String renderMode = EventButtonHtmlTag.RENDER_BUTTON;  

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);                
    // Write button tag
    JspUtil.writeOpenStartTag(out, renderMode.equals(EventButtonHtmlTag.RENDER_BUTTON) ? EventButtonHtmlTag.RENDER_BUTTON : EventButtonHtmlTag.RENDER_INPUT);
    if (renderMode.equals(EventButtonHtmlTag.RENDER_INPUT))
      JspUtil.writeAttribute(out, "type", "button");    
    JspUtil.writeAttribute(out, "id", id);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeEventAttributes(out, event);

    if (disabled != null) 
      out.write(" disabled ");

    if (event.getId() != null) {
      JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");
    }
    if (labelId != null && renderMode.equals(EventButtonHtmlTag.RENDER_INPUT)) {
      JspUtil.writeAttribute(out, "value", localizedLabel);      
    }
    if (renderMode.equals(EventButtonHtmlTag.RENDER_BUTTON))
      JspUtil.writeCloseStartTag_SS(out);      
    if (renderMode.equals(EventButtonHtmlTag.RENDER_INPUT))
      JspUtil.writeCloseStartEndTag(out);

    return EVAL_BODY_INCLUDE;    
  }    

  protected int doEndTag(Writer out) throws Exception {
    if (renderMode.equals(EventButtonHtmlTag.RENDER_BUTTON)) {
      if (localizedLabel != null)
        JspUtil.writeEscaped(out, localizedLabel);

      JspUtil.writeEndTag(out, "button");       
    }

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
    if (!(renderMode.equals(EventButtonHtmlTag.RENDER_BUTTON) || renderMode.equals(EventButtonHtmlTag.RENDER_INPUT)))
      throw new AraneaJspException("<ui:eventButton> 'renderMode' attribute must be '" + EventButtonHtmlTag.RENDER_BUTTON + "' or '"+ EventButtonHtmlTag.RENDER_INPUT+"'");
    this.renderMode = (String) evaluate("renderMode", renderMode, String.class);
  }  
}
