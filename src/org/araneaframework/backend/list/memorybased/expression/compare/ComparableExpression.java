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

import java.util.Comparator;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.expression.CompositeExpression;
import org.araneaframework.backend.list.memorybased.expression.StringExpression;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.uilib.list.util.ComparatorFactory;
import org.araneaframework.uilib.list.util.comparator.NullComparator;
import org.araneaframework.uilib.list.util.comparator.StringComparator;


public abstract class ComparableExpression implements CompositeExpression,
		StringExpression {
	
	protected Expression expr1;

	protected Expression expr2;

	protected Comparator comparator;

	public ComparableExpression(Expression expr1, Expression expr2,
			Comparator comparator) {
		if (expr1 == null || expr2 == null) {
			throw new IllegalArgumentException("Operands must be provided");
		}
		this.expr1 = expr1;
		this.expr2 = expr2;
		this.comparator = comparator;
	}

	public ComparableExpression(Expression expr1, Expression expr2) {
		this(expr1, expr2, ComparatorFactory.getDefault());
	}

	public Comparator getComparator() {
		return this.comparator;
	}

	public final Object evaluate(VariableResolver resolver)
			throws ExpressionEvaluationException {
		Object value1 = this.expr1.evaluate(resolver);
		Object value2 = this.expr2.evaluate(resolver);
		return doEvaluate(value1, value2) ? Boolean.TRUE : Boolean.FALSE;
	}

	protected abstract boolean doEvaluate(Object value1, Object value2);

	public Expression[] getChildren() {
		return new Expression[] { this.expr1, this.expr2 };
	}

	public boolean getIgnoreCase() {
		Comparator comp = this.comparator;
		if (comp instanceof NullComparator) {
			comp = ((NullComparator) comp).getNotNullComparator();
		}
		
		if (comp instanceof StringComparator) {
			return ((StringComparator) comp).getIgnoreCase();
		}
		return false;
	}
}
