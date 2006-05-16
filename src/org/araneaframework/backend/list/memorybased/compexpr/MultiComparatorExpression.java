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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.ExpressionEvaluationException;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;


public class MultiComparatorExpression implements CompositeComparatorExpression {

	private static final long serialVersionUID = 1L;

	private static final Logger log = Logger.getLogger(MultiComparatorExpression.class);
	
	protected List children = new ArrayList();
	
	public ComparatorExpression add(ComparatorExpression expression) {
		this.children.add(expression);
		return this;
	}
	
	public ComparatorExpression[] getChildren() {
		return (ComparatorExpression[]) this.children
		.toArray(new ComparatorExpression[this.children.size()]);
	}
	
	public int compare(VariableResolver resolver1, VariableResolver resolver2)
	throws ExpressionEvaluationException {
		if (this.children.size() == 0) {
			log.debug("No children specified - returning zero as the comparing result");
			return 0;
		}
		
		int result = 0;
		for (Iterator i = this.children.iterator(); i.hasNext();) {
			ComparatorExpression expr = (ComparatorExpression) i.next();
			result = expr.compare(resolver1, resolver2);
			if (result == 0) {
				return 0;
			}
		}
		return result;
	}
}
