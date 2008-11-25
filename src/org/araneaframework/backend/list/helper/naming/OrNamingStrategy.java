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

package org.araneaframework.backend.list.helper.naming;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Composite {@link NamingStrategy} that returns the result of the first child
 * that gives a not-null value.
 * 
 * @author Rein Raudjärv
 * @since 1.1
 */
public class OrNamingStrategy implements NamingStrategy {

  private final List children = new LinkedList();

  public void add(NamingStrategy namingStrategy) {
    children.add(namingStrategy);
  }

  public void addFirst(NamingStrategy namingStrategy) {
    children.add(0, namingStrategy);
  }

  public String fieldToColumnAlias(String fieldName) {
    for (Iterator it = children.iterator(); it.hasNext();) {
      NamingStrategy ns = (NamingStrategy) it.next();
      String result = ns.fieldToColumnAlias(fieldName);
      if (result != null) {
        return result;
      }
    }
    return fieldName; // do not change
  }

  public String fieldToColumnName(String fieldName) {
    for (Iterator it = children.iterator(); it.hasNext();) {
      NamingStrategy ns = (NamingStrategy) it.next();
      String result = ns.fieldToColumnName(fieldName);
      if (result != null) {
        return result;
      }
    }
    return fieldName; // do not change
  }
}
