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

package org.araneaframework.example.main.web.demo.tree;

import org.araneaframework.uilib.util.UilibEnvironmentUtil;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.tree.TreeNodeContext;
import org.araneaframework.uilib.tree.TreeNodeWidget;
import org.araneaframework.uilib.tree.TreeWidget;

/**
 * Tree without a data provider - nodes are added during events.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class ComplexTreeWidget extends TemplateBaseWidget {

  protected TreeWidget tree;

  @Override
  protected void init() throws Exception {
    setViewSelector("tree/complexTree");
    this.tree = new TreeWidget();
    this.tree.setRemoveChildrenOnCollapse(false);
    addWidget("tree", this.tree);
  }

  public void handleEventAddNode() {
    this.tree.addNode(new TreeNodeWidget(new ComplexTreeFirstDisplayWidget()));
  }

  public static class ComplexTreeFirstDisplayWidget extends BaseUIWidget {

    @Override
    protected void init() throws Exception {
      setViewSelector("tree/complexTreeFirstDisplay");
    }

    public void handleEventDelete() {
      getTreeNodeCtx().getParentNode().removeNode(getTreeNodeCtx().getIndex());
    }

    public void handleEventAddChildren() {
      TreeNodeWidget secondNode = new TreeNodeWidget(new ComplexTreeSecondDisplayWidget());
      getTreeNodeCtx().addNode(secondNode);
      getTreeNodeCtx().setCollapsed(false);
      secondNode.setCollapsed(false);
      for (int i = 0; i < 4; i++) {
        secondNode.addNode(new TreeNodeWidget(new ComplexTreeThirdDisplayWidget()));
      }
    }

    protected TreeNodeContext getTreeNodeCtx() {
      return UilibEnvironmentUtil.getTreeNodeContext(getEnvironment());
    }
  }

  public static class ComplexTreeSecondDisplayWidget extends BaseUIWidget {

    @Override
    protected void init() throws Exception {
      setViewSelector("tree/complexTreeSecondDisplay");
    }

    public void handleEventInvertCollapsed() {
      getTreeNodeCtx().toggleCollapsed();
    }

    protected TreeNodeContext getTreeNodeCtx() {
      return UilibEnvironmentUtil.getTreeNodeContext(getEnvironment());
    }

  }

  public static class ComplexTreeThirdDisplayWidget extends BaseUIWidget {

    @Override
    protected void init() throws Exception {
      setViewSelector("tree/complexTreeThirdDisplay");
    }

    public int getLevel() {
      return getTreeNodeCtx().getParentCount();
    }

    protected TreeNodeContext getTreeNodeCtx() {
      return UilibEnvironmentUtil.getTreeNodeContext(getEnvironment());
    }
  }
}
