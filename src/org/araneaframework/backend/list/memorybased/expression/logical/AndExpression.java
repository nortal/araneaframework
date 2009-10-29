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

package org.araneaframework.backend.list.memorybased.expression.logical;

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.expression.MultiExpression;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.core.Assert;

/**
 * Composite AND Expression.
 */
public class AndExpression extends MultiExpression {

  /**
   * Returns Boolean.TRUE if all expressions evaluate to Boolean.TRUE, Boolean.FALSE otherwise.
   * 
   * @return whether all expression evaluated to true
   */
  public Boolean evaluate(VariableResolver resolver) throws ExpressionEvaluationException {
    Assert.isTrue(!this.children.isEmpty(), "At least one children must be provided");
    for (Expression expr : this.children) {
      Boolean result = (Boolean) expr.evaluate(resolver);
      if (!result) {
        return false;
      }
    }
    return true;
  }
}
