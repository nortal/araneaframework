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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardActionListener;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.jsp.util.JspUtil;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class TreeNodeWidget extends BaseApplicationWidget implements TreeNodeContext {

	private static final long serialVersionUID = 1L;

	public static final Logger log = Logger.getLogger(TreeNodeWidget.class);

	/** Display widget id. */
  public static final String DISPLAY_KEY = "display";
  /** Toggle action id. */
  public static final String TOGGLE_ACTION = "toggle";
  /** Expand action id. */
  public static final String EXPAND_ACTION = "expand";
  /** Collapse action id. */
  public static final String COLLAPSE_ACTION = "collapse";

	private boolean collapsed = true;
	private boolean collapsedDecide = false;
	private int nodeCount = 0;
	private Widget initDisplay;
	private List initNodes;

	/* Used by TreeWidget */
  TreeNodeWidget() {
		super();
		this.collapsed = false;
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
		this.initDisplay = display;
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
		this.initNodes = nodes;
		this.collapsed = collapsed;
	}

	protected void init() throws Exception {
		addWidget(DISPLAY_KEY, initDisplay, getDisplayWidgetEnvironment());
		initDisplay = null;

		if (this.initNodes != null) {
			addAllNodes(initNodes);
			initNodes = null;
		}

		if (collapsedDecide) {
			collapsed = getTreeCtx().disposeChildren();
		}

    addActionListener(TOGGLE_ACTION, new InvertCollapsedListener());
		addActionListener(EXPAND_ACTION, new ExpandActionListener());
    addActionListener(COLLAPSE_ACTION, new CollapseActionListener());
	}

  private class InvertCollapsedListener extends StandardActionListener {
    public void processAction(Object actionId, String actionParam, InputData input, OutputData output) throws Exception {
      log.debug("Received action with id='" + actionId + "' and param='" + actionParam + "'");
      //update(input);
      invertCollapsed();
      //process();
      render(output);
    }
  }

	private class ExpandActionListener extends StandardActionListener {
    public void processAction(Object actionId, String actionParam, InputData input, OutputData output) throws Exception {
      log.debug("Received action with id='" + actionId + "' and param='" + actionParam + "'");
			//update(input);
			expand();
			//process();
			render(output);
		}
	}

  private class CollapseActionListener extends StandardActionListener {
    public void processAction(Object actionId, String actionParam, InputData input, OutputData output) throws Exception {
      log.debug("Received action with id='" + actionId + "' and param='" + actionParam + "'");
      //update(input);
      collapse();
      //process();
      render(output);
    }
  }

	protected Environment getDisplayWidgetEnvironment() {
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

	public boolean isCollapsed() {
		return collapsed;
	}

	public void expand() {
		if (getTreeCtx().disposeChildren()) {
			addAllNodes(loadChildren());
		}
		collapsed = false;
	}

	public void collapse() {
		if (getTreeCtx().disposeChildren()) {
			removeAllNodes();
		}
		collapsed = true;
	}

	public void invertCollapsed() {
		if (isCollapsed()) {
			expand();
		} else {
			collapse();
		}
	}

	public int getNodeCount() {
		return nodeCount;
	}

	public int addNode(TreeNodeWidget node) {
		addWidget(Integer.toString(nodeCount), node);
		return nodeCount++;
	}

	public void addAllNodes(List nodes) {
    if (nodes == null)
      return;

		for (Iterator i = nodes.iterator(); i.hasNext(); ) {
			addNode((TreeNodeWidget) i.next());
		}
	}

/*
  //TODO implementation 
	public void addNode(TreeNodeWidget node, int index) {
	}

  //TODO implementation 
	public TreeNodeWidget removeNode(int index) {
	}
*/

	public void removeAllNodes() {
		for (int i = 0; i < nodeCount; i++) {
			removeWidget(Integer.toString(i));
		}
		nodeCount = 0;
	}

	public Widget getDisplay() {
		return (Widget) getChildren().get(DISPLAY_KEY);
	}

	public TreeNodeWidget getNode(int index) {
		Assert.isTrue(index >= 0 && index < nodeCount, "Index out of bounds");
		return (TreeNodeWidget) getChildren().get(Integer.toString(index));
	}

	public List getNodes() {
		Map children = getChildren();
		List nodes = new ArrayList(getNodeCount());
		for (int i = 0; i < getNodeCount(); i++) {
			nodes.add(children.get(Integer.toString(i)));
		}
		return nodes;
	}

	public boolean hasNodes() {
		return getNodeCount() > 0;
	}

	protected void render(OutputData output) throws Exception {
		Writer out = ((HttpOutputData) output).getWriter();

		// Render display widget
		Widget display = getDisplay();
		if (display != null) {	// display is null if this is root node (TreeWidget)
      if (getTreeCtx().getDataProvider() != null) {
  		  JspUtil.writeOpenStartTag(out, "a");
  		  JspUtil.writeAttribute(out, "href", "#");
  		  JspUtil.writeAttribute(out, "onclick", "return AraneaTree.toggleNode(this);");
  		  JspUtil.writeCloseStartTag_SS(out);
  		  out.write(isCollapsed() ? "+" : "-");
  		  JspUtil.writeEndTag_SS(out, "a");
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
      if (display != null) {
        JspUtil.writeStartTag(out, "ul");
      } else {
        JspUtil.writeOpenStartTag(out, "ul");
        JspUtil.writeAttribute(out, "id", output.getScope());
        JspUtil.writeAttribute(out, "class", "aranea-tree");
        JspUtil.writeAttribute(out, "arn-tree-nosync", Boolean.toString(!getTreeCtx().getSync()));
        JspUtil.writeCloseStartTag_SS(out);
      }
      if (!isCollapsed() && hasNodes()) {
  			List nodes = getNodes();
  			for (ListIterator i = nodes.listIterator(); i.hasNext(); ) {
  				try {
  					output.pushScope(Integer.toString(i.nextIndex()));
  					JspUtil.writeOpenStartTag(out, "li");
  					JspUtil.writeAttribute(out, "id", output.getScope());
            JspUtil.writeAttribute(out, "class", "aranea-tree-node");
  					JspUtil.writeCloseStartTag(out);
            out.flush();
  					((TreeNodeWidget) i.next()).render(output);
  				} finally {
  					output.popScope();
  				}
  				JspUtil.writeEndTag(out, "li");
  			}
      }
			JspUtil.writeEndTag(out, "ul");
		}
	}

	public void renderNode(OutputData output) throws Exception {	// Called only from display widget
		output.popScope();
		try {
			render(output);
		} finally {
			output.pushScope(DISPLAY_KEY);
		}
	}

}
