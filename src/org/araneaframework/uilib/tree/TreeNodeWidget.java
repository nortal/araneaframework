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
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.action.StandardActionListener;
import org.araneaframework.core.event.StandardEventListener;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.uilib.util.UilibEnvironmentUtil;

/**
 * This class is a wrapper widget representing one node in a tree. This wrapper requires a display widget for rendering
 * the node, and may take a list of sub-nodes. It also defines events for which tree nodes react to.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.0.7
 */
public class TreeNodeWidget extends BaseApplicationWidget implements TreeNodeContext {

  /**
   * The ID of the display widget.
   */
  public static final String DISPLAY_KEY = "display";

  /**
   * The ID of the toggle event/action.
   */
  public static final String TOGGLE_KEY = "toggle";

  // Whether the node is collapsed.
  private boolean collapsed = true;

  // Whether this.collapsed will be resolved by tree contexts.
  private boolean collapsedDecide;

  // The display widget for the node.
  private Widget initDisplay;

  // The sub-nodes of this node.
  private List<TreeNodeWidget> initNodes;

  // The parent node of this node.
  private TreeNodeWidget parentNode;

  // The index for tree node to define ordering. Must be non-negative. Usually set by the context.
  private int index = -1;

  // The index that will be set to next sub-node when added.
  private int nextChildIndex;

  // The wrappers that contain child nodes and their IDs.
  private List<ChildNodeWrapper> childNodeWrappers;

  /**
   * Used by TreeWidget
   */
  TreeNodeWidget() {
    this.collapsed = false;
  }

  /**
   * Creates a new {@link TreeNodeWidget} instance. This node has no child nodes and will be collapsed (children hidden)
   * by default.
   * 
   * @param display widget that will be used to display this node.
   */
  public TreeNodeWidget(Widget display) {
    Assert.notNullParam(display, "display");
    this.initDisplay = display;
  }

  /**
   * Creates a new {@link TreeNodeWidget} instance and adds the given list of nodes as its children. Node will be
   * expanded (children shown) by default.
   * 
   * @param display widget that will be used to display this node.
   * @param nodes list of {@link TreeNodeWidget}s added as children.
   */
  public TreeNodeWidget(Widget display, List<TreeNodeWidget> nodes) {
    this(display, nodes, true);
    this.collapsedDecide = true;
  }

  /**
   * Creates a new {@link TreeNodeWidget} instance and adds the given list of nodes as its children.
   * 
   * @param display widget that will be used to display this node.
   * @param nodes list of {@link TreeNodeWidget}s added as children.
   * @param collapsed if tree node will be collapsed (children hidden) by default.
   */
  public TreeNodeWidget(Widget display, List<TreeNodeWidget> nodes, boolean collapsed) {
    this(display);
    Assert.notNullParam(nodes, "nodes");
    this.initNodes = nodes;
    this.collapsed = collapsed;
  }

  @Override
  protected void init() throws Exception {
    Assert.notNull(this.parentNode, "parentNode must be set");
    Assert.isTrue(this.index > -1, "index must be set");

    addWidget(TreeNodeWidget.DISPLAY_KEY, this.initDisplay, getDisplayWidgetEnvironment());
    this.initDisplay = null;

    if (this.initNodes != null) {
      addAllNodes(this.initNodes);
      this.initNodes = null;
    }

    if (this.collapsedDecide) {
      this.collapsed = getTreeCtx().isRemoveChildrenOnCollapse();
    }

    if (getTreeCtx().useActions()) {
      addActionListener(TreeNodeWidget.TOGGLE_KEY, new ToggleActionListener());
    } else {
      addEventListener(TreeNodeWidget.TOGGLE_KEY, new ToggleEventListener());
    }
  }

  protected Environment getDisplayWidgetEnvironment() throws Exception {
    return new StandardEnvironment(getEnvironment(), TreeNodeContext.class, this);
  }

  protected TreeContext getTreeCtx() {
    return UilibEnvironmentUtil.getTreeContext(getEnvironment());
  }

  // returns List<TreeNodeWidget>
  protected List<TreeNodeWidget> loadChildren() {
    return getTreeCtx().getDataProvider() != null ? getTreeCtx().getDataProvider().getChildren(this) : null;
  }

  protected boolean shouldRenderToggleLink() {
    if (getTreeCtx().getDataProvider() != null) {
      return isCollapsed() ? getTreeCtx().getDataProvider().hasChildren(this) : true;
    }
    return false;
  }

  public boolean isCollapsed() {
    return this.collapsed;
  }

  public void setCollapsed(boolean collapsed) {
    this.collapsed = collapsed;
    if (collapsed) {
      if (getTreeCtx().isRemoveChildrenOnCollapse()) {
        removeAllNodes();
      }
    } else {
      if (!hasNodes()) {
        List<TreeNodeWidget> children = loadChildren();
        if (children != null) {
          addAllNodes(children);
        }
      }
    }
  }

  public void toggleCollapsed() {
    setCollapsed(!isCollapsed());
  }

  public int getNodeCount() {
    return this.childNodeWrappers == null ? 0 : this.childNodeWrappers.size();
  }

  public int addNode(TreeNodeWidget node) {
    Assert.notNullParam(node, "node");
    if (this.childNodeWrappers == null) {
      this.childNodeWrappers = new ArrayList<ChildNodeWrapper>();
    }

    int nodeIndex = this.childNodeWrappers.size();
    String widgetId = Integer.toString(this.nextChildIndex++);

    this.childNodeWrappers.add(new ChildNodeWrapper(node, widgetId));
    node.setIndex(nodeIndex);
    node.setParentNode(this);

    addWidget(widgetId, node);
    return nodeIndex;
  }

  public void addNode(int nodeIndex, TreeNodeWidget node) {
    Assert.notNullParam(node, "node");
    Assert.isTrue(nodeIndex >= 0 && nodeIndex < getNodeCount(), "nodeIndex must be >= 0 and less than nodeCount");
    if (this.childNodeWrappers == null) {
      this.childNodeWrappers = new ArrayList<ChildNodeWrapper>();
    }

    String widgetId = Integer.toString(this.nextChildIndex++);

    this.childNodeWrappers.add(nodeIndex, new ChildNodeWrapper(node, widgetId));
    node.setIndex(nodeIndex);
    node.setParentNode(this);

    for (int i = nodeIndex + 1; i < this.childNodeWrappers.size(); i++) {
      getNodeWrapper(i).getNode().setIndex(i);
    }

    addWidget(widgetId, node);
  }

  public TreeNodeWidget removeNode(int nodeIndex) {
    Assert.isTrue(nodeIndex >= 0 && nodeIndex < getNodeCount(), "index must be >= 0 and less than nodeCount");

    ChildNodeWrapper nodeWrapper = getNodeWrapper(nodeIndex);
    removeWidget(nodeWrapper.getWidgetId());
    this.childNodeWrappers.remove(nodeIndex);

    for (int i = nodeIndex; i < this.childNodeWrappers.size(); i++) {
      getNodeWrapper(i).getNode().setIndex(i);
    }

    return nodeWrapper.getNode();
  }

  public void addAllNodes(List<TreeNodeWidget> nodes) {
    Assert.notNullParam(nodes, "nodes");
    for (TreeNodeWidget node : nodes) {
      addNode(node);
    }
  }

  public void removeAllNodes() {
    if (this.childNodeWrappers == null) {
      return;
    }

    for (ChildNodeWrapper nodeWrapper : this.childNodeWrappers) {
      removeWidget(nodeWrapper.getWidgetId());
    }

    this.childNodeWrappers.clear();
    this.nextChildIndex = 0;
  }

  public Widget getDisplayWidget() {
    return getWidget(TreeNodeWidget.DISPLAY_KEY);
  }

  public TreeNodeContext getNode(int nodeIndex) {
    Assert.isTrue(nodeIndex >= 0 && nodeIndex < getNodeCount(), "nodeIndex out of bounds");
    return getNodeWrapper(nodeIndex).getNode();
  }

  protected ChildNodeWrapper getNodeWrapper(int nodeIndex) {
    return this.childNodeWrappers.get(nodeIndex);
  }

  // returns List<TreeNodeWidget>
  public List<TreeNodeWidget> getNodes() {
    List<TreeNodeWidget> nodes = new ArrayList<TreeNodeWidget>(getNodeCount());
    if (this.childNodeWrappers != null) {
      for (ChildNodeWrapper nodeWrapper : this.childNodeWrappers) {
        nodes.add(nodeWrapper.getNode());
      }
    }
    return Collections.unmodifiableList(nodes);
  }

  public boolean hasNodes() {
    return getNodeCount() > 0;
  }

  public int getParentCount() {
    TreeNodeContext parent = getParentNode();
    return parent != null ? parent.getParentCount() + 1 : 0;
  }

  public void setParentNode(TreeNodeWidget parentNode) {
    Assert.notNullParam(parentNode, "parentNode");
    this.parentNode = parentNode;
  }

  public TreeNodeContext getParentNode() {
    return this.parentNode;
  }

  public int getIndex() {
    return this.index;
  }

  public void setIndex(int index) {
    Assert.isTrue(index >= 0, "Index must be non-negative!");
    this.index = index;
  }

  public String getFullId() {
    return getScope().toString();
  }

  public void renderNode(OutputData output) { // Called only from display widget's action
    Assert.notNullParam(output, "output");
    render(output);
  }

  // *******************************************************************
  // RENDERING METHODS
  // *******************************************************************

  @Override
  protected void render(OutputData output) {
    TreeRenderer renderer = getTreeCtx().getRenderer();
    Assert.notNull(renderer, "renderer must be set");

    Widget display = getDisplayWidget();

    try {
      Writer out = ((HttpOutputData) output).getWriter();

      // Render display widget
      if (display != null) { // display is null if this is root node (TreeWidget)
        renderDisplayPrefixRecursive(out, renderer);
        if (shouldRenderToggleLink()) {
          renderer.renderToggleLink(out, this);
        }
        out.flush();
        display._getWidget().render(output);
      }

      // Render child nodes
      if (display == null || (!isCollapsed() && hasNodes())) {
        if (display == null) {
          renderer.renderTreeStart(out, this);
        } else {
          renderer.renderChildrenStart(out, this);
        }

        if (!isCollapsed() && hasNodes()) {
          for (ChildNodeWrapper nodeWrapper : this.childNodeWrappers) {
            TreeNodeWidget node = nodeWrapper.getNode();
            renderer.renderChildStart(out, this, node);
            out.flush();
            node.render(output);
            renderer.renderChildEnd(out, this, node);
          }
        }

        if (display == null) {
          renderer.renderTreeEnd(out, this);
        } else {
          renderer.renderChildrenEnd(out, this);
        }
      }
    } catch (IOException e) {
      ExceptionUtil.uncheckException(e);
    }
  }

  protected void renderDisplayPrefixRecursive(Writer out, TreeRenderer renderer) {
    LinkedList<TreeNodeContext> parents = new LinkedList<TreeNodeContext>();
    TreeNodeContext parent = getParentNode();

    while (parent != null) {
      parents.addFirst(parent);
      parent = parent.getParentNode();
    }

    for (TreeNodeContext nodeContext : parents) {
      renderer.renderDisplayPrefix(out, nodeContext, false);
    }

    renderer.renderDisplayPrefix(out, this, true);
  }

  // The child node wrapper containing child node and its ID.
  private static class ChildNodeWrapper implements Serializable {

    private TreeNodeWidget node;

    private String widgetId;

    public ChildNodeWrapper(TreeNodeWidget node, String widgetId) {
      this.node = node;
      this.widgetId = widgetId;
    }

    public TreeNodeWidget getNode() {
      return this.node;
    }

    public String getWidgetId() {
      return this.widgetId;
    }

  }

  // The event listener that performs toggling.
  private class ToggleEventListener extends StandardEventListener {

    @Override
    public void processEvent(String eventId, String eventParam, InputData input) throws Exception {
      toggleCollapsed();
    }
  }

  // The action listener that performs toggling.
  private class ToggleActionListener extends StandardActionListener {

    @Override
    public void processAction(String actionId, String actionParam, InputData input, OutputData output) {
      toggleCollapsed();
      render(output);
    }
  }

}
