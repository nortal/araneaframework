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
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;

/**
 * ComparatorExpression that reverses another ComparatorExpression comparation
 * result.
 */
public class ReverseComparatorExpression implements
		CompositeComparatorExpression {

	private static final long serialVersionUID = 1L;
	
	protected ComparatorExpression expression;

	public ReverseComparatorExpression(ComparatorExpression expression) {
		if (expression == null) {
			throw new RuntimeException("Expression must be provided");
		}
		this.expression = expression;
	}

	public int compare(VariableResolver resolver1, VariableResolver resolver2)
			throws ExpressionEvaluationException {
		return this.expression.compare(resolver2, resolver1);
	}

	public ComparatorExpression[] getChildren() {
		return new ComparatorExpression[] { this.expression };
	}

}
