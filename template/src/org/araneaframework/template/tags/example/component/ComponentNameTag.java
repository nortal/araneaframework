package org.araneaframework.template.tags.example.component;

import java.io.Writer;
import javax.servlet.jsp.PageContext;
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

	protected void init() {
		super.init();
		styleClass = ComponentNameTag.DEFAULT_HEADER_NAME_STYLE;
	}
	
	protected int before(Writer out) throws Exception {
		super.before(out);

		// make sure we are inside component header and fail if no header is present.
		// not strictly necessary, mainly for demonstration of attribute usage.
		readAttribute(ComponentHeaderTag.COMPONENT_HEADER_KEY, PageContext.REQUEST_SCOPE);

		UiUtil.writeOpenStartTag(out, "div");
		UiUtil.writeAttribute(out, "class", styleClass);
		UiUtil.writeCloseStartTag(out);

		return EVAL_BODY_INCLUDE;
	}

	protected int after(Writer out) throws Exception {
		UiUtil.writeEndTag(out, "div");
		super.after(out);
		return EVAL_PAGE;
	}
}
