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

package org.araneaframework.uilib.form;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang.StringEscapeUtils;
import org.araneaframework.jsp.tag.uilib.form.BaseFormElementHtmlTag;

/**
 * Form element validation error renderer, which produces error messages directly attached to rendered
 * {@link FormElement}s.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class LocalFormElementValidationErrorRenderer implements FormElementValidationErrorRenderer {

  public static final LocalFormElementValidationErrorRenderer INSTANCE = new LocalFormElementValidationErrorRenderer();

  @SuppressWarnings("unchecked")
  public void addError(FormElement<?, ?> element, String error) {
    Set<String> c = (Set<String>) element.getProperty(FormElementValidationErrorRenderer.ERRORS_PROPERTY_KEY);
    if (c == null) {
      // usually form element produces just one validation error message
      c = new LinkedHashSet<String>(1);
      element.setProperty(FormElementValidationErrorRenderer.ERRORS_PROPERTY_KEY, c);
    }

    c.add(error);
  }

  public void clearErrors(FormElement<?, ?> element) {
    element.setProperty(FormElementValidationErrorRenderer.ERRORS_PROPERTY_KEY, null);
  }

  @SuppressWarnings("unchecked")
  public String getClientRenderText(FormElement<?, ?> element) {
    Collection<String> messages = (Collection<String>) element.getProperty(ERRORS_PROPERTY_KEY);
    StringBuffer sb = new StringBuffer();

    if (messages != null) {
      String elScope = element.getScope().toString();

      sb.append("<script type=\"text/javascript\">Aranea.UI.appendLocalFEValidationMessages('");

      // Attach error messages to the same span that contains rendered form element
      sb.append(BaseFormElementHtmlTag.FORMELEMENT_SPAN_PREFIX);
      sb.append(elScope);
      sb.append("', '<p class=\"");
      sb.append(RENDERED_FORMELEMENTERROR_STYLECLASS);
      sb.append(" ");
      sb.append(elScope);
      sb.append("\">");

      for (Iterator<String> i = messages.iterator(); i.hasNext();) {
        sb.append(getFormattedMessage(i.next().toString()));
      }

      sb.append("</p>')</script>");
    }

    return sb.toString();
  }

  protected String getFormattedMessage(String msg) {
    return StringEscapeUtils.escapeJavaScript(msg);
  }
}
