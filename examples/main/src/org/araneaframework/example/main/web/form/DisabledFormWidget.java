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
 * This example extends SimpleFormWidget demonstrating how easy it is to disable/enable form components.
 * <p>
 * Note that when elements are disabled in Aranea, they won't process requests. Therefore, even when inputs are enabled
 * in browser, their values changed, and committed back to the server, the values in disabled components will remain as
 * they were just before being disabled. However, it is possible to modify values of disabled form elements through the
 * API, as this demo confirms it.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 */
public class DisabledFormWidget extends SimpleFormWidget {

  @Override
  protected void init() {
    super.init();

    this.simpleForm.setDisabled(true);
    this.simpleForm.getElement("fillBtn").setDisabled(false); // Enable fill-with-data button
    this.simpleForm.getElement("validateBtn").setDisabled(false); // Enable validate-data button

    // Note: FormWidget.isDisabled() returns true only when all of its elements are disabled.
    // Here it would return false, since buttons are enabled.

    getMessageCtx().showPermanentMessage(MessageContext.INFO_TYPE, "form.msg.disabled");
  }

  @Override
  protected void destroy() {
    getMessageCtx().clearPermanentMessages();
  }
}
