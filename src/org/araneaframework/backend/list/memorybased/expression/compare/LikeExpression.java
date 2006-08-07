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

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.expression.CompositeExpression;
import org.araneaframework.backend.list.memorybased.expression.StringExpression;
import org.araneaframework.backend.list.memorybased.expression.Value;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;
import org.araneaframework.uilib.list.util.like.LikeConfiguration;
import org.araneaframework.uilib.list.util.like.LikeUtil;

public class LikeExpression implements CompositeExpression, StringExpression {
	
	private static final long serialVersionUID = 1L;
	
	private Expression expr;
	
	private Value mask;
	
	private boolean ignoreCase;
	
	private LikeConfiguration configuration;
	
	public LikeExpression(Expression expr, Value mask, boolean ignoreCase, LikeConfiguration configuration) {
		if (expr == null) {
			throw new IllegalArgumentException("Expression must be provided");
		}		
		if (mask == null) {
			throw new IllegalArgumentException("Value must be provided");
		}
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
		return new Boolean(LikeUtil.isLike(convert(this.expr.evaluate(resolver)),
				convert(this.mask.getValue()), this.ignoreCase, this.configuration));
	}
	
	private String convert(Object value) {
		return value == null ? "" : value.toString(); 
	}
	
	public Expression[] getChildren() {
		return new Expression[] { this.expr };
	}
}
