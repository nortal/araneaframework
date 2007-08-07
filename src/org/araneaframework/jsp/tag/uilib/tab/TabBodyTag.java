package org.araneaframework.jsp.tag.uilib.tab;

import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.include.WidgetIncludeTag;
import org.araneaframework.uilib.tab.TabContainerWidget;

/** 
 *  @jsp.tag
 *   name = "tabBody"
 *   body-content = "JSP"
 *   description = "This tag includes current tab." 
 *   
 * @author Nikita Salnikov-Tarnovski (<a href="mailto:nikem@webmedia.ee">nikem@webmedia.ee</a>)
 */
public class TabBodyTag extends WidgetIncludeTag {

  public TabBodyTag() throws JspException {
    setId(TabContainerWidget.SELECTED_TAB_KEY);
  }
}
