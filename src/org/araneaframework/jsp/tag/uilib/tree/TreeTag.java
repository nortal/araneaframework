/**
 * Copyright 2006 Webmedia Group Ltd.
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

package org.araneaframework.jsp.tag.uilib.tree;

import java.io.Writer;
import java.util.List;
import java.util.ListIterator;

import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.jsp.tag.context.WidgetContextTag;
import org.araneaframework.jsp.tag.uilib.BaseWidgetTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.tree.TreeNodeWidget;
import org.araneaframework.uilib.tree.TreeWidget;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 * @jsp.tag
 *   name = "tree"
 *   body-content = "empty"
 *   description = "Tree"
 */
public class TreeTag extends BaseWidgetTag {

	public int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		TreeWidget.ViewModel treeViewModel = (TreeWidget.ViewModel) viewModel;

		WidgetContextTag treeWidgetContextTag = new WidgetContextTag();
		registerSubtag(treeWidgetContextTag);
		treeWidgetContextTag.setId(id);
		executeStartSubtag(treeWidgetContextTag);

		WidgetContextTag widgetContextTag = new WidgetContextTag();
		registerSubtag(widgetContextTag);
		widgetContextTag.setId(treeViewModel.getRootId());
		executeStartSubtag(widgetContextTag);

		JspUtil.writeStartTag(out, "ul");
		renderNode(out, (TreeNodeWidget.ViewModel) treeViewModel.getRoot().getViewModel());
		JspUtil.writeEndTag(out, "ul");

		executeEndTagAndUnregister(widgetContextTag);

		executeEndTagAndUnregister(treeWidgetContextTag);

		return SKIP_BODY;
	}

	private void renderNode(Writer out, TreeNodeWidget.ViewModel node) throws Exception {
		JspUtil.writeStartTag(out, "li");

		/* Render display widget of node */
		ApplicationWidget display = JspWidgetUtil.getWidgetFromContext(node.getDisplayId(), pageContext);
		out.flush();
		WidgetContextTag widgetContextTag = new WidgetContextTag();
		registerSubtag(widgetContextTag);
		widgetContextTag.setId(node.getDisplayId());
		executeStartSubtag(widgetContextTag);
		display._getWidget().render(getOutputData());
		executeEndTagAndUnregister(widgetContextTag);

		/* Render child nodes */
		if (!node.isCollapsed()) {
			JspUtil.writeStartTag(out, "ul");
			List nodes = node.getNodes();
			for (ListIterator i = nodes.listIterator(); i.hasNext(); ) {
				widgetContextTag = new WidgetContextTag();
				registerSubtag(widgetContextTag);
				widgetContextTag.setId(Integer.toString(i.nextIndex()));
				executeStartSubtag(widgetContextTag);

				renderNode(out, (TreeNodeWidget.ViewModel) i.next());

				executeEndTagAndUnregister(widgetContextTag);
			}
			JspUtil.writeEndTag(out, "ul");
		}

		JspUtil.writeEndTag(out, "li");
	}

}
