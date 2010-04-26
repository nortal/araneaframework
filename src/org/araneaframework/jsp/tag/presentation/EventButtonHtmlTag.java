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
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * @jsp.tag name = "eventButton" body-content = "JSP" description = "Represents an HTML form button."
 */
public class EventButtonHtmlTag extends BaseEventButtonTag {

  public static final String RENDER_BUTTON = "button";

  public static final String RENDER_INPUT = "input";

  protected String renderMode = EventButtonHtmlTag.RENDER_BUTTON;

  public EventButtonHtmlTag() {
    this.baseStyleClass = "aranea-button";
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    // Write button tag
    JspUtil.writeOpenStartTag(out, this.renderMode.equals(RENDER_BUTTON) ? RENDER_BUTTON : RENDER_INPUT);
    if (this.renderMode.equals(EventButtonHtmlTag.RENDER_INPUT)) {
      JspUtil.writeAttribute(out, "type", "button");
    }
    JspUtil.writeAttribute(out, "id", this.id);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "tabindex", this.tabindex);
    JspUtil.writeEventAttributes(out, this.event);

    if (isDisabled()) {
      JspUtil.writeAttribute(out, "disabled", "disabled");
    }

    if (this.event.getId() != null) {
      JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");
    }
    if (this.labelId != null && this.renderMode.equals(EventButtonHtmlTag.RENDER_INPUT)) {
      JspUtil.writeAttribute(out, "value", this.localizedLabel);
    }
    if (this.renderMode.equals(EventButtonHtmlTag.RENDER_BUTTON)) {
      JspUtil.writeCloseStartTag_SS(out);
    }
    if (this.renderMode.equals(EventButtonHtmlTag.RENDER_INPUT)) {
      JspUtil.writeCloseStartEndTag(out);
    }

    return EVAL_BODY_INCLUDE;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    if (this.renderMode.equals(EventButtonHtmlTag.RENDER_BUTTON)) {
      if (this.localizedLabel != null) {
        JspUtil.writeEscaped(out, this.localizedLabel);
      }

      JspUtil.writeEndTag(out, "button");
    }

    return super.doEndTag(out);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Allowed values are (button | input) - the corresponding HTML tag will be used for rendering. Default is button."
   */
  public void setRenderMode(String renderMode) throws JspException {
    String tmpMode = evaluate("renderMode", renderMode, String.class);

    if (!(RENDER_BUTTON.equals(tmpMode) || RENDER_INPUT.endsWith(tmpMode))) {
      throw new AraneaJspException("<ui:eventButton> 'renderMode' attribute must be '" + RENDER_BUTTON + "' or '"
          + RENDER_INPUT + "'");
    }

    this.renderMode = tmpMode;
  }
}
