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

package org.araneaframework.backend.list.memorybased.expression;

import java.util.ArrayList;
import java.util.List;
import org.araneaframework.backend.list.memorybased.Expression;

/**
 * Composite Expression containing other expressions.
 */
public abstract class MultiExpression implements CompositeExpression {

  protected List children = new ArrayList();

  /**
   * Adds expression to this composite MultiExpression.
   * 
   * @param expression expression to add
   * @return this MultiExpression with added expression
   */
  public MultiExpression add(Expression expression) {
    this.children.add(expression);
    return this;
  }

  public Expression[] getChildren() {
    return (Expression[]) this.children.toArray(new Expression[this.children
        .size()]);
  }
}
