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

package org.araneaframework.example.main.web.form;

import org.araneaframework.framework.MessageContext;

/**
 * This example extends DisabledFormWidget demonstrating how it is possible to render form elements with "readonly"
 * attribute instead of "disabled" attribute.
 * <p>
 * Actually, this feature is more about the "view" than "controller" (in the context of model-view-controller pattern)
 * since read-only elements are basically the same as disabled ones by functionality, i.e. they cannot be edited.
 * Therefore, the widget just disables input elements, while JSP page (see simpleForm.jsp) will specify "readonly"
 * attribute on element tags.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 */
public class ReadOnlyFormWidget extends DisabledFormWidget {

  @Override
  protected void init() {
    super.init();

    // We pass read-only mode indicating flag to the JSP page, which accesses it with ${viewData.readonly}:
    putViewData("readonly", Boolean.TRUE);

    getMessageCtx().clearPermanentMessages();
    getMessageCtx().showPermanentMessage(MessageContext.INFO_TYPE, "form.msg.readonly");
  }
}
