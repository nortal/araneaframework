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

package org.araneaframework.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.araneaframework.Path;
import org.araneaframework.Scope;

/**
 * @since 1.1
 */
public class StandardScope implements Scope {

  private Object id;

  private Scope parent;

  public StandardScope(Object id, Scope parent) {
    this.id = id;
    this.parent = parent;
  }

  public Object getId() {
    return id;
  }

  public Scope getParent() {
    return parent;
  }

  public Path toPath() {
    Scope cur = this;
    List<String> idlist = new ArrayList<String>();
    while (cur != null) {
      if (cur.getId() != null) {
        idlist.add(cur.getId().toString());
      }
      cur = cur.getParent();
    }
    Collections.reverse(idlist);
    return new StandardPath(idlist);
  }

  @Override
  public String toString() {
    return toPath().toString();
  }
}
