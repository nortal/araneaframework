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

import java.io.IOException;
import java.util.List;
import org.araneaframework.Environment;
import org.araneaframework.OutputData;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.exception.AraneaRuntimeException;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.http.HttpOutputData;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.0.7
 */
public class TreeWidget extends TreeNodeWidget implements TreeContext {

  private TreeDataProvider dataProvider;

  private boolean removeChildrenOnCollapse = true;

  private boolean useActions;

  private boolean useSynchronizedActions = true;

  private TreeRenderer renderer;

  // TODO features:
  // * not-dispose-children in client side
  // * some nodes not collapsable
  // * disable concrete tree node toggling client-side when request has been submitted - response not yet arrived and processed

  /**
   * Creates a new {@link TreeWidget} instance.
   */
  public TreeWidget() {}

  /**
   * Creates a new {@link TreeWidget} instance which will acquire the tree data from supplied {@link TreeDataProvider}.
   * 
   * @param dataProvider tree data provider.
   */
  public TreeWidget(TreeDataProvider dataProvider) {
    Assert.notNullParam(dataProvider, "dataProvider");
    this.dataProvider = dataProvider;
  }

  @Override
  protected void init() throws Exception {
    List<TreeNodeWidget> children = loadChildren();
    if (children != null) {
      addAllNodes(children);
    }
  }

  @Override
  public Environment getEnvironment() {
    return new StandardEnvironment(super.getEnvironment(), TreeContext.class, this);
  }

  public TreeDataProvider getDataProvider() {
    return this.dataProvider;
  }

  /**
   * Set if actions are used instead of events in submit links. See {@link TreeContext#useActions()}.
   */
  public void setUseActions(boolean useActions) {
    this.useActions = useActions;
  }

  public boolean useActions() {
    return this.useActions;
  }

  /**
   * Set if AJAX requests to tree widget are synchronized. See {@link TreeContext#useSynchronizedActions()}.
   * 
   * @since 1.1
   */
  public void setUseSynchronizedActions(boolean useSynchronizedActions) {
    this.useSynchronizedActions = useSynchronizedActions;
  }

  public boolean useSynchronizedActions() {
    return this.useSynchronizedActions;
  }

  /**
   * Set if child nodes are removed and discarded when a node is closed.
   */
  public void setRemoveChildrenOnCollapse(boolean removeChildrenOnCollapse) {
    this.removeChildrenOnCollapse = removeChildrenOnCollapse;
  }

  public boolean isRemoveChildrenOnCollapse() {
    return this.removeChildrenOnCollapse;
  }

  /**
   * Set tree renderer.
   */
  public void setRenderer(TreeRenderer renderer) {
    Assert.notNullParam(renderer, "renderer");
    this.renderer = renderer;
  }

  public TreeRenderer getRenderer() {
    return this.renderer;
  }

  @Override
  protected void render(OutputData output) {
    super.render(output);

    try {
      ((HttpOutputData) output).getWriter().flush();
    } catch (IOException e) {
      ExceptionUtil.uncheckException(e);
    }
  }

  @Override
  public void setCollapsed(boolean collapsed) {
    if (!collapsed) {
      throw new AraneaRuntimeException("Cannot change TreeWidget collapsed property to false because it is always "
          + "expanded due to not having a display widget to expand it later.");
    }
  }

}
