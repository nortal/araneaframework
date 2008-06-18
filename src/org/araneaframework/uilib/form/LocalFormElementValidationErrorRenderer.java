package org.araneaframework.uilib.form;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang.StringEscapeUtils;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;

/**
 * Form element validation error renderer which produces error messages directly
 * attached to rendered {@link FormElement}s.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class LocalFormElementValidationErrorRenderer implements FormElementValidationErrorRenderer {
  private static final long serialVersionUID = 1L;
  public static final LocalFormElementValidationErrorRenderer INSTANCE = new LocalFormElementValidationErrorRenderer();

  @SuppressWarnings("unchecked")
  public void addError(FormElement<?,?> element, String error) {
    Set<String> c = (Set<String>) element.getProperty(FormElementValidationErrorRenderer.ERRORS_PROPERTY_KEY);
    if (c == null) {
      // usually form element produces just one validation error message
      c = new LinkedHashSet<String>(1);
      element.setProperty(FormElementValidationErrorRenderer.ERRORS_PROPERTY_KEY, c);
    }

    c.add(error);
  }

  public void clearErrors(FormElement<?,?> element) {
    element.setProperty(FormElementValidationErrorRenderer.ERRORS_PROPERTY_KEY, null);
  }

  @SuppressWarnings("unchecked")
  public String getClientRenderText(FormElement<?,?> element) {
    Collection<String> messages = (Collection<String>) element.getProperty(FormElementValidationErrorRenderer.ERRORS_PROPERTY_KEY);
    if (messages != null) {
      String elScope = element.getScope().toString();

      StringBuffer sb = new StringBuffer(
          "<script type=\"text/javascript\">");
      sb.append("Aranea.UI.appendLocalFEValidationMessages('");

      // attach error messages to the same span that contains rendered form element
      sb.append(BaseFormElementHtmlTag.FORMELEMENT_SPAN_PREFIX + elScope + "', ");

      sb.append("\"<p");
      sb.append(" class='"+ RENDERED_FORMELEMENTERROR_STYLECLASS + " " + elScope + "'");
      sb.append(">");

      for (Iterator<String> i = messages.iterator(); i.hasNext();) {
        sb.append(getFormattedMessage(i.next().toString()));
      }

      sb.append("</p>\");");
      sb.append("</script>");

      return sb.toString();
    }

    return "";
  }

  protected String getFormattedMessage(String msg) {
    return StringEscapeUtils.escapeJavaScript(msg);
  }
}
