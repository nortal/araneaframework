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

package org.araneaframework.backend.list.helper.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Composite implementation of {@link Fields} that combine the results of its
 * children.
 * <p>
 * The concatenation of all fields are returned with no duplications.
 * 
 * @see Fields
 * @author Rein Raudjärv
 * @since 1.1
 */
public class ConcatFields implements Fields {

  private List<Fields> children = new ArrayList<Fields>();

  /**
   * Adds an instance of {@link Fields} which results are used.
   */
  public void add(Fields fields) {
    children.add(fields);
  }

  public Collection<String> getNames() {
    // Concatenate all fields without duplicating any name
    Collection<String> result = new LinkedHashSet<String>();
    for (Fields child : children) {
      result.addAll(child.getNames());
    }
    return result;
  }

  public Collection<String> getResultSetNames() {
    // Concatenate all fields without duplicating any name
    Collection<String> result = new LinkedHashSet<String>();
    for (Fields child : children) {
      result.addAll(child.getResultSetNames());
    }
    return result;
  }
}
