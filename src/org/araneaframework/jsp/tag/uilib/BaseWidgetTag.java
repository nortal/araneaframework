package org.araneaframework.jsp.tag.uilib;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspWidgetUtil;

public class BaseWidgetTag extends BaseTag {

  protected String id;
  protected String fullId;
  protected ApplicationWidget widget;
  protected ApplicationWidget.WidgetViewModel viewModel;

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "UiLib widget id." 
   */
  public void setId(String id) throws JspException {
    this.id = (String) evaluateNotNull("id", id, String.class);
  }

  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    if (id == null) {
      widget = getContextWidget();
    } else {
      widget = JspWidgetUtil.traverseToSubWidget(getContextWidget(), id);
    }
    fullId = widget.getScope().toString();
    viewModel = (ApplicationWidget.WidgetViewModel) widget._getViewable().getViewModel();

    // Continue
    return EVAL_BODY_INCLUDE;    
  }

  public void doFinally() {
    super.doFinally();
    // to prevent memory leaks in containers where tags might live very long
    id = fullId = null;
    widget = null;
    viewModel = null;
  }

}
