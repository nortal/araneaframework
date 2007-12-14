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

package org.araneaframework.backend.list.memorybased.expression;

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.ExpressionFactory;

/**
 * Expression that is initialized lazily.
 * 
 * @author Rein Raudjärv
 * 
 * @since 1.1
 */
public class LazyExpression implements Expression {
	
	private static final long serialVersionUID = 1L;
	
	private final ExpressionFactory factory;
	private Expression expression;

	public LazyExpression(ExpressionFactory factory) {
		if (factory == null) {
			throw new IllegalArgumentException("ExpressionFactory must be provided");
		}
		this.factory = factory;
	}

	public ExpressionFactory getFactory() {
		return factory;
	}
	
	public Expression getExpression() {
		if (expression == null) {
			expression = factory.createExpression();
			if (expression == null) {
				throw new AssertionError("ExpressionFactory must not create a null object");
			}
		}
		return expression;
	}

	public Object evaluate(VariableResolver resolver) throws ExpressionEvaluationException {
		return getExpression().evaluate(resolver);
	}
}
