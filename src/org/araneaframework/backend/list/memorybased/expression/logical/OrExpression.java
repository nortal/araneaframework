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

package org.araneaframework.backend.list.memorybased.expression.logical;

import java.util.Iterator;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.expression.MultiExpression;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;


public class OrExpression extends MultiExpression {

	private static final long serialVersionUID = 1L;

	public Object evaluate(VariableResolver resolver)
			throws ExpressionEvaluationException {
		if (this.children.size() == 0) {
			throw new ExpressionEvaluationException(
					"At least one children must be provided");
		}
		for (Iterator i = this.children.iterator(); i.hasNext();) {
			Expression expr = (Expression) i.next();
			Boolean value = (Boolean) expr.evaluate(resolver);
			if (Boolean.TRUE.equals(value)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
}
