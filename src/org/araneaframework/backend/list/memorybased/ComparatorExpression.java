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

package org.araneaframework.backend.list.memorybased;

import java.io.Serializable;


/**
 * Expression that compares two <code>Variables</code> using two
 * <code>VariableResolvers</code> for evaluating these Variables.
 * 
 * @see org.araneaframework.backend.list.memorybased.Resolver
 */
public interface ComparatorExpression extends Serializable {
	/**
	 * Returns a negative integer, zero, or a positive integer as the first
	 * <code>Variable</code> is less than, equal to, or greater than the
	 * second.
	 * 
	 * @param resolver1
	 *            Resolver that is used to evalute the first
	 *            <code>Variable</code>.
	 * @param resolver2
	 *            Resolver that is used to evalute the second
	 *            <code>Variable</code>.
	 * @return a negative integer, zero, or a positive integer as the first
	 *         <code>Variable</code> is less than, equal to, or greater than
	 *         the second.
	 * @throws ExpressionEvaluationException
	 *             when the comparation fails.
	 */
	int compare(Resolver resolver1, Resolver resolver2)
			throws ExpressionEvaluationException;
}
