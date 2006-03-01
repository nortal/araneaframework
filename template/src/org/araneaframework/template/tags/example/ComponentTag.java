package org.araneaframework.template.tags.example;

import java.io.Writer;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.UiPresentationTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 * @jsp.tag
 *   name = "component"
 *   body-content = "JSP"
 */
public class ComponentTag extends UiPresentationTag {
	public final static String COMPONENT_KEY= "example.component.key";
	public final static String DEFAULT_COMPONENT_STYLE = "component";

	protected void init() {
		super.init();
		styleClass = ComponentTag.DEFAULT_COMPONENT_STYLE;
	}

	protected int before(Writer out) throws Exception {
		super.before(out);
		
		pushAttribute(ComponentTag.COMPONENT_KEY, this, PageContext.REQUEST_SCOPE);

		UiUtil.writeOpenStartTag(out, "div");
		UiUtil.writeAttribute(out, "class", styleClass);
		UiUtil.writeCloseStartTag(out);
		
		// second div... maybe should be moved out
		UiUtil.writeOpenStartTag(out, "div");
		UiUtil.writeAttribute(out, "class", "w100p");
		UiUtil.writeCloseStartTag(out);

		return EVAL_BODY_INCLUDE;
	}

	protected int after(Writer out) throws Exception {
		UiUtil.writeEndTag(out, "div");
		UiUtil.writeEndTag(out, "div");
		super.after(out);
		return EVAL_PAGE;
	}
}
