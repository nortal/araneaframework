package org.araneaframework.template.tags.example;

import java.io.Writer;
import org.araneaframework.jsp.tag.UiPresentationTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 *
 * @jsp.tag
 *   name = "exampleContainerHeader"
 *   body-content = "JSP"
 */
public class ContainerHeader extends UiPresentationTag {
	public final static String DEFAULT_HEADER_STYLE = "component-header";

	protected void init() {
		super.init();
		styleClass = ContainerHeader.DEFAULT_HEADER_STYLE;
	}
	
	protected int before(Writer out) throws Exception {
		super.before(out);
		UiUtil.writeOpenStartTag(out, "div");
		UiUtil.writeAttribute(out, "class", styleClass);
		UiUtil.writeCloseStartTag(out);
		
		UiUtil.writeOpenStartTag(out, "div");
		UiUtil.writeAttribute(out, "class", "name");
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
