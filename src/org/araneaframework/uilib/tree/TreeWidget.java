/*
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
 */

package org.araneaframework.uilib.tree;

import java.io.Writer;
import java.util.List;
import org.araneaframework.Environment;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.http.HttpOutputData;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.0.7
 */
public class TreeWidget extends TreeNodeWidget implements TreeContext {

  private TreeDataProvider dataProvider;
  private boolean removeChildrenOnCollapse = true;
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
   */
  public TreeWidget() {
  }

  /**
   * Creates a new {@link TreeWidget} instance which will acquire the
   * tree data from supplied {@link TreeDataProvider}.
   * 
   * @param dataProvider
   *          tree data provider.
   */
  public TreeWidget(TreeDataProvider dataProvider) {
    Assert.notNullParam(dataProvider, "dataProvider");
    this.dataProvider = dataProvider;
  }

  @Override
  protected void init() throws Exception {
    List<TreeNodeWidget> children = loadChildren();
    if (children != null)
      addAllNodes(children);
  }

  @Override
  public Environment getEnvironment() {
    return new StandardEnvironment(super.getEnvironment(), TreeContext.class, this);
  }

  public TreeDataProvider getDataProvider() {
    return dataProvider;
  }

  /**
   * Set if actions are used instead of events in submit links. See
   * {@link TreeContext#useActions()}.
   */
  public void setUseActions(boolean useActions) {
    this.useActions = useActions;
  }

  public boolean useActions() {
    return useActions;
  }

  /**
   * Set if AJAX requests to tree widget are synchronized. See
   * {@link TreeContext#useSynchronizedActions()}.
   * 
   * @since 1.1
   */
  public void setUseSynchronizedActions(boolean useSynchronizedActions) {
    this.useSynchronizedActions = useSynchronizedActions;
  }

  public boolean useSynchronizedActions() {
    return useSynchronizedActions;
  }

  /**
   * Set if child nodes are removed and discarded when a node is closed.
   */
  public void setRemoveChildrenOnCollapse(boolean removeChildrenOnCollapse) {
    this.removeChildrenOnCollapse = removeChildrenOnCollapse;
  }

  public boolean isRemoveChildrenOnCollapse() {
    return removeChildrenOnCollapse;
  }

  /**
   * Set tree renderer.
   */
  public void setRenderer(TreeRenderer renderer) {
    Assert.notNullParam(renderer, "renderer");
    this.renderer = renderer;
  }

  public TreeRenderer getRenderer() {
    return renderer;
  }

  @Override
  protected void render(OutputData output) throws Exception {
    super.render(output);
    Writer out = ((HttpOutputData) output).getWriter();
    out.flush();
  }

  // The following methods do nothing, because the root node of the tree has no
  // display widget and therefore is always expanded.

  @Override
  public Widget getDisplayWidget() {
    return null;
  }

  @Override
  public void setCollapsed(boolean collapsed) {
  }

  @Override
  public void toggleCollapsed() {
  }

}
