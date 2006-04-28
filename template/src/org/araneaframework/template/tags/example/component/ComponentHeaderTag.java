package org.araneaframework.template.tags.example.component;

import java.io.Writer;
import org.araneaframework.jsp.tag.UiPresentationTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 *
 * @jsp.tag
 *   name = "componentHeader"
 *   body-content = "JSP"
 */
public class ComponentHeaderTag extends UiPresentationTag {
	public final static String COMPONENT_HEADER_KEY= "example.component.header.key";
	public final static String DEFAULT_HEADER_STYLE = "component-header";

	public ComponentHeaderTag() {
		styleClass = ComponentHeaderTag.DEFAULT_HEADER_STYLE;
	}
	
	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		addContextEntry(ComponentHeaderTag.COMPONENT_HEADER_KEY, this);

		UiUtil.writeOpenStartTag(out, "div");
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeCloseStartTag(out);
		
		return EVAL_BODY_INCLUDE;
	}

	protected int doEndTag(Writer out) throws Exception {
		UiUtil.writeEndTag(out, "div");
		return super.doEndTag(out);
	}
}
