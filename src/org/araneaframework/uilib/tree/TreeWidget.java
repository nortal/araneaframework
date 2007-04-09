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

import java.io.Writer;
import org.araneaframework.Environment;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.http.HttpOutputData;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class TreeWidget extends TreeNodeWidget implements TreeContext {

  private static final long serialVersionUID = 1L;

  private TreeDataProvider dataProvider;
  private boolean removeCollapsedChildren = true;
  private boolean useActions = false;
  private boolean useSynchronizedActions = true;
  private TreeRenderer renderer;

  //TODO features:
  // * not-dispose-children in client side
  // * some nodes not collapsable
  // * disable concrete tree node toggling client-side when request has been
  //   submitted - response not yet arrived and processed
  // * make sure that all methods can be called before init

  /**
   * Creates a new {@link TreeWidget} instance.
   * 
   * @param dataProvider
   *          tree data provider. Can be <code>null</code>, then nodes are
   *          not self-openable (plus sign is not shown in front of every node).
   */
  public TreeWidget(TreeDataProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

  /**
   * Creates a new {@link TreeWidget} instance.
   * 
   * @param dataProvider
   *          tree data provider.
   * @param useActions
   *          if actions are used instead of events in submit links. See
   *          {@link TreeContext#useActions()}.
   */
  public TreeWidget(TreeDataProvider dataProvider, boolean useActions) {
    this(dataProvider);
    this.useActions = useActions;
  }

  /**
   * Creates a new {@link TreeWidget} instance.
   * 
   * @param dataProvider
   *          tree data provider.
   * @param useActions
   *          if actions are used instead of events in submit links. See
   *          {@link TreeContext#useActions()}.
   * @param useSynchronizedActions
   *          if AJAX requests to tree widget are synchronized. See
   *          {@link TreeContext#useSynchronizedActions()}.
   */
  public TreeWidget(TreeDataProvider dataProvider, boolean useActions, boolean useSynchronizedActions) {
    this(dataProvider, useActions);
    this.useSynchronizedActions = useSynchronizedActions;
  }

  protected void init() throws Exception {
    addAllNodes(loadChildren());
  }

  public Environment getEnvironment() {
    return new StandardEnvironment(super.getEnvironment(), TreeContext.class, this);
  }

  public TreeDataProvider getDataProvider() {
    return dataProvider;
  }

  public boolean useActions() {
    return useActions;
  }

  public boolean useSynchronizedActions() {
    return useSynchronizedActions;
  }

  /**
   * Set if child nodes are removed and discarded when a node is closed.
   */
  public void setRemoveCollapsedChildren(boolean removeCollapsedChildren) {
    this.removeCollapsedChildren = removeCollapsedChildren;
  }

  public boolean removeCollapsedChildren() {
    return removeCollapsedChildren;
  }

  public void setRenderer(TreeRenderer renderer) {
    this.renderer = renderer;
  }

  public TreeRenderer getRenderer() {
    return renderer;
  }

  public Widget getDisplay() {
    return null;
  }

  protected void render(OutputData output) throws Exception {
    super.render(output);
    Writer out = ((HttpOutputData) output).getWriter();
    out.flush();
  }

  // The following methods do nothing, because the root node of the tree has no
  // display widget and therefore is always expanded.

  public void setCollapsed(boolean collapsed) {
  }

  public void toggleCollapsed() {
  }

}
