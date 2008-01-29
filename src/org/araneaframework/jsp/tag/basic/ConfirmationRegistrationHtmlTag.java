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

package org.araneaframework.jsp.tag.basic;

import java.io.Writer;
import org.araneaframework.framework.ConfirmationContext;
import org.araneaframework.jsp.tag.BaseTag;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 * 
 * @jsp.tag
 *   name = "registerConfirmation"
 *   body-content = "empty"
 *   description = "Registers confirmation event executor to be started on body load."
 */
public class ConfirmationRegistrationHtmlTag extends BaseTag {
  protected int doStartTag(Writer out) throws Exception {
    OnLoadEventHtmlTag tag = new OnLoadEventHtmlTag();
    ConfirmationContext ctx = (ConfirmationContext) getEnvironment().getEntry(ConfirmationContext.class);
    String message = ctx != null ? ctx.getConfirmationMessage() : null;
    if (message != null) {
      tag.setEvent("Aranea.UI.flowEventConfirm('" + ctx.getConfirmationScope().toString() + "', '" +  message + "');");
    }

    registerAndExecuteStartTag(tag);
    executeEndTagAndUnregister(tag);

    return SKIP_BODY;
  }

  protected int doEndTag(Writer out) throws Exception {
    return super.doEndTag(out);
  }
}
