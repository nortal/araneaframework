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

package org.araneaframework.jsp.tag.presentation;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.apache.commons.lang.StringEscapeUtils;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Message tag - show the messages in {@link MessageContext} 
 * with given type. 
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "messages"
 *   body-content = "empty"
 */

public class MessagesHtmlTag extends PresentationTag {
  protected String type;
  protected String divId;

  {
    baseStyleClass = "aranea-messages";
  }

  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    return SKIP_BODY;
  }

  protected int doEndTag(Writer out) throws Exception {
    super.doEndTag(out);

    MessageContext messageContext = (MessageContext) getEnvironment().requireEntry(MessageContext.class);
    Map messageMap = messageContext.getMessages();

    List entries = new ArrayList();
    if (messageMap != null) {
      for (Iterator i = messageMap.entrySet().iterator(); i.hasNext(); ) {
        Map.Entry entry = (Map.Entry) i.next();
        if (type == null || ((String)entry.getKey()).equals(type)) {
          entries.add(entry);
        }
      }
    }

    /* matching messages, write them out */
    writeMessagesStart(out, entries);
    writeMessages(out, entries);
    writeMessagesEnd(out, entries);

    return EVAL_PAGE;
  }

  /**
   * @since 1.1
   */
  protected void writeMessagesStart(Writer out, List entries) throws Exception {
    JspUtil.writeOpenStartTag(out, "div");
    JspUtil.writeAttribute(out, "divId", getDivId());
    JspUtil.writeAttribute(out, "class", getStyleClass());
    if (type != null) {
      JspUtil.writeAttribute(out, "arn-msgs-type", type);
    }
    JspUtil.writeAttribute(out, "style", entries.size() == 0 ? "display: none" : getStyle());
    JspUtil.writeAttributes(out, attributes);
    JspUtil.writeCloseStartTag(out);
  }

  /**
   * @since 1.1
   */
  protected void writeMessagesEnd(Writer out, List entries) throws Exception {
    JspUtil.writeEndTag(out, "div");
  }

  /**
   * @since 1.1
   */
  protected void writeMessages(Writer out, List entries) throws Exception {
    for (Iterator i = entries.iterator(); i.hasNext(); ) {
      Collection messages = (Collection) ((Map.Entry) i.next()).getValue();

      for (Iterator j = messages.iterator(); j.hasNext();) {
        writeMessageBody(out, (String) j.next());
        if (j.hasNext())
          writeMessageSeparator(out);
      }
      if (i.hasNext())
        writeMessageSeparator(out);
    }
  }

  /**
   * @since 1.1
   */
  protected void writeMessageBody(Writer out, String message) throws Exception {
    out.write(StringEscapeUtils.escapeHtml(message));
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

  /* ***********************************************************************************
   * Tag attributes
   * ***********************************************************************************/

  /**
   * @jsp.attribute
   * type = "java.lang.String"
   * required = "false"
   * description = "Type of messages to show."
   */
  public void setType(String type) throws JspException {
    this.type = (String) evaluate("type", type, String.class);
  }
  
  /**
   * @jsp.attribute
   * type = "java.lang.String"
   * required = "false"
   * description = "Sets the ID of the HTML &lt;DIV&gt; inside which the messages are rendered."
   * 
   * @since 1.1
   */
  public void setDivId(String divId) throws JspException {
    this.divId = (String) evaluate("divId", divId, String.class);
  }
}
