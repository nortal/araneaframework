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

package org.araneaframework.backend.list.memorybased.compexpr;

import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.ComparatorExpressionFactory;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;

/**
 * ComparatorExpression that is initialized lazily.
 * 
 * @author Rein Raudjärv
 * 
 * @since 1.1
 */
public class LazyComparatorExpression implements ComparatorExpression {

	private static final long serialVersionUID = 1L;
	
	private final ComparatorExpressionFactory factory;
	private ComparatorExpression expression;

	public LazyComparatorExpression(ComparatorExpressionFactory factory) {
		if (factory == null) {
			throw new IllegalArgumentException("ComparatorExpressionFactory must be provided");
		}
		this.factory = factory;
	}

	public ComparatorExpressionFactory getFactory() {
		return factory;
	}
	
	public ComparatorExpression getComparatorExpression() {
		if (expression == null) {
			expression = factory.createComparatorExpression();
			if (expression == null) {
				throw new AssertionError("ComparatorExpressionFactory must not create a null object");
			}
		}
		return expression;
	}

	public int compare(VariableResolver resolver1, VariableResolver resolver2)
			throws ExpressionEvaluationException {
		return getComparatorExpression().compare(resolver1, resolver2);
	}

}
