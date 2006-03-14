package org.araneaframework.template.tags.example.component;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.log4j.Logger;
import org.araneaframework.jsp.tag.layout.UiLayoutBaseTag;
import org.araneaframework.jsp.tag.layout.UiLayoutRowTagInterface;
import org.araneaframework.jsp.tag.layout.UiStdLayoutRowTag;
import org.araneaframework.jsp.tag.uilib.list.UiListRowsTag;
import org.araneaframework.jsp.util.UiUtil;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 * @jsp.tag
 *   name = "componentList"
 *   body-content = "JSP"
 *   description = "Starts a container that is suitable for inserting &lt;listRows&gt;"
 */
public class ComponentListTag extends UiLayoutBaseTag {
	private static final Logger log = Logger.getLogger(ComponentListTag.class);
	public final static String COMPONENT_LIST_STYLE_CLASS = "data";
	public final static String COMPONENT_LIST_EVEN_ROW_CLASS = "even";
	
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
		String rowRequestId = (String) getAttribute(UiListRowsTag.ROW_REQUEST_ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		if (rowRequestId != null) {
			// this row is inside the list
			long id = Long.parseLong(rowRequestId);
			if (((id + 1) % 2) == 0)
				return new UiStdLayoutRowTag(ComponentListTag.COMPONENT_LIST_EVEN_ROW_CLASS, null);	
		}

		return new UiStdLayoutRowTag(styleClass, null);
	}
}
