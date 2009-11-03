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

package org.araneaframework.example.main.web.tree;
import org.araneaframework.uilib.util.UilibEnvironmentUtil;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.StandardActionListener;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.tree.TreeDataProvider;
import org.araneaframework.uilib.tree.TreeNodeContext;
import org.araneaframework.uilib.tree.TreeNodeWidget;
import org.araneaframework.uilib.tree.TreeWidget;

/**
 * Tree, that uses unsynchronized actions. Each node has five child nodes and
 * depth of the tree is limited to five nodes. Child nodes are disposed when
 * parent node is collapsed.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class UnsynchronizedTreeWidget extends BaseUIWidget {

  private static final Log LOG = LogFactory.getLog(UnsynchronizedTreeWidget.class);

  private TreeWidget tree;

  @Override
  protected void init() throws Exception {
    setViewSelector("tree/unsynchronizedTree");
    this.tree = new TreeWidget(new UnsynchronizedTreeDataProvider());
    this.tree.setUseActions(true);
    this.tree.setUseSynchronizedActions(false);
    addWidget("tree", this.tree);
  }

  public static class UnsynchronizedTreeDataProvider implements TreeDataProvider{

    public List<TreeNodeWidget> getChildren(TreeNodeContext parent) {
      List<TreeNodeWidget> children = new ArrayList<TreeNodeWidget>();
      for (int i = 0; i < 5; i++) {
        children.add(new TreeNodeWidget(new UnsynchronizedTreeDisplayWidget()));
      }
      return children;
    }

    public boolean hasChildren(TreeNodeContext parent) {
      return parent.getParentCount() < 5;
    }
  }

  public static class UnsynchronizedTreeDisplayWidget extends BaseUIWidget {

    private int counter;

    @Override
    protected void init() throws Exception {
      setViewSelector("tree/unsynchronizedTreeDisplay");
      putViewData("counter", new Integer(counter));
      addActionListener("test", new StandardActionListener() {

        @Override
        public void processAction(String actionId, String actionParam, InputData input, OutputData output) throws Exception {
          LOG.debug("Received action with id='" + actionId + "' and param='" + actionParam + "'");
          putViewData("counter", new Integer(++counter));
//TODO          getTreeNodeCtx().renderNode(output); // Boilerplate code
        }

      });

      addActionListener("sleep", new StandardActionListener() {

        @Override
        public void processAction(String actionId, String actionParam, InputData input, OutputData output) throws Exception {
          LOG.debug("Received action with id='" + actionId + "' and param='" + actionParam + "'");
          Thread.sleep(10000);
//TODO          getTreeNodeCtx().renderNode(output); // Boilerplate code
        }
      });
    }

    protected TreeNodeContext getTreeNodeCtx() {
      return UilibEnvironmentUtil.getTreeNodeContext(getEnvironment());
    }
  }
}
