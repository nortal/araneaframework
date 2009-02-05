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

package org.araneaframework.backend.list.memorybased.expression.compare;

import org.apache.commons.lang.ObjectUtils;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.expression.CompositeExpression;
import org.araneaframework.backend.list.memorybased.expression.StringExpression;
import org.araneaframework.backend.list.memorybased.expression.Value;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.list.util.like.LikeConfiguration;

public class LikeExpression implements CompositeExpression, StringExpression {

  private static final long serialVersionUID = 1L;

  protected Expression expr;

  protected Value mask;

  protected boolean ignoreCase;

  protected LikeConfiguration configuration;

  public LikeExpression(Expression expr, Value mask, boolean ignoreCase,
      LikeConfiguration configuration) {
    Assert.notNull(expr, "Expression must be provided.");
    Assert.notNull(mask, "Mask value must be provided.");
    this.expr = expr;
    this.mask = mask;
    this.ignoreCase = ignoreCase;
    this.configuration = configuration;
  }

  public boolean getIgnoreCase() {
    return this.ignoreCase;
  }

  public Value getMask() {
    return this.mask;
  }

  public LikeConfiguration getConfiguration() {
    return configuration;
  }

  public Object evaluate(VariableResolver resolver)
      throws ExpressionEvaluationException {
    String stringToCompare = ObjectUtils.toString(expr.evaluate(resolver));
    String maskStr = ObjectUtils.toString(mask.getValue());

    if (this.ignoreCase) {
      stringToCompare = stringToCompare.toLowerCase();
      maskStr = maskStr.toLowerCase();
    }

    return new Boolean(stringToCompare.indexOf(maskStr) >= 0);
  }

  public Expression[] getChildren() {
    return new Expression[] { this.expr };
  }
}
