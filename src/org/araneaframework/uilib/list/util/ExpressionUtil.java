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
package org.araneaframework.uilib.list.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.lang.Validate;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.MultiExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.ComparedEqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.EqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.GreaterThanExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.IsNullExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.LikeExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.LowerThanExpression;
import org.araneaframework.backend.list.memorybased.expression.constant.ValueExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.AndExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.NotExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.OrExpression;
import org.araneaframework.backend.list.memorybased.expression.variable.VariableExpression;
import org.araneaframework.uilib.list.util.like.LikeConfiguration;

/**
 * Expression factory.
 * 
 * Null as a returned value means that there is no filter
 * (like always true expression).
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class ExpressionUtil {
	
	private static final Comparator DEFAULT_COMPARATOR = new NullComparator(); 
	
	private static final LikeConfiguration DEFAULT_LIKE_CONFIGURATION = new LikeConfiguration();

	/**
	 * Creaters VARIABLE expression.
	 */
	public static VariableExpression var(String name) {
		Validate.notNull(name);		
		return new VariableExpression(name);
	}
	
	/**
	 * Creaters VALUE expression with name.
	 */
	public static ValueExpression value(String name, Object value) {
		if (value == null) {
			return null;
		}
		return new ValueExpression(name, value);
	}
	
	/**
	 * Creaters VALUE expression.
	 */
	public static ValueExpression value(Object value) {
		return value(null, value);
	}
	
	/**
	 * Creaters NULL value expression.
	 */
	public static ValueExpression nullValue() {
		return new ValueExpression(null);
	}
	
	/**
	 * Creaters EQUALS expression.
	 */
	public static Expression eq(Expression expr1,
			Expression expr2, Comparator comp) {
		if (expr1 == null || expr2 == null) {
			return null;
		}
		if (comp == null) {
			return new EqualsExpression(expr1, expr2);
		}
		return new ComparedEqualsExpression(expr1, expr2, comp);
	}
	
	/**
	 * Creaters NOT EQUALS expression.
	 */
	public static Expression ne(Expression expr1,
			Expression expr2, Comparator comp) {
		if (expr1 == null || expr2 == null) {
			return null;
		}
		return not(eq(expr1, expr2, comp));
	}
	
	/**
	 * Creaters GREATER THAN expression.
	 */	
	public static Expression gt(Expression expr1,
			Expression expr2, Comparator comp) {
		if (expr1 == null || expr2 == null) {
			return null;
		}
		if (comp == null) {
			comp = DEFAULT_COMPARATOR;
		}
		return new GreaterThanExpression(expr1, expr2, comp);
	}
	
	/**
	 * Creaters LOWER THAN expression.
	 */	
	public static Expression lt(Expression expr1,
			Expression expr2, Comparator comp) {
		if (expr1 == null || expr2 == null) {
			return null;
		}
		if (comp == null) {
			comp = DEFAULT_COMPARATOR;
		}
		return new LowerThanExpression(expr1, expr2, comp);
	}
	
	/**
	 * Creaters GREATER THAN OR EQUALS expression.
	 */	
	public static Expression ge(Expression expr1,
			Expression expr2, Comparator comp) {		
		if (expr1 == null || expr2 == null) {
			return null;
		}
		return or(gt(expr1, expr2, comp), eq(expr1, expr2, comp));
	}
	
	/**
	 * Creaters LOWER THAN OR EQUALS expression.
	 */	
	public static Expression le(Expression expr1,
			Expression expr2, Comparator comp) {
		if (expr1 == null || expr2 == null) {
			return null;
		}
		return new LowerThanExpression(expr1, expr2, comp);
	}
	
	/**
	 * Creaters IS NULL expression.
	 */	
	public static Expression isNull(Expression expr) {
		if (expr == null) {
			return null;
		}
		return new IsNullExpression(expr);		
	}
	
	/**
	 * Creaters IS NOT NULL expression.
	 */	
	public static Expression isNotNull(Expression expr) {
		if (expr == null) {
			return null;
		}
		return not(isNull(expr));		
	}
	
	/**
	 * Creaters LIKE expression.
	 */	
	public static Expression like(Expression expr, ValueExpression pattern,
			boolean ignoreCase, LikeConfiguration conf) {
		if (expr == null || pattern == null) {
			return null;
		}		
		if (conf == null) {
			conf = DEFAULT_LIKE_CONFIGURATION;
		}
		return new LikeExpression(expr, pattern, ignoreCase, conf);
	}
	
	/**
	 * Creaters NOT expression.
	 */	
	public static Expression not(Expression expr) {
		if (expr == null) {
			return null;
		}
		return new NotExpression(expr);		
	}
	
	/**
	 * Creaters AND expression.
	 */	
	public static Expression and(Expression expr1, Expression expr2) {
		if (expr1 != null && expr2 != null) {
			return new AndExpression().add(expr1).add(expr2);
		} else if (expr1 == null) {
			return expr2;
		} else if (expr2 == null) {
			return expr1;
		}
		return null;
	}
	
	/**
	 * Creaters AND expression.
	 */	
	public static Expression and(Expression[] exprs) {
		return addAll(new AndExpression(), exprs);		
	}
	
	/**
	 * Creaters AND expression.
	 */	
	public static Expression and(Collection exprs) {
		return addAll(new AndExpression(), exprs);		
	}
	
	/**
	 * Creaters AND expression.
	 */	
	public static Expression and(Iterator exprs) {
		return addAll(new AndExpression(), exprs);		
	}
	
	/**
	 * Creaters OR expression.
	 */		
	public static Expression or(Expression expr1, Expression expr2) {
		if (expr1 != null && expr2 != null) {
			return new OrExpression().add(expr1).add(expr2);
		} else if (expr1 == null) {
			return expr2;
		} else if (expr2 == null) {
			return expr1;
		}
		return null;
	}
	
	/**
	 * Creaters OR expression.
	 */		
	public static Expression or(Expression[] exprs) {
		return addAll(new OrExpression(), exprs);		
	}
	
	/**
	 * Creaters OR expression.
	 */		
	public static Expression or(Collection exprs) {
		return addAll(new OrExpression(), exprs);		
	}
	
	/**
	 * Creaters OR expression.
	 */		
	public static Expression or(Iterator exprs) {
		return addAll(new OrExpression(), exprs);		
	}
	
	// Private mthods
	
	private static Expression addAll(MultiExpression multiExpr, Expression[] children) {
		if (children == null || children.length == 0) {
			return null;
		}
		
		int count = 0;
		for (int i = 0; i < children.length; i++) {
			if (children[i] != null) {
				multiExpr.add(children[i]);				
				count++;
			}
		}
		if (count == 0) {
			return null;
		}
		return multiExpr;
	}
	
	private static Expression addAll(MultiExpression multiExpr, Collection children) {
		if (children == null || children.isEmpty()) {
			return null;
		}
		
		int count = 0;
		for (Iterator i = children.iterator(); i.hasNext();) {
			Expression expr = (Expression) i.next();
			if (expr != null) {
				multiExpr.add(expr);				
			}
		}
		if (count == 0) {
			return null;
		}
		return multiExpr;
	}	
	
	private static Expression addAll(MultiExpression multiExpr, Iterator children) {
		if (children == null || !children.hasNext()) {
			return null;
		}
		
		int count = 0;
		while (children.hasNext()) {
			Expression expr = (Expression) children.next();
			if (expr != null) {
				multiExpr.add(expr);
			}
		}
		if (count == 0) {
			return null;
		}
		return multiExpr;
	}		
}
