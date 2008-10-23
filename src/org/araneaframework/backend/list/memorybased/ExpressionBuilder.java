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

package org.araneaframework.backend.list.memorybased;

import java.io.Serializable;
import java.util.Map;

/**
 * General interface for objects that can create an expression.
 * <p>
 * Transforms a Map into an Expression.
 * 
 * @see Expression
 * @see ExpressionFactory
 * @author Rein Raudjärv
 */
public interface ExpressionBuilder extends Serializable {

  /**
   * Builds <code>Expression</code>.
   * 
   * @param data object that can be used to configure building the expressions.
   * @return <code>Expression</code> that is built according to the
   *         <code>data</code> or null if no expression has been built.
   */
  Expression buildExpression(Map data);
}
