package org.araneaframework.template.tags.example.component;

import java.io.Writer;
import org.araneaframework.jsp.tag.UiPresentationTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 *
 * @jsp.tag
 *   name = "componentName"
 *   body-content = "JSP"
 */
public class ComponentNameTag extends UiPresentationTag {
  public final static String COMPONENT_HEADER_KEY= "example.component.header.key";
  public final static String DEFAULT_HEADER_NAME_STYLE = "name";

  public ComponentNameTag() {
    styleClass = ComponentNameTag.DEFAULT_HEADER_NAME_STYLE;
  }
  
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    // make sure we are inside component header and fail if no header is present.
    // not strictly necessary, mainly for demonstration of attribute usage.
    requireContextEntry(ComponentHeaderTag.COMPONENT_HEADER_KEY);

    UiUtil.writeOpenStartTag(out, "div");
    UiUtil.writeAttribute(out, "class", styleClass);
    UiUtil.writeCloseStartTag(out);

    return EVAL_BODY_INCLUDE;
  }

  protected int doEndTag(Writer out) throws Exception {
    UiUtil.writeEndTag(out, "div");
    return super.doEndTag(out);
  }
}
