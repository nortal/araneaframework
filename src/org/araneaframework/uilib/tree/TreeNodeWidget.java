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
import org.araneaframework.jsp.UiEvent;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;

/**
 * @author Alar Kvell (alar@araneaframework.org)
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
    Assert.notNull(display);
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
    initNodes = nodes;
    this.collapsed = collapsed;
  }

  protected void init() throws Exception {
    Assert.notNull(parentNode, "parentNode must be set");
    Assert.isTrue(index > -1, "index must be set");

    addWidget(DISPLAY_KEY, initDisplay);
    initDisplay = null;

    if (initNodes != null) {
      addAllNodes(initNodes);
      initNodes = null;
    }

    if (collapsedDecide) {
      collapsed = getTreeCtx().isDisposeChildren();
    }

    if (getTreeCtx().isActions()) {
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

  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(getEnvironment(), TreeNodeContext.class, this);
  }

  protected TreeContext getTreeCtx() {
    return (TreeContext) getEnvironment().getEntry(TreeContext.class);
  }

  protected TreeNodeContext getParentNodeCtx() {
    return (TreeNodeContext) getEnvironment().getEntry(TreeNodeContext.class);
  }

  // returns List<TreeNodeWidget>
  protected List loadChildren() {
    if (getTreeCtx().getDataProvider() != null) {
      return getTreeCtx().getDataProvider().getChildren(this);
    }
    return null;
  }

  public boolean isCollapsed() {
    return collapsed;
  }

  public void setCollapsed(boolean collapsed) {
    this.collapsed = collapsed;
    if (getTreeCtx().isDisposeChildren()) {
      if (collapsed) {
        removeAllNodes();
      } else {
        addAllNodes(loadChildren());
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
    if (nodes == null)
      return;

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

  public Widget getDisplay() {
    return getWidget(DISPLAY_KEY);
  }

  public TreeNodeWidget getNode(int nodeIndex) {
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
    return getParentNodeCtx().getParentCount() + 1;
  }

  public void setParentNode(TreeNodeWidget parentNode) {
    this.parentNode = parentNode;
  }

  public TreeNodeWidget getParentNode() {
    return parentNode;
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public void renderNode(OutputData output) throws Exception {  // Called only from display widget's action
    output.popScope();
    try {
      render(output);
    } finally {
      output.pushScope(DISPLAY_KEY);
    }
  }

  //*******************************************************************
  // RENDERING METHODS
  //*******************************************************************  

  protected void render(OutputData output) throws Exception {
    Writer out = ((HttpOutputData) output).getWriter();

    // Render display widget
    Widget display = getDisplay();
    if (display != null) {  // display is null if this is root node (TreeWidget)
      renderDisplayPrefixRecursive(out, output, output.getScope().toString(), true);
      if (getTreeCtx().getDataProvider() != null && getTreeCtx().getDataProvider().hasChildren(this)) {
        renderToggleLink(out, output);
      }
      try {
        output.pushScope(TreeNodeWidget.DISPLAY_KEY);
        out.flush();
        display._getWidget().render(output);
      } finally {
        output.popScope();
      }
    }

    // Render child nodes
    if (display == null || (!isCollapsed() && hasNodes())) {
      renderChildrenStart(out, output);
      if (!isCollapsed() && hasNodes()) {
        for (Iterator i = childNodeWrappers.iterator(); i.hasNext(); ) {
          ChildNodeWrapper nodeWrapper = (ChildNodeWrapper) i.next();
          try {
            output.pushScope(nodeWrapper.getWidgetId());
            TreeNodeWidget node = nodeWrapper.getNode();
            renderChildStart(out, output, node);
            out.flush();
            node.render(output);
            renderChildEnd(out, output, node);
          } finally {
            output.popScope();
          }
        }
      }
      renderChildrenEnd(out, output);
    }
  }

  /**
   * Renders toggle link after {@link #renderDisplayPrefix} and before
   * DisplayWidget. Called only if TreeDataProvider exists.
   */
  protected void renderToggleLink(Writer out, OutputData output) throws Exception {
    JspUtil.writeOpenStartTag(out, "a");
    JspUtil.writeAttribute(out, "href", "#");
    if (getTreeCtx().isActions()) {
      JspUtil.writeAttribute(out, "onclick", "return AraneaTree.toggleNode(this);");
    } else {
      UiEvent event = new UiEvent("toggle", output.getScope().toString(), null);
      JspUtil.writeEventAttributes(out, event);
      JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");
    }
    JspUtil.writeCloseStartTag_SS(out);
    out.write(isCollapsed() ? "+" : "-");
    JspUtil.writeEndTag_SS(out, "a");
  }

  /**
   * Renders HTML after DisplayWidget and before child nodes. Called only if
   * there are child nodes and they are not collapsed.
   */
  protected void renderChildrenStart(Writer out, OutputData output) throws Exception {
    JspUtil.writeStartTag(out, "ul");
  }

  /**
   * Renders HTML after all child nodes have been rendered. Called only if
   * there are child nodes and they are not collapsed.
   */
  protected void renderChildrenEnd(Writer out, OutputData output) throws Exception {
    JspUtil.writeEndTag(out, "ul");
  }

  /**
   * Renders HTML immediately before each child node.
   * @param node Child node that is about to be rendered
   */
  protected void renderChildStart(Writer out, OutputData output, TreeNodeWidget node) throws Exception {
    JspUtil.writeOpenStartTag(out, "li");
    JspUtil.writeAttribute(out, "id", output.getScope());
    JspUtil.writeAttribute(out, "class", "aranea-tree-node");
    JspUtil.writeCloseStartTag(out);
  }

  /**
   * Renders HTML immediately after each child node.
   * 
   * @param node
   *          child node that was just rendered
   */
  protected void renderChildEnd(Writer out, OutputData output, TreeNodeWidget node) throws Exception {
    JspUtil.writeEndTag(out, "li");
  }

  public void renderDisplayPrefixRecursive(Writer out, OutputData output, String path, boolean current) throws Exception {
    int index = Integer.parseInt(path.substring(path.lastIndexOf('.') + 1));
    String parentPath = path.substring(0, path.lastIndexOf('.'));

    TreeNodeContext parent = getParentNodeCtx();
    parent.renderDisplayPrefixRecursive(out, output, parentPath, false);

    renderDisplayPrefix(out, output, index, current);
  }

  /**
   * Renders HTML before DisplayWidget's toggle link. Called for each
   * TreeNodeWidget, staring from TreeWidget. Usually overridden.
   * 
   * @param index
   *          this TreeNodeWidget's index as parent's child
   * @param current
   *          if this TreeNodeWidget's DisplayWidget is about to be rendered
   */
  protected void renderDisplayPrefix(Writer out, OutputData output, int index, boolean current) throws Exception {
  }

}
