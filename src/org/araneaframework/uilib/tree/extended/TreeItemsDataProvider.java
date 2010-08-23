package org.araneaframework.uilib.tree.extended;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.araneaframework.uilib.tree.TreeDataProvider;
import org.araneaframework.uilib.tree.TreeNodeContext;
import org.araneaframework.uilib.tree.TreeNodeWidget;
import org.araneaframework.uilib.tree.TreeWidget;

/**
 * Using data returned by {@link #getTreeItemsData()}, builds tree node display widgets
 * for the {@link TreeWidget}.
 * 
 * @see TreeSelectWidget
 * @author Jevgeni Võssotski
 */
public abstract class TreeItemsDataProvider implements TreeDataProvider {
  private TreeItemsData treeItemsData;
  private TreeSelectContext treeSelectContext;
  
  public void setTreeSelectContext(TreeSelectContext treeSelectContext) {
    this.treeSelectContext = treeSelectContext;
  }
  
  public List getChildren(TreeNodeContext parent) {
    if (treeItemsData == null)
      treeItemsData = getTreeItemsData();
    
    String treeNodeKey = getTreeNodeKey(parent); 
    return getChildren(treeNodeKey);
  }
  
  public List getChildren(String key) {
    List children = new ArrayList();
    List childrenNodes = treeItemsData.getChildren(key);
    
    for (Iterator i = childrenNodes.iterator();i.hasNext();) {
      TreeNode treeNode = (TreeNode)i.next();
      TreeSelectNodeDisplayWidget nodeDisplayWidget = new TreeSelectNodeDisplayWidget(treeNode, treeSelectContext);
      children.add(new TreeNodeWidget(nodeDisplayWidget));
    }
    
    return children;
  }

  public boolean hasChildren(TreeNodeContext parent) {
    String treeNodeKey = getTreeNodeKey(parent);
    List childrenNodes = treeItemsData.getChildren(treeNodeKey);
    return childrenNodes != null && !childrenNodes.isEmpty();
  }

  protected String getTreeNodeKey(TreeNodeContext widget) {
    if (widget instanceof TreeWidget) {
      return null;
    }
    
    return ((TreeSelectNodeDisplayWidget)widget.getDisplayWidget()).getKey();
  }

  public boolean isChildOf(String selectedNodeKey, String key) {
    return treeItemsData.isChildOf(selectedNodeKey, key);
  }
  
  /**
   * @return TreeItemsData[ {@link TreeNode} ]
   */
  public abstract TreeItemsData loadTreeItemsData();
  
  public TreeItemsData getTreeItemsData() {
    if (treeItemsData == null) {
      treeItemsData = loadTreeItemsData();
    }
    return treeItemsData;
  }
}
