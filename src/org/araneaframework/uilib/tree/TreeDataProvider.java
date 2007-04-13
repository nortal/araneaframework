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
import java.util.List;

/**
 * Tree data provider is used by {@link TreeWidget} to retrieve tree data.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
public interface TreeDataProvider extends Serializable {

  /**
   * Returns a list of child nodes for specified parent node.
   * 
   * @param parent
   *          tree node whose children will be returned. Root node of the tree
   *          ({@link TreeWidget}) has no display widget.
   * @return list of {@link TreeNodeWidget}s. May return <code>null</code>
   *         instead of empty list.
   */
  List getChildren(TreeNodeContext parent);

  /**
   * Returns if the specified tree node has any children.
   * 
   * @param parent
   *          tree node which will be checked for existence of child nodes.
   * @return if the specified tree node has any children.
   */
  boolean hasChildren(TreeNodeContext parent);

}
