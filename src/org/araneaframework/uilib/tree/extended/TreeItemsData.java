package org.araneaframework.uilib.tree.extended;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TreeItemsData implements Serializable {
  protected Map nodes = new HashMap();
  protected Map treeStructure = new LinkedHashMap();
  
  public void setNodes(Map nodes) {
    this.nodes = nodes;
  }
  public Map getNodes() {
    return this.nodes;
  }
  public TreeNode getNode(Object key) {
    return (TreeNode)nodes.get(key);
  }
  public Map getTreeStructure() {
    return treeStructure;
  }
  
  public void addNode(TreeNode treeNode, Object key, Object parentKey) {
    nodes.put(key, treeNode);
    
    //Assign node to treeStructure
    if (parentKey == null)
      treeStructure.put(key, new LinkedHashMap());
    else {
      //Find parent object
      addSubNode(treeStructure, key, parentKey);
    }
  }
  public void removeNode(Object key) {
    removeNodeInternal(treeStructure, key);
    nodes.remove(key);
  }
  
  public void mergeTrees(TreeItemsData tid, Object parentKey) {
    addNodes(tid.getTreeStructure(), tid.getNodes(), parentKey);
  }
  
  public List getChildren(Object key) {
    List result = new ArrayList();
    Set children = null;
    if (key == null)
      children = treeStructure.keySet();
    else
      children = getChildrenInternal(treeStructure, key);
    
    if (children == null || children.isEmpty())
      return result;
    for (Iterator i = children.iterator();i.hasNext();) {
      result.add(nodes.get(i.next()));
    }
    return result;
  }
  
  /**
   * @param nodeKey key of the child node in question.
   * @return parent of the node identified by given key.
   */
  public TreeNode getParent(Object nodeKey) {
    TreeNode node = getNode(nodeKey);
    return getNode(node.getParentKey());
  }
  
  private Set getChildrenInternal(Map treeLevel, Object key) {
    if (treeLevel.keySet().contains(key))
      return ((LinkedHashMap)treeLevel.get(key)).keySet();
    
    Set children = null;
    for (Iterator i = treeLevel.keySet().iterator();i.hasNext();) {
      children = getChildrenInternal((LinkedHashMap)treeLevel.get(i.next()), key);
      if (children != null)
        return children;
    }
    return null;
  }
  
  private void addNodes(Map treeStructure, Map nodes, Object parentKey) {
    for (Iterator i = treeStructure.entrySet().iterator();i.hasNext();) {
      Map.Entry entry = (Map.Entry)i.next();
      TreeNode node = (TreeNode)nodes.get(entry.getKey());
      addNode(node, entry.getKey(), parentKey);
      Map subTree = (Map)entry.getValue();
      if (subTree != null)
        addNodes(subTree, nodes, entry.getKey());
    }
  }
  
  public Long getNodeCount() {
    return new Long(nodes.size());
  }
  private boolean addSubNode(Map treeLevel, Object key, Object parentKey) {
    Map parent = (Map)treeLevel.get(parentKey);
    if (parent != null) {
      parent.put(key, new LinkedHashMap());
      return true;
    }
    else {
      for (Iterator i = treeLevel.values().iterator();i.hasNext();)
        if (addSubNode((Map)i.next(), key, parentKey))
          return true;
    }
    return false;
  }
  private boolean removeNodeInternal(Map treeLevel, Object key) {
    Map node = (Map)treeLevel.get(key);
    if (node != null) {
      removeSubNodes((Map)treeLevel.get(key));
      treeLevel.remove(key);
    }
    else
      for (Iterator i = treeLevel.values().iterator();i.hasNext();)
        if (removeNodeInternal((Map)i.next(), key))
          return true;
    return false;
  }
 
  private void removeSubNodes(Map treeLevel) {
    for (Iterator i = treeLevel.entrySet().iterator();i.hasNext();) {
      Map.Entry entry = (Map.Entry)i.next();
      Map subLevel = (Map)entry.getValue();
      if (subLevel != null)
        removeSubNodes(subLevel);
      nodes.remove(entry.getKey());
      i.remove();
    }
  }
  
  /**
   * Checks whether one node is among children of another node (can be more that 1 level away).
   * @param nodeKey key of node in question (child candidate).
   * @param ancestorKey key of the questioned ancestor node.
   */
  public boolean isChildOf(String nodeKey, String ancestorKey) {
    while (true) {
      TreeNode parentNode = getParent(nodeKey);
      if (parentNode == null) {
        // reached top of tree
        return false;
      }
      if (parentNode.getKey().equals(ancestorKey)) {
        return true;
      }
      nodeKey = parentNode.getKey(); // do the same check for parent of just checked node
    } 
  }
}
