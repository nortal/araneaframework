package org.araneaframework.jsp.tag.uilib;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.jsp.container.UiWidgetContainer;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspWidgetUtil;

public class BaseWidgetTag extends BaseTag {
  protected String id;
  protected String fullId;
  protected String scopedFullId;
  protected ApplicationWidget widget;
  protected ApplicationWidget.WidgetViewModel viewModel;
  protected UiWidgetContainer container;

  /**
   * @jsp.attribute
   *   type = "java.lang.String"
   *   required = "false"
   *   description = "UiLib widget id." 
   */
  public void setId(String id) throws JspException {
    this.id = (String)evaluateNotNull("id", id, String.class);
  }

  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
  
    container = (UiWidgetContainer) requireContextEntry(UiWidgetContainer.REQUEST_CONTEXT_KEY);
  
    // Get data
    widget = JspWidgetUtil.getWidgetFromContext(id, pageContext);
    viewModel = (ApplicationWidget.WidgetViewModel) widget._getViewable().getViewModel();
    fullId = JspWidgetUtil.getWidgetFullIdFromContext(id, pageContext);    
  
    if (fullId == null) 
      throw new AraneaJspException("Widget must have an id!");        
  
    scopedFullId = container.scopeWidgetFullId(pageContext, fullId);
  
    // Continue
    return EVAL_BODY_INCLUDE;    
  }

}