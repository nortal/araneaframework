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
