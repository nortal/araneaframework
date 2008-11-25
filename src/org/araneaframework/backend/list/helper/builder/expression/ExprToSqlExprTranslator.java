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

package org.araneaframework.backend.list.helper.builder.expression;

import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.helper.builder.ExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.memorybased.Expression;

/**
 * General interface for translating an <code>Expression</code> into
 * <code>SqlExpression</code>. It is generally used to translate only a
 * specific type of <code>Expression</code>. Use (@see
 * org.araneaframework.uilib.widgets.lists.refactor.backend.builder.ExpressionToSqlExprBuilder)
 * to translate the whole hierarchy.
 */
public interface ExprToSqlExprTranslator {

  /**
   * Translates an <code>Expression</code> into <code>SqlExpression</code>.
   * 
   * @param expr an <code>Expression</code>.
   * @param builder <code>ExpressionToSqlExprBuilder</code> that is used to
   *            translate the children of the <code>Expression</code>.
   * @return the <code>SqlExpression</code> that represents the former
   *         <code>Expression</code>.
   */
  SqlExpression translate(Expression expr, ExpressionToSqlExprBuilder builder);

}
