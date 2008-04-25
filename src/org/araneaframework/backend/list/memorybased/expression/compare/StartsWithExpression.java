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

package org.araneaframework.backend.list.memorybased.expression.compare;

import org.apache.commons.lang.ObjectUtils;
import org.araneaframework.backend.list.helper.builder.expression.StandardExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.expression.Value;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.uilib.list.util.like.LikeConfiguration;
import org.araneaframework.uilib.list.util.like.RegexpLikeUtil;

/**
 * Expression for StartsWith condition. Supports both database query and
 * mempry-based lists. To use this expression in Aranea, it must be bound
 * to a translator that recognizes it.
 * 
 * @see StandardExpressionToSqlExprBuilder
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.1.3
 */
public class StartsWithExpression extends LikeExpression {

	private static final long serialVersionUID = 1L;

	public StartsWithExpression(Expression expr, Value mask, boolean ignoreCase,
			LikeConfiguration configuration) {
		super(expr, mask, ignoreCase, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object evaluate(VariableResolver resolver)
			throws ExpressionEvaluationException {
		String stringToCompare = ObjectUtils.toString(expr.evaluate(resolver));
		String maskStr = ObjectUtils.toString(mask.getValue());
		boolean result = RegexpLikeUtil.isStartsWith(stringToCompare, maskStr,
				ignoreCase, configuration);
		return result ? Boolean.TRUE : Boolean.FALSE;
	}

}
