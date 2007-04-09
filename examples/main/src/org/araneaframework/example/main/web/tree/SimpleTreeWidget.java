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

package org.araneaframework.example.main.web.tree;

import java.util.ArrayList;
import java.util.List;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.tree.TreeDataProvider;
import org.araneaframework.uilib.tree.TreeNodeWidget;
import org.araneaframework.uilib.tree.TreeWidget;

/**
 * Widget that shows two simple trees. One tree uses events for submit links and
 * the other uses actions (in that case only the current tree node and its
 * children are rendered). Each node has five child nodes (tree is infinite).
 * Child nodes are disposed when parent node is collapsed.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class SimpleTreeWidget extends BaseUIWidget {

  protected void init() throws Exception {
    setViewSelector("tree/simpleTree");
    addWidget("tree1", new TreeWidget(new SimpleTreeDataProvider()));
    addWidget("tree2", new TreeWidget(new SimpleTreeDataProvider(), true));
  }

  public static class SimpleTreeDataProvider implements TreeDataProvider {

    public List getChildren(TreeNodeWidget parent) {
      List children = new ArrayList();
      for (int i = 0; i < 5; i++) {
        children.add(new TreeNodeWidget(new SimpleTreeDisplayWidget()));
      }
      return children;
    }

    public boolean hasChildren(TreeNodeWidget parent) {
      return true;
    }

  }

  public static class SimpleTreeDisplayWidget extends BaseUIWidget {

    protected void init() throws Exception {
      setViewSelector("tree/simpleTreeDisplay");
    }

  }

}
