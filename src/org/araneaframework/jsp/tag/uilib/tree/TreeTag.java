/*
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
 */

package org.araneaframework.jsp.tag.uilib.tree;

import java.io.Writer;
import org.araneaframework.OutputData;
import org.araneaframework.jsp.UiEvent;
import org.araneaframework.jsp.tag.uilib.BaseWidgetTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.tree.TreeContext;
import org.araneaframework.uilib.tree.TreeNodeContext;
import org.araneaframework.uilib.tree.TreeRenderer;
import org.araneaframework.uilib.tree.TreeWidget;

/**
 * Tree widget tag. Uses {@link StandardTreeRenderer} for rendering the tree in HTML.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.0.7
 * 
 * @jsp.tag
 *  name = "tree"
 *  body-content = "JSP"
 *  description = "Tree widget tag. Provides a renderer with HTML unordered list <code>&lt;ul&gt;</code> and list item <code>&lt;li&gt;</code> tags."
 */
public class TreeTag extends BaseWidgetTag {

  @Override
  public int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);

    OutputData output = getOutputData();

    ((TreeWidget) this.widget).setRenderer(buildTreeRenderer((TreeContext) this.widget));

    try {
      hideGlobalContextEntries(this.pageContext);
      out.flush();
      this.widget._getWidget().render(output);
    } finally {
      restoreGlobalContextEntries(this.pageContext);
    }

    return EVAL_PAGE;
  }

  /**
   * Builds a {@link TreeRenderer} that is used to render current {@link TreeWidget}. Usually overridden.
   */
  protected TreeRenderer buildTreeRenderer(TreeContext treeWidget) {
    return treeWidget.getRenderer() != null ? treeWidget.getRenderer() : new StandardTreeRenderer(treeWidget);
  }

  /**
   * Standard implementation of tree widget renderer that uses HTML unordered list <code>&lt;ul&gt;</code> and list item
   * <code>&lt;li&gt;</code> tags.
   */
  public static class StandardTreeRenderer implements TreeRenderer {

    protected TreeContext tree;

    public StandardTreeRenderer(TreeContext tree) {
      this.tree = tree;
    }

    protected TreeContext getTree() {
      return this.tree;
    }

    public void renderTreeStart(Writer out, TreeNodeContext node) throws Exception {
      JspUtil.writeOpenStartTag(out, "ul");
      JspUtil.writeAttribute(out, "id", node.getFullId());
      JspUtil.writeAttribute(out, "class", "aranea-tree");
      if (!getTree().useSynchronizedActions()) {
        JspUtil.writeAttribute(out, "arn-tree-sync", "false");
      }
      JspUtil.writeCloseStartTag_SS(out);
    }

    public void renderTreeEnd(Writer out, TreeNodeContext node) throws Exception {
      JspUtil.writeEndTag(out, "ul");
    }

    public void renderToggleLink(Writer out, TreeNodeContext node) throws Exception {
      JspUtil.writeOpenStartTag(out, "a");
      JspUtil.writeAttribute(out, "href", "#");

      if (getTree().useActions()) {
        JspUtil.writeAttribute(out, "onclick", "return AraneaTree.toggleNode(this);");
      } else {
        UiEvent event = new UiEvent("toggle", node.getFullId(), null);
        JspUtil.writeEventAttributes(out, event);
        JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");
      }

      JspUtil.writeCloseStartTag_SS(out);
      out.write(node.isCollapsed() ? "+" : "-");
      JspUtil.writeEndTag_SS(out, "a");
    }

    public void renderChildrenStart(Writer out, TreeNodeContext node) throws Exception {
      JspUtil.writeStartTag(out, "ul");
    }

    public void renderChildrenEnd(Writer out, TreeNodeContext node) throws Exception {
      JspUtil.writeEndTag(out, "ul");
    }

    public void renderChildStart(Writer out, TreeNodeContext node, TreeNodeContext childNode) throws Exception {
      JspUtil.writeOpenStartTag(out, "li");
      JspUtil.writeAttribute(out, "id", childNode.getFullId());
      JspUtil.writeAttribute(out, "class", "aranea-tree-node");
      JspUtil.writeCloseStartTag(out);
    }

    public void renderChildEnd(Writer out, TreeNodeContext node, TreeNodeContext childNode) throws Exception {
      JspUtil.writeEndTag(out, "li");
    }

    public void renderDisplayPrefix(Writer out, TreeNodeContext node, boolean current) throws Exception {}

  }

}
