package org.araneaframework.template.tags.example.component;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.layout.UiLayoutBaseTag;
import org.araneaframework.jsp.tag.layout.UiLayoutRowTagInterface;
import org.araneaframework.jsp.tag.layout.UiStdLayoutRowTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 * @jsp.tag
 *   name = "componentForm"
 *   body-content = "JSP"
 */
public class ComponentFormTag extends UiLayoutBaseTag {
	public final static String COMPONENT_FORM_STYLE_CLASS = "form";
	
	protected String widthClass;
	
	public ComponentFormTag() {
		styleClass = ComponentFormTag.COMPONENT_FORM_STYLE_CLASS;
	}
	
	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		UiUtil.writeOpenStartTag(out, "table");
		UiUtil.writeAttribute(out, "class", styleClass);
		
		UiUtil.writeAttribute(out, "width", width);
		UiUtil.writeAttribute(out, "height", height);
		UiUtil.writeCloseStartTag(out);
		
		return EVAL_BODY_INCLUDE;
	}

	protected int doEndTag(Writer out) throws Exception {
		UiUtil.writeEndTag(out, "table");
		return super.doEndTag(out);
	}

	public UiLayoutRowTagInterface getRowTag(String styleClass) throws JspException {
		return new UiStdLayoutRowTag(styleClass, getCellClass());
	}
}
