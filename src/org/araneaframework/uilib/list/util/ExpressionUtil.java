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
import java.util.List;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.lang.Validate;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.MultiExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.ComparedEqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.EndsWithExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.EqualsExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.GreaterThanExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.IsNullExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.LikeExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.LowerThanExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.StartsWithExpression;
import org.araneaframework.backend.list.memorybased.expression.constant.ValueExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.AndExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.InExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.NotExpression;
import org.araneaframework.backend.list.memorybased.expression.logical.OrExpression;
import org.araneaframework.backend.list.memorybased.expression.procedure.ProcedureExpression;
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
	 * Creates VARIABLE expression.
	 */
	public static VariableExpression var(String name) {
		Validate.notEmpty(name);		
		return new VariableExpression(name);
	}
	
	/**
	 * Creates VALUE expression with name.
	 */
	public static ValueExpression value(String name, Object value) {
		if (value == null) {
			return null;
		}
		return new ValueExpression(name, value);
	}
	
	/**
	 * Creates VALUE expression.
	 */
	public static ValueExpression value(Object value) {
		return value(null, value);
	}
	
	/**
	 * Creates NULL value expression.
	 */
	public static ValueExpression nullValue(String name) {
		return new ValueExpression(name, null);
	}
	
	/**
	 * Creates NULL value expression.
	 */
	public static ValueExpression nullValue() {
		return new ValueExpression(null);
	}
	
	/**
	 * Creates EQUALS expression.
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
	 * Creates NOT EQUALS expression.
	 */
	public static Expression ne(Expression expr1,
			Expression expr2, Comparator comp) {
		if (expr1 == null || expr2 == null) {
			return null;
		}
		return not(eq(expr1, expr2, comp));
	}
	
	/**
	 * Creates GREATER THAN expression.
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
	 * Creates LOWER THAN expression.
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
	 * Creates GREATER THAN OR EQUALS expression.
	 */	
	public static Expression ge(Expression expr1,
			Expression expr2, Comparator comp) {		
		if (expr1 == null || expr2 == null) {
			return null;
		}
		return or(gt(expr1, expr2, comp), eq(expr1, expr2, comp));
	}
	
	/**
	 * Creates LOWER THAN OR EQUALS expression.
	 */	
	public static Expression le(Expression expr1,
			Expression expr2, Comparator comp) {
		if (expr1 == null || expr2 == null) {
			return null;
		}

		return or(lt(expr1, expr2, comp), eq(expr1, expr2, comp));
	}
	
	/**
	 * Creates IS NULL expression.
	 */	
	public static Expression isNull(Expression expr) {
		if (expr == null) {
			return null;
		}
		return new IsNullExpression(expr);		
	}
	
	/**
	 * Creates IS NOT NULL expression.
	 */	
	public static Expression notNull(Expression expr) {
		if (expr == null) {
			return null;
		}
		return not(isNull(expr));		
	}
	
	/**
	 * Creates LIKE expression.
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
	 * Creates a StartsWith expression.
	 */
	public static Expression startsWith(Expression expr, ValueExpression pattern,
			boolean ignoreCase, LikeConfiguration conf) {
		if (expr == null || pattern == null) {
			return null;
		}
		if (conf == null) {
			conf = DEFAULT_LIKE_CONFIGURATION;
		}
		return new StartsWithExpression(expr, pattern, ignoreCase, conf);
	}

	/**
	 * Creates a EndsWith expression.
	 */
	public static Expression endsWith(Expression expr, ValueExpression pattern,
			boolean ignoreCase, LikeConfiguration conf) {
		if (expr == null || pattern == null) {
			return null;
		}
		if (conf == null) {
			conf = DEFAULT_LIKE_CONFIGURATION;
		}
		return new EndsWithExpression(expr, pattern, ignoreCase, conf);
	}

	/**
	 * Creates PROCEDURE expression.
	 */	
	public static ProcedureExpression sqlFunction(String name, Expression[] exprs) {
		return (ProcedureExpression) addAll(new ProcedureExpression(name), exprs, false);
	}
	
	/**
	 * Creates PROCEDURE expression.
	 */	
	public static ProcedureExpression sqlFunction(String name, Collection exprs) {
		return (ProcedureExpression) addAll(new ProcedureExpression(name), exprs, false);
	}
	
	/**
	 * Creates PROCEDURE expression.
	 */	
	public static ProcedureExpression sqlFunction(String name, Iterator exprs) {
		return (ProcedureExpression) addAll(new ProcedureExpression(name), exprs, false);
	}
	
	/**
	 * Creates NOT expression.
	 */	
	public static Expression not(Expression expr) {
		if (expr == null) {
			return null;
		}
		return new NotExpression(expr);		
	}
	
	/**
	 * Creates AND expression.
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
	 * Creates AND expression.
	 */	
	public static Expression and(Expression[] exprs) {
		return addAll(new AndExpression(), exprs, true);		
	}
	
	/**
	 * Creates AND expression.
	 */	
	public static Expression and(Collection exprs) {
		return addAll(new AndExpression(), exprs, true);		
	}
	
	/**
	 * Creates AND expression.
	 */	
	public static Expression and(Iterator exprs) {
		return addAll(new AndExpression(), exprs, true);		
	}
	
	/**
	 * Creates OR expression.
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
	 * Creates OR expression.
	 */		
	public static Expression or(Expression[] exprs) {
		return addAll(new OrExpression(), exprs, true);		
	}
	
	/**
	 * Creates OR expression.
	 */		
	public static Expression or(Collection exprs) {
		return addAll(new OrExpression(), exprs, true);		
	}
	
	/**
	 * Creates OR expression.
	 */		
	public static Expression or(Iterator exprs) {
		return addAll(new OrExpression(), exprs, true);		
	}

    /**
     * Creates IN expression.
     * 
     * @since 1.1.4
     */     
    public static Expression in(Expression expression, List expressions) {
      return new InExpression(expression, expressions);
    }

	// Private mthods
	
	private static Expression addAll(MultiExpression multiExpr, Expression[] children, boolean allowNulls) {
		if (children == null || children.length == 0) {
			return null;
		}
		
		int count = 0;
		for (int i = 0; i < children.length; i++) {
			if (children[i] != null) {
				multiExpr.add(children[i]);				
				count++;
			} else if (!allowNulls) {
				throw new IllegalArgumentException("Expression can not be null");				
			}
		}
		if (count == 1) {
			return multiExpr.getChildren()[0];
		} else if (count == 0) {
			return null;
		}
		return multiExpr;
	}
	
	private static Expression addAll(MultiExpression multiExpr, Collection children, boolean allowNulls) {
		if (children == null || children.isEmpty()) {
			return null;
		}
		
		int count = 0;
		for (Iterator i = children.iterator(); i.hasNext();) {
			Expression expr = (Expression) i.next();
			if (expr != null) {
				multiExpr.add(expr);
				count++;
			} else if (!allowNulls) {
				throw new IllegalArgumentException("Expression can not be null");				
			}
		}
		if (count == 1) {
			return multiExpr.getChildren()[0];
		} else if (count == 0) {
			return null;
		}

		return multiExpr;
	}	
	
	private static Expression addAll(MultiExpression multiExpr, Iterator children, boolean allowNulls) {
		if (children == null || !children.hasNext()) {
			return null;
		}
		
		int count = 0;
		while (children.hasNext()) {
			Expression expr = (Expression) children.next();
			if (expr != null) {
				multiExpr.add(expr);
				count++;
			} else if (!allowNulls) {
				throw new IllegalArgumentException("Expression can not be null");				
			}
		}
		if (count == 1) {
			return multiExpr.getChildren()[0];
		} else if (count == 0) {
			return null;
		}

		return multiExpr;
	}		

}
