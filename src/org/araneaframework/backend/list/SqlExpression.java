/*
 * Copyright 2006-2008 Webmedia Group Ltd.
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
 */

package org.araneaframework.backend.list;

/**
 * SQL Expression that can be evaluated into SQL <code>String</code> and SQL
 * arguments <code>Object[]</code>. One SQL Expression can contain other SQL
 * Expressions and return their SQL Strings and arguments as part of itselves.
 * SQL Expressions are usually built from
 * {@link org.araneaframework.backend.list.memorybased.Expression}s using an
 * {@link org.araneaframework.backend.list.helper.builder.ExpressionToSqlExprBuilder}.
 */
public interface SqlExpression {

  /**
   * Returns the SQL <code>String</code> of this <code>SqlExpression</code>.
   * The returned String may not be <code>null</code>. An empty String should
   * be used instead.
   * 
   * @return the SQL <code>String</code> of this <code>SqlExpression</code>.
   */
  String toSqlString();

  /**
   * Returns the SQL arguments of this <code>SqlExpression</code>. The
   * returned values may not be <code>null</code>. An empty array should be
   * used instead.
   * 
   * @return the SQL arguments of this <code>SqlExpression</code>.
   */
  Object[] getValues();
}
