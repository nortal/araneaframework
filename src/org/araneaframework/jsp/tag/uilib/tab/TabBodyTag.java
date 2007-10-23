/**
 * Copyright 2007 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package org.araneaframework.jsp.tag.uilib.tab;

import java.io.Writer;
import org.araneaframework.jsp.tag.include.WidgetIncludeTag;
import org.araneaframework.uilib.tab.TabContainerWidget;
import org.araneaframework.uilib.tab.TabWidget;

/** 
 *  @jsp.tag
 *   name = "tabBody"
 *   body-content = "empty"
 *   description = "Writes out currently active tab content. It is meant to be used inside &lt;ui:tabContainer&gt; tag." 
 * 
 * @author Nikita Salnikov-Tarnovski (<a href="mailto:nikem@webmedia.ee">nikem@webmedia.ee</a>)
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @see TabContainerWidget
 */
public class TabBodyTag extends WidgetIncludeTag {
	protected int doStartTag(Writer out) throws Exception {   
		TabContainerWidget tabContainerWidget = (TabContainerWidget) requireContextEntry(TabContainerHtmlTag.TAB_CONTAINER_WIDGET);
		String tabContentWidgetId = 
			tabContainerWidget.getScope().getId().toString() + "." + 
			tabContainerWidget.getSelectedTab().getScope().getId().toString() + "." + TabWidget.CONTENT_WIDGET_KEY;

		setId(tabContentWidgetId);
		super.doStartTag(out);
		return SKIP_BODY;
	}
}
