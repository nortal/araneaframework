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

package org.araneaframework.backend.list.helper.builder.expression;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.helper.builder.ExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.memorybased.Expression;


/**
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class BaseExpressionToSqlExprBuilder implements ExpressionToSqlExprBuilder {
	private static final Logger log = Logger.getLogger(BaseExpressionToSqlExprBuilder.class);
	
	private Map translators = new HashMap();
	
	protected void addTranslator(Class expressionClass, ExprToSqlExprTranslator translator) {
		this.translators.put(expressionClass, translator);
	}

	public SqlExpression buildSqlExpression(Expression expression) {
		if (log.isDebugEnabled()) {
			log.debug("Expression class: " + expression.getClass().getName());			
		}
		ExprToSqlExprTranslator translator =
			(ExprToSqlExprTranslator) this.translators.get(expression.getClass());
		if (translator == null) {
			throw new RuntimeException("Expression of class " + expression.getClass() + " not supported");
		}
		return translator.translate(expression, this);
	}
}
