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

import org.araneaframework.OutputData;

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

}
