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

package org.araneaframework.jsp.tag.uilib.form.element;

import org.araneaframework.Path;

import java.io.IOException;
import java.io.Writer;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.event.OnClickEventListener;

/**
 * Button form element tag, represents {@link org.araneaframework.uilib.form.control.ButtonControl}. Rendered with
 * either &lt;input type=&quot;button&quot;&gt; or just &lt;button&gt; depending on <code>renderMode</code> attribute.
 * 
 * @author Oleg Mürk
 * @jsp.tag name = "button" body-content = "JSP" description = "Form button, represents UiLib 'ButtonControl'."
 */
public class FormButtonHtmlTag extends BaseFormButtonTag {

  public FormButtonHtmlTag() {
    this.baseStyleClass = "aranea-button";
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // Prepare
    String name = this.getFullFieldId();

    writeButtonStartTag(out);

    JspUtil.writeAttribute(out, "id", name);
    JspUtil.writeAttribute(out, "name", name);
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttribute(out, "tabindex", this.tabindex);
    if (this.events && !this.viewModel.isDisabled()) {
      writeEventAttribute(out);
    } else if (this.viewModel.isDisabled()) {
      JspUtil.writeAttribute(out, "disabled", "disabled");
    }
    JspUtil.writeAttributes(out, this.attributes);
    if (this.accessKey != null) {
      JspUtil.writeAttribute(out, "accesskey", this.accessKey);
    }

    writeButtonCloseTag(out, false);

    // Continue
    return EVAL_BODY_INCLUDE;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    writeButtonCloseTag(out, true);

    // Continue
    super.doEndTag(out);
    return EVAL_PAGE;
  }

  protected boolean writeEventAttribute(Writer out) throws IOException {
    if (this.viewModel.isOnClickEventRegistered()) {
      UiUpdateEvent event = new UiUpdateEvent();
      event.setId(OnClickEventListener.ON_CLICK_EVENT);
      event.setTarget(this.formFullId + Path.SEPARATOR + this.derivedId);
      event.setUpdateRegionNames(this.updateRegionNames);
      event.setEventPrecondition(this.onClickPrecondition);

      JspUtil.writeEventAttributes(out, event);
      JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");
    }

    return this.viewModel.isOnClickEventRegistered();
  }
}
