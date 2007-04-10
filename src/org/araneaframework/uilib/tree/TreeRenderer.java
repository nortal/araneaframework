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

/**
 * Tree widget renderer.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
public interface TreeRenderer extends Serializable {

  /**
   * Renders HTML in the beginning of the whole tree.
   * 
   * @param node
   *          tree node that is being rendered
   */
  void renderTreeStart(Writer out, TreeNodeContext node) throws Exception;

  /**
   * Renders HTML in the end of the whole tree.
   * 
   * @param node
   *          tree node that is being rendered
   */
  void renderTreeEnd(Writer out, TreeNodeContext node) throws Exception;

  /**
   * Renders toggle link after display prefix and before DisplayWidget. Called
   * only if TreeDataProvider exists.
   * 
   * @param node
   *          tree node that is being rendered
   */
  void renderToggleLink(Writer out, TreeNodeContext node) throws Exception;

  /**
   * Renders HTML after DisplayWidget and before child nodes. Called only if
   * there are child nodes and they are not collapsed.
   * 
   * @param node
   *          tree node that is being rendered
   */
  void renderChildrenStart(Writer out, TreeNodeContext node) throws Exception;

  /**
   * Renders HTML after all child nodes have been rendered. Called only if there
   * are child nodes and they are not collapsed.
   * 
   * @param node
   *          tree node that is being rendered
   */
  void renderChildrenEnd(Writer out, TreeNodeContext node) throws Exception;

  /**
   * Renders HTML immediately before each child node.
   * 
   * @param node
   *          parent node whose children are being rendered
   * @param childNode
   *          child node that is about to be rendered
   */
  void renderChildStart(Writer out, TreeNodeContext node, TreeNodeContext childNode) throws Exception;

  /**
   * Renders HTML immediately after each child node.
   * 
   * @param node
   *          parent node whose children are being rendered
   * @param node
   *          child node that was just rendered
   */
  void renderChildEnd(Writer out, TreeNodeContext node, TreeNodeContext childNode) throws Exception;

  /**
   * Renders HTML before DisplayWidget's toggle link. Called for each
   * TreeNodeWidget, staring from TreeWidget. Usually overridden.
   * 
   * @param node
   *          tree node that is being rendered
   * @param current
   *          if this TreeNodeWidget's DisplayWidget is about to be rendered
   */
  void renderDisplayPrefix(Writer out, TreeNodeContext node, boolean current) throws Exception;

}
