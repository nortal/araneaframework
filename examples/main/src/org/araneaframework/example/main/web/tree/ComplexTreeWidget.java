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

import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.tree.TreeNodeContext;
import org.araneaframework.uilib.tree.TreeNodeWidget;
import org.araneaframework.uilib.tree.TreeWidget;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class ComplexTreeWidget extends BaseUIWidget {

  protected TreeWidget tree;

  protected void init() throws Exception {
    setViewSelector("tree/complexTree");
    tree = new TreeWidget(null);
    tree.setDisposeChildren(false);
    addWidget("tree", tree);
  }

  public void handleEventAddNode() {
    tree.addNode(new TreeNodeWidget(new ComplexTreeFirstDisplayWidget()));
  }

  public static class ComplexTreeFirstDisplayWidget extends BaseUIWidget {

    protected void init() throws Exception {
      setViewSelector("tree/complexTreeFirstDisplay");
    }

    public void handleEventDelete() {
      getTreeNodeCtx().getParentNode().removeNode(getTreeNodeCtx().getIndex());
    }

    public void handleEventAddChildren() {
      TreeNodeWidget secondNode = new TreeNodeWidget(new ComplexTreeSecondDisplayWidget());
      getTreeNodeCtx().addNode(secondNode);
      getTreeNodeCtx().expand();
      secondNode.expand();
      for (int i = 0; i < 4; i++) {
        secondNode.addNode(new TreeNodeWidget(new ComplexTreeThirdDisplayWidget()));
      }
    }

    protected TreeNodeContext getTreeNodeCtx() {
      return (TreeNodeContext) getEnvironment().getEntry(TreeNodeContext.class);
    }

  }

  public static class ComplexTreeSecondDisplayWidget extends BaseUIWidget {

    protected void init() throws Exception {
      setViewSelector("tree/complexTreeSecondDisplay");
    }

    public void handleEventInvertCollapsed() {
      getTreeNodeCtx().invertCollapsed();
    }

    protected TreeNodeContext getTreeNodeCtx() {
      return (TreeNodeContext) getEnvironment().getEntry(TreeNodeContext.class);
    }

  }

  public static class ComplexTreeThirdDisplayWidget extends BaseUIWidget {

    protected void init() throws Exception {
      setViewSelector("tree/complexTreeThirdDisplay");
    }

    public int getLevel() {
      return getTreeNodeCtx().getParentCount() + 1;
    }

    protected TreeNodeContext getTreeNodeCtx() {
      return (TreeNodeContext) getEnvironment().getEntry(TreeNodeContext.class);
    }

  }

}
