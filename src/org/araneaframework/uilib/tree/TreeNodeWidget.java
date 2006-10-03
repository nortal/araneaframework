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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.araneaframework.Environment;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class TreeNodeWidget extends BaseApplicationWidget implements TreeNodeContext {

	private static final long serialVersionUID = 1L;

	public static final String DISPLAY_KEY = "display";

	private boolean collapsed = true;
	private boolean collapsedDecide = false;
	private int nodeCount = 0;
	private Widget display;
	private List initNodes;

	TreeNodeWidget() {
		super();
		this.collapsed = false;
	}

	public TreeNodeWidget(Widget display) {
		super();
		Assert.notNull(display);
		this.display = display;
	}

	public TreeNodeWidget(Widget display, List nodes) {
		this(display, nodes, true);
		collapsedDecide = true;
	}

	public TreeNodeWidget(Widget display, List nodes, boolean collapsed) {
		this(display);
		this.initNodes = nodes;
		this.collapsed = collapsed;
	}

	protected void init() throws Exception {
		addWidget(DISPLAY_KEY, display, getDisplayWidgetEnvironment());
		display = null;

		if (this.initNodes != null) {
			addAllNodes(initNodes);
			initNodes = null;
		}

		if (collapsedDecide) {
			collapsed = getTreeCtx().disposeChildren();
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
		return getTreeCtx().getDataProvider().getChildren(this);
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void expand() {
		Assert.isTrue(isCollapsed(), "Node must be collapsed");
		if (getTreeCtx().disposeChildren()) {
			addAllNodes(loadChildren());
		}
		collapsed = false;
	}

	public void collapse() {
		Assert.isTrue(!isCollapsed(), "Node must be expanded");
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
		for (Iterator i = nodes.iterator(); i.hasNext(); ) {
			addNode((TreeNodeWidget) i.next());
		}
	}
/*
	public void addNode(TreeNodeWidget node, int index) {
		
	}

	public TreeNodeWidget removeNode(int index) {
		
	}
*/
	// returns List<TreeNodeWidget>
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

	public Object getViewModel() throws Exception {
		return new ViewModel();
	}

	public class ViewModel extends BaseUIWidget.ViewModel {

		public Widget getDisplay() {
			return TreeNodeWidget.this.getDisplay();
		}

		public String getDisplayId() {
			return TreeNodeWidget.DISPLAY_KEY;
		}

		public boolean isCollapsed() {
			return TreeNodeWidget.this.isCollapsed();
		}

		public List getNodes() throws Exception {
			List nodes = TreeNodeWidget.this.getNodes();
			List viewModels = new ArrayList(getNodeCount());
			for (Iterator i = nodes.iterator(); i.hasNext(); ) {
				viewModels.add(((TreeNodeWidget) i.next()).getViewModel());
			}
			return viewModels;
		}

	}

}
