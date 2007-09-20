package org.araneaframework.jsp.tag.uilib.newtab;

import java.io.Writer;
import org.araneaframework.jsp.tag.include.WidgetIncludeTag;
import org.araneaframework.uilib.newtab.TabContainerWidget;
import org.araneaframework.uilib.newtab.TabWidget;

/** 
 *  @jsp.tag
 *   name = "newtabBody"
 *   body-content = "empty"
 *   description = "This tag includes current tab." 
 *   
 * @author Nikita Salnikov-Tarnovski (<a href="mailto:nikem@webmedia.ee">nikem@webmedia.ee</a>)
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class TabBodyTag extends WidgetIncludeTag {
	protected int doStartTag(Writer out) throws Exception {   
		TabContainerWidget tabContainerWidget = (TabContainerWidget) requireContextEntry(TabContainerTag.TAB_CONTAINER_WIDGET);
		String tabContentWidgetId = 
			tabContainerWidget.getScope().getId().toString() + "." + 
			tabContainerWidget.getSelectedTab().getScope().getId().toString() + "." + TabWidget.CONTENT_WIDGET_KEY;

		setId(tabContentWidgetId);
		super.doStartTag(out);
		return SKIP_BODY;
	}
}
