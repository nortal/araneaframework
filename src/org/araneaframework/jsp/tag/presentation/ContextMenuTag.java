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
package org.araneaframework.jsp.tag.presentation;

import java.io.Writer;
import org.araneaframework.jsp.tag.uilib.WidgetTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.menu.ContextMenuItem;
import org.araneaframework.uilib.menu.ContextMenuWidget;
import org.araneaframework.uilib.util.NameUtil;
/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "contextMenu"
 *   body-content = "empty"
 *   description = "Registers context menu with given identifier. Context menu will be rendered in supported browsers
 *   (IE & Mozilla based browsers) when mouse right-click is made on widget associated with particular context menu widget."
 */
public class ContextMenuTag extends WidgetTag {
	public int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);

		ContextMenuWidget widget = (ContextMenuWidget) JspWidgetUtil.traverseToSubWidget(getContextWidget(), id);
		ContextMenuItem menu = widget.getMenu();
		
		JspUtil.writeOpenStartTag(out, "script");
		JspUtil.writeAttribute(out, "type", "text/javascript");
		JspUtil.writeCloseStartTag(out);
		
		out.write("araneaContextMenuHolder.addMenu('");
		out.write(NameUtil.getLongestPrefix(widget.getScope().toString()));
		out.write("',");

		writeContextMenu(out, menu);
		out.write(");");

		JspUtil.writeEndTag(out, "script");

		return SKIP_BODY;
	}
	
	protected void writeContextMenu(Writer out, ContextMenuItem menu) throws Exception {
		out.write(menu.toJSON().toString());
	}
}
