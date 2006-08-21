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

import org.araneaframework.uilib.list.OrderInfo;


/**
 * Factory interface for building {@link ComparatorExpression}s.
 * 
 * @see ComparatorExpression
 */
public interface ComparableExpressionFactory extends Serializable {
	/**
	 * Builds <code>ComparatorExpression</code>.
	 * Can also return <code>null</code> if no expression was built.
	 * 
	 * @param resolver
	 *            variable values and types resolver.
	 * @return built <code>ComparatorExpression</code>.
	 */
	ComparatorExpression buildExpression(OrderInfo orderInfo);
}
