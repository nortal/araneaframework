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
      tag.setEvent("function() { Aranea.UI.flowEventConfirm(" + " xxxxxx " + "); }");
    }

    return SKIP_BODY;
  }

  protected int doEndTag(Writer out) throws Exception {
    return super.doEndTag(out);
  }
}
