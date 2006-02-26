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

package org.araneaframework.backend.list.helper.builder;

import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.memorybased.Expression;

/**
 * General interface for translating <code>Expression</code> hierarchy into
 * <code>SqlExpression</code>.
 */
public interface ExpressionToSqlExprBuilder {
	/**
	 * Builds <code>SqlExpression</code> from <code>Expression</code>.
	 * 
	 * @param expression
	 *            <code>Expression</code>.
	 * @return <code>SqlExpression</code> built from the
	 *         <code>Expression</code>.
	 */
	SqlExpression buildSqlExpression(Expression expression);
}
