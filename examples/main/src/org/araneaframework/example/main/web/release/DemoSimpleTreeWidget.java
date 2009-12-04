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

package org.araneaframework.example.main.web.release;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
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
 * Widget that shows two simple trees. One tree uses events for submit links and the other uses actions (in that case
 * only the current tree node and its children are rendered).
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoSimpleTreeWidget extends TemplateBaseWidget {

  @Override
  protected void init() throws Exception {
    setViewSelector("release/simpleTree/tree");

    TreeWidget tree1 = new TreeWidget(new SimpleTreeDataProvider());
    tree1.setRemoveChildrenOnCollapse(false);
    addWidget("tree1", tree1);

    TreeWidget tree2 = new TreeWidget(new SimpleTreeDataProvider());
    tree2.setUseActions(true);
    addWidget("tree2", tree2);
  }

  private class SimpleTreeDataProvider implements TreeDataProvider {

    public List<TreeNodeWidget> getChildren(TreeNodeContext parent) {
      List<TreeNodeWidget> children = new ArrayList<TreeNodeWidget>();
      Iterator<String> i = getResourceIterator(parent);

      if (i == null || !i.hasNext()) {
        return null;
      }

      while (i.hasNext()) {
        children.add(new TreeNodeWidget(new SimpleTreeDisplayWidget(i.next())));
      }

      return children;
    }

    public boolean hasChildren(TreeNodeContext parent) {
      return getChildren(parent) != null && !getChildren(parent).isEmpty();
    }

    @SuppressWarnings("unchecked")
    private Iterator<String> getResourceIterator(TreeNodeContext widget) {
      ServletContext servletContext = DemoSimpleTreeWidget.this.getEnvironment().getEntry(ServletContext.class);
      String path = widget instanceof TreeWidget ? "/" : ((SimpleTreeDisplayWidget) widget.getDisplayWidget()).getPath();
      Set<String> set = servletContext.getResourcePaths(path);
      return set != null ? set.iterator() : null;
    }
  }

  public static class SimpleTreeDisplayWidget extends BaseUIWidget {

    private String path;

    public SimpleTreeDisplayWidget(String name) {
      this.path = name;
    }

    @Override
    protected void init() throws Exception {
      setViewSelector("release/simpleTree/treeNode");
    }

    public String getPath() {
      return this.path;
    }

    /**
     * Retrieves the name of the tree node from the path as the string following the last forward slash ("/"). If the
     * path ends with the forward slash, it is removed first.
     * 
     * @return The name of the tree node.
     */
    public String getName() {
      String path = StringUtils.chomp(this.path, "/");
      return StringUtils.substringAfterLast(path, "/");
    }
  }
}
