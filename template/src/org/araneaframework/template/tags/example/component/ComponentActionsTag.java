package org.araneaframework.template.tags.example.component;

import java.io.Writer;
import org.araneaframework.jsp.tag.UiPresentationTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 * @jsp.tag
 *   name = "componentActions"
 *   body-content = "JSP"
 */
public class ComponentActionsTag extends UiPresentationTag {
	public final static String COMPONENT_ACTION_STYLE_CLASS = "actions";

	public ComponentActionsTag() {
		styleClass = ComponentActionsTag.COMPONENT_ACTION_STYLE_CLASS;
	}

	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		UiUtil.writeOpenStartTag(out, "div");
		UiUtil.writeAttribute(out, "class", styleClass);
		UiUtil.writeCloseStartTag(out);
		
		return EVAL_BODY_INCLUDE;
	}

	protected int doEndTag(Writer out) throws Exception {
		UiUtil.writeEndTag(out, "div");
		super.doEndTag(out);
		return EVAL_PAGE;
	}
}
