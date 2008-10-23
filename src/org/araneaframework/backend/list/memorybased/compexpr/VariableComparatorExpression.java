/*
 * Copyright 2006-2008 Webmedia Group Ltd.
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

package org.araneaframework.backend.list.memorybased.compexpr;

import java.util.Comparator;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Variable;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.core.Assert;

public class VariableComparatorExpression
  implements ComparatorExpression, Variable {

  private static final long serialVersionUID = 1L;

  protected String name;

  protected Comparator comparator;

  public VariableComparatorExpression(String name, Comparator comparator) {
    Assert.notNull(name, "Name must be provided");
    Assert.notNull(comparator, "Comparator must be provided");
    this.name = name;
    this.comparator = comparator;
  }

  public String getName() {
    return this.name;
  }

  public Comparator getComparator() {
    return this.comparator;
  }

  public int compare(VariableResolver resolver1, VariableResolver resolver2) {
    Object value1 = resolver1.resolve(this);
    Object value2 = resolver2.resolve(this);
    int result = this.comparator.compare(value1, value2);
    return result;
  }
}
