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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletContext;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.tree.TreeDataProvider;
import org.araneaframework.uilib.tree.TreeNodeContext;
import org.araneaframework.uilib.tree.TreeNodeWidget;
import org.araneaframework.uilib.tree.TreeWidget;

/**
 * Widget that shows two simple trees. One tree uses events for submit links and
 * the other uses actions (in that case only the current tree node and its
 * children are rendered).
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class SimpleTreeWidget extends TemplateBaseWidget {
  protected void init() throws Exception {
    setViewSelector("tree/simpleTree");

    TreeWidget tree1 = new TreeWidget(new SimpleTreeDataProvider());
    tree1.setRemoveChildrenOnCollapse(false);
    addWidget("tree1", tree1);

    TreeWidget tree2 = new TreeWidget(new SimpleTreeDataProvider());
    tree2.setUseActions(true);
    addWidget("tree2", tree2);
  }

  private class SimpleTreeDataProvider implements TreeDataProvider {
    public List getChildren(TreeNodeContext parent) {
      List children = new ArrayList();

      Iterator i = getResourceIterator(parent);

      if (i == null || !i.hasNext()) {
        return null;
      }

      while (i.hasNext()) {
        children.add(new TreeNodeWidget(new SimpleTreeDisplayWidget(i.next().toString())));
      }

      return children;
    }

    public boolean hasChildren(TreeNodeContext parent) {
      return (getChildren(parent) != null && !getChildren(parent).isEmpty());
    }

    private Iterator getResourceIterator(TreeNodeContext widget) {
      ServletContext servletContext = (ServletContext) SimpleTreeWidget.this.getEnvironment().getEntry(ServletContext.class);
      String path = null;
      if (widget instanceof TreeWidget)
        path = "/";
      else
        path = ((SimpleTreeDisplayWidget)widget.getDisplayWidget()).getPath();
      Set set = servletContext.getResourcePaths(path);
      return set != null ? set.iterator() : null;
    }

  }

  public static class SimpleTreeDisplayWidget extends BaseUIWidget {
    private String path;

    public SimpleTreeDisplayWidget(String name) {
      this.path = name;
    }

    protected void init() throws Exception {
      setViewSelector("tree/simpleTreeDisplay");
    }

    public String getPath() {
      return path;
    }

    public String getName() {
      if (!path.endsWith("/"))
        return path.substring(path.lastIndexOf("/") + 1);

      String s = path.substring(0, path.length() - 1);
      return s.substring(s.lastIndexOf("/"));
    }
  }
}
