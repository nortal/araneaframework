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
import org.araneaframework.jsp.util.JspUtil;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class TreeWidget extends TreeNodeWidget implements TreeContext {

	private static final long serialVersionUID = 1L;

	private TreeDataProvider dataProvider;
	private boolean disposeChildren = true;
  private boolean sync = true;

  //TODO features:
  // * disable use of action calls (AJAX)
  // * not show collapse/expand by TreeNodeWidgets
  // * not-dispose-children in client side
  // * some nodes not collapsable
  // * disable concrete tree node toggling client-side when request has been
  //   submitted - response not yet arrived and processed

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
   * @param sync
   *          if AJAX requests to tree widget are synchronized. See
   *          {@link TreeContext#getSync()}.
   */
  public TreeWidget(TreeDataProvider dataProvider, boolean sync) {
    this(dataProvider);
    this.sync = sync;
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

  public boolean getSync() {
    return sync;
  }

	public boolean disposeChildren() {
		return disposeChildren;
	}

	public Widget getDisplay() {
		return null;
	}

  public int getParentCount() {
    return 0;
  }

  protected void renderChildrenStart(Writer out, OutputData output) throws Exception {
    JspUtil.writeOpenStartTag(out, "ul");
    JspUtil.writeAttribute(out, "id", output.getScope());
    JspUtil.writeAttribute(out, "class", "aranea-tree");
    JspUtil.writeAttribute(out, "arn-tree-nosync", Boolean.toString(!getTreeCtx().getSync()));
    JspUtil.writeCloseStartTag_SS(out);
  }

  public void renderGfx(Writer out, OutputData output, boolean current) throws Exception {
    renderMyGfx(out, output, 0, current);
  }

  protected void render(OutputData output) throws Exception {
    super.render(output);
    Writer out = ((HttpOutputData) output).getWriter();
    out.flush();
  }

  // The following methods do nothing, because the root node of the tree has no
  // display widget and therefore is always expanded.

	public void expand() {
	}

	public void collapse() {
	}

	public void invertCollapsed() {
	}

}
