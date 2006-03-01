package org.araneaframework.template.tags.example;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.layout.UiLayoutBaseTag;
import org.araneaframework.jsp.tag.layout.UiLayoutRowTagInterface;
import org.araneaframework.jsp.tag.layout.UiStdLayoutRowTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 * @jsp.tag
 *   name = "componentList"
 *   body-content = "JSP"
 */
public class ComponentListTag extends UiLayoutBaseTag {
	public final static String COMPONENT_LIST_STYLE_CLASS = "data";
	
	protected void init() {
		super.init();
		styleClass = ComponentListTag.COMPONENT_LIST_STYLE_CLASS;
	}
	
	protected int before(Writer out) throws Exception {
		super.before(out);
		UiUtil.writeOpenStartTag(out, "table");
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeCloseStartTag(out);

		return EVAL_BODY_INCLUDE;
	}

	protected int after(Writer out) throws Exception {
		UiUtil.writeEndTag(out, "table");
		super.after(out);
		return EVAL_PAGE;
	}

	public UiLayoutRowTagInterface getRowTag(String styleClass) throws JspException {
		return new UiStdLayoutRowTag(styleClass, null);
	}
}
