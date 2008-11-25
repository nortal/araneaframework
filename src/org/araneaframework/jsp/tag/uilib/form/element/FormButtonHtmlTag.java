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

import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringEscapeUtils;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.util.JspStringUtil;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.event.OnClickEventListener;

/**
 * Button form element tag, represents {@link org.araneaframework.uilib.form.control.ButtonControl}. 
 * Rendered with either &lt;input type=&quot;button&quot;&gt; or just &lt;button&gt;
 * depending on <code>renderMode</code> attribute.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "button"
 *   body-content = "JSP"
 *   description = "Form button, represents UiLib "ButtonControl"."
 */
public class FormButtonHtmlTag extends BaseFormButtonTag {
  public static final String RENDER_BUTTON = "button";
  public static final String RENDER_INPUT = "input";
  protected String renderMode = FormButtonHtmlTag.RENDER_BUTTON;

  {
    baseStyleClass = "aranea-button";
  }

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Prepare
    String name = this.getFullFieldId();

    JspUtil.writeOpenStartTag(out, renderMode.equals(FormButtonHtmlTag.RENDER_BUTTON) ? FormButtonHtmlTag.RENDER_BUTTON : FormButtonHtmlTag.RENDER_INPUT);

    if (renderMode.equals(FormButtonHtmlTag.RENDER_INPUT))
      JspUtil.writeAttribute(out, "type", "button");
    JspUtil.writeAttribute(out, "id", name);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    if (showLabel && renderMode.equals(FormButtonHtmlTag.RENDER_INPUT)) {
      if (accessKey != null) {
        String escapedLabel = StringEscapeUtils.escapeHtml(localizedLabel);
        JspUtil.writeAttribute(out, "value", JspStringUtil
            .underlineAccessKey(escapedLabel, accessKey));
      } else {
        JspUtil.writeAttribute(out, "value", localizedLabel);      
      }
    }
    if (renderMode.equals(FormButtonHtmlTag.RENDER_BUTTON))
      JspUtil.writeAttribute(out, "label", localizedLabel);
    JspUtil.writeAttribute(out, "tabindex", tabindex);
    if (events && !viewModel.isDisabled()) {
      writeEventAttribute(out);
    } else if (viewModel.isDisabled()) {
      JspUtil.writeAttribute(out, "disabled", "disabled");
    }
    JspUtil.writeAttributes(out, attributes);
    if (accessKey != null)
      JspUtil.writeAttribute(out, "accesskey", accessKey);
    if (renderMode.equals(FormButtonHtmlTag.RENDER_BUTTON))
      JspUtil.writeCloseStartTag_SS(out);      
    if (renderMode.equals(FormButtonHtmlTag.RENDER_INPUT))
      JspUtil.writeCloseStartEndTag(out);      

    // Continue
    return EVAL_BODY_INCLUDE;
  }

  protected int doEndTag(Writer out) throws Exception {

    if (renderMode.equals(FormButtonHtmlTag.RENDER_BUTTON)) {
      if (showLabel) {
        if (accessKey != null) {
          String escapedLabel = StringEscapeUtils.escapeHtml(localizedLabel);
          out.write(JspStringUtil
              .underlineAccessKey(escapedLabel, accessKey));
        } else {
          JspUtil.writeEscaped(out, localizedLabel);
        }
      }
      JspUtil.writeEndTag(out, FormButtonHtmlTag.RENDER_BUTTON);
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
    String tmpMode = (String) evaluate("renderMode", renderMode, String.class);

    if (!(RENDER_BUTTON.equals(tmpMode) || RENDER_INPUT.equals(tmpMode))) {
      throw new AraneaJspException("<ui:button> 'renderMode' attribute "
          + "must be '" + RENDER_BUTTON + "' or '" + RENDER_INPUT + "'");
    }

    this.renderMode = tmpMode;
  }

  protected boolean writeEventAttribute(Writer out) throws IOException {
    UiUpdateEvent event = new UiUpdateEvent();
    event.setId(OnClickEventListener.ON_CLICK_EVENT);
    event.setTarget(formFullId+"."+ derivedId);
    event.setUpdateRegionNames(updateRegionNames);
    event.setEventPrecondition(onClickPrecondition);

    if (viewModel.isOnClickEventRegistered()) {
      JspUtil.writeEventAttributes(out, event);
      JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");
    }
    
    return viewModel.isOnClickEventRegistered();
  }
}
