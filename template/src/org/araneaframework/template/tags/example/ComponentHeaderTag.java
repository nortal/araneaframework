package org.araneaframework.template.tags.example;

import java.io.Writer;
import javax.servlet.jsp.PageContext;
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

	protected void init() {
		super.init();
		styleClass = ComponentHeaderTag.DEFAULT_HEADER_STYLE;
	}
	
	protected int before(Writer out) throws Exception {
		super.before(out);
		
		pushAttribute(ComponentHeaderTag.COMPONENT_HEADER_KEY, this, PageContext.REQUEST_SCOPE);
		
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
