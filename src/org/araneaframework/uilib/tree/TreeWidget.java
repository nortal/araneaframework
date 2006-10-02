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
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class TreeWidget extends BaseApplicationWidget implements TreeContext {

	private static final long serialVersionUID = 1L;

	public static final String ROOT_KEY = "root";

	private TreeDataProvider dataProvider;
	private boolean disposeChildren = true;

	public TreeWidget(TreeDataProvider dataProvider) {
		super();
		Assert.notNull(dataProvider);
		this.dataProvider = dataProvider;
	}

	protected void init() throws Exception {
		super.init();
		addWidget(ROOT_KEY, loadRoot());
	}

	protected TreeNodeWidget loadRoot() {
		return getDataProvider().getRoot();
	}

	protected Environment getChildWidgetEnvironment() throws Exception {
		return new StandardEnvironment(getEnvironment(), TreeContext.class, this);
	}

	public TreeDataProvider getDataProvider() {
		return dataProvider;
	}

	public boolean disposeChildren() {
		return disposeChildren;
	}

	public Object getViewModel() throws Exception {
		return new ViewModel();
	}

	public class ViewModel extends BaseUIWidget.ViewModel {

		public TreeNodeWidget getRoot() {
			return (TreeNodeWidget) TreeWidget.this.getChildren().get(TreeWidget.ROOT_KEY);
		}

		public String getRootId() {
			return TreeWidget.ROOT_KEY;
		}

	}

}
