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

import java.io.Serializable;

/**
 * Tree context. General interface that can be used to access current tree
 * configuration.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
public interface TreeContext extends Serializable {

  /**
   * Returns tree data provider.
   */
  TreeDataProvider getDataProvider();

  /**
   * Returns if action calls (by AJAX requests) to tree widget are synchronized.
   * Default is <code>true</code>. If set to <code>false</code> (in
   * TreeWidget constructor), then
   * {@link org.araneaframework.http.router.StandardHttpSessionRouterService}
   * does not synchronize requests to this <code>TreeWidget</code> and its
   * children.
   */
  public boolean getSync();

  /**
   * Returns if child nodes are removed and discarded when a node is closed.
   */
  boolean isDisposeChildren();

}
