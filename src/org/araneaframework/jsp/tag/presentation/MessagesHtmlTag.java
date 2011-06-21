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

package org.araneaframework.jsp.tag.presentation;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringEscapeUtils;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Message tag - show the messages in {@link MessageContext} with given type.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @jsp.tag name = "messages" body-content = "empty"
 */

public class MessagesHtmlTag extends PresentationTag {

  protected String type;

  protected String divId;

  protected Boolean escapeHtml = true;

  public MessagesHtmlTag() {
    this.baseStyleClass = "aranea-messages";
  }

  @Override
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    return SKIP_BODY;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    super.doEndTag(out);

    MessageContext messageContext = getEnvironment().requireEntry(MessageContext.class);
    LocalizationContext locContext = getEnvironment().requireEntry(LocalizationContext.class);
    Map<String, Collection<String>> messageMap = messageContext.getResolvedMessages(locContext);

    List<Map.Entry<String, Collection<String>>> entries = new ArrayList<Map.Entry<String, Collection<String>>>();
    if (messageMap != null) {
      for (Map.Entry<String, Collection<String>> entry : messageMap.entrySet()) {
        if (this.type == null || entry.getKey().equals(this.type)) {
          entries.add(entry);
        }
      }
    }

    // matching messages, write them out
    writeMessagesStart(out, entries);
    writeMessages(out, entries);
    writeMessagesEnd(out, entries);

    return EVAL_PAGE;
  }

  /**
   * @since 1.1
   */
  protected void writeMessagesStart(Writer out, List<Map.Entry<String, Collection<String>>> entries) throws Exception {
    JspUtil.writeOpenStartTag(out, "div");
    JspUtil.writeAttribute(out, "id", getDivId());
    JspUtil.writeAttribute(out, "class", getStyleClass());

    if (this.type != null) {
      JspUtil.writeAttribute(out, "arn-msgs-type", this.type);
    }

    JspUtil.writeAttribute(out, "style", entries.size() == 0 ? "display: none" : getStyle());
    JspUtil.writeAttributes(out, this.attributes);
    JspUtil.writeCloseStartTag(out);
  }

  /**
   * Writes any HTML (etc) that should follow messages.
   * @param out The writer of rendered output.
   * @param entries A collection of messages to render.
   * @throws Exception Any exception that may occur.
   * @since 1.1
   */
  protected void writeMessagesEnd(Writer out, List<Map.Entry<String, Collection<String>>> entries) throws Exception {
    JspUtil.writeEndTag(out, "div");
  }

  /**
   * @since 1.1
   */
  protected void writeMessages(Writer out, List<Map.Entry<String, Collection<String>>> entries) throws Exception {
    for (Iterator<Map.Entry<String, Collection<String>>> i = entries.iterator(); i.hasNext();) {
      Collection<String> messages = i.next().getValue();

      for (Iterator<String> j = messages.iterator(); j.hasNext();) {
        writeMessageBody(out, j.next());
        if (j.hasNext()) {
          writeMessageSeparator(out);
        }
      }

      if (i.hasNext()) {
        writeMessageSeparator(out);
      }
    }
  }

  /**
   * @since 1.1
   */
  protected void writeMessageBody(Writer out, String message) throws Exception {
    if (this.escapeHtml) {
      message = StringEscapeUtils.escapeHtml(message);
    }
    out.write(message);
  }

  /**
   * @since 1.1
   */
  protected void writeMessageSeparator(Writer out) throws Exception {
    JspUtil.writeStartEndTag(out, "br");
  }

  /** @since 1.1 */
  protected String getDivId() {
    return this.divId;
  }

  // Tag attributes

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Type of messages to show."
   */
  public void setType(String type) {
    this.type = evaluate("type", type, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Sets the ID of the HTML &lt;DIV&gt; inside which the messages are rendered."
   * 
   * @since 1.1
   */
  public void setDivId(String divId) {
    this.divId = evaluate("divId", divId, String.class);
  }

  /**
   * @jsp.attribute
   *    type = "java.lang.String"
   *    required = "false"
   *    description = "Sets whether the messages should be escaped or not (default: escape)."
   * 
   * @since 1.1.4
   */
  public void setEscapeHtml(String escapeHtml) throws JspException {
    this.escapeHtml = evaluateNotNull("escapeHtml", escapeHtml, Boolean.class);
  }
}
