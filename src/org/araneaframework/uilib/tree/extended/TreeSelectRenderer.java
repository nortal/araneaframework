package org.araneaframework.uilib.tree.extended;

import java.io.Writer;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.uilib.tree.TreeNodeContext;
import org.araneaframework.uilib.tree.TreeRenderer;

/**
 * A rendered for a kind of tree that has selectable nodes.
 * 
 * @see TreeSelectWidget
 * @author Jevgeni Võssotski
 * 
 */
public class TreeSelectRenderer implements TreeRenderer {

  private final TreeItemsDataProvider treeDataProvider;

  public TreeSelectRenderer(TreeItemsDataProvider treeDataProvider) {
    this.treeDataProvider = treeDataProvider;
  }

  public void renderTreeStart(Writer out, TreeNodeContext node) throws Exception {
    JspUtil.writeOpenStartTag(out, "div");
    JspUtil.writeAttribute(out, "id", node.getFullId());
    JspUtil.writeAttribute(out, "class", "aranea-tree");
    JspUtil.writeCloseStartTag(out); // div >

    JspUtil.writeStartTag(out, "table");
  }

  public void renderTreeEnd(Writer out, TreeNodeContext node) throws Exception {
    JspUtil.writeEndTag_SS(out, "table");
    JspUtil.writeEndTag_SS(out, "div");
  }

  public void renderChildStart(Writer out, TreeNodeContext parentNode, TreeNodeContext node) throws Exception {
    
    // TBODY is used here, as nothing else was found acceptable, i.e.
    // it must be element, that will reside within table as a child (in terms of DOM)
    // and can contain upcoming TR element. Body of this element (ID will be used to look it up)
    // will be redrawn on Toggle action.
    JspUtil.writeOpenStartTag(out, "tbody");
    
    JspUtil.writeAttribute(out, "id", node.getFullId());
    JspUtil.writeAttribute(out, "class", "aranea-tree-node");
    JspUtil.writeCloseStartTag(out); // table >


    if (!treeDataProvider.hasChildren(node)) {
      // row is started here or within renderToggleLink, it can't be started in both places, as
      // this block is executed only for leafs and the renderToggleLink is called for every node with children.
      JspUtil.writeStartTag(out, "tr"); 
      JspUtil.writeStartTag(out, "td");
      {
        JspUtil.writeOpenStartTag(out, "img");
        JspUtil.writeAttribute(out, "src", "gfx/tree_leaf.gif");
        JspUtil.writeAttribute(out, "align", "right");
        JspUtil.writeCloseStartEndTag_SS(out);
      }
      JspUtil.writeEndTag_SS(out, "td");
    }

    // child widget itself should be rendered inside TD!
  }

  public void renderChildEnd(Writer out, TreeNodeContext parentNode, TreeNodeContext node) throws Exception {
//     child widget itself should be rendered inside TD! *and* close </TR>!
    JspUtil.writeEndTag_SS(out, "tbody");
  }

  public void renderChildrenStart(Writer out, TreeNodeContext node) throws Exception {
    JspUtil.writeStartTag_SS(out, "tr");
    JspUtil.writeStartEndTag(out, "td"); // this empty cell matches parent's "toggle" or "leaf image" node.

    // this is for nesting, so that every next level is nicely indented
    JspUtil.writeStartTag(out, "td");
    JspUtil.writeStartTag(out, "table"); 
  }

  public void renderChildrenEnd(Writer out, TreeNodeContext node) throws Exception {
    JspUtil.writeEndTag_SS(out, "table");
    JspUtil.writeEndTag_SS(out, "td");
    JspUtil.writeEndTag_SS(out, "tr");
    
  }

  public void renderDisplayPrefix(Writer out, TreeNodeContext node, boolean current) throws Exception {
    // don't use any prefix
  }

  public void renderToggleLink(Writer out, TreeNodeContext node) throws Exception {
    JspUtil.writeStartTag(out, "tr");
    JspUtil.writeStartTag(out, "td");

    // link starting tag
    JspUtil.writeOpenStartTag(out, "a");
    JspUtil.writeAttribute(out, "href", "#");

    
    
    JspUtil.writeAttribute(out, "onclick", //" document.getElementById('" + node.getFullId() + "').style='{ background-color: red; }'; " +
          "return AraneaTree.toggleNode(this); ");
    

    JspUtil.writeCloseStartTag_SS(out); // link starting tag closed

    // image inside link
    JspUtil.writeOpenStartTag(out, "img");

    if (node.isCollapsed()) {
      JspUtil.writeAttribute(out, "src", "gfx/tree_closed.gif");
    } else {
      JspUtil.writeAttribute(out, "src", "gfx/tree_open.gif");
    }

    JspUtil.writeAttribute(out, "border", "0");
    JspUtil.writeAttribute(out, "align", "right");
    JspUtil.writeAttribute(out, "name", "j_img_" + node.getFullId());
    JspUtil.writeCloseStartEndTag_SS(out); // image tag closed

    JspUtil.writeEndTag_SS(out, "a");

    JspUtil.writeEndTag_SS(out, "td");
  }
}
