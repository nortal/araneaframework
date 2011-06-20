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
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
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

    getMessageCtx().showPermanentMessage(MessageContext.INFO_TYPE, "#The form elements (except buttons) are disabled!");
  }

  @Override
  protected void destroy() throws Exception {
    getMessageCtx().clearPermanentMessages();
  }
}
