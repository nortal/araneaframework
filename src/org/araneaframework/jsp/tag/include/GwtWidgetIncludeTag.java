package org.araneaframework.jsp.tag.include;

import java.io.Writer;

import javax.servlet.jsp.JspException;

import org.araneaframework.integration.gwt.GwtWidget;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.uilib.BaseWidgetTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * GWT widget include tag.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "gwtWidgetInclude"
 *   body-content = "JSP"
 *   description = "TODO"
 */
public class GwtWidgetIncludeTag extends BaseWidgetTag {

  protected GwtWidget.ViewModel gwtWidgetViewModel;
  protected String styleClass;

  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    try {
      gwtWidgetViewModel = (GwtWidget.ViewModel) viewModel;
    } catch (ClassCastException e) {
      throw new AraneaJspException("Could not acquire GwtWidget view model.", e);
    }

    JspUtil.writeOpenStartTag(out, "div");
    JspUtil.writeAttribute(out, "id", fullId);
    JspUtil.writeAttribute(out, "class", "aranea-gwt" + (styleClass != null ? " " + styleClass : ""));
    JspUtil.writeCloseStartTag(out);

    return EVAL_BODY_INCLUDE;   
  }

  protected int doEndTag(Writer out) throws Exception {
    super.doEndTag(out);

    JspUtil.writeEndTag(out, "div");

    JspUtil.writeOpenStartTag(out, "script");
    JspUtil.writeAttribute(out, "type", "text/javascript");
    JspUtil.writeCloseStartTag(out);
    boolean firstRender = viewModel.getData().containsKey(GwtWidget.FIRSTRENDER_KEY);
    if (firstRender) {
      out.write("araneaGwtAddModule('" + fullId + "', '" + gwtWidgetViewModel.getModule() + "');\n");
    }
    out.write("araneaGwtRenderModule('" + fullId + "', '" + gwtWidgetViewModel.getModule() + "');\n");
    JspUtil.writeEndTag(out, "script");

    return EVAL_PAGE;
  }

  public void doFinally() {
    super.doFinally();
    gwtWidgetViewModel = null;
    styleClass = null;
  }

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false" 
   *   description = "CSS class for tag"
   */
  public void setStyleClass(String styleClass) throws JspException {
    if (styleClass != null)
      this.styleClass = (String) evaluate("styleClass", styleClass, String.class);
  }

}
