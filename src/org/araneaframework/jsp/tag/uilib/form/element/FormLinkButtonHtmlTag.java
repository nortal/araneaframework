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
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.event.OnClickEventListener;


/**
 * Standard button link form element tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "linkButton"
 *   body-content = "JSP"
 *   description = "HTML link, represents UiLib "ButtonControl"."
 */
public class FormLinkButtonHtmlTag extends BaseFormButtonTag {
  {
    baseStyleClass = "aranea-link";
  }
  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Write button tag             
    JspUtil.writeOpenStartTag(out, "a");
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "id", this.getFullFieldId());
    JspUtil.writeAttribute(out, "href", "#");    
    JspUtil.writeAttribute(out, "tabindex", tabindex);

    if (events) {
      writeEventAttribute(out);
    }

    JspUtil.writeAttributes(out, attributes);
    JspUtil.writeCloseStartTag_SS(out);

    // Continue
    return EVAL_BODY_INCLUDE;
  }    

  @Override
  protected int doEndTag(Writer out) throws Exception {
    if (showLabel)
      JspUtil.writeEscaped(out, localizedLabel);

    JspUtil.writeEndTag(out, "a");

    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;
  }  

  protected boolean writeEventAttribute(Writer out) throws IOException {
    if (viewModel.isOnClickEventRegistered()) {
      UiUpdateEvent event = new UiUpdateEvent(OnClickEventListener.ON_CLICK_EVENT, formFullId + "." + derivedId, null, updateRegionNames);
      event.setEventPrecondition(onClickPrecondition);
      JspUtil.writeEventAttributes(out, event);
      JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");
    }

    return viewModel.isOnClickEventRegistered();
  }
}
