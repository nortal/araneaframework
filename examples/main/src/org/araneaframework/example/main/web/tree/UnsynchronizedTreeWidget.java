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
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.StandardActionListener;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.tree.TreeDataProvider;
import org.araneaframework.uilib.tree.TreeNodeContext;
import org.araneaframework.uilib.tree.TreeNodeWidget;
import org.araneaframework.uilib.tree.TreeWidget;

/**
 * Tree, that uses unsynchronized actions. Each node has five child nodes (tree
 * is infinite). Child nodes are disposed when parent node is collapsed.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class UnsynchronizedTreeWidget extends BaseUIWidget {

  private static final Logger log = Logger.getLogger(UnsynchronizedTreeWidget.class);

  private TreeWidget tree;

  protected void init() throws Exception {
    setViewSelector("tree/unsynchronizedTree");
    tree = new TreeWidget(new UnsynchronizedTreeDataProvider(), false);
    addWidget("tree", tree);
  }

  public static class UnsynchronizedTreeDataProvider implements TreeDataProvider {

    public List getChildren(TreeNodeWidget parent) {
      List children = new ArrayList();
      for (int i = 0; i < 5; i++) {
        children.add(new TreeNodeWidget(new UnsynchronizedTreeDisplayWidget()));
      }
      return children;
    }

  }

  public static class UnsynchronizedTreeDisplayWidget extends BaseUIWidget {

    private int counter;

    protected void init() throws Exception {
      setViewSelector("tree/unsynchronizedTreeDisplay");
      putViewData("counter", new Integer(counter));

      addActionListener("test", new StandardActionListener() {

        public void processAction(Object actionId, String actionParam, InputData input, OutputData output) throws Exception {
          log.debug("Received action with id='" + actionId + "' and param='" + actionParam + "'");
          putViewData("counter", new Integer(++counter));
          getTreeNodeCtx().renderNode(output);                                                     // Boilerplate code
        }

      });

      addActionListener("sleep", new StandardActionListener() {

        public void processAction(Object actionId, String actionParam, InputData input, OutputData output) throws Exception {
          log.debug("Received action with id='" + actionId + "' and param='" + actionParam + "'");
          Thread.sleep(10000);
          getTreeNodeCtx().renderNode(output);                                                     // Boilerplate code
        }

      });
    }

    protected TreeNodeContext getTreeNodeCtx() {
      return (TreeNodeContext) getEnvironment().getEntry(TreeNodeContext.class);
    }

  }

}
