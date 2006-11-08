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

import org.araneaframework.Environment;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class TreeWidget extends TreeNodeWidget implements TreeContext {

	private static final long serialVersionUID = 1L;

	private TreeDataProvider dataProvider;
	private boolean disposeChildren = true; // remove all children when node is collapsed
	                                        //(implies reloading children when node is reopened)
	//private boolean updateWholeTree = false;
  private boolean sync = false;           // synchronize action calls to widgets

	public TreeWidget(TreeDataProvider dataProvider) {
		super();
		Assert.notNullParam(dataProvider, "dataProvider");
		this.dataProvider = dataProvider;
	}

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
		// Only called from render()
		return null;
	}

	public void expand() {
		// Should never be called!
	}

	public void collapse() {
		// Should never be called!
	}

	public void invertCollapsed() {
		// Should never be called!
	}

}
