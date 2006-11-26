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

  public MessagesHtmlTag() {
    styleClass = "aranea-messages";
  }
  
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    return SKIP_BODY;
  }

  protected int doEndTag(Writer out) throws Exception {
    super.doEndTag(out);

    Map messageMap = (Map) getOutputData().getAttribute(MessageContext.MESSAGE_KEY);
    if (messageMap == null)
      return EVAL_PAGE;

    List entries = new ArrayList();
    for (Iterator i = messageMap.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      if (type == null || ((String)entry.getKey()).equals(type)) {
        entries.add(entry);
      }
    }

    if (entries.size() == 0)
      return EVAL_PAGE;

    /* matching messages, write them out */
    JspUtil.writeOpenStartTag(out, "div");
    JspUtil.writeAttribute(out, "class", getStyleClass());
    JspUtil.writeAttribute(out, "style", getStyle());
    JspUtil.writeAttributes(out, attributes);
    JspUtil.writeCloseStartTag(out);

    JspUtil.writeStartTag(out, "div");
    JspUtil.writeStartTag(out, "div");
    JspUtil.writeStartTag(out, "div");

    for (Iterator i = entries.iterator(); i.hasNext(); ) {
      Collection messages = (Collection) ((Map.Entry) i.next()).getValue();

      for (Iterator j = messages.iterator(); j.hasNext();) {
        out.write(StringEscapeUtils.escapeHtml((String) j.next()));
        if (j.hasNext())
          JspUtil.writeStartEndTag(out, "br");
      }
      if (i.hasNext())
        JspUtil.writeStartEndTag(out, "br");;
    }

    JspUtil.writeEndTag(out, "div");
    JspUtil.writeEndTag(out, "div");
    JspUtil.writeEndTag(out, "div");
    JspUtil.writeEndTag(out, "div");

    return EVAL_PAGE;
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
}
