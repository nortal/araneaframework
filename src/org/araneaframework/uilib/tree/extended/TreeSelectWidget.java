package org.araneaframework.uilib.tree.extended;

import java.io.Serializable;
import org.araneaframework.uilib.tree.TreeWidget;

/**
 * An extension of Aranea's tree widget, enabled with selection of nodes.
 * {@link TreeNodeHandler#handleEventSelect(Object)} is called upon selection. dataProvider is used to provide
 * {@link TreeNode} objects, {@link TreeNode#isSelectable()} determines if the node will be made selectable.
 * 
 * @see TreeItemsDataProvider , {@link TreeNodeHandler}, {@link TreeSelectNodeDisplayWidget}
 * @author Jevgeni Võssotski
 */
public class TreeSelectWidget extends TreeWidget implements TreeSelectContext, Serializable {

  private final TreeItemsDataProvider dataProvider;
  private final TreeNodeHandler treeNodeHandler;
  private String highlightedElementId;
  private String preselectedNodeKey;
  
  public TreeSelectWidget(TreeItemsDataProvider dataProvider, TreeNodeHandler treeNodeHandler) {
    super(dataProvider);
    this.dataProvider = dataProvider;
    this.treeNodeHandler = treeNodeHandler;
    dataProvider.setTreeSelectContext(this);
  }

  /**
   * @param dataProvider
   * @param treeNodeHandler
   * @param preselectedNodeKey key of initially selected node (nodes containing it should be expanded)
   */
  public TreeSelectWidget(TreeItemsDataProvider dataProvider, TreeNodeHandler treeNodeHandler, String preselectedNodeKey) {
    this(dataProvider, treeNodeHandler);
    this.preselectedNodeKey = preselectedNodeKey;
  }

  public void init() throws Exception {
    setRenderer(new TreeSelectRenderer(dataProvider));
    setUseActions(true);
    super.init();
    
  }

  public String getHighlightedElementId() {
    return highlightedElementId;
  }

  public void setHighlightedElementId(String elementId) {
    highlightedElementId = elementId;
  }

  public TreeNodeHandler getTreeNodeHandler() {
    return treeNodeHandler;
  }

  public void setPreSelectedNodeKey(String nodeKey) {
    this.preselectedNodeKey = nodeKey;
  }

  public String getPreselectedNodeKey() {
    return preselectedNodeKey;
  }

  /**
   * Returns true if the node with given key should be expanded, due to the fact, that it has
   * preselected node among its children.
   */
  public boolean shouldBeExpanded(String nodeKey) {
    if (preselectedNodeKey == null) {
      return false;
    }
    return dataProvider.getTreeItemsData().isChildOf(preselectedNodeKey, nodeKey);
  }
}
