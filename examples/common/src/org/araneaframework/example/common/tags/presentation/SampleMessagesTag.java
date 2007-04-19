package org.araneaframework.example.common.tags.presentation;

import java.io.Writer;
import java.util.List;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.jsp.tag.presentation.MessagesHtmlTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Message tag - show the messages in {@link MessageContext} 
 * with given type. 
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "messages"
 *   body-content = "empty"
 */
public class SampleMessagesTag extends MessagesHtmlTag {

  protected void writeMessagesStart(Writer out, List entries) throws Exception {
    super.writeMessagesStart(out, entries);
    JspUtil.writeStartTag(out, "div");
    JspUtil.writeStartTag(out, "div");
    JspUtil.writeStartTag(out, "div");
  }

  protected void writeMessagesEnd(Writer out, List entries) throws Exception {
    JspUtil.writeEndTag(out, "div");
    JspUtil.writeEndTag(out, "div");
    JspUtil.writeEndTag(out, "div");
    super.writeMessagesEnd(out, entries);
  }

}
