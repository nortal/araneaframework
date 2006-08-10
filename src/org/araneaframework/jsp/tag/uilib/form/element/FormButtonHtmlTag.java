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
import org.araneaframework.jsp.AraneaAttributes;
import org.araneaframework.jsp.DefaultEvent;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.util.JspStringUtil;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;

/**
 * Standard button form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "button"
 *   body-content = "JSP"
 *   description = "Form button, represents UiLib "ButtonControl"."
 */
public class FormButtonHtmlTag extends BaseFormButtonTag {
  private static final String RENDER_BUTTON = "button";
  private static final String RENDER_INPUT = "input";
  protected String renderMode = FormButtonHtmlTag.RENDER_BUTTON;

  {
    baseStyleClass = "aranea-button";
  }

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Prepare
    String name = this.getScopedFullFieldId();

    JspUtil.writeOpenStartTag(out, renderMode.equals(FormButtonHtmlTag.RENDER_BUTTON) ? FormButtonHtmlTag.RENDER_BUTTON : FormButtonHtmlTag.RENDER_INPUT);

    if (renderMode.equals(FormButtonHtmlTag.RENDER_INPUT))
      JspUtil.writeAttribute(out, "type", "button");
    JspUtil.writeAttribute(out, "id", name);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    if (showLabel && renderMode.equals(FormButtonHtmlTag.RENDER_INPUT)) {
      if (accessKey != null) {
        String escapedLabel = JspStringUtil
        .escapeHtmlEntities(localizedLabel);
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
      JspUtil.writeAttribute(out, "disabled", "true");
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
          String escapedLabel = JspStringUtil
          .escapeHtmlEntities(localizedLabel);
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
    if (!(renderMode.equals(FormButtonHtmlTag.RENDER_BUTTON) || renderMode.equals(FormButtonHtmlTag.RENDER_INPUT)))
      throw new AraneaJspException("<ui:button> 'renderMode' attribute must be '" + FormButtonHtmlTag.RENDER_BUTTON + "' or '"+ FormButtonHtmlTag.RENDER_INPUT+"'");
    this.renderMode = (String) evaluate("renderMode", renderMode, String.class);
  }

  protected boolean writeEventAttribute(Writer out) throws IOException,
  JspException {
    DefaultEvent event = new DefaultEvent();
    event.setId("onClicked");
    event.setTarget(formFullId+"."+ derivedId);
    event.setUpdateRegionNames(updateRegionNames);

    if (viewModel.isOnClickEventRegistered()) {
      JspUtil.writeEventAttributes(out, event);
      JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");
      JspUtil.writeAttribute(out, AraneaAttributes.EVENT_PRECONDITION_PREFIX+"onclick", onClickPrecondition);
    }
    
    /*
    this.writeEventAttributeForUiEvent(out, "onclick", derivedId, "onClicked",
          validateOnEvent, onClickPrecondition, updateRegionNames);*/

    return viewModel.isOnClickEventRegistered();
  }
}
