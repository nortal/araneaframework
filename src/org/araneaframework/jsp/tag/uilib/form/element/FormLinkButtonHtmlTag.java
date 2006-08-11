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
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;


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
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Write button tag             
    JspUtil.writeOpenStartTag(out, "a");
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "id", this.getScopedFullFieldId());
    JspUtil.writeAttribute(out, "href", "#");    
    JspUtil.writeAttribute(out, "label", localizedLabel);
    JspUtil.writeAttribute(out, "tabindex", tabindex);

    if (events) {
      writeEventAttribute(out);
    }

    JspUtil.writeAttributes(out, attributes);
    JspUtil.writeCloseStartTag_SS(out);

    // Continue
    return EVAL_BODY_INCLUDE;
  }    

  protected int doEndTag(Writer out) throws Exception {
    if (showLabel)
      JspUtil.writeEscaped(out, localizedLabel);

    JspUtil.writeEndTag(out, "a");

    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;
  }  

  protected boolean writeEventAttribute(Writer out) throws IOException, JspException {
    if (viewModel.isOnClickEventRegistered()) {
      DefaultEvent event = new DefaultEvent("onClicked", formFullId + "." + derivedId, null, updateRegionNames);
      JspUtil.writeEventAttributes(out, event);
      JspUtil.writeAttribute(out, AraneaAttributes.EVENT_PRECONDITION_PREFIX+"onclick", onClickPrecondition);
      JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");
      
      /*
      this.writeEventAttributeForUiEvent(out, "onclick", derivedId, "onClicked", validateOnEvent, onClickPrecondition,
          updateRegionNames);*/
    }

    return viewModel.isOnClickEventRegistered();
  }
}
