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

package org.araneaframework.uilib.tree;

import java.io.Serializable;
import java.io.Writer;
import java.util.List;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;

/**
 * Tree node context. General interface that can be used to access current tree
 * node configuration.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
public interface TreeNodeContext extends Serializable {

  /**
   * Returns if tree node is collapsed (children hidden).
   */
  boolean isCollapsed();

  /**
   * Sets collapsed state of tree node. If <code>true</code>, child nodes are
   * hidden, otherwise child nodes are shown. This may trigger removal or
   * retrieval of child nodes using {@link TreeDataProvider} if
   * {@link TreeContext#removeCollapsedChildren()} is <code>true</code>.
   */
  void setCollapsed(boolean collapsed);

  /**
   * Inverts collapsed state of tree node, collapsing expanded node and vice
   * versa.
   */
  void toggleCollapsed();

  /**
   * Renders tree node and all of its children to specified {@link OutputData}.
   * Could be called from action listener of tree node display widget.
   */
  void renderNode(OutputData data) throws Exception;

  /**
   * Returns the number of child nodes this tree node has. The display widget is
   * not counted as a child node.
   */
  int getNodeCount();

  /**
   * Adds the given node as the last child node of this tree node.
   * 
   * @param node
   *          child node to be added.
   * @return index of the added child node.
   */
  int addNode(TreeNodeWidget node);

  /**
   * Adds the given node to the specified position. Moves all subsequent nodes
   * one position further.
   * 
   * @param index
   *          Position of the added node
   * @param node
   *          Node to add
   */
  void addNode(int index, TreeNodeWidget node);

  /**
   * Removes child node at the specified position.
   * 
   * @param index
   *          Position of the removed node
   * @return Node that was removed
   */
  TreeNodeWidget removeNode(int index);

  /**
   * Appends all given nodes to this tree node.
   * 
   * @param nodes
   *          list of {@link TreeNodeWidget}s to be added.
   */
  void addAllNodes(List nodes);

  /**
   * Removes all child nodes of this tree node.
   */
  void removeAllNodes();

  /**
   * Returns the display widget of this tree node. Root node of the tree ({@link TreeWidget})
   * has no display widget ({@link #getDisplay()} is <code>null</code>).
   */
  Widget getDisplay();

  /**
   * Returns a child node of this tree node.
   * 
   * @param index
   *          index of the returned child node.
   */
  TreeNodeWidget getNode(int index);

  /**
   * Returns all child nodes of this tree node.
   * 
   * @return list of {@link TreeNodeWidget}s.
   */
  List getNodes();

  /**
   * Returns if this tree node has any child nodes.
   */
  boolean hasNodes();

  /**
   * Renders HTML before DisplayWidget's toggle link. Calls
   * {@link TreeNodeWidget#renderDisplayPrefixRecursive} on each TreeNodeWidget,
   * staring from TreeWidget.
   * 
   * @param current
   *          if this TreeNodeWidget's DisplayWidget is about to be rendered
   */
  void renderDisplayPrefixRecursive(Writer out, boolean current) throws Exception;

  /**
   * Returns how many parent nodes this TreeNodeWidget has. TreeWidget (root
   * node) has zero parents, it's immediate children have one parent, etc.
   * 
   * @return number of parents in hierarchy.
   */
  int getParentCount();

  /**
   * Returns parent node of this tree node or null if called on the root node.
   */
  TreeNodeWidget getParentNode();

  /**
   * Returns the index this node is under its parent.
   */
  int getIndex();

}
