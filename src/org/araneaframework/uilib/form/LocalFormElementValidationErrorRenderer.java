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
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.lang.StringEscapeUtils;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.MessageContext.MessageData;
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

  public void addError(FormElement<?, ?> element, MessageData messageData) {
    getMessages(element).add(messageData);
  }

  public void clearErrors(FormElement<?, ?> element) {
    getMessages(element).clear();
  }

  public String getClientRenderText(FormElement<?, ?> element) {
    Collection<MessageData> messages = getMessages(element);
    StringBuffer sb = new StringBuffer();

    if (!messages.isEmpty()) {
      LocalizationContext locCtx = element.getEnvironment().requireEntry(LocalizationContext.class);
      String elScope = element.getScope().toString();

      // Attach error messages to the same span that contains rendered form element
      sb.append("<script type=\"text/javascript\">Aranea.UI.appendLocalFEValidationMessages('"
          + BaseFormElementHtmlTag.FORMELEMENT_SPAN_PREFIX);
      sb.append(elScope);
      sb.append("', '<p class=\"" + RENDERED_FORMELEMENTERROR_STYLECLASS + " ");
      sb.append(elScope);
      sb.append("\">");

      for (MessageData msg : messages) {
        String msgStr = locCtx.getMessage(msg.getMessage(), msg.getMessageParameters());
        sb.append(getFormattedMessage(msgStr));
      }

      sb.append("</p>')</script>");
    }

    return sb.toString();
  }

  /**
   * Retrieves the messages collection for given form <code>element</code>.
   * 
   * @param element The form element for which the messages collection should be retrieved.
   * @return A non-<code>null</code> collection.
   * @since 2.0
   */
  @SuppressWarnings("unchecked")
  protected static Set<MessageData> getMessages(FormElement<?, ?> element) {
    Set<MessageData> msgs = (Set<MessageData>) element.getProperty(ERRORS_PROPERTY_KEY);

    if (msgs == null) {
      msgs = new LinkedHashSet<MessageData>();
      element.setProperty(ERRORS_PROPERTY_KEY, msgs);
    }

    return msgs;
  }

  /**
   * Formats the given message by escaping it from JavaScript code.
   * 
   * @param msg The message to escape.
   * @return The escaped message that can be used in JavaScript code.
   */
  protected static String getFormattedMessage(String msg) {
    return StringEscapeUtils.escapeJavaScript(msg);
  }
}
