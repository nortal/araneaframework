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

package org.araneaframework.backend.list.helper;

import java.util.Map;
import org.araneaframework.backend.list.memorybased.Variable;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.core.util.Assert;

/**
 * VariableResolver that has a map of Variable names and their resolvable values.
 */
public class VariableMapper implements VariableResolver {

  private Map<String, Object> map;

  public VariableMapper(Map<String, Object> map) {
    Assert.notNull(map, "Mapping must be provided");
    this.map = map;
  }

  public Object resolve(Variable variable) {
    Assert.notNull(variable, "Variable must be provided");

    String name = variable.getName();
    Assert.notNull(name, "Variable name must be provided");

    Object result = this.map.get(name);
    Assert.notNull(result, "Variable " + name + " not supported");

    return result;
  }
}
