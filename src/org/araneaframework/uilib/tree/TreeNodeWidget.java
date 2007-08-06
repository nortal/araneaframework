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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardActionListener;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardEventListener;
import org.araneaframework.http.HttpOutputData;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.0.7
 */
public class TreeNodeWidget extends BaseApplicationWidget implements TreeNodeContext {

  private static final long serialVersionUID = 1L;

  /** Display widget id. */
  public static final String DISPLAY_KEY = "display";
  /** Toggle event or action id. */
  public static final String TOGGLE_KEY = "toggle";

  private boolean collapsed = true;
  private boolean collapsedDecide = false;
  private Widget initDisplay;
  private List initNodes;
  private TreeNodeWidget parentNode;
  private int index = -1;
  private int nextChildIndex = 0;
  private List childNodeWrappers;

  private static class ChildNodeWrapper implements Serializable {

    private TreeNodeWidget node;
    private String widgetId;

    public ChildNodeWrapper(TreeNodeWidget node, String widgetId) {
      this.node = node;
      this.widgetId = widgetId;
    }

    public TreeNodeWidget getNode() {
      return node;
    }

    public String getWidgetId() {
      return widgetId;
    }

  }

  /* Used by TreeWidget */
  TreeNodeWidget() {
    super();
    collapsed = false;
  }

  /**
   * Creates a new {@link TreeNodeWidget} instance. This node has no child nodes
   * and will be collapsed (children hidden) by default.
   * 
   * @param display
   *          widget that will be used to display this node.
   */
  public TreeNodeWidget(Widget display) {
    super();
    Assert.notNullParam(display, "display");
    initDisplay = display;
  }

  /**
   * Creates a new {@link TreeNodeWidget} instance and adds the given list of
   * nodes as its children. Node will be expanded (children shown) by default.
   * 
   * @param display
   *          widget that will be used to display this node.
   * @param nodes
   *          list of {@link TreeNodeWidget}s added as children.
   */
  public TreeNodeWidget(Widget display, List nodes) {
    this(display, nodes, true);
    collapsedDecide = true;
  }

  /**
   * Creates a new {@link TreeNodeWidget} instance and adds the given list of
   * nodes as its children.
   * 
   * @param display
   *          widget that will be used to display this node.
   * @param nodes
   *          list of {@link TreeNodeWidget}s added as children.
   * @param collapsed
   *          if tree node will be collapsed (children hidden) by default.
   */
  public TreeNodeWidget(Widget display, List nodes, boolean collapsed) {
    this(display);
    Assert.notNullParam(nodes, "nodes");
    initNodes = nodes;
    this.collapsed = collapsed;
  }

  protected void init() throws Exception {
    Assert.notNull(parentNode, "parentNode must be set");
    Assert.isTrue(index > -1, "index must be set");

    addWidget(DISPLAY_KEY, initDisplay, getDisplayWidgetEnvironment());
    initDisplay = null;

    if (initNodes != null) {
      addAllNodes(initNodes);
      initNodes = null;
    }

    if (collapsedDecide) {
      collapsed = getTreeCtx().isRemoveChildrenOnCollapse();
    }

    if (getTreeCtx().useActions()) {
      addActionListener(TOGGLE_KEY, new ToggleActionListener());
    } else {
      addEventListener(TOGGLE_KEY, new ToggleEventListener());
    }
  }

  private class ToggleEventListener extends StandardEventListener {

    private static final long serialVersionUID = 1L;

    public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
      toggleCollapsed();
    }

  }

  private class ToggleActionListener extends StandardActionListener {

    private static final long serialVersionUID = 1L;

    public void processAction(Object actionId, String actionParam, InputData input, OutputData output) throws Exception {
      toggleCollapsed();
      render(output);
    }

  }

  protected Environment getDisplayWidgetEnvironment() throws Exception {
    return new StandardEnvironment(getEnvironment(), TreeNodeContext.class, this);
  }

  protected TreeContext getTreeCtx() {
    return (TreeContext) getEnvironment().getEntry(TreeContext.class);
  }

  // returns List<TreeNodeWidget>
  protected List loadChildren() {
    if (getTreeCtx().getDataProvider() != null) {
      return getTreeCtx().getDataProvider().getChildren(this);
    }
    return null;
  }

  protected boolean shouldRenderToggleLink() {
    if (getTreeCtx().getDataProvider() != null) {
      if (isCollapsed()) {
        return getTreeCtx().getDataProvider().hasChildren(this);
      }
      return true;
    }
    return false;
  }

  public boolean isCollapsed() {
    return collapsed;
  }

  public void setCollapsed(boolean collapsed) {
    this.collapsed = collapsed;
    if (collapsed) {
      if (getTreeCtx().isRemoveChildrenOnCollapse()) {
        removeAllNodes();
      }
    } else {
      if (!hasNodes()) {
        List children = loadChildren();
        if (children != null)
          addAllNodes(children);
      }
    }
  }

  public void toggleCollapsed() {
    setCollapsed(!isCollapsed());
  }

  public int getNodeCount() {
    if (childNodeWrappers == null)
      return 0;
    return childNodeWrappers.size();
  }

  public int addNode(TreeNodeWidget node) {
    Assert.notNullParam(node, "node");
    if (childNodeWrappers == null)
      childNodeWrappers = new ArrayList();

    int nodeIndex = childNodeWrappers.size();
    String widgetId = Integer.toString(nextChildIndex++);

    childNodeWrappers.add(new ChildNodeWrapper(node, widgetId));
    node.setIndex(nodeIndex);
    node.setParentNode(this);

    addWidget(widgetId, node);
    return nodeIndex;
  }

  public void addNode(int nodeIndex, TreeNodeWidget node) {
    Assert.notNullParam(node, "node");
    Assert.isTrue(nodeIndex >= 0 && nodeIndex < getNodeCount(), "nodeIndex must be >= 0 and less than nodeCount");
    if (childNodeWrappers == null)
      childNodeWrappers = new ArrayList();

    String widgetId = Integer.toString(nextChildIndex++);

    childNodeWrappers.add(nodeIndex, new ChildNodeWrapper(node, widgetId));
    node.setIndex(nodeIndex);
    node.setParentNode(this);

    for (int i = nodeIndex + 1; i < childNodeWrappers.size(); i++) {
      getNodeWrapper(i).getNode().setIndex(i);
    }

    addWidget(widgetId, node);
  }

  public TreeNodeWidget removeNode(int nodeIndex) {
    Assert.isTrue(nodeIndex >= 0 && nodeIndex < getNodeCount(), "index must be >= 0 and less than nodeCount");

    ChildNodeWrapper nodeWrapper = getNodeWrapper(nodeIndex);
    removeWidget(nodeWrapper.getWidgetId());
    childNodeWrappers.remove(nodeIndex);

    for (int i = nodeIndex; i < childNodeWrappers.size(); i++) {
      getNodeWrapper(i).getNode().setIndex(i);
    }

    return nodeWrapper.getNode();
  }

  public void addAllNodes(List nodes) {
    Assert.notNullParam(nodes, "nodes");
    for (Iterator i = nodes.iterator(); i.hasNext(); ) {
      addNode((TreeNodeWidget) i.next());
    }
  }

  public void removeAllNodes() {
    if (childNodeWrappers == null)
      return;

    for (Iterator i = childNodeWrappers.iterator(); i.hasNext(); ) {
      ChildNodeWrapper nodeWrapper = (ChildNodeWrapper) i.next();
      removeWidget(nodeWrapper.getWidgetId());
    }
    childNodeWrappers.clear();
    nextChildIndex = 0;
  }

  public Widget getDisplayWidget() {
    return getWidget(DISPLAY_KEY);
  }

  public TreeNodeContext getNode(int nodeIndex) {
    Assert.isTrue(nodeIndex >= 0 && nodeIndex < getNodeCount(), "nodeIndex out of bounds");
    return getNodeWrapper(nodeIndex).getNode();
  }

  protected ChildNodeWrapper getNodeWrapper(int nodeIndex) {
    return (ChildNodeWrapper) childNodeWrappers.get(nodeIndex);
  }

  // returns List<TreeNodeWidget>
  public List getNodes() {
    List nodes = new ArrayList(getNodeCount());
    if (childNodeWrappers != null) {
      for (Iterator i = childNodeWrappers.iterator(); i.hasNext(); ) {
        ChildNodeWrapper nodeWrapper = (ChildNodeWrapper) i.next();
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
    if (parent != null) {
      return parent.getParentCount() + 1;
    }
    return 0;
  }

  public void setParentNode(TreeNodeWidget parentNode) {
    Assert.notNullParam(parentNode, "parentNode");
    this.parentNode = parentNode;
  }

  public TreeNodeContext getParentNode() {
    return parentNode;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    Assert.isTrue(index >= 0, "index must be >= 0");
    this.index = index;
  }

  public String getFullId() {
    return getScope().toString();
  }

  public void renderNode(OutputData output) throws Exception {  // Called only from display widget's action
    Assert.notNullParam(output, "output");
    render(output);
  }

  //*******************************************************************
  // RENDERING METHODS
  //*******************************************************************  

  protected void render(OutputData output) throws Exception {
    TreeRenderer renderer = getTreeCtx().getRenderer();
    Assert.notNull(renderer, "renderer must be set");
    Writer out = ((HttpOutputData) output).getWriter();

    // Render display widget
    Widget display = getDisplayWidget();
    if (display != null) {  // display is null if this is root node (TreeWidget)
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
        for (Iterator i = childNodeWrappers.iterator(); i.hasNext(); ) {
          ChildNodeWrapper nodeWrapper = (ChildNodeWrapper) i.next();
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
  }

  protected void renderDisplayPrefixRecursive(Writer out, TreeRenderer renderer) throws Exception {
    LinkedList parents = new LinkedList();
    TreeNodeContext parent = getParentNode();
    while (parent != null) {
      parents.addFirst(parent);
      parent = parent.getParentNode();
    }
    for (Iterator i = parents.iterator(); i.hasNext(); ) {
      renderer.renderDisplayPrefix(out, (TreeNodeContext) i.next(), false);
    }
    renderer.renderDisplayPrefix(out, this, true);
  }

}
