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
   * Sets collapsed state of tree node to <code>false</code>, thus showing
   * child nodes. This may trigger retrieval of child nodes using
   * {@link TreeDataProvider} if {@link TreeContext#disposeChildren()} is
   * <code>true</code>.
   */
  void expand();

  /**
   * Sets collapsed state of tree node to <code>true</code>, thus hiding
   * child nodes. This may trigger removal of child nodes if
   * {@link TreeContext#disposeChildren()} is <code>true</code>.
   */
  void collapse();

  /**
   * Inverts collapsed state of tree node, collapsing expanded node and vice
   * versa.
   */
  void invertCollapsed();

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
  void renderDisplayPrefixRecursive(Writer out, OutputData output, boolean current) throws Exception;

  /**
   * Returns how many parent nodes this TreeNodeWidget has. TreeWidget (root
   * node) has zero parents, it's immediate children have one parent, etc.
   * 
   * @return number of parents in hierarchy.
   */
  int getParentCount();

  /**
   * Returns index of child node.
   * 
   * @param node
   *          child node whose index is being sought.
   * @return index of child node.
   * @throws AraneaRuntimeException
   *           if node is not found among immediate children.
   */
  int getNodeIndex(TreeNodeWidget node);

}
